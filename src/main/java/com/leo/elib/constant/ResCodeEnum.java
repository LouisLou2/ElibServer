package com.leo.elib.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.leo.elib.entity.dto.dao.Announcement;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ResCodeEnum {
  Success(0, "success"),
  /*---General Error-------------------*/
  InvalidParam(1, "invalid param"),
  InvalidRequest(2, "invalid request"),
  /*---Auth----------------------------*/
  UserNotExist(101, "user not exist"),
  PasswordIncorrect(102, "password not correct"),
  PasswordUnset(103, "password unset"),
  VerifyCodeIncorrect(104, "verify code incorrect or expired"),
  UnAuthorized(105, "unauthorized"),
  /*---Bookshelf-------------------------*/
  ShelfIsFull(201, "shelf has reached its max capacity"),
  /*---BookMark-------------------------*/
  BookCollectionIsFull(301, "book collection has reached its max capacity"),
  AlreadyCollected(302, "book already collected"),
  BookNotCollected(303, "book not collected"),
  /*---BookReserve-------------------------*/
  ReserveNotAvailable(401, "该书籍暂无剩余副本"),
  TooMuchReservedOrUnreturned(402, "too much reserved or unreturned books"),
  BeenRestricted(403, "has been restricted"),
  TooMuchOverdue(404, "too much overdue books"),
  ReserveFailed(405, "失败，预约人数过多，系统繁忙"),

  CancelWillCauseOverdue(501, "取消预约将导致超时次数+1"),

  /*----Resource----------------------------*/
  ResourceNotFound(601, "resource not found"),

  /*----Announcement------------------------*/
  AnnouncementNotFound(701, "announcement not found");

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

  public boolean isSuccess() {
    return this == Success;
  }
}