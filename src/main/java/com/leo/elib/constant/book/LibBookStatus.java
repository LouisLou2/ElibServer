package com.leo.elib.constant.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LibBookStatus {
  Available((byte) 0, "available"),
  Reserved((byte) 1, "reserved"),
  Borrowed((byte) 2, "borrowed"),
  Locked((byte) 3, "locked"),
  Maintaining((byte) 4, "maintenance");

  private final byte code;

  @Getter
  private final String desc;

  @JsonValue
  public byte getCode() {
    return code;
  }

  @JsonCreator
  public static LibBookStatus valueOf(byte code) {
    for (LibBookStatus status : LibBookStatus.values()) {
      if (status.code == code) {
        return status;
      }
    }
    return null;
  }
}
