package com.leo.elib.constant;

// 实现这个接口的枚举，都必须保证枚举值code连续，从0开始，不可重复
public interface BaseCodeEnum {
  byte getCode();
  // 必须提供此方法，这里这个数组的index是code，value是枚举值
  BaseCodeEnum[] getCodeEnums();
}
