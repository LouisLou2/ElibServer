package com.leo.elib.service.specific.inter.user_behavior;

import com.leo.elib.constant.user.UserBookBehavior;
import com.leo.elib.usecase.inter.chart.TrendingBookManager;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ReserveBookBehaviorResolver {

  @Resource
  private TrendingBookManager trendingBookManager;

  @Before("@annotation(com.leo.elib.constant.user.UserReserveBookBehavior)")
  public void resolveViewBookBehavior(JoinPoint joinPoint) {
    // 获取参数
    Object[] args = joinPoint.getArgs();
    var isbn = (String) args[2];
    trendingBookManager.reportUserBehavior(isbn, UserBookBehavior.Reserve);
  }
}
