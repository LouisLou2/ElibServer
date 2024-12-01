package com.leo.elib.usecase.impl;

import com.leo.elib.comp_struct.Expected;
import com.leo.elib.comp_struct.TokenInfo;
import com.leo.elib.comp_struct.TokenPair;
import com.leo.elib.constant.AuthenCodeType;
import com.leo.elib.constant.DeviceTypeEnum;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.constant.TokenRes;
import com.leo.elib.entity.dto.dao.User;
import com.leo.elib.entity.AuthedUser;
import com.leo.elib.mapper.UserMapper;
import com.leo.elib.usecase.inter.AuthTokenManager;
import com.leo.elib.usecase.inter.AuthUsecase;
import com.leo.elib.usecase.inter.verify.AuthenCodeManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AuthUsecaseImpl implements AuthUsecase {
  @Resource
  private UserMapper userMapper;
  @Resource
  private AuthTokenManager authTokenManager;
  @Resource
  private AuthenCodeManager authenCodeManager;

  @Override
  public Expected<AuthedUser, ResCodeEnum> loginEmailPwd(String email, String password, DeviceTypeEnum deviceType) {
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

  @Override
  public Expected<AuthedUser, ResCodeEnum> loginEmailCode(String email, String code, DeviceTypeEnum deviceType) {
    if (!authenCodeManager.verifyVerifyCode(email, AuthenCodeType.Email, code))
      return Expected.error(ResCodeEnum.VerifyCodeIncorrect);
    User user = userMapper.getUserByEmail(email);
    if (user == null)
      return Expected.error(ResCodeEnum.UserNotExist);
    TokenPair tp = authTokenManager.refreshTokenPair(user.getUserId(), deviceType);
    return Expected.success(new AuthedUser(user, tp));
  }

  @Override
  public boolean checkEmailExist(String email) {
    return userMapper.checkEmailExist(email);
  }

  @Override
  public String getNewAt(String rt) {
    Expected<TokenInfo, TokenRes> result = authTokenManager.verifyToken(false, rt);
    if (!result.isSuccess()) {
      // 说明rt无效
      return null;
    }
    TokenInfo info = result.getValue();
    return authTokenManager.createAT(info.getUserId(), info.getDeviceType());
  }
}