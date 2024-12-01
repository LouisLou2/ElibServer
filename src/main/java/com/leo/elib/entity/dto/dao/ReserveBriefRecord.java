package com.leo.elib.entity.dto.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.config.ServiceConstConfig;
import com.leo.elib.config.ServiceNetConfig;
import com.leo.elib.constant.book.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReserveBriefRecord {

  @JsonProperty("reserved_id")
  private int reservedId;

  private String title;

  @JsonProperty("cover_s_url")
  private String coverSUrl;

  private ReservationStatus status;

  @JsonProperty("pick_up_time")
  private LocalDateTime pickUpTime;

  @JsonIgnore
  private boolean urlSet = false;

  public void buildUrl() {
    if (urlSet) return;
    this.coverSUrl = ServiceNetConfig.equip(coverSUrl);
    urlSet = true;
  }

}
