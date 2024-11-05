package com.leo.elib.mapper;

import com.leo.elib.entity.dto.dao.SimpleLib;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface LibBorrowMapper {
  List<SimpleLib> getLibsWithStatus(String isbn, byte status);
  Integer getOneBookUniqueId(String isbn, int libId, byte status);
  // 返回值为1表示成功锁定一本书，返回值为0表示锁定失败
  int setStatusWithOriginalStatus(int bookUnqId, byte status, byte originalStatus);
}
