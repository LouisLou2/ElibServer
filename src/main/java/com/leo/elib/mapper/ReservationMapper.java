package com.leo.elib.mapper;

import com.leo.elib.constant.book.ReservationStatus;
import com.leo.elib.entity.dto.dao.ReserveBriefRecord;
import com.leo.elib.entity.dto.dao.ReserveRecord;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReservationMapper {
  void insertRecord(int userId, int bookUnqId, int libId, String isbn, byte status, LocalDateTime reserveTime, LocalDateTime pickUpTime);
  void updateStatus(int reserveId, byte status);

  // 现在用户等待取书的数量
  int getUserRecordWithStatus(int userId, byte status);

  List<ReserveBriefRecord> getReserved(int userId, ReservationStatus status, int offset, int num);

  ReserveRecord getReserveRecord(int reserveId);
}
