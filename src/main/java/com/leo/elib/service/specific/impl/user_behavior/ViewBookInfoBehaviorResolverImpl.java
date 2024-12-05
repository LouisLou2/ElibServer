package com.leo.elib.service.specific.impl.user_behavior;
import com.leo.elib.constant.user.UserBookBehavior;
import com.leo.elib.service.specific.inter.user_behavior.ViewBookInfoBehaviorResolver;
import com.leo.elib.usecase.inter.chart.book.TrendingBookManager;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ViewBookInfoBehaviorResolverImpl implements ViewBookInfoBehaviorResolver {

  @Resource
  private TrendingBookManager trendingBookManager;

  @Before("@annotation(com.leo.elib.constant.user.UserViewBookBehavior)")
  @Override
  public void resolveBehavior(JoinPoint joinPoint) {
    // 获取参数
    Object[] args = joinPoint.getArgs();
    var isbn = (String) args[0];
    assert isbn != null;
    trendingBookManager.reportUserBehavior(isbn, UserBookBehavior.View);
  }
}