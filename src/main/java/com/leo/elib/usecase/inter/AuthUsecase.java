package com.leo.elib.usecase.inter;

import com.leo.elib.comp_struct.Expected;
import com.leo.elib.constant.DeviceTypeEnum;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.resp.AuthedUser;

public interface AuthUsecase {

  Expected<AuthedUser, ResCodeEnum> login(String email, String password, DeviceTypeEnum deviceType);
}
