package com.leo.elib.mapper;

import com.leo.elib.dto.dao.BookDaoForList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookInfoMapper {
  BookDaoForList getBookInfoByIsbn(String isbn);
  List<BookDaoForList> getBookInfoList(List<String> bookIsbns);
}
