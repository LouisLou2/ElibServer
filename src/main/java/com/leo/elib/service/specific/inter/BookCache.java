package com.leo.elib.service.specific.inter;

import org.springframework.data.util.Pair;

import java.util.List;

public interface BookCache {
  Pair<String,String> getCategoryName(int categoryId, int subCategoryId);
  List<String> getTagNames(List<Short> tagIds);
}
