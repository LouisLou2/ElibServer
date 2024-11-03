package com.leo.elib.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AuthenCodeType {
  Phone(0),
  Email(1);
  final int value;

  @JsonValue
  public int getValue() {
    return value;
  }

  @JsonCreator
  public static AuthenCodeType valueOf(int value) {
    for (AuthenCodeType type : AuthenCodeType.values()) {
      if (type.value == value) {
        return type;
      }
    }
    return null;
  }
}
