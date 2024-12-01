package com.leo.elib.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.config.ServiceNetConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class TmpBookCover {

  private String isbn;

  @JsonProperty("cover_url")
  private String coverUrl;

  @JsonIgnore
  private boolean urlSet = false;

  public void buildUrl() {
    if(urlSet) return;
    this.coverUrl = ServiceNetConfig.equip(coverUrl);
    urlSet = true;
  }
}
