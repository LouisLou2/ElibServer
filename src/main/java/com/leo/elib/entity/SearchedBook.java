package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.config.ServiceNetConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
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

  @JsonIgnore
  private boolean urlSet = false;

  public void buildUrl() {
    if (urlSet) return;
    this.coverSUrl = ServiceNetConfig.equip(coverSUrl);
    urlSet = true;
  }
}
