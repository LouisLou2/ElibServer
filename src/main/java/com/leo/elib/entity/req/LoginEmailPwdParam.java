package com.leo.elib.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginEmailPwdParam {
  public String email;
  public String password;

  @JsonProperty("device_type_code")
  public Integer deviceTypeCode;
  
  public boolean allSet() {
    return email != null && password != null && deviceTypeCode != null;
  }
}