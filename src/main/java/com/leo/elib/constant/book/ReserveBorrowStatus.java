package com.leo.elib.constant.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.leo.elib.constant.BaseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReserveBorrowStatus implements BaseCodeEnum {
  WaitingPickUp((byte) 0),
  OverduePickUp((byte) 1),
  Cancelled((byte) 2),
  WaitingReturn((byte) 3),
  Returned((byte) 4),
  OverdueReturn((byte) 5),
  CantReturn((byte) 6);

  private final byte code;

  private static final ReserveBorrowStatus[] enums;

  static {
    enums = new ReserveBorrowStatus[7];
    for (ReserveBorrowStatus status : ReserveBorrowStatus.values()) {
      enums[status.code] = status;
    }
  }

  @JsonValue
  @Override
  public byte getCode() {
    return code;
  }

  @Override
  public BaseCodeEnum[] getCodeEnums() {
    return enums;
  }

  @JsonCreator
  public static ReserveBorrowStatus fromCode(byte code) {
    if (code < 0 || code >= enums.length) {
      return null;
    }
    return enums[code];
  }
}
