package com.leo.elib.service.specific.impl.cache;

import com.leo.elib.entity.dto.dao.Announcement;
import com.leo.elib.service.specific.inter.cache.AnnounCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class AnnounCacheApplcationImpl implements AnnounCache{

  @Value("${sys_setting.announcement.use-application-cache.max-capacity}")
  private int maxAnnounSize;

  // 链表实现
  private List<Announcement> announs;


  @Override
  public void flushWithData(List<Announcement> announs) {
    // 如果参数是链表实现，直接赋值即可，使用isInstance判断
    if (announs instanceof LinkedList) {
      this.announs = announs;
    }else{
      this.announs = new LinkedList<>(announs);
    }
  }

  @Override
  public int cacheMaxCapacity() {
    return maxAnnounSize;
  }

  @Override
  public void insertAnnounAsLatest(Announcement announ) {
    assert announs!=null;
    // 放在链表头部
    announs.add(0, announ);
  }

  @Override
  public List<Announcement> getLatestAnnoun(int num, int offset) {
    assert announs != null;
    assert num > 0 && offset >= 0;
    List<Announcement> announs = new ArrayList<>(num);
    int fromIndex = Math.min(offset, this.announs.size());
    int toIndex = Math.min(offset + num, this.announs.size());
    // 从 offset 开始取 num 个
    for (int i = fromIndex; i < toIndex; i++) {
      announs.add(this.announs.get(i));
    }
    return announs;
  }

  @Override
  public boolean hasNew(int readedLatestId) {
    assert announs != null;
    // 比较与最新的通知 id
    if (announs.isEmpty()) {
      return false;
    }
    return announs.get(0).getAnnouncementId() > readedLatestId;
  }
}