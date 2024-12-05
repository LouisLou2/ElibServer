package com.leo.elib.usecase.impl.chart.book;

import com.leo.elib.constant.user.UserBookBehavior;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.service.base_service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.BookPopularityCalc;
import com.leo.elib.usecase.inter.chart.book.TrendingBookManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class TrendingBookMangerImpl implements TrendingBookManager {

  @Value("${container.redis.chart.trending_book_zset}")
  private String trendingBookCont;

  @Value("${sys_setting.chart.trending_book.list_size}")
  private int listSize;

  @Value("${sys_setting.chart.trending_book.popularity_decay_rate}")
  private double popularityDecayRate;

  @Resource
  private RCacheManager rCacheManager;
  private ZSetOperations<String, String> opsForZSet;

  @Resource
  private BookPopularityCalc bookPopCalc;

  // 应用内的级别的缓存，重启后数据重新获取
  // 这里只是榜单的简略信息，没有使用BookInfo这个大对象
  private List<BookBrief> trendingBooks;

  private Set<String> trendingBookIsbn;

  @Resource
  private BookInfoMapper bookInfoMapper;

  @PostConstruct
  private void init () {
    assert listSize > 0 && listSize < 5000; // 5000 is too large
    opsForZSet = rCacheManager.getOpsForZSet();
  }


//  private void initBriefBooks() {
//    // 从数据库中获取热门书籍isbn
//    // 不能使用bookInListIsbn，那是init后才有的
//    trendingBookIsbn = opsForZSet.reverseRange(trendingBookCont, 0, listSize - 1);
//    assert trendingBookIsbn != null;
//    trendingBooks = bookInfoMapper.getBookBriefList(
//      trendingBookIsbn.stream().toList()
//    );
//  }

  // 使用消息队列会更好在，这里简化为异步调用
  @Async("taskExecutor")
  @Override
  public void reportUserBehavior(String isbn, UserBookBehavior behavior) {
    int score = bookPopCalc.calcPopularity(behavior);
    // 使用 ZSet 的 incrementScore 方法来实现：如果 ZSet 已经包含此元素，就增加分数，如果没有则新增元素
    opsForZSet.incrementScore(trendingBookCont, isbn, score);
    System.out.println("reportUserBehavior: " + isbn + " " + behavior);
  }

  @Override
  public Set<String> bookInListIsbnClone() {
    // return opsForZSet.reverseRange(trendingBookCont, 0, listSize - 1); // 使用这个也可以，现在的设计是一致的，使用下面的方法更快
    return new HashSet<>(trendingBookIsbn);
  }

  @Override
  public Set<String> bookInListIsbn() {
    return trendingBookIsbn;
  }

  @Override
  public List<BookBrief> getTrendingBooks(int pageNum, int pageSize) {
    int size = trendingBooks.size();
    int start = pageNum * pageSize;
    int end = start + pageSize;
    if (start > size) {
      return List.of();
    }
    if (end > size) {
      end = size;
    }
    return trendingBooks.subList(start, end);
  }

  @Override
  public List<String> removeColdBooksAndApplyDecay(boolean applyTimeDecay) {
    opsForZSet.removeRange(trendingBookCont, listSize, -1);
    // 将所有的书籍的分数降低一定比例,批量操作
    Set<ZSetOperations.TypedTuple<String>> tuples = opsForZSet.reverseRangeWithScores(trendingBookCont, 0, -1);
    assert tuples != null;
    List<String> result = tuples.stream()
      .map(ZSetOperations.TypedTuple::getValue)
      .toList();

    if (applyTimeDecay){
      Set<ZSetOperations.TypedTuple<String>> newTuples = tuples.stream()
        .map(tuple -> ZSetOperations.TypedTuple.of(
          Objects.requireNonNull(tuple.getValue()), tuple.getScore() * popularityDecayRate)
        )
        .collect(Collectors.toSet());

      opsForZSet.add(trendingBookCont, newTuples); // 批量操作， 如果元素不存在就新增，存在就修改
    }

    trendingBookIsbn = new HashSet<>(result);
    return result;
  }

  @Override
  public void updateInnerCache(List<BookBrief> books) {
    assert trendingBookIsbn.size() == books.size();
    trendingBooks = books;
    trendingBookIsbn = books.stream()
      .map(BookBrief::getIsbn)
      .collect(Collectors.toSet());
  }
}