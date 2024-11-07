package com.leo.elib.util;

import java.util.List;

public class ListUtil {
  public static int numOfNullInTail(List<?> list) {
    int count = 0;
    for (int i = list.size() - 1; i >= 0; i--) {
      if (list.get(i) == null) {
        count++;
      } else {
        break;
      }
    }
    return count;
  }
}
