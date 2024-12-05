package com.leo.elib.usecase.inter;

import com.leo.elib.entity.BookCate;

import java.util.List;

public interface HitCateRepository {
  List<BookCate> getHitSubCategory(int num);
}
