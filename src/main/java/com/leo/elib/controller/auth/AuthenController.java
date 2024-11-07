package com.leo.elib.controller.auth;

import com.leo.elib.comp_struct.Expected;
import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.constant.AuthenCodeType;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.AuthedUser;
import com.leo.elib.entity.req.LoginEmailCodeParam;
import com.leo.elib.entity.req.LoginEmailPwdParam;
import com.leo.elib.usecase.inter.AuthUsecase;
import com.leo.elib.usecase.inter.BookMarkUsecase;
import com.leo.elib.usecase.inter.Notifier;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


class VerifyCodeReq{
  public String contact;
  public AuthenCodeType way;
}

@RestController
@RequestMapping("/auth") // auth不在过滤器中
public class AuthenController {
  @Resource
  private BookMarkUsecase bookMarkUsecase;
  @Resource
  private AuthUsecase authUsecase;
  @Resource
  private Notifier notifier;

  @PostMapping("/login/email_pwd")
  public RespWrapper<?> login(@RequestBody LoginEmailPwdParam body) {
    if (!body.allSet())
      return RespWrapper.error(ResCodeEnum.InvalidParam);
    Expected<AuthedUser, ResCodeEnum> res = authUsecase.loginEmailPwd(body.email, body.password, body.deviceType);
    return res.isSuccess() ?
      RespWrapper.success(res.getValue()) :
      RespWrapper.error(res.getError());
  }

  @PostMapping("/send_verify_code")
  public RespWrapper<?> sendVerifyCode(@RequestBody VerifyCodeReq body) {
    assert body.way == AuthenCodeType.Email; // 仅支持邮箱验证码
    // 检查邮箱是否存在
    if (!authUsecase.checkEmailExist(body.contact))
      return RespWrapper.error(ResCodeEnum.UserNotExist);
    notifier.sendAndNoteEmailVerifyCode(body.contact);
    return RespWrapper.SUCCESS;
  }

  @PostMapping("/login/email_code")
  public RespWrapper<?> loginEmailCode(@RequestBody LoginEmailCodeParam body) {
    if (!body.allSet())
      return RespWrapper.error(ResCodeEnum.InvalidParam);
    Expected<AuthedUser, ResCodeEnum> res = authUsecase.loginEmailCode(body.email, body.code, body.deviceType);
    return res.isSuccess() ?
      RespWrapper.success(res.getValue()) :
      RespWrapper.error(res.getError());
  }
}
