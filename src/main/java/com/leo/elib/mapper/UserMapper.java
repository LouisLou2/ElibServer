package com.leo.elib.mapper;

import com.leo.elib.entity.UserRestriction;
import com.leo.elib.entity.dto.dao.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
  User getUserById(int id);
  User getUserByEmail(String email);
  boolean checkEmailExist(String email);
  UserRestriction getUserRestriction(int userId, byte pendingReserve, byte unreturned);

  // 注解式
  @Delete("delete from user where email = #{email}")
  boolean debug_deleteUser(String email);
}
