package com.leo.elib.constant.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.leo.elib.constant.BaseCodeEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserBookBehavior implements BaseCodeEnum{

  View((byte) 0),
  Collect((byte) 1),
  Reserve((byte) 2);

  private final byte code;
  private static final UserBookBehavior[] enums;

  static {
    enums = new UserBookBehavior[3];
    for (UserBookBehavior behavior : UserBookBehavior.values()) {
      enums[behavior.code] = behavior;
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
  public static UserBookBehavior fromCode(byte code) {
    if (code < 0 || code >= enums.length) {
      return null;
    }
    return enums[code];
  }
}