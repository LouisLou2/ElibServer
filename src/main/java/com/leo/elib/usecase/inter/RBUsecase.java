package com.leo.elib.usecase.inter;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.RBDetail;
import com.leo.elib.entity.ReserveBorrowBrief;

import java.time.LocalDateTime;
import java.util.List;

public interface RBUsecase {
  List<ReserveBorrowBrief> getBriefsByUserId(int userId, Byte status, int num, int offset);
  RBDetail getDetailsByReserveId(int reserveId);
  NullablePair<ResCodeEnum, RBDetail> reserve(int userId, int libId, String isbn, LocalDateTime deadline);
}
