package com.leo.elib.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BorrowMapper {

  int recordNumWithStatus(int userId, byte status);
}
