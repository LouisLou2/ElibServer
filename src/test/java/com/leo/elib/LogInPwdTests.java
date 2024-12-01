package com.leo.elib;

import com.leo.elib.constant.DeviceTypeEnum;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.usecase.inter.AuthUsecase;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LogInPwdTests {
  @Resource
  AuthUsecase authUsecase;

  @Test
  void testLoginWithNonExistUser() {
    String email = "nonexist@example.com";
    String pwd = "abc123456";
    DeviceTypeEnum device = DeviceTypeEnum.Mobile;
    var res = authUsecase.loginEmailPwd(email, pwd, device);
    assertNotNull(res);
    assertNull(res.getValue());
    assertEquals(res.getError(), ResCodeEnum.UserNotExist);
    System.out.println(res);
  }

  @Test
  void loginWithUserWithPwdUset() {
    String email = "865113609@qq.com";
    String pwd = "abc123456";
    DeviceTypeEnum device = DeviceTypeEnum.Mobile;
    var res = authUsecase.loginEmailPwd(email, pwd, device);
    assertNotNull(res);
    assertNull(res.getValue());
    assertEquals(res.getError(), ResCodeEnum.PasswordUnset);
    System.out.println(res);
  }

  // unmatched pwd
  @Test
  void loginWithUserWithUnmatchedPwd() {
    String email = "lskleo@163.com";
    String pwd = "abc12345";
    DeviceTypeEnum device = DeviceTypeEnum.Mobile;
    var res = authUsecase.loginEmailPwd(email, pwd, device);
    // 测试断言
    assertNotNull(res);
    assertNull(res.getValue());
    assertEquals(res.getError(), ResCodeEnum.PasswordIncorrect);
    System.out.println(res);
  }

  // matched pwd
  @Test
  void loginWithUserWithMatchedPwd() {
    String email = "lskleo@163.com";
    String pwd = "abc123456";
    DeviceTypeEnum device = DeviceTypeEnum.Mobile;
    var res = authUsecase.loginEmailPwd(email, pwd, device);
    assertNotNull(res);
    assertNotNull(res.getValue());
    assertNull(res.getError());
    System.out.println(res);
  }
}
