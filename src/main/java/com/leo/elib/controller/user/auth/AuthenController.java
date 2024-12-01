package com.leo.elib.controller.user.auth;

import com.leo.elib.comp_struct.Expected;
import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.constant.AuthenCodeType;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.AuthedUser;
import com.leo.elib.entity.AuthedUserWithData;
import com.leo.elib.entity.UserHomeData;
import com.leo.elib.entity.req.LoginEmailCodeParam;
import com.leo.elib.entity.req.LoginEmailPwdParam;
import com.leo.elib.entity.req.VerifyCodeReq;
import com.leo.elib.usecase.inter.AggregationUserDataProvider;
import com.leo.elib.usecase.inter.AuthUsecase;
import com.leo.elib.usecase.inter.Notifier;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user/auth") // auth不在过滤器中
public class AuthenController {
  @Resource
  private AggregationUserDataProvider prov;
  @Resource
  private AuthUsecase authUsecase;
  @Resource
  private Notifier notifier;

  @PostMapping("/login/email_pwd")
  public RespWrapper<?> login(@RequestBody LoginEmailPwdParam body) {
    if (!body.allSet())
      return RespWrapper.error(ResCodeEnum.InvalidParam);
    Expected<AuthedUser, ResCodeEnum> res = authUsecase.loginEmailPwd(body.email, body.password, body.deviceType);
    if (!res.isSuccess()){
      return RespWrapper.error(res.getError());
    }
    AuthedUser authedUser = res.getValue();
    UserHomeData data = prov.getHomeData(
      authedUser.getUser().getUserId(),
      body.lastReadedAnnounId,
      body.viewingHistoryPageSize,
      body.recoBookPageSize,
      body.chartPageSize
    );
    data.buildUrl();
    return RespWrapper.success(new AuthedUserWithData(authedUser, data));
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
    if (!res.isSuccess()){
      return RespWrapper.error(res.getError());
    }
    AuthedUser authedUser = res.getValue();
    UserHomeData data = prov.getHomeData(
        authedUser.getUser().getUserId(),
        body.lastReadedAnnounId,
        body.viewingHistoryPageSize,
        body.recoBookPageSize,
        body.chartPageSize
    );
    data.buildUrl();
    return RespWrapper.success(new AuthedUserWithData(authedUser, data));
  }


  @GetMapping("/token/refresh_at")
  public ResponseEntity<RespWrapper<?>> getNewAt(String rt) {
    String at = authUsecase.getNewAt(rt);
    if (at == null)
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    return ResponseEntity.ok(RespWrapper.success(at));
  }
}
