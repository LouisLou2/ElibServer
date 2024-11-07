package com.leo.elib.mapper.type;

import com.leo.elib.constant.AnnounType;
import com.leo.elib.mapper.BaseEnumTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(value = {AnnounType.class})
@MappedJdbcTypes(value = {JdbcType.TINYINT})
public class AnnounTypeHandler extends BaseEnumTypeHandler<AnnounType> {
  public AnnounTypeHandler() {
    super(AnnounType.Other);
  }
}