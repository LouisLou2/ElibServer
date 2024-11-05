package com.leo.elib.usecase.inter.chart;

import com.leo.elib.constant.user.UserBookBehavior;

public interface TrendingBookManager {
  void reportUserBehavior(String isbn, UserBookBehavior behavior);
}
