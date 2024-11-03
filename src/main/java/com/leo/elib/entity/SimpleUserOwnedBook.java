package com.leo.elib.entity;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
public class SimpleUserOwnedBook {
  private String isbn;
  private String title;
  private String coverMUrl;
  LocalDateTime time;
  private int category1;
  // 此字段不从数据库取，数据库不需要管这个字段，它空即可
  private String category1Name;
  
  public SimpleUserOwnedBook(String isbn, String title, String coverMUrl, LocalDateTime time, int category1) {
    this.isbn = isbn;
    this.title = title;
    this.coverMUrl = coverMUrl;
    this.time = time;
    this.category1 = category1;
  }
  
  public int getCategory1() {
    return category1;
  }
}
