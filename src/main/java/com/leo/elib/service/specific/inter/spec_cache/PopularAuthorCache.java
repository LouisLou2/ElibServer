package com.leo.elib.service.specific.inter.spec_cache;

import com.leo.elib.entity.dto.dao.Author;

import java.util.List;

public interface PopularAuthorCache {
  void cacheAuthor(Author author);
  Author getAuthor(int authorId);
  List<Author> getAuthors(List<Integer> authorIds);
}
