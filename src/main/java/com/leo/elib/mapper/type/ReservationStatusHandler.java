package com.leo.elib.mapper.type;

import com.leo.elib.constant.book.ReservationStatus;
import com.leo.elib.mapper.BaseEnumTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(value = {ReservationStatus.class})
@MappedJdbcTypes(value = {JdbcType.TINYINT})
public class ReservationStatusHandler extends BaseEnumTypeHandler<ReservationStatus> {
  public ReservationStatusHandler() {
    super(ReservationStatus.Pending);
  }
}