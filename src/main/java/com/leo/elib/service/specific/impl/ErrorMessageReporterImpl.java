//package com.leo.elib.service.specific.impl;
//
//import com.leo.elib.comp_struct.NullablePair;
//import com.leo.elib.constant.ResCodeEnum;
//import com.leo.elib.service.specific.inter.ErrorMessageReporter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ErrorMessageReporterImpl implements ErrorMessageReporter{
//
//  @Value("${sys_setting.reservation.reserve_and_unreturn_limit}")
//  private short reserveOrUnreturnedBookLimit;
//
//  @Value("${sys_setting.reservation.max_overdue_per_month}")
//  private short maxOverdueTimes;
//
//  @Override
//  public String getSpecificMsg(ResCodeEnum resCode) {
//    // 一般情况，直接不添加详细信息
//    switch (resCode) {
//      case TooMuchOverdue -> {
//        return "用户本月超期未还的书籍数量已经达到" + maxOverdueTimes + "本";
//      }
//      case TooMuchReservedOrUnreturned -> {
//        return "失败，预约未取或未还的书籍数超过" + reserveOrUnreturnedBookLimit + "本";
//      }
//      default -> {
//        return null;
//      }
//    }
//  }
//
//  @Override
//  public NullablePair<ResCodeEnum, String> getSpecificMsgWithCode(ResCodeEnum resCode) {
//    String msg = getSpecificMsg(resCode);
//    return new NullablePair<>(resCode, msg);
//  }
//}
