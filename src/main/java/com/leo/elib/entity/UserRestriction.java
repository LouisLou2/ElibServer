package com.leo.elib.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserRestriction {
  private boolean isRestricted;
  private byte overdueTimesThisMonth;
  private int reservedButUnpicked;
  private int borrowedButUnreturned;
}
