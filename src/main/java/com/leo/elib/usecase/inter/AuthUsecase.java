package com.leo.elib.usecase.inter;

import com.leo.elib.comp_struct.Expected;
import com.leo.elib.constant.DeviceTypeEnum;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.AuthedUser;

public interface AuthUsecase {

  Expected<AuthedUser, ResCodeEnum> loginEmailPwd(String email, String password, DeviceTypeEnum deviceType);
  Expected<AuthedUser, ResCodeEnum> loginEmailCode(String email, String code, DeviceTypeEnum deviceType);

  boolean checkEmailExist(String email);
}
