package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.config.ServiceNetConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Topic {

  @Getter
  @Setter
  private int id;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  @JsonProperty("cover_url")
  private String coverUrl;

  @JsonIgnore
  private boolean urlSet = false;

  public void buildUrl(){
    assert coverUrl!=null && !coverUrl.isEmpty();
    if(urlSet) return;
    coverUrl = ServiceNetConfig.equip(coverUrl);
    urlSet = true;
  }
}
