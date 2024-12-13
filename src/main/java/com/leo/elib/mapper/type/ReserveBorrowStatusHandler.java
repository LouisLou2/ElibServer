package com.leo.elib.mapper.type;

import com.leo.elib.constant.book.ReserveBorrowStatus;
import com.leo.elib.mapper.BaseEnumTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(value = {ReserveBorrowStatus.class})
@MappedJdbcTypes(value = {JdbcType.TINYINT})
public class ReserveBorrowStatusHandler extends BaseEnumTypeHandler<ReserveBorrowStatus> {
  public ReserveBorrowStatusHandler() {
    super(ReserveBorrowStatus.Cancelled);
  }
}
