package com.leo.elib.entity.dto.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document(indexName = "publisher")
public class Publisher {

  public static final List<String> worthSearchFriendlyFields = List.of(
    "publisherName", "desc"
  );

  @Id
  @Field(type = FieldType.Integer, index = false)
  @JsonProperty("publisher_id")
  private int publisherId;

  @Field(type = FieldType.Text)
  @JsonProperty("publisher_name")
  private String publisherName;

  @Field(type = FieldType.Text)
  private String desc;

  @Field(type = FieldType.Integer, index = false)
  @JsonProperty("book_count")
  private int bookCount;
}
