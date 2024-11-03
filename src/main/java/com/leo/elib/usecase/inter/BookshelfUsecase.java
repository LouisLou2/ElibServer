package com.leo.elib.usecase.inter;

import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.SimpleUserOwnedBook;

import java.util.List;

public interface BookshelfUsecase {
   List<SimpleUserOwnedBook> getAllBooksFromShelf(int userId);
   ResCodeEnum addBookToShelf(int userId, String isbn);
   void removeBookFromShelf(int userId, List<String> isbns);
   void updateShelfBookPrivate(int userId, List<String> isbns);
   void updateShelfBookPublic(int userId, List<String> isbns);
}
