package com.leo.elib.mapper;

import com.leo.elib.dto.dao.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
  User getUserById(int id);
  User getUserByEmail(String email);
}
