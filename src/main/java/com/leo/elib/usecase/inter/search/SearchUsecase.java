package com.leo.elib.usecase.inter.search;

import com.leo.elib.entity.SearchedAuthor;
import com.leo.elib.entity.SearchedBook;
import com.leo.elib.entity.SearchedPublisher;

import java.util.List;

public interface SearchUsecase {
  List<SearchedBook> searchInBooks(String keyword, int pageNum, int pageSize);
  List<SearchedAuthor> searchInAuthors(String keyword, int pageNum, int pageSize);
  List<SearchedPublisher> searchInPublishers(String keyword, int pageNum, int pageSize);
}
