package com.leo.elib.mapper;

import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.BookInfo;
import com.leo.elib.entity.CateBookNum;
import com.leo.elib.entity.TmpBookCover;
import com.leo.elib.entity.dto.dao.BookBrief;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookInfoMapper {
  BookInfo getBookInfoWithoutLibs(String isbn);

  /*
   * 注意返回的元素顺序不保证与参数中的isbn顺序一致
   */
  List<BookInfo> getBookInfoWithoutLibsList(List<String> isbns);

  /*
  * 注意返回的元素顺序不保证与参数中的isbn顺序一致
  */
  List<BookBrief> getBookBriefList(List<String> isbns);


  // TODO: 目前默认是按照评分降序得到的
  List<BookBrief> getCategoryBriefBooks(byte categoryLevel, int cateId, int offset, int num);
  // TODO: 目前是根据评分降序排列
  List<BookBrief> getAuthorBooks(int authorId, int offset, int num);
  AuthorWithBookLis getAuthorWithBooks(int authorId, int num);
  // List<BookInfo> getBooksByRating(int num);
  List<BookBrief> getBookBriefsByRating(int num, int offset);


  // 注意这个方法是为了调试用的
  List<BookInfo> debug_getBookInfo(int offset, int num);
  List<String> debug_getIsbnsByTime(int offset, int num);
  List<TmpBookCover> debug_getTmpBookCover(int offset, int num);
  void debug_setTmpBookCoverAndShortDesc(String isbn, String coverUrl, String shortDesc);
  List<String> dev_getIsbn(int offset, int num);
  void debug_setColor(String isbn, long color);
  List<CateBookNum> debug_getCateBookNum();
  List<CateBookNum> debug_getSubCateBookNum();
}