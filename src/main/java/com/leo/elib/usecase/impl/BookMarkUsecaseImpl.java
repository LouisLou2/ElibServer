package com.leo.elib.usecase.impl;

import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.dto.dao.BookMarkInfo;
import com.leo.elib.entity.dto.dao.MarkedBook;
import com.leo.elib.mapper.BookMarkMapper;
import com.leo.elib.usecase.inter.BookMarkUsecase;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookMarkUsecaseImpl implements BookMarkUsecase {
  @Value("${sys_setting.book_mark.constraint}")
  private int maxBookMarkNum;
  @Resource
  private BookMarkMapper bookMarkMapper;

  public ResCodeEnum markBook(String isbn, int userId) {
    // 检查是否超过最大收藏数
    var num = bookMarkMapper.numOfBookMarks(userId);
    if (num >= maxBookMarkNum) {
      return ResCodeEnum.BookCollectionIsFull;
    }
    // 收藏书籍
    if (bookMarkMapper.insertBookMark(userId, isbn, LocalDateTime.now()) == 1) {
      return ResCodeEnum.Success;
    }
    return ResCodeEnum.AlreadyCollected;
  }

  @Override
  public ResCodeEnum unmarkBook(String isbn, int userId) {
    if (bookMarkMapper.deleteBookMark(userId, isbn) == 1) {
      return ResCodeEnum.Success;
    }
    return ResCodeEnum.BookNotCollected;
  }

  @Override
  public List<MarkedBook> getBookMarks(int userId, int offset, int num) {
    return bookMarkMapper.getBookMarks(userId, offset, num);
  }

  @Override
  public BookMarkInfo getBookMarkInfo(int userId, int withNum) {
    return bookMarkMapper.getBookMarkInfo(userId, withNum);
  }
}