package com.leo.elib.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookTag {
  private short tagId;
  private String tagName;
  private String tagDesc;

  @Override
  public String toString() {
    return "BookTag{" +
      "tagId=" + tagId +
      ", tagName='" + tagName + '\'' +
      ", tagDesc='" + tagDesc + '\'' +
      '}';
  }
}
