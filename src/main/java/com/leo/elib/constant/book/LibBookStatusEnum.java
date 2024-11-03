package com.leo.elib.constant.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LibBookStatusEnum {
  AVAILABLE((byte) 0, "available"),
  BORROWED((byte) 1, "borrowed"),
  RESERVED((byte) 2, "reserved"),
  LOCKED((byte) 3, "locked"),
  MAINTENANCE((byte) 4, "maintenance");

  private final byte code;

  @Getter
  private final String desc;

  @JsonValue
  public byte getCode() {
    return code;
  }

  @JsonCreator
  public static LibBookStatusEnum valueOf(byte code) {
    for (LibBookStatusEnum status : LibBookStatusEnum.values()) {
      if (status.code == code) {
        return status;
      }
    }
    return null;
  }
}
