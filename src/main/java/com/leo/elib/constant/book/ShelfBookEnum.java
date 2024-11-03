package com.leo.elib.constant.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ShelfBookEnum {
  // 书架中的书籍状态
  Public((byte) 0),
  Private((byte) 1);
  final byte code;

  @JsonValue
  public byte getCode() {
    return code;
  }

  @JsonCreator
  public static ShelfBookEnum valueOf(byte code) {
    for (ShelfBookEnum status : ShelfBookEnum.values()) {
      if (status.code == code) {
        return status;
      }
    }
    return null;
  }
}
