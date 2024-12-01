package com.leo.elib.usecase.impl.chart;

import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.usecase.inter.chart.HighRatingBookChartManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// 注意这里缓存是直接使用应用级别的缓存，而不是使用 Redis 缓存，就算重启，数据扔可从SQL中重新获取
@Service
public class HighRatingBookChartManagerImpl implements HighRatingBookChartManager {

  @Value("${sys_setting.chart.high_rating_book.list_size}")
  private int listSize;

  // 这里只是榜单的简略信息，没有使用BookInfo这个大对象
  private List<BookBrief> highRatingBooks;
  private Set<String> highRatingBookIsbn;

  @Resource
  private BookInfoMapper bookInfoMapper;

  @PostConstruct
  void init () {
    assert listSize > 0 && listSize < 5000; // 5000 is too large
    // updateHighRatingBooks();
  }


  @Override
  public void updateHighRatingBooks() {
    highRatingBooks = bookInfoMapper.getBookBriefsByRating(listSize, 0);
    highRatingBookIsbn = highRatingBooks.stream()
      .map(BookBrief::getIsbn)
      .collect(Collectors.toSet());
  }

  @Override
  public Set<String> highRatingBookIsbn() {
    return highRatingBookIsbn;
  }

  @Override
  public Set<String> highRatingBookIsbnClone() {
    return new HashSet<>(highRatingBookIsbn);
  }

  @Override
  public List<BookBrief> getHighRatingBooks(int pageNum, int pageSize) {
    if (pageNum==10){
      int x=0;
    }
    int size = highRatingBooks.size();
    int start = pageNum * pageSize;
    int end = start + pageSize;
    if (start >= size) {
      return List.of();
    }
    if (end > size) {
      end = size;
    }
    return highRatingBooks.subList(start, end);
  }

}