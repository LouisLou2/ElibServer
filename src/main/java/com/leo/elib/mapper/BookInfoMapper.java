package com.leo.elib.mapper;

import com.leo.elib.entity.dto.dao.Author;
import com.leo.elib.entity.dto.dao.AuthorWithBookDaoLis;
import com.leo.elib.entity.dto.dao.BookDaoForList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookInfoMapper {
  BookDaoForList getBookInfoByIsbn(String isbn);
  List<BookDaoForList> getBookInfoList(List<String> bookIsbns);
  
  // TODO: 目前默认是按照评分降序得到的
  List<BookDaoForList> getCategoryBooks(byte categoryLevel, int cateId, int offset, int num);
  Author getAuthor(int authorId);
  // TODO: 目前是根据评分降序排列
  List<BookDaoForList> getAuthorBooks(int authorId, int offset, int num);
  
  AuthorWithBookDaoLis getAuthorWithBooks(int authorId, int num);
}