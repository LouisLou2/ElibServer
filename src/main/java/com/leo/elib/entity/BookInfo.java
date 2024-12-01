package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.config.ServiceNetConfig;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.entity.dto.dao.SimpleLib;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookInfo {
  private String isbn;

  private String title;

  @JsonProperty("original_title")
  private String originalTitle;

  @JsonProperty("publisher_id")
  private int publisherId;

  @JsonProperty("publisher_name")
  private String publisherName;

  @JsonProperty("pub_date")
  private Date pubDate;

  private String desc;

  @JsonProperty("short_desc")
  private String shortDesc;

  @JsonProperty("word_count")
  private short wordCount;

  @JsonProperty("lang_id")
  private short langId;

  @JsonProperty("lang_name")
  private String langName;

  @JsonProperty("cover_s_url")
  private String coverSUrl;

  @JsonProperty("cover_m_url")
  private String coverMUrl;

  @JsonProperty("cover_l_url")
  private String coverLUrl;

  @JsonProperty("cover_dom_color")
  private long coverDomColor;

  private short rating;

  @JsonProperty("ebookUrl")
  private String ebookUrl;

  private int category1;

  @JsonProperty("category1_name")
  private String category1Name;

  private int category2;

  @JsonProperty("category2_name")
  private String category2Name;

  @JsonProperty("author_ids")
  private List<Integer> authorIds;

  @JsonProperty("author_names")
  private List<String> authorNames;

  @JsonProperty("tag_ids")
  private List<Short> tagIds;

  @JsonProperty("tag_names")
  private List<String> tagNames;

  @JsonProperty("available_libs")
  private List<SimpleLib> availableLibs;

  @JsonIgnore
  private boolean urlSet = false;


  public void setNames(
      String langName,
      String category1Name,
      String category2Name,
      List<String> tagNames
  ) {
    this.langName = langName;
    this.category1Name = category1Name;
    this.category2Name = category2Name;
    this.tagNames = tagNames;
  }

  public BookBrief toBrief() {
    return new BookBrief(
        isbn,
        title,
        shortDesc,
        authorNames,
        publisherName,
        coverSUrl,
        coverMUrl,
        coverDomColor,
        rating,
        ebookUrl != null,
        urlSet
    );
  }

  public void buildUrl() {
    if (urlSet) return;
    this.coverSUrl = ServiceNetConfig.equip(coverSUrl);
    this.coverMUrl = ServiceNetConfig.equip(coverMUrl);
    this.coverLUrl = ServiceNetConfig.equip(coverLUrl);
    urlSet = true;
  }
}