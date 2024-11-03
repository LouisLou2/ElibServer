package com.leo.elib.entity.dto.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MarkedBook {
  private String isbn;
  private String title;
  @JsonProperty("author_names")
  private List<String> authorNames;
  @JsonProperty("cover_s_url")
  private String coverSUrl;
  @JsonProperty("marked_time")
  private LocalDateTime markedTime;
}
