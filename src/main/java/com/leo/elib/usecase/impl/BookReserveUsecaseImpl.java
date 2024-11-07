package com.leo.elib.usecase.impl;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.constant.book.LibBookStatus;
import com.leo.elib.constant.book.ReservationStatus;
import com.leo.elib.constant.user.UserReserveBookBehavior;
import com.leo.elib.entity.dto.dao.ReserveBriefRecord;
import com.leo.elib.mapper.LibBorrowMapper;
import com.leo.elib.mapper.ReservationMapper;
import com.leo.elib.usecase.inter.BookReserveUsecase;
import com.leo.elib.usecase.inter.UserRestrictionInspector;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookReserveUsecaseImpl implements BookReserveUsecase {

  @Value("${sys_setting.reservation.max_try_times}")
  private int maxTry;
  @Resource
  private LibBorrowMapper libBorrowMapper;
  @Resource
  private ReservationMapper reservationMapper;
  @Resource
  private UserRestrictionInspector userRestrictionInspector;

  private static NullablePair<ResCodeEnum,String> reserveNotAvailable = NullablePair.of(ResCodeEnum.ReserveNotAvailable);
  private static NullablePair<ResCodeEnum,String> reserveSuccess = NullablePair.of(ResCodeEnum.Success);
  private static NullablePair<ResCodeEnum,String> reserveFailed = NullablePair.of(ResCodeEnum.ReserveFailed);

  @UserReserveBookBehavior
  @Override
  public NullablePair<ResCodeEnum,String> reserveBook(int userId, int libId, String isbn, LocalDateTime pickUpTime) {
    // 检查用户是否有预约书籍的资格
    var res = userRestrictionInspector.canUserReserveABook(userId);
    if (res.getFirst() != ResCodeEnum.Success) {
      return res;
    }
    // 拿到一本书的唯一标识
    Integer bookUnqId = libBorrowMapper.getOneBookUniqueId(
      isbn, libId,
      LibBookStatus.Available.getCode()
    );
    if (bookUnqId == null) {
      return reserveNotAvailable;
    }
    // 锁定这本书
    int lockResult = libBorrowMapper.setStatusWithOriginalStatus(
      bookUnqId,
      LibBookStatus.Reserved.getCode(),
      LibBookStatus.Available.getCode()
    );
    if (lockResult == 0) {
      int tryTimes = 0;
      while (lockResult == 0 && tryTimes < maxTry) {
        bookUnqId = libBorrowMapper.getOneBookUniqueId(
          isbn, libId,
          LibBookStatus.Available.getCode()
        );
        if (bookUnqId == null) {
          return reserveNotAvailable;
        }
        lockResult = libBorrowMapper.setStatusWithOriginalStatus(
          bookUnqId,
          LibBookStatus.Reserved.getCode(),
          LibBookStatus.Available.getCode()
        );
        ++tryTimes;
      }
      if (lockResult == 0) {
        return reserveFailed;
      }
    }
    // 可以预约这本书
    reservationMapper.insertRecord(
      userId,
      bookUnqId,
      libId,
      isbn,
      ReservationStatus.Pending.getCode(),
      LocalDateTime.now(),
      pickUpTime
    );
    return reserveSuccess;
  }

  @Override
  public ResCodeEnum cancelReservation(int userId, int reserveId, boolean confirm) {
    return null;//TODO:g
  }

  @Override
  public List<ReserveBriefRecord> getReserved(int userId, ReservationStatus status, int offset, int num) {
    return reservationMapper.getReserved(userId, status, offset, num);
  }
}