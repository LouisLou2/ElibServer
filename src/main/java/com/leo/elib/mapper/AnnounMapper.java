package com.leo.elib.mapper;

import com.leo.elib.entity.AnnounceBrief;
import com.leo.elib.entity.dto.dao.Announcement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnnounMapper {
  void insertAnnoun(Announcement announ);
  List<Announcement> getLatestAnnouns(int num, int offset);
  List<AnnounceBrief> getLatestAnnounBrief(int num, int offset);
  Announcement getAnnounDetail(int id);
  // List<Announcement> getAnnounsExpiryTimeAfter(LocalDateTime atime, int num, int offset);
}
