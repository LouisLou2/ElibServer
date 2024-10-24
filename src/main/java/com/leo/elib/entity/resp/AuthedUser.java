package com.leo.elib.entity.resp;

import com.leo.elib.comp_entity.TokenPair;
import com.leo.elib.entity.dao.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuthedUser {
  private User user;
  private String at;
  private String rt;
  public AuthedUser(User user, TokenPair tp) {
    this.user = user;
    at = tp.getAt();
    rt = tp.getRt();
  }
}
