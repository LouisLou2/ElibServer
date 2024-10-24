package com.leo.elib.mapper;

import com.leo.elib.entity.dao.Author;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface  AuthorMapper {
    Author getAuthorById(int id);
}