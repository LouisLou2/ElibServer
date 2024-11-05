package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
//@Document(indexName = "book_detailed_info")
public class SearchedBook {
  private String isbn;
  private String title;
  @JsonProperty("author_names")
  private List<String> authorNames;
  private int rating;
  @JsonProperty("cover_s_url")
  private String coverSUrl;
}
