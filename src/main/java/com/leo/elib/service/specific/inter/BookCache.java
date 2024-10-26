package com.leo.elib.service.specific.inter;

import org.springframework.data.util.Pair;

public interface BookCache {
  Pair<String,String> getCategoryName(int categoryId, int subCategoryId);
}
