package com.leo.elib.usecase.impl;

import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.BookInfoForList;
import com.leo.elib.entity.dto.dao.Author;
import com.leo.elib.entity.dto.dao.BookDaoForList;
import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.service.specific.inter.BookCache;
import com.leo.elib.service.specific.inter.LangCache;
import com.leo.elib.service.specific.inter.RecoBookProvider;
import com.leo.elib.usecase.inter.BookInfoProvider;
import jakarta.annotation.Resource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
  private BookInfoForList getBookInfoForListByDao(BookDaoForList bookDao) {
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
  public BookInfoForList getBookInfo(String isbn) {
    BookDaoForList bookDao = bookInfoMapper.getBookInfoByIsbn(isbn);
    return getBookInfoForListByDao(bookDao);
  }

  @Override
  public List<BookInfoForList> getBookInfoList(List<String> bookIsbns) {
    List<BookDaoForList> bookDaoList = bookInfoMapper.getBookInfoList(bookIsbns);
    return bookDaoList.stream()
      .map(this::getBookInfoForListByDao)
      .collect(Collectors.toList());
  }

  @Override
  public List<BookInfoForList> getRecoBooks(int userId, int offset, int num) {
    List<String> recoBookIsbns = recoBookProv.getRecoBookIsbns(userId,offset, num);
    return getBookInfoList(recoBookIsbns);
  }

  /* 其中一个实现思路是直接在一个sql中查询出所有的信息(目前的视线较为复杂，且有临时表)
  *  另一个思路是先查询出作者的基本信息，再查询出作者的书籍信息，然后组合在一起
  *  测试下来，第二种思路的性能更好，因此采用第二种思路
  */
  @Override
  public AuthorWithBookLis getAuthorWithBooks(int authorId, int num) {
    Author author = bookInfoMapper.getAuthor(authorId);
    List<BookDaoForList> bookDaoList = bookInfoMapper.getAuthorBooks(authorId, 0, num);
    return new AuthorWithBookLis(
      author, 
      bookDaoList
        .stream()
        .map(this::getBookInfoForListByDao)
        .collect(Collectors.toList())
    );
  }
}
