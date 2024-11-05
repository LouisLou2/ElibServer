package com.leo.elib.service.specific.inter;

import com.leo.elib.constant.user.UserBookBehavior;

public interface BookPopularityCalc {
  int calcPopularity(UserBookBehavior behavior);
}
