package com.leo.elib.usecase.impl;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.constant.book.BorrowStatus;
import com.leo.elib.constant.book.ReservationStatus;
import com.leo.elib.entity.UserRestriction;
import com.leo.elib.mapper.UserMapper;
import com.leo.elib.service.specific.inter.ErrorMessageReporter;
import com.leo.elib.usecase.inter.UserRestrictionInspector;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserRestrictionInspectorImpl implements UserRestrictionInspector{

  @Value("${sys_setting.reservation.reserve_and_unreturn_limit}")
  private short reserveOrUnreturnedBookLimit;

  @Resource
  private UserMapper userMapper;

  @Resource
  private ErrorMessageReporter reporter;

  @Override
  public NullablePair<ResCodeEnum,String> canUserReserveABook(int userId) {
    UserRestriction res = userMapper.getUserRestriction(
      userId,
      ReservationStatus.Pending.getCode(),
      BorrowStatus.UserReturned.getCode()
    );
    if (res.isRestricted())
      return NullablePair.of(ResCodeEnum.BeenRestricted);

    if (res.getOverdueTimesThisMonth() >= reserveOrUnreturnedBookLimit)
      return reporter.getSpecificMsgWithCode(ResCodeEnum.TooMuchOverdue);

    if (res.getReservedButUnpicked() + res.getBorrowedButUnreturned() >= reserveOrUnreturnedBookLimit)
      return reporter.getSpecificMsgWithCode(ResCodeEnum.TooMuchReservedOrUnreturned);

    return NullablePair.of(ResCodeEnum.Success);
  }
}