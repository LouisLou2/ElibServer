package com.leo.elib.usecase.impl.chart;

import com.leo.elib.entity.BookInfo;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.service.specific.inter.cache.ChartsBookCacheExecutor;
import com.leo.elib.service.specific.inter.cache.static_type.BookInfoCacheManager;
import com.leo.elib.usecase.inter.chart.BookChartsManager;
import com.leo.elib.usecase.inter.chart.HighRatingBookChartManager;
import com.leo.elib.usecase.inter.chart.TrendingBookManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookChartsManagerImpl implements BookChartsManager {
  @Resource
  private BookInfoMapper bookInfoMapper;
  @Resource
  private BookInfoCacheManager bInfoCache;
  @Resource
  private ChartsBookCacheExecutor chartsBookCacheExecutor;
  @Resource
  private TrendingBookManager trendingBookManager;
  @Resource
  private HighRatingBookChartManager highRatingBookChartManager;

  @PostConstruct
  private void init() {
    updateBookChartsInner(false);
  }

  // 定时任务，凌晨2:00更新榜单
  @Scheduled(cron = "0 0 2 * * ?")
  @Async("taskExecutor")
  @Override
  public void updateBookCharts() {
    updateBookChartsInner(true);
  }

  private void updateBookChartsInner(boolean withTimeDecay){
    // 先更新一下highRatingBookChartManager
    highRatingBookChartManager.updateHighRatingBooks();
    Set<String> highRatingBookIsbn = highRatingBookChartManager.highRatingBookIsbnClone(); // 会修改数据，所以需要获取clone的set


    // 进行热度衰减，和冷书籍的删除，内部的trendingBookIsbn会更新
    // 注意这是内部需要的BookBrief缓存还没有更新，会在下面更新
    List<String> trendingBookIsbnList = trendingBookManager.removeColdBooksAndApplyDecay(withTimeDecay);
    // beware: can't modify trendingBookIsbn in the process!!!!
    Set<String> trendingBookIsbn = trendingBookManager.bookInListIsbn();//只有确保这个方法不会修改数据，才能这样使用，否则需要获取clone的set

    // 获取set的并集，highRatingBookIsbn作为并集使用
    highRatingBookIsbn.addAll(trendingBookIsbn);

    // 获取已经缓存的书籍的isbn
    Set<String> cachedBooksIsbn = chartsBookCacheExecutor.getCachedBooksIsbnClone(); // 会修改数据，所以需要获取clone的set

    // 求交集
    Set<String> needAndHave =
      highRatingBookIsbn.stream()
        .filter(cachedBooksIsbn::contains)
        .collect(Collectors.toSet());


    // 冗余的数据,cachedBooksIsbn的含义变为了冗余的数据
    cachedBooksIsbn.removeAll(needAndHave);
    // 删除冗余的数据
    chartsBookCacheExecutor.deleteBatch(cachedBooksIsbn.toArray(new String[0]));

    // 需要的数据,highRatingBookIsbn的含义变为了需要但是没有的数据
    highRatingBookIsbn.removeAll(needAndHave);

    // 从数据库获取需要的数据，这是新的数据
    List<BookInfo> books;
    if (highRatingBookIsbn.isEmpty()) books = new ArrayList<>();
    else books = bookInfoMapper.getBookInfoWithoutLibsList(highRatingBookIsbn.stream().toList());
    for (BookInfo book : books) {
      bInfoCache.setBookInfoCachedFields(book);
    }
    /* 在数据存储到缓存之前，进行一步操作：trendingBookManager的内部缓存更新(内部List<BookBrief>)
     * List<String> trendingBookIsbnList是需要的List<BookBrief>顺序，但是对应的资源可能一部分存在于缓存,一部分在books中，
     * 先去缓存探测，然后对元素位置为null的，到books中去找，找到就替换(按现在的逻辑，不肯呢个两处都找不到)
     */
    List<BookInfo> trendingList = chartsBookCacheExecutor.getBooksWithoutLibs(trendingBookIsbnList);
    List<BookBrief> trendingBookBriefList = new ArrayList<>(trendingList.size());
    List<Integer> nullIndex = new ArrayList<>();
    for (int i=0;i<trendingList.size();i++){
      BookInfo info = trendingList.get(i);
      if (info == null){
        nullIndex.add(i);
        trendingBookBriefList.add(null);
      }else {
        trendingBookBriefList.add(info.toBrief());
      }
    }

    if (!nullIndex.isEmpty()){
      // 构建一个索引 Map，提前为每个 isbn 创建映射
      Map<String, BookInfo> bookInfoMap = books.stream()
        .collect(Collectors.toMap(BookInfo::getIsbn, book -> book));
      for (int i:nullIndex) {
        String isbn = trendingBookIsbnList.get(i);
        BookInfo info = bookInfoMap.get(isbn);
        assert info != null;
        trendingBookBriefList.set(i, info.toBrief());
      }
    }
    // 更新trendingBookManager的内部缓存
    trendingBookManager.updateInnerCache(trendingBookBriefList);
    // 存储需要的数据
    chartsBookCacheExecutor.storeBook(books);
  }

}