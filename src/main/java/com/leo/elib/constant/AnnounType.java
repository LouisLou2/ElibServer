package com.leo.elib.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AnnounType implements BaseCodeEnum {
  EventNotification((byte)0),
  OpenHoursAdjust((byte)1),
  NewServiceIntro((byte)2),
  ServiceAdjust((byte)3),
  CollectionChange((byte)4),
  Emergency((byte)5),
  ReaderSurvey((byte)6),
  NewBookIntro((byte)7),
  Other((byte)8);

  private byte code;
  private static final AnnounType[] enums;

  static {
    enums = new AnnounType[9];
    for (AnnounType announEnum : AnnounType.values()) {
      enums[announEnum.code] = announEnum;
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
  public static AnnounType fromCode(byte code) {
    if (code < 0 || code >= enums.length) {
      return null;
    }
    return enums[code];
  }
}