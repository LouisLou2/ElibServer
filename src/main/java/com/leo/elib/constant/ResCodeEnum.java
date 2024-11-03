package com.leo.elib.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ResCodeEnum {
  Success(0, "success"),
  /*---General Error-------------------*/
  InvalidParam(1, "invalid param"),
  /*---Auth----------------------------*/
  UserNotExist(101, "user not exist"),
  PasswordIncorrect(102, "password not correct"),
  PasswordUnset(103, "password unset"),
  VerifyCodeIncorrect(104, "verify code incorrect or expired"),
  /*---Bookshelf-------------------------*/
  ShelfIsFull(201, "shelf has reached its max capacity"),
  /*---BookMark-------------------------*/
  BookCollectionIsFull(301, "book collection has reached its max capacity"),
  AlreadyCollected(302, "book already collected"),
  BookNotCollected(303, "book not collected");

  final int code;
  @Getter
  final String msg;

  @JsonValue
  public int getCode() {
    return code;
  }

  @JsonCreator
  public static ResCodeEnum valueOf(int code) {
    for (ResCodeEnum type : ResCodeEnum.values()) {
      if (type.code == code) {
        return type;
      }
    }
    return null;
  }
}