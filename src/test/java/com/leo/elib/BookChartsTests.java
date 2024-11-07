package com.leo.elib;

import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.service.base_service.inter.RCacheManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class BookChartsTests {

  @Value("${container.redis.chart.trending_book_zset}")
  private String trendingBookCont;

  @Value("${sys_setting.chart.trending_book.list_size}")
  private int trendingListSize;

  @Resource
  private RCacheManager rCacheManager;
  private ZSetOperations<String, String> opsForZSet;

  @Resource
  private BookInfoMapper bookInfoMapper;

  @PostConstruct
  void init () {
    opsForZSet = rCacheManager.getOpsForZSet();
  }

  @Test
  void insert(){
    List<String> trendingIsbn = bookInfoMapper.debug_getIsbnsByTime(0, trendingListSize);
    // 初始score都是0，批量插入
    Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
    for (int i = 0; i < trendingIsbn.size(); i++) {
      tuples.add(ZSetOperations.TypedTuple.of(trendingIsbn.get(i), 10+i*0.01));
    }
    opsForZSet.add(trendingBookCont, tuples);
    int x = 0;
  }

}
