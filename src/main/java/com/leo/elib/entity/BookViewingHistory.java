package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Setter
@Getter
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

  @Field(type = FieldType.Integer) // 用户ID是整数类型
  private int userId;

  @Field(type = FieldType.Keyword) // ISBN 是一个固定不变的字段，使用 Keyword 类型
  private String isbn;

  @Field(type = FieldType.Text) // 书名是文本类型，允许分词
  private String title;

  @Field(type = FieldType.Text) // 作者名字是文本类型，允许分词
  private List<String> authorNames;

  @Field(type = FieldType.Text) // 出版社名字是文本类型
  private String publisherName;

  @Field(type = FieldType.Date, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS", format = {})
  private LocalDateTime viewingTime;

  void setUserIdAndIsbn(int userId, String isbn) {
    this.userId = userId;
    this.isbn = isbn;
    id = userId + ":" + isbn;
  }

  public BookViewingHistory(int userId, String isbn, String title, List<String> authorNames, String publisherName, LocalDateTime viewingTime) {
    this.userId = userId;
    this.isbn = isbn;
    this.title = title;
    this.authorNames = authorNames;
    this.publisherName = publisherName;
    this.viewingTime = viewingTime;
    id = userId + ":" + isbn;
  }

  public static String allowedTimeFormat(LocalDateTime t){
    return formatter.format(t);
  }
}

