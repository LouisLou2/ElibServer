package com.leo.elib.service.specific.inter.cache;

import java.util.List;

public interface HitCategoryIdRepository {
  List<Integer> getHitSubCategoryIds(int num);
}