package com.leo.elib.mapper;

import com.leo.elib.entity.dto.dao.BookMarkInfo;
import com.leo.elib.entity.dto.dao.MarkedBook;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
@Mapper
public interface BookMarkMapper {
  List<MarkedBook> getBookMarks(int userId, int offset, int num);
  int insertBookMark(int userId, String isbn, LocalDateTime markedTime);
  int deleteBookMark(int userId, String isbn);
  int numOfBookMarks(int userId);
  BookMarkInfo getBookMarkInfo(int userId, int withNum);
}
