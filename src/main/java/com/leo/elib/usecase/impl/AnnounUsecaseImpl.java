package com.leo.elib.usecase.impl;

import com.leo.elib.entity.AnnounceBrief;
import com.leo.elib.entity.dto.dao.Announcement;
import com.leo.elib.mapper.AnnounMapper;
import com.leo.elib.service.specific.impl.cache.AnnounCacheApplcationImpl;
import com.leo.elib.service.specific.impl.cache.AnnounCacheImpl;
import com.leo.elib.service.specific.inter.cache.AnnounCache;
import com.leo.elib.usecase.inter.AnnounUsecase;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnounUsecaseImpl implements AnnounUsecase {

  @Value("${sys_setting.announcement.use-application-cache.enable}")
  private boolean useApplicationCache;

  @Value("${sys_setting.announcement.update-when-reboot}")
  private boolean flushCacheWhenReboot;

  private AnnounCache announCache;
  @Resource
  private AnnounMapper announMapper;

  @Resource
  private ApplicationContext applicationContext;

  private void injectAnnounCache() {
    if (useApplicationCache){
      // 不会再考虑flushCacheWhenReboot
      announCache = applicationContext.getBean(AnnounCacheApplcationImpl.class);
      List<Announcement> announs = announMapper.getLatestAnnouns(announCache.cacheMaxCapacity(), 0);
      announCache.flushWithData(announs);
      return;
    }

    // redis缓存尚未实现好，只能使用application cache
    throw new UnsupportedOperationException("redis cache not implemented yet");

//    announCache = applicationContext.getBean(AnnounCacheImpl.class);
//    if (flushCacheWhenReboot) {
//      List<Announcement> announs = announMapper.getLatestAnnouns(announCache.cacheMaxCapacity(), 0);
//      announCache.flushWithData(announs);
//    }
  }

  @PostConstruct
  private void init() {
    injectAnnounCache();
  }

  @Override
  public void insertAnnounAsLatest(Announcement announ) {
    assert announ!=null && announ.urlUnBuildOrNull();
    // 插入数据库和缓存
    announMapper.insertAnnoun(announ);
    announCache.insertAnnounAsLatest(announ);
  }

  @Override
  public List<Announcement> getLatestAnnoun(int num, int offset) {
    // 先去缓存中找
    List<Announcement> announs = announCache.getLatestAnnoun(num, offset);
    // 如果数量不够，再去数据库中补充
    if (announs.size() < num) {
      List<Announcement> announsFromDB = announMapper.getLatestAnnouns(num - announs.size(), offset + announs.size());
      announsFromDB.forEach(Announcement::buildUrl);
      // 不加入缓存
      announs.addAll(announsFromDB);
    }
    return announs;
  }

  @Override
  public List<AnnounceBrief> getLatestAnnounBrief(int num, int offset) {
    List<AnnounceBrief> announBriefs = announCache.getLatestAnnounBrief(num, offset);
    if (announBriefs.size() < num) {
      List<AnnounceBrief> announBriefsFromDB = announMapper.getLatestAnnounBrief(num - announBriefs.size(), offset + announBriefs.size());
      announBriefsFromDB.forEach(AnnounceBrief::buildUrl);
      announBriefs.addAll(announBriefsFromDB);
    }
    return announBriefs;
  }

  @Override
  public Announcement getAnnounDetail(int id) {
    // 先去缓存中找
    Announcement announ = announCache.getAnnounDetail(id);
    if (announ == null) {
      // 再去数据库中找
      announ = announMapper.getAnnounDetail(id);
    }
    if (announ != null)  announ.buildUrl();
    return announ;
  }

  @Override
  public boolean hasNew(int readedLatestId) {
    return announCache.hasNew(readedLatestId);
  }
}