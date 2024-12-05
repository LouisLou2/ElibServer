package com.leo.elib.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AnnounType implements BaseCodeEnum {
  EventNotification((byte) 0, "事件通知"),
  OpenHoursAdjust((byte) 1, "营业时间调整"),
  NewServiceIntro((byte) 2, "新服务介绍"),
  ServiceAdjust((byte) 3, "服务调整"),
  CollectionChange((byte) 4, "馆藏变化"),
  Emergency((byte) 5, "紧急通知"),
  ReaderSurvey((byte) 6, "读者调查"),
  NewBookIntro((byte) 7, "新书介绍"),
  Other((byte) 8, "其他");

  private final byte code;
  // 新增的 getter 方法，获取对应的 description 字符串值
  @Getter
  private final String tag;

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