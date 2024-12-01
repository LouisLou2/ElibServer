package com.leo.elib;

import com.leo.elib.constant.DeviceTypeEnum;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.mapper.UserMapper;
import com.leo.elib.usecase.inter.AuthUsecase;
import com.leo.elib.usecase.inter.Notifier;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LogInEmailCodeTests {
  @Resource
  private AuthUsecase authUsecase;
  @Resource
  private Notifier notifier;
  @Resource
  private UserMapper userMapper;

  String rightCode = "605883";
  String email = "email123@qq.com";

//  @Test
//  void getEmailCode() {
//    notifier.sendAndNoteEmailVerifyCode(email);
//  }

  @Test
  void testUserExist() {
    var res = authUsecase.checkEmailExist(email);
    assertTrue(res);
    System.out.println(res);
  }

  @Test
  void LogInWithWrongCode() {
    String code = "XXXXXX";
    DeviceTypeEnum device = DeviceTypeEnum.Mobile;
    var res = authUsecase.loginEmailCode(email, code, device);
    assertNull(res.getValue());
    assertEquals(res.getError(), ResCodeEnum.VerifyCodeIncorrect);
    System.out.println(res);
  }

  @Test
  void LogInWithCorrectCode() {
    String code = rightCode;
    DeviceTypeEnum device = DeviceTypeEnum.Mobile;
    var res = authUsecase.loginEmailCode(email, code, device);
    assertNotNull(res.getValue());
    assertNull(res.getError());
    System.out.println(res);
  }

  @Test
  void LogInWithNoneExistEmail() {
    String code = rightCode;
    DeviceTypeEnum device = DeviceTypeEnum.Mobile;
//    userMapper.debug_deleteUser(email);
    var res = authUsecase.loginEmailCode(email, code, device);
    System.out.println(res);
  }
}
