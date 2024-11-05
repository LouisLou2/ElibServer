package com.leo.elib.mapper;

import com.leo.elib.entity.dto.dao.Publisher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PublisherMapper {
  Publisher getPublisherById(int publisherId);

  List<Publisher> debug_getPublishers(int num, int offset);
}