package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//@Document(indexName = "author")
public class SearchedAuthor {

  @JsonProperty("author_id")
  private Integer authorId;

  private String name;

  @JsonProperty("book_count")
  private int bookCount; // 书籍总数
}