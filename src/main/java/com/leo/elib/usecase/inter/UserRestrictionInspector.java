package com.leo.elib.usecase.inter;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.constant.ResCodeEnum;

public interface UserRestrictionInspector {
  NullablePair<ResCodeEnum,String> canUserReserveABook(int userId);
}