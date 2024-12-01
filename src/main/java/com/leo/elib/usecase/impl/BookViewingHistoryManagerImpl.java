package com.leo.elib.usecase.impl;

import co.elastic.clients.json.JsonData;
import com.leo.elib.entity.BookViewingHistory;
import com.leo.elib.usecase.inter.BookViewingHistoryManager;
import com.leo.elib.util.SearchHintTranfer;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookViewingHistoryManagerImpl implements BookViewingHistoryManager {

  @Value("${sys_setting.viewing_history.book.max_days}")
  private short maxBookDays;

  @Value("${es.doc.viewing_history.book.index_name}")
  private String bookViewingHistoryIndexName;

  @Resource
  private ElasticsearchOperations esOperations;

  @Async("taskExecutor")
  @Override
  public void addViewingHistory(BookViewingHistory history) {
    esOperations.save(history);
    // System.out.println("addViewingHistory success");
  }

  @Override
  public List<BookViewingHistory> getViewingHistory(int userId, int pageNum, int pageSize) {
    // 创建查询条件：根据userId和viewingTime查询
    Criteria criteria = new Criteria(BookViewingHistory.userIdFieldName).is(userId);
    criteria.and(new Criteria(BookViewingHistory.timeFieldName).greaterThan(LocalDateTime.now().minusDays(maxBookDays)));
    // 设置分页
    PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
    // 创建查询对象
    Query query = new CriteriaQuery(criteria);
    query.setPageable(pageRequest);
    query.addSort(Sort.by(Sort.Order.desc(BookViewingHistory.timeFieldName)));

    SearchHits<BookViewingHistory> searchHits = esOperations.search(query, BookViewingHistory.class, IndexCoordinates.of(bookViewingHistoryIndexName));
    // 执行查询
    return SearchHintTranfer.getList(searchHits);
  }

  @Override
  public List<BookViewingHistory> getViewingHistory(int userId, String keyword, int pageNum, int pageSize) {
    // 获取当前时间和30天前的时间
    String earliestTime = BookViewingHistory.allowedTimeFormat(
      LocalDateTime.now().minusDays(maxBookDays)
    );
    // 创建分页参数
    Pageable pageable = PageRequest.of(pageNum, pageSize);
    // 构建查询
    NativeQuery searchQuery = NativeQuery.builder()
      .withQuery(q -> q
        .bool(b -> b
          // 必须满足 userId 一致
          .filter(f -> f.term(t -> t.field(BookViewingHistory.userIdFieldName).value(userId)))
          // 必须满足日期在最近30天内
          .filter(f -> f.range(r -> r.field(BookViewingHistory.timeFieldName).gte(JsonData.fromJson("\""+earliestTime+"\""))))
          // 对 title, authorNames, publisherName 字段进行模糊匹配
          .must(m -> m.multiMatch(mm -> mm
            .fields(BookViewingHistory.worthSearchFriendlyFields)  // 查询的字段
            .query(keyword)            // 查询的关键词
            .fuzziness("0")        // 不设置模糊匹配
          ))
        )
      )
      .withPageable(pageable) // 分页参数
      .build();
    // 执行查询
    SearchHits<BookViewingHistory> searchHits = esOperations.search(searchQuery, BookViewingHistory.class, IndexCoordinates.of(bookViewingHistoryIndexName));
    return SearchHintTranfer.getList(searchHits);
  }
}