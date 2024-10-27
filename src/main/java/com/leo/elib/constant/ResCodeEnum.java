package com.leo.elib.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResCodeEnum {
  Success(0, "success"),
  /*---General Error-------------------*/
  InvalidParam(1, "invalid param"),
  /*---Auth----------------------------*/
  UserNotExist(101, "user not exist"),
  PasswordIncorrect(102, "password not correct"),
  PasswordUnset(103, "password unset"),
  /*---Bookshelf-------------------------*/
  ShelfIsFull(201, "shelf has reached its max capacity");
  int code;
  String msg;
}