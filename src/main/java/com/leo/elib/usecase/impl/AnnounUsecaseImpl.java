package com.leo.elib.usecase.impl;

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
    announCache = useApplicationCache ?
        applicationContext.getBean(AnnounCacheApplcationImpl.class) :
        applicationContext.getBean(AnnounCacheImpl.class);
    List<Announcement> announs = announMapper.getLatestAnnouns(announCache.cacheMaxCapacity(), 0);
    announCache.flushWithData(announs);
  }

  @PostConstruct
  private void init() {
    injectAnnounCache();
  }

  @Override
  public void insertAnnounAsLatest(Announcement announ) {
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
      List<Announcement> announsFromDB = announMapper.getLatestAnnouns(num - announs.size(), offset+announs.size());
      // 不加入缓存
      announs.addAll(announsFromDB);
    }
    return announs;
  }

  @Override
  public boolean hasNew(int readedLatestId) {
    return announCache.hasNew(readedLatestId);
  }
}