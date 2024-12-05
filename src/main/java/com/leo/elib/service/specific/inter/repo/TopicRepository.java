package com.leo.elib.service.specific.inter.repo;

import com.leo.elib.entity.Topic;

import java.util.List;

public interface TopicRepository {
  List<Topic> getTopics(int desiredNum);
}
