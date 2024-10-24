package com.leo.elib.controller.auth;

import com.leo.elib.comp_entity.Expected;
import com.leo.elib.comp_entity.RespWrapper;
import com.leo.elib.constant.DeviceTypeEnum;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.req.LoginEmailPwdParam;
import com.leo.elib.entity.resp.AuthedUser;
import com.leo.elib.usecase.inter.AuthUsecase;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth") // auth不在过滤器中
public class AuthenController {
  @Resource
  private AuthUsecase authUsecase;
  @PostMapping("/login")
  public RespWrapper<?> login(@RequestBody LoginEmailPwdParam body) {
    if (!body.allSet())
      return RespWrapper.error(ResCodeEnum.InvalidParam);
    DeviceTypeEnum deviceType = DeviceTypeEnum.valueOf(body.deviceTypeCode);
    if (deviceType == null)
      return RespWrapper.error(ResCodeEnum.InvalidParam);
    Expected<AuthedUser, ResCodeEnum> res = authUsecase.login(body.email, body.password, deviceType);
    return res.isSuccess() ?
      RespWrapper.success(res.getValue()):
      RespWrapper.error(res.getError());
  }
}
