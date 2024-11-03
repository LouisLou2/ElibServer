package com.leo.elib.mapper;

import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.BookInfo;
import com.leo.elib.entity.dto.dao.BookBrief;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookInfoMapper {
  BookInfo getBookInfoByIsbn(String isbn);
  List<BookBrief> getBookBriefList(List<String> isbns);
  // TODO: 目前默认是按照评分降序得到的
  List<BookBrief> getCategoryBriefBooks(byte categoryLevel, int cateId, int offset, int num);
  // TODO: 目前是根据评分降序排列
  List<BookBrief> getAuthorBooks(int authorId, int offset, int num);
  
  AuthorWithBookLis getAuthorWithBooks(int authorId, int num);
}