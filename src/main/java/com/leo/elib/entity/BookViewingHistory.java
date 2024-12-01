package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.config.ServiceNetConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(indexName = "book_viewing_history") // 设定索引名称
public class BookViewingHistory {

  public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS");
  public static final String userIdFieldName = "userId";
  public static final String timeFieldName = "viewingTime";

  public static List<String> worthSearchFriendlyFields = List.of(
    "title", "authorNames", "publisherName"
  );

  @JsonIgnore
  @Id
  private String id;

  @JsonProperty("user_id")
  @Field(type = FieldType.Integer) // 用户ID是整数类型
  private int userId;

  @Field(type = FieldType.Keyword) // ISBN 是一个固定不变的字段，使用 Keyword 类型
  private String isbn;

  @Field(type = FieldType.Text) // 书名是文本类型，允许分词
  private String title;

  @JsonProperty("author_names")
  @Field(type = FieldType.Text) // 作者名字是文本类型，允许分词
  private List<String> authorNames;

  @JsonProperty("publisher_name")
  @Field(type = FieldType.Text) // 出版社名字是文本类型
  private String publisherName;

  @JsonProperty("viewing_time")
  @Field(type = FieldType.Date, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS", format = {})
  private LocalDateTime viewingTime;

  @JsonProperty("cover_m_url")
  @Field(type = FieldType.Keyword, index = false) // 不参与索引
  private String coverMUrl;

  @JsonProperty("cover_dom_color")
  @Field(type = FieldType.Long, index = false) // 不参与索引
  private long coverDomColor;

  @JsonIgnore
  private boolean urlSet = false;

  public void buildUrl() {
    if (urlSet) return;
    coverMUrl = ServiceNetConfig.equip(coverMUrl);
    urlSet = true;
  }

  public static String allowedTimeFormat(LocalDateTime time) {
    return time.format(formatter);
  }
}

