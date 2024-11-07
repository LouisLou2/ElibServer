package com.leo.elib.mapper;

import com.leo.elib.constant.BaseCodeEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BaseEnumTypeHandler <E extends BaseCodeEnum> extends BaseTypeHandler<E> {
  private final E[] enums;

  public BaseEnumTypeHandler(E instance) {
    assert instance != null;
    enums = (E[]) instance.getCodeEnums();
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws java.sql.SQLException {
    ps.setByte(i, parameter.getCode());
  }

  @Override
  public E getNullableResult(ResultSet rs, String columnName) throws java.sql.SQLException {
    byte code = rs.getByte(columnName);
    if (rs.wasNull()) {
      return null;
    } else {
      return locateEnumStatus(code);
    }
  }

  @Override
  public E getNullableResult(ResultSet rs, int columnIndex) throws java.sql.SQLException {
    byte code = rs.getByte(columnIndex);
    if (rs.wasNull()) {
      return null;
    } else {
      return locateEnumStatus(code);
    }
  }

  @Override
  public E getNullableResult(CallableStatement cs, int columnIndex) throws java.sql.SQLException {
    byte code = cs.getByte(columnIndex);
    if (cs.wasNull()) {
      return null;
    } else {
      return locateEnumStatus(code);
    }
  }

  private E locateEnumStatus(byte code) {
    return enums[code];
  }
}