package com.leo.elib.usecase.impl;

import com.leo.elib.comp_entity.Expected;
import com.leo.elib.comp_entity.TokenPair;
import com.leo.elib.constant.DeviceTypeEnum;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.dao.User;
import com.leo.elib.entity.resp.AuthedUser;
import com.leo.elib.mapper.UserMapper;
import com.leo.elib.usecase.inter.AuthTokenManager;
import com.leo.elib.usecase.inter.AuthUsecase;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AuthUsecaseImpl implements AuthUsecase {
  @Resource
  private UserMapper userMapper;
  @Resource
  private AuthTokenManager authTokenManager;

  @Override
  public Expected<AuthedUser, ResCodeEnum> login(String email, String password, DeviceTypeEnum deviceType) {
    User user = userMapper.getUserByEmail(email);
    if (user == null)
      return Expected.error(ResCodeEnum.UserNotExist);
    if (!user.passwordSet())
      return Expected.error(ResCodeEnum.PasswordUnset);
    if (!user.getPassword().equals(password))
      return Expected.error(ResCodeEnum.PasswordIncorrect);
    TokenPair tp = authTokenManager.refreshTokenPair(user.getUserId(), deviceType);
    return Expected.success(new AuthedUser(user, tp));
  }
}