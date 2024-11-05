package com.leo.elib.constant.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.leo.elib.constant.BaseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReservationStatus implements BaseCodeEnum {
  Pending((byte)0),
  Picked((byte)1),
  Timeout((byte)2),
  ForceClosed((byte)3);

  final byte code;
  private static final ReservationStatus[] enums;

  static {
    enums = new ReservationStatus[4];
    for (ReservationStatus status : ReservationStatus.values()) {
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
  public static ReservationStatus fromCode(byte code) {
    if (code < 0 || code >= enums.length) {
      return null;
    }
    return enums[code];
  }
}
