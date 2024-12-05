package com.leo.elib.service.specific.inter.cache;

import com.leo.elib.entity.AnnounceBrief;
import com.leo.elib.entity.dto.dao.Announcement;

import java.util.List;

public interface AnnounCache {
  /**
   * 获取最新的通知
   * @param announs 里面的url是相对路径
   */
  void flushWithData(List<Announcement> announs);

  int cacheMaxCapacity();

  /**
   * 获取最新的通知
   * @param announ 里面的url是相对路径
   */
  void insertAnnounAsLatest(Announcement announ);

  /**
   * 获取最新的通知
   * @param num 获取的数量
   * @param offset 偏移量
   * @return 其中的Announcement中的url都是完整的
   */
  List<Announcement> getLatestAnnoun(int num, int offset);
  /**
   * 获取最新的通知
   * @param num 获取的数量
   * @param offset 偏移量
   * @return 其中的Announcement中的url都是完整的
   */
  List<AnnounceBrief> getLatestAnnounBrief(int num, int offset);

  Announcement getAnnounDetail(int id);

  // 给出已经阅读的最新通知的 id，判断是否有新的通知
  boolean hasNew(int readedLatestId);
}