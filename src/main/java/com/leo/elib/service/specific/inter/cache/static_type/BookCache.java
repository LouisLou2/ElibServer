package com.leo.elib.service.specific.inter.cache.static_type;

import com.leo.elib.entity.BookCate;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;

public interface BookCache {
  Pair<String,String> getCategoryName(int categoryId, int subCategoryId);
  // 注意category的设计一级分类和二级分类的数值都是不重复的，没必要加上level
  String getCategoryName(int categoryId);
  List<String> getTagNames(List<Short> tagIds);

  List<BookCate> getCate(List<Integer> cateIds);
  List<BookCate> getAllCates();
  List<BookCate> getSubCates(int parentId);
  Map<Integer,String> debug_GetAllCategoryNames();
}
