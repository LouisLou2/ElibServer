package com.leo.elib.service.specific.inter.cache;

import com.leo.elib.entity.dto.dao.Announcement;

import java.util.List;

public interface AnnounCache {
  void flushWithData(List<Announcement> announs);
  int cacheMaxCapacity();
  void insertAnnounAsLatest(Announcement announ);
  List<Announcement> getLatestAnnoun(int num, int offset);
  // 给出已经阅读的最新通知的 id，判断是否有新的通知
  boolean hasNew(int readedLatestId);
}
