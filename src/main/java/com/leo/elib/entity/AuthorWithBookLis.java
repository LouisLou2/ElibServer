package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.entity.dto.dao.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuthorWithBookLis {
  @JsonProperty("author_id")
  private Integer authorId;
  private String name;
  private String desc;
  @JsonProperty("book_count")
  private int bookCount; // 书籍总数
  List<BookInfoForList> books;
  
  public AuthorWithBookLis(Author author, List<BookInfoForList> books) {
    this.authorId = author.getAuthorId();
    this.name = author.getName();
    this.desc = author.getDesc();
    this.bookCount = author.getBookCount();
    this.books = books;
  }
}