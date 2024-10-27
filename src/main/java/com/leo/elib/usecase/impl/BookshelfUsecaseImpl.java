package com.leo.elib.usecase.impl;

import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.constant.book.ShelfBookEnum;
import com.leo.elib.entity.dto.dao.SimpleUserOwnedBook;
import com.leo.elib.mapper.BookshelfMapper;
import com.leo.elib.service.specific.inter.BookCache;
import com.leo.elib.usecase.inter.BookshelfUsecase;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class BookshelfUsecaseImpl implements BookshelfUsecase {
  @Value("${sys_setting.bookshelf.constraint}")
  short maxBookshelfSize;
  @Resource
  private BookshelfMapper bookshelfMapper;
  @Resource
  private BookCache bookCache;

  @Override
  public List<SimpleUserOwnedBook> getAllBooksFromShelf(int userId) {
    var lis = bookshelfMapper.getAllBooksFromShelf(userId);
    lis.forEach( 
      ele -> 
        ele.setCategory1Name(
          bookCache.getCategoryName(ele.getCategory1())
        )
    );
    return lis;
  }

  @Override
  public ResCodeEnum addBookToShelf(int userId, String isbn) {
    if (bookshelfMapper.numOfBooksInShelf(userId) >= maxBookshelfSize) {
      return ResCodeEnum.ShelfIsFull;
    }
    bookshelfMapper.addBookToShelf(userId, isbn, LocalDateTime.now());
    return ResCodeEnum.Success;
  }

  @Override
  public void removeBookFromShelf(int userId, List<String> isbns) {
    bookshelfMapper.removeBookFromShelf(userId, isbns);
  }

  @Override
  public void updateShelfBookPrivate(int userId, List<String> isbns) {
    bookshelfMapper.updateShelfBookStatus(userId, isbns, ShelfBookEnum.Private.getCode());
  }

  @Override
  public void updateShelfBookPublic(int userId, List<String> isbns) {
    bookshelfMapper.updateShelfBookStatus(userId, isbns, ShelfBookEnum.Public.getCode());
  }
}
