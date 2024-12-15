package com.leo.elib.entity;

import com.leo.elib.constant.ResCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserRestriction {
  private int sumOfPickWaitedReturnWaited;
  private int cancelledTimesWithin;
  private int abnormalPickReturnWithin;
  private boolean restricted;
}
