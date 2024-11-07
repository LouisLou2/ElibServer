package com.leo.elib.usecase.inter;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.constant.book.ReservationStatus;
import com.leo.elib.entity.dto.dao.ReserveBriefRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface BookReserveUsecase {

  NullablePair<ResCodeEnum,String> reserveBook(int userId, int libId, String isbn, LocalDateTime pickUpTime);
  ResCodeEnum cancelReservation(int userId, int reserveId, boolean confirm);
  List<ReserveBriefRecord> getReserved(int userId, ReservationStatus status, int offset, int num);
}
