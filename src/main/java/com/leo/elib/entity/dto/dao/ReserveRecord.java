package com.leo.elib.entity.dto.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.constant.book.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
public class ReserveRecord {
  @JsonProperty("reserve_id")
  private int reserveId;

  private String isbn;
  private String title;
  @JsonProperty("one_author_name")
  private String oneAuthorName;
  @JsonProperty("cover_s_url")
  private String coverSUrl;

  private ReservationStatus status;

  @JsonProperty("lib_id")
  private int libId;

  @JsonProperty("lib_name")
  private String libName;

  private String location;

  @JsonProperty("reserve_time")
  private LocalDateTime reserveTime;

  private LocalDateTime deadline;
}
