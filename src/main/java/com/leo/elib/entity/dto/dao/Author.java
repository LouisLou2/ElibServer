package com.leo.elib.entity.dto.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Author {
    @JsonProperty("author_id")
    private Integer authorId;
    private String name;
    private String desc;
    @JsonProperty("book_count")
    private int bookCount; // 书籍总数
}