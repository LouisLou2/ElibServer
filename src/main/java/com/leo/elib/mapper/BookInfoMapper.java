package com.leo.elib.mapper;

import com.leo.elib.dto.dao.BookDaoForList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookInfoMapper {
  BookDaoForList getBookInfoByIsbn(String isbn);
}
