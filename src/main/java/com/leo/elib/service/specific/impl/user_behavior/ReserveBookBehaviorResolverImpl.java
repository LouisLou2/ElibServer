package com.leo.elib.service.specific.impl.user_behavior;
import com.leo.elib.constant.user.UserBookBehavior;
import com.leo.elib.service.specific.inter.user_behavior.ReserveBookBehaviorResolver;
import com.leo.elib.usecase.inter.chart.book.TrendingBookManager;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ReserveBookBehaviorResolverImpl implements ReserveBookBehaviorResolver {

  @Resource
  private TrendingBookManager trendingBookManager;

  @Before("@annotation(com.leo.elib.constant.user.UserReserveBookBehavior)")
  public void resolveBehavior(JoinPoint joinPoint) {
    // 获取参数
    Object[] args = joinPoint.getArgs();
    var isbn = (String) args[2];
    assert isbn != null;
    trendingBookManager.reportUserBehavior(isbn, UserBookBehavior.Reserve);
  }
}
