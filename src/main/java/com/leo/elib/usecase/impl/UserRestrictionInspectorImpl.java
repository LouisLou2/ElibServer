package com.leo.elib.usecase.impl;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.constant.book.ReserveBorrowStatus;
import com.leo.elib.entity.UserRestriction;
import com.leo.elib.mapper.UserMapper;
import com.leo.elib.service.specific.inter.ErrorMessageReporter;
import com.leo.elib.usecase.inter.UserRestrictionInspector;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserRestrictionInspectorImpl implements UserRestrictionInspector{

  @Value("${sys_setting.reservation.reserve_and_unreturn_limit}")
  private int reserveAndUnreturnLimit;

  @Value("${sys_setting.reservation.max_abnormal_per_month}")
  private short maxAbnormalPerMonth;

  @Value("${sys_setting.reservation.max_cancellation_per_month}")
  private short maxCancellationPerMonth;

  @Value("${sys_setting.reservation.max_keep_num}")
  private int maxKeepNum;

  @Value("${sys_setting.reservation.over_due_days}")
  private int overDueDays;

  @Value("${sys_setting.reservation.max_pickup_days}")
  private short maxPickupDays;

  @Resource
  private UserMapper userMapper;

//  @Resource
//  private ErrorMessageReporter reporter;

  private ResCodeEnum giveOneResCodeEnum(UserRestriction rest) {
    if (rest.isRestricted())
      return ResCodeEnum.BeenRestricted;
    if (rest.getCancelledTimesWithin() >= maxCancellationPerMonth)
      return ResCodeEnum.CancelledTooMuch;
    if (rest.getAbnormalPickReturnWithin() >= maxAbnormalPerMonth)
      return ResCodeEnum.TooMuchOverdue;
    if (rest.getSumOfPickWaitedReturnWaited() >= reserveAndUnreturnLimit)
      return ResCodeEnum.TooMuchReservedOrUnreturned;
    return ResCodeEnum.Success;
  }

  @Override
  public ResCodeEnum canUserReserveABook(int userId) {
    UserRestriction res = userMapper.getUserRestriction(
      userId,
      LocalDateTime.now().minusMonths(1),
      ReserveBorrowStatus.WaitingPickUp,
      ReserveBorrowStatus.OverduePickUp,
      ReserveBorrowStatus.Cancelled,
      ReserveBorrowStatus.WaitingReturn,
      ReserveBorrowStatus.OverdueReturn,
      ReserveBorrowStatus.CantReturn
    );
    return giveOneResCodeEnum(res);
  }
}