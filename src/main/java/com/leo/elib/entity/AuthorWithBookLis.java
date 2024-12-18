package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.entity.dto.dao.Author;
import com.leo.elib.entity.dto.dao.BookBrief;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuthorWithBookLis implements Cloneable{
  @JsonIgnore
  int authorId;

  Author author;

  List<BookBrief> books;

  @JsonIgnore
  boolean urlSet = false;

  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("author", author);
    map.put("books", books);
    return map;
  }

  @SneakyThrows
  public AuthorWithBookLis clone(){
    AuthorWithBookLis cloned = new AuthorWithBookLis(authorId, author, null, urlSet);
    List<BookBrief> books = this.books.stream().map(BookBrief::clone).toList();
    cloned.setBooks(books);
    return cloned;
  }

  public AuthorWithBookLis cloneWithNewBooks(List<BookBrief> books){
    return new AuthorWithBookLis(authorId, author, books, urlSet);
  }

  public void buildUrl() {
    if (urlSet) return;
    books.forEach(BookBrief::buildUrl);
    urlSet = true;
  }
}