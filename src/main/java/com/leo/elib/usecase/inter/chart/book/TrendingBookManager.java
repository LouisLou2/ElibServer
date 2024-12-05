package com.leo.elib.usecase.inter.chart.book;

import com.leo.elib.constant.user.UserBookBehavior;
import com.leo.elib.entity.dto.dao.BookBrief;

import java.util.List;
import java.util.Set;

public interface TrendingBookManager {
  // 报告用户行为，这个行为可能会影响书籍的热度
  void reportUserBehavior(String isbn, UserBookBehavior behavior);
  // 获取热门书籍列表,因为zset的长度可能会随用户的行为增长，但是只关心前n名，这个方法会返回前n名的书籍isbn
  Set<String> bookInListIsbnClone();
  Set<String> bookInListIsbn();

  List<BookBrief> getTrendingBooks(int pageNum, int pageSize);

  // removeColdBooksAndApplyDecay和updateInnerCache会在进行更新的时候同时调用
  // removeColdBooksAndApplyDecay会删除掉冷门书籍，并且对热门书籍的热度进行衰减, 内部还会更新trendingBookIsbn这个set, 返回有序的isbn列表
  List<String> removeColdBooksAndApplyDecay(boolean applyTimeDecay);
  void updateInnerCache(List<BookBrief> books);
}
