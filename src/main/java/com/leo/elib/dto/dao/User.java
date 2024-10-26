package com.leo.elib.dto.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User {
  @JsonProperty("user_id")
  private int userId;
  private String email;
  private String name;
  // true: teacher, false: student
  private boolean role;
  // 校区代号
  private byte location;
  private boolean gender;
  private String password;
  
  public boolean passwordSet() {
    return password != null;
  }
}