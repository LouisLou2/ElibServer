package com.leo.elib.service.specific.inter.cache.static_type;

import org.springframework.data.util.Pair;

import java.util.List;

public interface BookCache {
  Pair<String,String> getCategoryName(int categoryId, int subCategoryId);
  // 注意category的设计一级分类和二级分类的数值都是不重复的，没必要加上level
  String getCategoryName(int categoryId);
  List<String> getTagNames(List<Short> tagIds);
}
