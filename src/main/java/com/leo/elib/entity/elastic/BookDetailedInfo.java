package com.leo.elib.entity.elastic;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leo.elib.config.ServiceNetConfig;
import com.leo.elib.entity.BookInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(indexName = "book_detailed_info")
public class BookDetailedInfo {

  public static final List<String> worthSearchFriendlyFields = List.of(
    "title", "originalTitle",
    "publisherName", "category1Name",
    "category2Name", "authorNames", "tagNames"
  );

  @Id
  @Field(type = FieldType.Keyword)
  private String isbn;

  @Field(type = FieldType.Text)
  private String title;

  @Field(type = FieldType.Text)
  private String originalTitle;

  @Field(type = FieldType.Integer, index = false) // 修改为 Integer
  private int publisherId;

  @Field(type = FieldType.Text)
  private String publisherName;

  @Field(type = FieldType.Date, index = false) // 修改为 Date
  private Date pubDate;

  @Field(type = FieldType.Text)
  private String desc;

  @Field(type = FieldType.Text, index = false) // 不参与索引
  private String shortDesc;

  @Field(type = FieldType.Integer, index = false) // 修改为 Integer
  private int wordCount;

  @Field(type = FieldType.Integer, index = false) // 修改为 Integer
  private int langId;

  @Field(type = FieldType.Keyword, index = false) // 不参与索引
  private String langName;

  @Field(type = FieldType.Keyword, index = false) // 不参与索引
  private String coverSUrl;

  @Field(type = FieldType.Keyword, index = false) // 不参与索引
  private String coverMUrl;

  @Field(type = FieldType.Keyword, index = false) // 不参与索引
  private String coverLUrl;

  @Field(type = FieldType.Long, index = false) // 修改为 Long
  private long coverDomColor;

  @Field(type = FieldType.Integer, index = false) // 修改为 Integer
  private int rating;

  @Field(type = FieldType.Keyword, index = false) // 不参与索引
  private String eBookUrl;

  @Field(type = FieldType.Integer, index = false)
  private int category1;

  @Field(type = FieldType.Text)
  private String category1Name;

  @Field(type = FieldType.Integer, index = false)
  private int category2;

  @Field(type = FieldType.Text)
  private String category2Name;

  @Field(type = FieldType.Integer, index = false) // 不参与索引
  private List<Integer> authorIds;

  @Field(type = FieldType.Text)
  private List<String> authorNames;

  @Field(type = FieldType.Short, index = false) // 可以保留为 Short
  private List<Short> tagIds;

  @Field(type = FieldType.Text)
  private List<String> tagNames;

  @JsonIgnore
  private boolean urlSet = false;

  public BookDetailedInfo(BookInfo info){
    this.isbn = info.getIsbn();
    this.title = info.getTitle();
    this.originalTitle = info.getOriginalTitle();
    this.publisherId = info.getPublisherId();
    this.publisherName = info.getPublisherName();
    this.pubDate = info.getPubDate();
    this.desc = info.getDesc();
    this.shortDesc = info.getShortDesc();
    this.wordCount = info.getWordCount();
    this.langId = info.getLangId();
    this.langName = info.getLangName();
    this.coverSUrl = info.getCoverSUrl();
    this.coverMUrl = info.getCoverMUrl();
    this.coverLUrl = info.getCoverLUrl();
    this.coverDomColor = info.getCoverDomColor();
    this.rating = info.getRating();
    this.eBookUrl = info.getEbookUrl();
    this.category1 = info.getCategory1();
    this.category1Name = info.getCategory1Name();
    this.category2 = info.getCategory2();
    this.category2Name = info.getCategory2Name();
    this.authorIds = info.getAuthorIds();
    this.authorNames = info.getAuthorNames();
    this.tagIds = info.getTagIds();
    this.tagNames = info.getTagNames();
  }

  public void buildUrl() {
    if (urlSet) return;
    this.coverSUrl = ServiceNetConfig.equip(coverSUrl);
    this.coverMUrl = ServiceNetConfig.equip(coverMUrl);
    this.coverLUrl = ServiceNetConfig.equip(coverLUrl);
    urlSet = true;
  }
}
