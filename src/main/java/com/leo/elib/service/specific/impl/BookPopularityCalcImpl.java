package com.leo.elib.service.specific.impl;

import com.leo.elib.constant.user.UserBookBehavior;
import com.leo.elib.service.specific.inter.BookPopularityCalc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BookPopularityCalcImpl implements BookPopularityCalc {

  @Value("${sys_setting.user_behavior.weight.tap}")
  private int tapWeight;

  @Value("${sys_setting.user_behavior.weight.collect}")
  private int collectWeight;

  @Value("${sys_setting.user_behavior.weight.reserve}")
  private int reserveWeight;

  @Override
  public int calcPopularity(UserBookBehavior behavior) {
    return switch (behavior) {
      case Tap -> tapWeight;
      case Collect -> collectWeight;
      case Reserve -> reserveWeight;
    };
  }
}