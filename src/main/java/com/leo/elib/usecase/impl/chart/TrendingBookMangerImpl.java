package com.leo.elib.usecase.impl.chart;

import com.leo.elib.constant.user.UserBookBehavior;
import com.leo.elib.service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.BookPopularityCalc;
import com.leo.elib.usecase.inter.chart.TrendingBookManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ZSetOperations;

public class TrendingBookMangerImpl implements TrendingBookManager {

  @Value("${container.redis.chart.trending_book_zset}")
  private String trendingBookCont;

  @Resource
  private RCacheManager rCacheManager;

  @Resource
  private BookPopularityCalc bookPopCalc;

  private ZSetOperations<String, String> opsForZSet;


  @PostConstruct
  void init () {
    opsForZSet = rCacheManager.getOpsForZSet();
  }

  @Override
  public void reportUserBehavior(String isbn, UserBookBehavior behavior) {
    int score = bookPopCalc.calcPopularity(behavior);
  }
}