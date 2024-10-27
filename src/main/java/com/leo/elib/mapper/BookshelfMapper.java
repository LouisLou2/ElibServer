package com.leo.elib.mapper;

import com.leo.elib.entity.dto.dao.SimpleUserOwnedBook;

import java.time.LocalDateTime;
import java.util.List;

public interface BookshelfMapper {
  void addBookToShelf(int userId, String isbn, LocalDateTime time);
  int removeBookFromShelf(int userId, List<String> isbns);
  int removeAllBooksFromShelf(int userId);
  boolean bookExistInShelf(int userId, String isbn);
  // 默认是根据时间排序的(最新的在前面)
  List<SimpleUserOwnedBook> getAllBooksFromShelf(int userId);
  short numOfBooksInShelf(int userId);
  void updateShelfBookStatus(int userId, List<String> isbns, byte status);
}
