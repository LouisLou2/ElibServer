package com.leo.elib.usecase.impl;

import com.leo.elib.dto.dao.BookDaoForList;
import com.leo.elib.dto.resp.BookInfoForList;
import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.service.specific.inter.BookCache;
import com.leo.elib.service.specific.inter.LangCache;
import com.leo.elib.service.specific.inter.BookInfoProvider;
import com.leo.elib.usecase.inter.RecoBookProvider;
import jakarta.annotation.Resource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookInfoProviderImpl implements BookInfoProvider {
  @Resource
  private BookInfoMapper bookInfoMapper;
  @Resource
  private BookCache bookCache;
  @Resource
  private LangCache langCache;
  @Resource
  private RecoBookProvider recoBookProv;

  @Override
  public BookInfoForList getBookInfo(String isbn) {
    BookDaoForList bookDao = bookInfoMapper.getBookInfoByIsbn(isbn);
    Pair<String,String> categoryNamePair = bookCache.getCategoryName(bookDao.getCategory1(), bookDao.getCategory2());
    List<String> tagNames = bookCache.getTagNames(bookDao.getTagIds());
    return new BookInfoForList(
      bookDao, 
      langCache.getLangName((byte) bookDao.getLangId()), 
      categoryNamePair.getFirst(), 
      categoryNamePair.getSecond(), 
      tagNames
    );
  }

  @Override
  public List<BookInfoForList> getBookInfoList(List<String> bookIsbns) {
    List<BookDaoForList> bookDaoList = bookInfoMapper.getBookInfoList(bookIsbns);
    Pair<String,String> categoryNamePair;
    List<String> tagNames;
    List<BookInfoForList> bookInfoList = new ArrayList<>();
    // 没用stream写法，因为映射函数内都多个局部变量，频繁开辟释放可能会影响性能
    for (BookDaoForList bookDao : bookDaoList) {
      categoryNamePair = bookCache.getCategoryName(bookDao.getCategory1(), bookDao.getCategory2());
      tagNames = bookCache.getTagNames(bookDao.getTagIds());
      bookInfoList.add(
        new BookInfoForList(
          bookDao, 
          langCache.getLangName((byte) bookDao.getLangId()), 
          categoryNamePair.getFirst(), 
          categoryNamePair.getSecond(), 
          tagNames
        )
      );
    }
    return bookInfoList;
  }

  @Override
  public List<BookInfoForList> getRecoBooks(int userId, int offset, int num) {
    List<String> recoBookIsbns = recoBookProv.getRecoBookIsbns(userId,offset, num);
    return getBookInfoList(recoBookIsbns);
  }
}
