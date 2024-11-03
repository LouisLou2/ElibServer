package com.leo.elib.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.constant.DeviceTypeEnum;

public class LoginEmailCodeParam {
  public String email;
  public String code;
  @JsonProperty("device_type")
  public DeviceTypeEnum deviceType;

  public boolean allSet() {
    return email != null && code != null && deviceType != null;
  }
}