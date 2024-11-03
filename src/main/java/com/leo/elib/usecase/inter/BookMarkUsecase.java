package com.leo.elib.usecase.inter;

import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.dto.dao.BookMarkInfo;
import com.leo.elib.entity.dto.dao.MarkedBook;

import java.util.List;

public interface BookMarkUsecase {
  ResCodeEnum markBook(String isbn, int userId);
  ResCodeEnum unmarkBook(String isbn, int userId);
  List<MarkedBook> getBookMarks(int userId, int offset, int num);
  BookMarkInfo getBookMarkInfo(int userId, int withNum);
}
