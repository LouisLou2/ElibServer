package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.config.ServiceNetConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookCate {
  @JsonProperty("cate_id")
  private int cateId;
  @JsonProperty("cate_name")
  private String cateName;
  @JsonProperty("cover_url")
  private String coverUrl;
  @JsonProperty("book_num")
  private int bookNum;
  @JsonProperty("dom_color")
  private long domColor;

  @JsonIgnore
  private boolean urlSet = false;

  public void buildUrl(){
    assert coverUrl != null;
    if(urlSet)return;
    coverUrl = ServiceNetConfig.equip(coverUrl);
    urlSet = true;
  }

  public static void buildUrlForLis(List<BookCate> lis){
    assert lis != null;
    lis.forEach(BookCate::buildUrl);
  }
}
