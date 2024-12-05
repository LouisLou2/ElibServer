package com.leo.elib.service.specific.impl.repo;

import com.leo.elib.entity.Topic;
import com.leo.elib.service.base_service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.repo.TopicRepository;
import jakarta.annotation.Resource;

import java.util.List;

public class TopicRepositoryImpl implements TopicRepository {

  @Resource
  private RCacheManager rCacheManager;

  @Override
  public List<Topic> getTopics(int desiredNum) {
    return List.of();
  }
}