package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.config.ServiceNetConfig;
import com.leo.elib.constant.book.ReservationStatus;
import com.leo.elib.constant.book.ReserveBorrowStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReserveBorrowBrief {
  @JsonProperty("reserve_id")
  private int reserveId;
  private ReserveBorrowStatus status;
  private String title;
  private String isbn;

  @JsonProperty("reserve_time")
  private LocalDateTime reservedTime;

  @JsonProperty("pick_up_deadline")
  private LocalDateTime pickUpDeadline;
  @JsonProperty("due_time")
  private LocalDateTime dueTime;

  @JsonProperty("cover_m_url")
  private String coverMUrl;

  @JsonIgnore
  private boolean urlSet = false;

  public void buildUrl() {
    assert coverMUrl!=null;
    if (urlSet) return;
    coverMUrl = ServiceNetConfig.equip(coverMUrl);
    urlSet = true;
  }
}
