package com.leo.elib.mapper;

import com.leo.elib.entity.dto.dao.SimpleLib;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface LibBorrowMapper {
  List<SimpleLib> getLibsWithStatus(String isbn, byte status);
}
