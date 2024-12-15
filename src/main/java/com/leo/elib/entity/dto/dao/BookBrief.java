package com.leo.elib.entity.dto.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.config.ServiceNetConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookBrief implements Cloneable{
  private String isbn;

  private String title;

  @JsonProperty("short_desc")
  private String shortDesc;

  @JsonProperty("author_names")
  private List<String> authorNames;

  @JsonProperty("publisher_name")
  private String publisherName;

  @JsonProperty("cover_s_url")
  private String coverSUrl;

  @JsonProperty("cover_m_url")
  private String coverMUrl;

  @JsonProperty("cover_dom_color")
  private long coverDomColor;

  private short rating;

  @JsonProperty("has_ebook")
  private boolean hasEbook;

  @JsonIgnore
  private boolean urlSet = false;

  public void buildUrl() {
    if (urlSet) return;
    this.coverSUrl = ServiceNetConfig.equip(coverSUrl);
    this.coverMUrl = ServiceNetConfig.equip(coverMUrl);
    urlSet = true;
  }

  public BookBrief clone(){
    return new BookBrief(isbn, title, shortDesc, authorNames, publisherName, coverSUrl, coverMUrl, coverDomColor, rating, hasEbook, urlSet);
  }
}