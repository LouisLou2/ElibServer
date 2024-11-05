package com.leo.elib.constant.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BorrowStatus {
  UserKeeping((byte) 0),
  UserReturned((byte) 1),
  UableToReturn((byte) 2);

  private final byte code;

  @JsonValue
  public byte getCode() {
    return code;
  }

  @JsonCreator
  public static BorrowStatus fromCode(byte code) {
    for (BorrowStatus status : BorrowStatus.values()) {
      if (status.code == code) {
        return status;
      }
    }
    throw new IllegalArgumentException("Invalid BorrowStatus code: " + code);
  }
}
