package com.leo.elib.service.specific.impl.cache;

import com.leo.elib.entity.AnnounceBrief;
import com.leo.elib.entity.dto.dao.Announcement;
import com.leo.elib.service.base_service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.cache.AnnounCache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnounCacheImpl implements AnnounCache {

  @Value("${container.redis.announcement.cont-name}")
  private String announCont;

  @Value("${container.redis.announcement.max_capacity}")
  private int maxAnnounSize;

  @Resource
  private RCacheManager rCacheManager;

  private ListOperations<String, Object> opsForList;

  @PostConstruct
  private void init() {
    opsForList = rCacheManager.getOpsForList();
  }

  @Override
  public void flushWithData(List<Announcement> announs) {
    assert announs.stream().allMatch(Announcement::urlUnBuildOrNull);
    // 清空原有数据
    opsForList.getOperations().delete(announCont);
    // 重新插入
    opsForList.leftPushAll(announCont, announs.toArray());
  }

  @Override
  public int cacheMaxCapacity() {
    return maxAnnounSize;
  }

  @Override
  public void insertAnnounAsLatest(Announcement announ) {
    assert announ != null;
    assert announ.urlUnBuildOrNull();
    // 将 Map 存储为 JSON 字符串或直接存储 Map 对象
    opsForList.leftPush(announCont, announ);
    // 仅保留最新的 2000 条通知
    opsForList.trim(announCont, 0, maxAnnounSize - 1);
  }

  @Override
  public List<Announcement> getLatestAnnoun(int num, int offset) {
    List<Object> list = opsForList.range(announCont, offset, offset + num - 1);
    assert list != null;
    List<Announcement> announs = (List<Announcement>) (List<?>) list;
    announs.forEach(Announcement::buildUrl);
    return announs;
  }

  @Override
  public List<AnnounceBrief> getLatestAnnounBrief(int num, int offset) {
    List<Announcement> announs = getLatestAnnoun(num, offset);
    return announs.stream().map(AnnounceBrief::fromAnnouncementUrlBuildOrNull).toList();
  }

  @Override
  public Announcement getAnnounDetail(int id) {
    // TODO: implement this method
    throw new RuntimeException("Not implemented");
  }

  @Override
  public boolean hasNew(int readedLatestId) {
    // 由于是最新的在最前面，所以只需要比较最前面的一个
    Announcement announ = (Announcement) opsForList.index(announCont, 0);
    return announ != null && announ.getAnnouncementId() > readedLatestId;
  }
}