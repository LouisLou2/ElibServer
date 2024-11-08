package com.leo.elib.entity.dto.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookBrief {
  private String isbn;
  private String title;
  @JsonProperty("author_names")
  private List<String> authorNames;
  @JsonProperty("publisher_name")
  private String publisherName;
  @JsonProperty("cover_s_url")
  private String coverSUrl;
  @JsonProperty("cover_m_url")
  private String coverMUrl;
  private short rating;
  @JsonProperty("has_ebook")
  private boolean hasEbook;
}