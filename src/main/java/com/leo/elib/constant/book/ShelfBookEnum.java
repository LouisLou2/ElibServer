package com.leo.elib.constant.book;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ShelfBookEnum {
  // 书架中的书籍状态
  Public((byte) 0),
  Private((byte) 1);
  final byte code;
}
