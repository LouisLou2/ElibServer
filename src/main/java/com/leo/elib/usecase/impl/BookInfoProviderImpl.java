package com.leo.elib.usecase.impl;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.constant.book.LibBookStatus;
import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.BookInfo;
import com.leo.elib.entity.dto.dao.Author;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.entity.dto.dao.SimpleLib;
import com.leo.elib.mapper.AuthorMapper;
import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.mapper.LibBorrowMapper;
import com.leo.elib.service.specific.inter.RecoBookProvider;
import com.leo.elib.service.specific.inter.cache.AuthorCacheNew;
import com.leo.elib.service.specific.inter.cache.ChartsBookCacheExecutor;
import com.leo.elib.service.specific.inter.cache.static_type.BookInfoCacheManager;
import com.leo.elib.usecase.inter.BookInfoProvider;
import com.leo.elib.util.ListUtil;
import jakarta.annotation.Resource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookInfoProviderImpl implements BookInfoProvider {

  @Resource
  private BookInfoMapper bookInfoMapper;

  @Resource
  private AuthorMapper authorMapper;

  @Resource
  private LibBorrowMapper libBorrowMapper;

  @Resource
  private RecoBookProvider recoBookProv;

  @Resource
  private BookInfoCacheManager bInfoCache;

  @Resource
  private ChartsBookCacheExecutor chartsBookCache;

  @Resource
  private AuthorCacheNew authorCache;

  /**
   * 此方法会进行缓存加入，如果有需要的话
   * 注意如果缓存里的不足要用的数量，会从数据库中获取，然后加入缓存
   * @param authorId
   * @param num
   * @return
   */
  private List<BookBrief> getFromCacheAndDB(int authorId, int num) {
    NullablePair<Boolean, List<BookBrief>> cacheRes = authorCache.getBookBriefsByAuthorId(authorId, num, 0);
    if (cacheRes.getFirst()) {
      return cacheRes.getSecond();
    }
    // 数据库中获取
    AuthorWithBookLis res = bookInfoMapper.getAuthorWithBooks(authorId, Integer.MAX_VALUE);
    authorCache.insertAuthorWithBookLis(res);
    return ListUtil.safeSubList(0, num, res.getBooks());
  }

  @Override
  public BookInfo getBookInfo(String isbn, int relatedBookNum) {
    BookInfo bi = chartsBookCache.getBookWithoutLibs(isbn);
    if (bi == null) {
      bi = bookInfoMapper.getBookInfoWithoutLibs(isbn);
    }
    if (bi == null) {
      return null;
    }
    List<SimpleLib> libs = libBorrowMapper.getLibsWithStatus(
      isbn,
      LibBookStatus.Available.getCode()
    );
    bi.setAvailableLibs(libs);
    bInfoCache.setBookInfoCachedFields(bi);
    assert !bi.getAuthorIds().isEmpty();
    int firstAuthorId = bi.getAuthorIds().get(0);
    List<BookBrief> authorBooks = getFromCacheAndDB(firstAuthorId, relatedBookNum);
    // 剔除自己
    authorBooks.removeIf(bookBrief -> bookBrief.getIsbn().equals(isbn));
    bi.setRelatedBooks(authorBooks);
    return bi;
  }

  @Override
  public List<BookBrief> getRecoBooks(int userId, int offset, int num) {
    List<String> recoBookIsbns = recoBookProv.getRecoBookIsbns(userId, offset, num);
    // 注意接下来的操作返回的BookBrief的顺序不一定与recoBookIsbns中的isbn顺序一致, 在这里是可以接受的
    Pair<List<BookInfo>,Boolean> cacheRes = chartsBookCache.getBooksWithoutLibs(recoBookIsbns);
    if (cacheRes.getSecond()) {
      // 说明一个也没找到
      // 干脆全部从数据库中查找
      return bookInfoMapper.getBookBriefList(recoBookIsbns);
    }
    List<BookInfo> bookInfos = cacheRes.getFirst();
    List<BookBrief> bookBriefs = new java.util.ArrayList<>(bookInfos.size());
    List<Integer> nullIndexs = new java.util.ArrayList<>();
    List<String> nullIsbns = new java.util.ArrayList<>();
    for (int i = 0; i < bookInfos.size(); i++) {
      BookInfo bi = bookInfos.get(i);
      if (bi == null) {
        nullIndexs.add(i);
        nullIsbns.add(recoBookIsbns.get(i));
        bookBriefs.add(null);
      } else {
        bookBriefs.add(bi.toBrief());
      }
    }
    if (!nullIndexs.isEmpty()) {
      List<BookBrief> nullBookBriefs = bookInfoMapper.getBookBriefList(nullIsbns);
      for (int i = 0; i < nullIndexs.size(); i++) {
        bookBriefs.set(nullIndexs.get(i), nullBookBriefs.get(i));
      }
    }
    return bookBriefs;
  }

  @Override
  public List<BookBrief> getBooksBySubCate(int subCateId, int offset, int num) {
    return bookInfoMapper.getCategoryBriefBooks((byte) 2, subCateId, offset, num);
  }

//  /* 其中一个实现思路是直接在一个sql中查询出所有的信息(目前的视线较为复杂，且有临时表)
//  *  另一个思路是先查询出作者的基本信息，再查询出作者的书籍信息，然后组合在一起
//  *  测试下来，第二种思路的性能更好，因此采用第二种思路
//  */
//  @Override
//  public AuthorWithBookLis getAuthorWithBooks(int authorId, int num) {
//    /*
//    * 不可用，还没有在此方法引入缓存
//    * */
//    Author author = authorMapper.getAuthor(authorId);
//    List<BookBrief> bookBriefList = bookInfoMapper.getAuthorBooks(authorId, 0, num);
//    return new AuthorWithBookLis(
//      authorId,
//      author,
//      bookBriefList
//    );
//  }

//  @Override
//  public List<BookBrief> getBooksByAuthor(int authorId, int offset, int num) {
//    /*
//     * 不可用，还没有在此方法引入缓存
//     * */
//    return bookInfoMapper.getAuthorBooks(authorId, offset, num);
//  }

  @Override
  public List<BookInfo> debug_getBooksByIsbn(int offset, int num) {
    List<BookInfo> bookInfos = bookInfoMapper.debug_getBookInfo(offset, num);
    for (BookInfo bi : bookInfos) {
      bInfoCache.setBookInfoCachedFields(bi);
    }
    return bookInfos;
  }
}
