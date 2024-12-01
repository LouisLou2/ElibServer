package com.leo.elib.usecase.impl;
import com.leo.elib.entity.UserHomeData;
import com.leo.elib.usecase.inter.AggregationUserDataProvider;
import com.leo.elib.usecase.inter.AnnounUsecase;
import com.leo.elib.usecase.inter.BookInfoProvider;
import com.leo.elib.usecase.inter.BookViewingHistoryManager;
import com.leo.elib.usecase.inter.chart.HighRatingBookChartManager;
import com.leo.elib.usecase.inter.chart.TrendingBookManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AggregationUserDataProviderImpl implements AggregationUserDataProvider {

  @Resource
  private AnnounUsecase announUsecase;

  @Resource
  private BookViewingHistoryManager bookViewingHistoryManager;

  @Resource
  private BookInfoProvider bookInfoProvider;

  @Resource
  private TrendingBookManager trendingBookManager;

  @Resource
  private HighRatingBookChartManager highRatingBookChartManager;

  @Override
  public UserHomeData getHomeData(
    int userId,
    int announId,
    int viewingHistoryPageSize,
    int recoBookPageSize,
    int chartPageSize
  ) {
    UserHomeData data = new UserHomeData();
    data.setHasNewAnnoun(announUsecase.hasNew(announId));
    data.setViewingHistory(bookViewingHistoryManager.getViewingHistory(userId, 0, viewingHistoryPageSize));
    data.setRecoBooks(bookInfoProvider.getRecoBooks(userId, 0, recoBookPageSize));
    data.setTrendingBooks(trendingBookManager.getTrendingBooks(0, chartPageSize));
    data.setHighlyRatedBooks(highRatingBookChartManager.getHighRatingBooks(0, chartPageSize));
    return data;
  }
}