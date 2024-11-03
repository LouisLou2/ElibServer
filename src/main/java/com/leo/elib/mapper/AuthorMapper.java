package com.leo.elib.mapper;

import com.leo.elib.entity.dto.dao.Author;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthorMapper {
    Author getAuthor(int authorId);
}