package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthedUserWithData {
  @JsonProperty("user")
  private AuthedUser user;
  @JsonProperty("extra_data")
  private UserHomeData extraData;
}
