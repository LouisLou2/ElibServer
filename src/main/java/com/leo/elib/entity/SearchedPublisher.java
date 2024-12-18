package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
//@Document(indexName = "publisher")
public class SearchedPublisher {

  @JsonProperty("publisher_id")
  private int publisherId;

  @JsonProperty("publisher_name")
  private String publisherName;

  @JsonProperty("book_count")
  private int bookCount;
}
