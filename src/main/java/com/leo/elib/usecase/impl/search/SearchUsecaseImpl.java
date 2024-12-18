package com.leo.elib.usecase.impl.search;
import com.leo.elib.entity.SearchedPublisher;
import com.leo.elib.entity.dto.dao.Author;
import com.leo.elib.entity.SearchedAuthor;
import com.leo.elib.entity.SearchedBook;
import com.leo.elib.entity.dto.dao.Publisher;
import com.leo.elib.entity.elastic.BookDetailedInfo;
import com.leo.elib.usecase.inter.search.SearchUsecase;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchUsecaseImpl implements SearchUsecase {

  @Value("${es.doc.book_detail_info.index_name}")
  private String bookIndexName;

  @Value("${es.doc.author_info.index_name}")
  private String authorIndexName;

  @Value("${es.doc.publisher_info.index_name}")
  private String publisherIndexName;

  @Resource
  private ElasticsearchOperations esOperations;

  private static final String [] wantedBookFields;
  private static final String [] wantedAuthorFields;
  private static final String [] wantedPublisherFields;

  static {
    // 反射机制获取 SearchedBook 的所有字段
    wantedBookFields = getWantedFields(BookDetailedInfo.class);
    // 反射机制获取 SearchedAuthor 的所有字段
    wantedAuthorFields = getWantedFields(SearchedAuthor.class);
    // 反射机制获取 SearchedPublisher 的所有字段
    wantedPublisherFields = getWantedFields(SearchedPublisher.class);
  }

  private static String[] getWantedFields(Class<?> clazz) {
    var fields = clazz.getDeclaredFields();
    var wantedFields = new String[fields.length];
    for (int i = 0; i < fields.length; i++) {
      wantedFields[i] = fields[i].getName();
    }
    return wantedFields;
  }

  private <T> List<T> searchInIndex(String keyword,
                                    int pageNum,
                                    int pageSize,
                                    String indexName,
                                    boolean fuzzy,
                                    List<String> searchingFields,
                                    String[] wantedFields,
                                    Class<T> clazz) {
    // 设置分页
    Pageable pageable = PageRequest.of(pageNum, pageSize);
    // 构建查询
    NativeQuery searchQuery = NativeQuery.builder()
      .withQuery(q -> q
        .multiMatch(m -> m
          .fields(searchingFields).query(keyword).fuzziness(fuzzy ? "AUTO" : "0")
        )
      )
      .withPageable(pageable)
      .withSourceFilter(new FetchSourceFilterBuilder()
        .withIncludes(wantedFields)
        .build())
      .build();
    SearchHits<T> searchHits = esOperations.search(searchQuery, clazz, IndexCoordinates.of(indexName));
    List<T> result;
    result = searchHits.getSearchHits().stream().map(SearchHit::getContent).toList();
    return result;
  }

  @Override
  public List<SearchedBook> searchInBooks(String keyword, int pageNum, int pageSize) {
    return searchInIndex(
      keyword,
      pageNum,
      pageSize,
      bookIndexName,
      true,
      BookDetailedInfo.worthSearchFriendlyFields,
      wantedBookFields,
      SearchedBook.class
    );
  }

  @Override
  public List<SearchedAuthor> searchInAuthors(String keyword, int pageNum, int pageSize) {
    return searchInIndex(
      keyword,
      pageNum,
      pageSize,
      authorIndexName,
      false,
      Author.worthSearchFriendlyFields,
      wantedAuthorFields,
      SearchedAuthor.class
    );
  }

  @Override
  public List<SearchedPublisher> searchInPublishers(String keyword, int pageNum, int pageSize) {
    return searchInIndex(
      keyword,
      pageNum,
      pageSize,
      publisherIndexName,
      false,
      Publisher.worthSearchFriendlyFields,
      wantedPublisherFields,
      SearchedPublisher.class
    );
  }
}