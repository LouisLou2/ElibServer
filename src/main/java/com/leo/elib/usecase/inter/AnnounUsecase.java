package com.leo.elib.usecase.inter;

import com.leo.elib.entity.AnnounceBrief;
import com.leo.elib.entity.dto.dao.Announcement;

import java.util.List;

public interface AnnounUsecase {
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

  /**
   * 获取
   * @return 其中的Announcement中的url都是完整的
   */
  Announcement getAnnounDetail(int id);
  boolean hasNew(int readedLatestId);
}