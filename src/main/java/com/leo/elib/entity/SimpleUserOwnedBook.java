package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.config.ServiceNetConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SimpleUserOwnedBook {

  @JsonProperty("shelf_ele_id")
  private int shelfEleId;

  private String isbn;

  private String title;

  @JsonProperty("cover_m_url")
  private String coverMUrl;

  LocalDateTime time;

  private int category1;

  // 此字段不从数据库取，数据库不需要管这个字段，它空即可
  @JsonProperty("category1_name")
  private String category1Name;

  private byte status;

  @JsonIgnore
  private boolean urlSet = false;

  public void buildUrl() {
    if (urlSet) return;
    this.coverMUrl = ServiceNetConfig.equip(coverMUrl);
    urlSet = true;
  }
}
