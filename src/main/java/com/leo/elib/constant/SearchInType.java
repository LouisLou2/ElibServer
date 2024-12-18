package com.leo.elib.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchInType {
  BOOKS(1),
  AUTHORS(2),
  PUBLISHERS(3);

  final int value;

  @JsonValue
  public int getValue() {
    return value;
  }

  @JsonCreator
  public static SearchInType valueOf(int value) {
    for (SearchInType type : SearchInType.values()) {
      if (type.value == value) {
        return type;
      }
    }
    return null;
  }
}
