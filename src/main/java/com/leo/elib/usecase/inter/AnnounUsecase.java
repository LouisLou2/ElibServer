package com.leo.elib.usecase.inter;

import com.leo.elib.entity.dto.dao.Announcement;

import java.util.List;

public interface AnnounUsecase {
  void insertAnnounAsLatest(Announcement announ);
  List<Announcement> getLatestAnnoun(int num, int offset);
  boolean hasNew(int readedLatestId);
}