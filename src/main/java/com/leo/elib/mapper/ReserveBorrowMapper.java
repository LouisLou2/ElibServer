package com.leo.elib.mapper;

import com.leo.elib.constant.book.ReservationStatus;
import com.leo.elib.entity.ReserveBorrowBrief;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReserveBorrowMapper {
  List<ReserveBorrowBrief> getBriefsByUserId(int userId, Byte status, int num, int offset);
}