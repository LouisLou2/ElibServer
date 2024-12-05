package com.leo.elib.service.specific.impl.cache;

import com.leo.elib.entity.BookCate;
import com.leo.elib.service.base_service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.cache.HitCategoryIdRepository;
import com.leo.elib.service.specific.inter.cache.static_type.BookCache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class HitCategoryRepositoryIdImpl implements HitCategoryIdRepository {

  @Resource
  private RCacheManager rCacheManager;

  private ZSetOperations<String, Integer> opsForZSetInt;

  @Value("${container.redis.hit-cate.hit-sub-cate-zset}")
  private String hitSubCategoryCont;

  List<Integer> hitSubCateIds;

  @PostConstruct
  private void init(){
    opsForZSetInt = rCacheManager.getOpsForZSetInt();
    // 获取所有
    // reverseRange：获取所有元素按分数降序排列
    Set<Integer> range = opsForZSetInt.reverseRange(hitSubCategoryCont, 0, -1); // -1 表示获取所有元素
    // 将结果转换为 List<Integer>
    assert range != null;
    hitSubCateIds = new ArrayList<>(range);
  }

  @Override
  public List<Integer> getHitSubCategoryIds(int num) {
    int max = Math.min(num, hitSubCateIds.size());
    return hitSubCateIds.subList(0, max);
  }
}