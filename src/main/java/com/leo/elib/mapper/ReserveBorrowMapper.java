package com.leo.elib.mapper;

import com.leo.elib.constant.book.ReservationStatus;
import com.leo.elib.constant.book.ReserveBorrowStatus;
import com.leo.elib.entity.RBDetail;
import com.leo.elib.entity.ReserveBorrowBrief;
import com.leo.elib.entity.ReserveParam;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReserveBorrowMapper {
  List<ReserveBorrowBrief> getBriefsByUserId(int userId, Byte status, int num, int offset);
  RBDetail getDetailsByReserveId(int reserveId);
  int insertRecord(ReserveParam rParams);
}