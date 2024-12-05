package com.leo.elib.usecase.impl;

import com.leo.elib.entity.BookCate;
import com.leo.elib.service.specific.inter.cache.HitCategoryIdRepository;
import com.leo.elib.service.specific.inter.cache.static_type.BookCache;
import com.leo.elib.usecase.inter.HitCateRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HitCateRepositoryImpl implements HitCateRepository {
  @Resource
  private HitCategoryIdRepository hitCategoryIdRepo;
  @Resource
  private BookCache bookCache;


  @Override
  public List<BookCate> getHitSubCategory(int num) {
    List<Integer> hitSubCategoryIds = hitCategoryIdRepo.getHitSubCategoryIds(num);
    return bookCache.getCate(hitSubCategoryIds);
  }
}
