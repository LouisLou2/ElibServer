package com.leo.elib.usecase.impl;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.constant.book.LibBookStatus;
import com.leo.elib.constant.book.ReserveBorrowStatus;
import com.leo.elib.entity.LibTimeSpan;
import com.leo.elib.entity.RBDetail;
import com.leo.elib.entity.ReserveBorrowBrief;
import com.leo.elib.entity.ReserveParam;
import com.leo.elib.entity.dto.dao.SimpleLib;
import com.leo.elib.entity.struct.TimeSpan;
import com.leo.elib.mapper.LibBorrowMapper;
import com.leo.elib.mapper.ReserveBorrowMapper;
import com.leo.elib.usecase.inter.RBUsecase;
import com.leo.elib.usecase.inter.UserRestrictionInspector;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RBUsecaseImpl implements RBUsecase {

  @Resource
  private ReserveBorrowMapper rbMapper;

  @Value("${sys_setting.reservation.over_due_days}")
  private int overdueDaysFromReserve;
  @Resource
  private LibBorrowMapper libBorrowMapper;
  @Resource
  private UserRestrictionInspector userRestrictionInspector;

  private static NullablePair<ResCodeEnum,RBDetail> reserveNotAvailable = NullablePair.of(ResCodeEnum.ReserveNotAvailable);
  private static NullablePair<ResCodeEnum,RBDetail> reserveFailed = NullablePair.of(ResCodeEnum.ReserveFailed);

  @Override
  public List<ReserveBorrowBrief> getBriefsByUserId(int userId, Byte status, int num, int offset) {
    return rbMapper.getBriefsByUserId(userId, status, num, offset);
  }

  @Override
  public RBDetail getDetailsByReserveId(int reserveId) {
    return rbMapper.getDetailsByReserveId(reserveId);
  }

  @Override
  public NullablePair<ResCodeEnum, RBDetail> reserve(int userId, int libId, String isbn, LocalDateTime deadline) {
    // 检查用户是否有预约书籍的资格
    ResCodeEnum res = userRestrictionInspector.canUserReserveABook(userId);
    if (res != ResCodeEnum.Success) {
      return NullablePair.of(res);
    }
    // 拿到一本书的唯一标识
    Integer bookUnqId = libBorrowMapper.markBookAsReserved(isbn, libId, LibBookStatus.Available.getCode(), LibBookStatus.Reserved.getCode());
    if (bookUnqId == null) {
      return reserveNotAvailable;
    }
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime dueTime = now.plusDays(overdueDaysFromReserve);
    ReserveParam rParams = new ReserveParam();
    rParams.setUserId(userId);
    rParams.setReserveTime(now);
    rParams.setDeadline(deadline);
    rParams.setIsbn(isbn);
    rParams.setStatus(ReserveBorrowStatus.WaitingPickUp);
    rParams.setBookUnqId(bookUnqId);
    rParams.setLibId(libId);
    rParams.setDueTime(dueTime);
    rbMapper.insertRecord(rParams);
    if (rParams.getReserveId() == null) {
      return reserveFailed;
    }
    RBDetail detail = rbMapper.getDetailsByReserveId(rParams.getReserveId());
    return new NullablePair<>(ResCodeEnum.Success, detail);
  }

  @Override
  public List<LibTimeSpan> getReserveTimeSpans(String isbn) {
    List<SimpleLib> libs = libBorrowMapper.getLibsWithStatus(isbn, LibBookStatus.Available.getCode());
    return getReserveTimeSpans(libs);
  }

  private List<LibTimeSpan> getReserveTimeSpans(List<SimpleLib> libs) {
    // 当前时间
    LocalTime now = LocalTime.now();
    List<TimeSpan> timeSpans = new ArrayList<>();

    // 如果当前时间超过了22:00
    if (now.isAfter(LocalTime.of(22, 0))) {
      // 从明天开始加入时间段
      timeSpans.add(createTimeSpan(1)); // 明天7:00到22:00
      timeSpans.add(createTimeSpan(2)); // 明天7:00到22:00
      timeSpans.add(createTimeSpan(3)); // 明天7:00到22:00
    } else {
      // 今天从现在到22:00
      timeSpans.add(createTimeSpan(0)); // 今天从现在到22:00
      // 明天从7:00到22:00
      timeSpans.add(createTimeSpan(1)); // 明天7:00到22:00
      timeSpans.add(createTimeSpan(2)); // 明天7:00到22:00
    }
    List<LibTimeSpan> libTimeSpans = new ArrayList<>();
    for (SimpleLib lib : libs) {
      LibTimeSpan lts = new LibTimeSpan();
      lts.setLibId(lib.getLibId());
      lts.setLibName(lib.getName());
      lts.setTimeSpans(timeSpans);
      libTimeSpans.add(lts);
    }
    return libTimeSpans;
  }

  // 根据传入的天数创建 TimeSpan
  private static TimeSpan createTimeSpan(int dayOffset) {
    TimeSpan timeSpan = new TimeSpan();
    LocalDateTime startTime;
    LocalDateTime endTime;
    if (dayOffset == 0) {
      // 今天的时间段
      startTime = LocalDateTime.now();
      endTime = LocalDateTime.now().withHour(22).withMinute(0);
    } else {
      // 明天的时间段
      startTime = LocalDateTime.now().plusDays(dayOffset).withHour(7).withMinute(0);
      endTime = LocalDateTime.now().plusDays(dayOffset).withHour(22).withMinute(0);
    }
    timeSpan.setDate(java.sql.Date.valueOf(startTime.toLocalDate())); // 设置日期
    timeSpan.setHourBegin((byte) startTime.getHour() < 7 ? 7 : (byte) startTime.getHour());  // 设置开始小时
    timeSpan.setHourEnd((byte) endTime.getHour());  // 设置结束小时
    return timeSpan;
  }
}
