package com.leo.elib.usecase.impl.verify;

import com.leo.elib.usecase.inter.verify.AuthenCodeGenerator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthenCodeGeneratorImpl implements AuthenCodeGenerator {

  @Value("${authen.captcha.length}")
  private byte captchaLength;

  @Value("${authen.verify.length}")
  private byte emailCodeLength;

  private int captchaCodeBoundLeft;
  private int captchaCodeBoundRight;
  private int emailCodeBoundLeft;
  private int emailCodeBoundRight;

  @PostConstruct
  private void init(){
    captchaCodeBoundLeft = (int) Math.pow(10, captchaLength - 1);
    captchaCodeBoundRight = (int) Math.pow(10, captchaLength) - 1;
    emailCodeBoundLeft = (int) Math.pow(10, emailCodeLength - 1);
    emailCodeBoundRight = (int) Math.pow(10, emailCodeLength) - 1;
  }

  @Override
  public String generateCaptchaCode() {
    // use thread local random to generate random code
    return String.valueOf(
      ThreadLocalRandom.current().nextInt(captchaCodeBoundLeft, captchaCodeBoundRight)
    );
  }

  @Override
  public String generateVerifyCode() {
    return String.valueOf(
      ThreadLocalRandom.current().nextInt(emailCodeBoundLeft, emailCodeBoundRight)
    );
  }
}