package com.leo.elib.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.constant.DeviceTypeEnum;

public class LoginEmailPwdParam {
  public String email;
  public String password;

  @JsonProperty("device_type")
  public DeviceTypeEnum deviceType;
  
  public boolean allSet() {
    return email != null && password != null && deviceType != null;
  }
}