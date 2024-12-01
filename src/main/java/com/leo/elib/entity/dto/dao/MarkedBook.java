package com.leo.elib.entity.dto.dao;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.leo.elib.config.ServiceNetConfig;

import lombok.*;


import java.time.LocalDateTime;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MarkedBook {
  private String isbn;

  private String title;

  @JsonProperty("author_names")
  private List<String> authorNames;

  @JsonProperty("cover_s_url")
  private String coverSUrl;

  @JsonProperty("marked_time")
  private LocalDateTime markedTime;

  @JsonIgnore
  private boolean urlSet = false;

  public void buildUrl() {
    if (urlSet) return;
    this.coverSUrl = ServiceNetConfig.equip(coverSUrl);
    urlSet = true;
  }
}
