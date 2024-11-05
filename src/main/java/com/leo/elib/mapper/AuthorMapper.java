package com.leo.elib.mapper;

import com.leo.elib.entity.dto.dao.Author;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthorMapper {
    Author getAuthor(int authorId);
    List<Author> debug_getAuthor(int num, int offset);
}