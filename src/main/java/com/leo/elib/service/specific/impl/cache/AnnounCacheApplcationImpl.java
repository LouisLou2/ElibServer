package com.leo.elib.service.specific.impl.cache;

import com.leo.elib.entity.AnnounceBrief;
import com.leo.elib.entity.dto.dao.Announcement;
import com.leo.elib.service.specific.inter.cache.AnnounCache;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnnounCacheApplcationImpl implements AnnounCache{

  @Value("${sys_setting.announcement.use-application-cache.max-capacity}")
  private int maxAnnounSize;

  // 链表实现
  private List<Announcement> announs;
  private List<AnnounceBrief> announBriefs;
  private ConcurrentHashMap<Integer, Announcement> announMap;

  @PostConstruct
  void init() {
    assert maxAnnounSize > 1;
  }

  @Override
  public void flushWithData(List<Announcement> announs) {
    assert announs.stream().allMatch(Announcement::urlUnBuildOrNull);
    // 如果参数是链表实现，直接赋值即可，使用isInstance判断
    if (announs instanceof LinkedList) {
      this.announs = announs;
    }else {
      this.announs = new LinkedList<>(announs);
    }
    announs.forEach(Announcement::buildUrl);
    announBriefs = new LinkedList<>();
    for (Announcement announ : announs) {
      announBriefs.add(AnnounceBrief.fromAnnouncementUrlBuildOrNull(announ));
    }
    announMap = new ConcurrentHashMap<>();
    for (Announcement announ : announs) {
      announMap.put(announ.getAnnouncementId(), announ);
    }
  }

  @Override
  public int cacheMaxCapacity() {
    return maxAnnounSize;
  }

  @Override
  public void insertAnnounAsLatest(Announcement announ) {
    assert announs!=null;
    assert announ != null;
    assert announ.urlUnBuildOrNull();
    announ.buildUrl();
    // 放在链表头部
    announs.add(0, announ);
    announBriefs.add(0, AnnounceBrief.fromAnnouncementUrlBuildOrNull(announ));
    // map
    announMap.put(announ.getAnnouncementId(), announ);
    // 如果超过最大容量，删除最后一个
    if (announs.size() > maxAnnounSize) {
      int idToBeRm = announs.get(maxAnnounSize).getAnnouncementId();
      announMap.remove(idToBeRm);
      // list
      announs.remove(maxAnnounSize);
      announBriefs.remove(maxAnnounSize);
    }
  }

  @Override
  public List<Announcement> getLatestAnnoun(int num, int offset) {
    assert announs != null;
    assert num > 0 && offset >= 0;
    List<Announcement> result = new ArrayList<>(num);
    // 确保 offset 和 num 不超过实际列表的大小
    int fromIndex = Math.min(offset, announs.size());
    // 使用迭代器来遍历公告列表，从 offset 开始
    Iterator<Announcement> iterator = announs.listIterator(fromIndex);
    int count = 0;
    while (iterator.hasNext() && count < num) {
      Announcement announ = iterator.next();
      result.add(announ);
      ++count;
    }
    return result;
  }

  @Override
  public List<AnnounceBrief> getLatestAnnounBrief(int num, int offset) {
    assert announBriefs != null;
    assert num > 0 && offset >= 0;
    List<AnnounceBrief> result = new ArrayList<>(num);
    // 确保 offset 和 num 不超过实际列表的大小
    int fromIndex = Math.min(offset, announBriefs.size());
    // 使用迭代器来遍历公告列表，从 offset 开始
    Iterator<AnnounceBrief> iterator = announBriefs.listIterator(fromIndex);
    int count = 0;
    while (iterator.hasNext() && count < num) {
      AnnounceBrief announ = iterator.next();
      result.add(announ);
      count++;
    }
    return result;
  }

  @Override
  public Announcement getAnnounDetail(int id) {
    assert announs != null;
    return announMap.get(id);
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