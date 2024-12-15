package com.leo.elib.usecase.impl;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.constant.book.LibBookStatus;
import com.leo.elib.constant.book.ReservationStatus;
import com.leo.elib.constant.book.ReserveBorrowStatus;
import com.leo.elib.entity.RBDetail;
import com.leo.elib.entity.ReserveBorrowBrief;
import com.leo.elib.entity.ReserveParam;
import com.leo.elib.mapper.LibBorrowMapper;
import com.leo.elib.mapper.ReservationMapper;
import com.leo.elib.mapper.ReserveBorrowMapper;
import com.leo.elib.usecase.inter.RBUsecase;
import com.leo.elib.usecase.inter.UserRestrictionInspector;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RBUsecaseImpl implements RBUsecase {

  @Resource
  private ReserveBorrowMapper rbMapper;

  @Value("${sys_setting.reservation.max_try_times}")
  private int maxTry;
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
    var res = userRestrictionInspector.canUserReserveABook(userId);
    if (res.getFirst() != ResCodeEnum.Success) {
      return NullablePair.of(res.getFirst());
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
}
