package com.leo.elib.service.specific.inter;

import com.leo.elib.dto.resp.BookInfoForList;

import java.util.List;

public interface BookInfoProvider {
  BookInfoForList getBookInfo(String isbn);
  List<BookInfoForList> getBookInfoList(List<String> bookIsbns);
  List<BookInfoForList> getRecoBooks(int userId, int offset, int num);
}
