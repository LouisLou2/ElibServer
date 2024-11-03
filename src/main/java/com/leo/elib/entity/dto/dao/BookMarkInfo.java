package com.leo.elib.entity.dto.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookMarkInfo {
  private int count;
  @JsonProperty("marked_books")
  private List<MarkedBook> markedBooks;
}
