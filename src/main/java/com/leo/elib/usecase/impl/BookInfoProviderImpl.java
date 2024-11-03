package com.leo.elib.usecase.impl;

import com.leo.elib.constant.book.LibBookStatusEnum;
import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.BookInfo;
import com.leo.elib.entity.dto.dao.Author;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.entity.dto.dao.SimpleLib;
import com.leo.elib.mapper.AuthorMapper;
import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.mapper.LibBorrowMapper;
import com.leo.elib.service.specific.inter.BookCache;
import com.leo.elib.service.specific.inter.LangCache;
import com.leo.elib.service.specific.inter.RecoBookProvider;
import com.leo.elib.usecase.inter.BookInfoProvider;
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
  private BookCache bookCache;
  @Resource
  private LangCache langCache;
  @Resource
  private RecoBookProvider recoBookProv;
  
  private void setBookInfoCachedFields(BookInfo bookInfo) {
    Pair<String,String> categoryNamePair = bookCache.getCategoryName(bookInfo.getCategory1(), bookInfo.getCategory2());
    List<String> tagNames = bookCache.getTagNames(bookInfo.getTagIds());
    bookInfo.setNames(
      langCache.getLangName((byte) bookInfo.getLangId()),
      categoryNamePair.getFirst(),
      categoryNamePair.getSecond(),
      tagNames
    );
  }

  @Override
  public BookInfo getBookInfo(String isbn) {
    BookInfo bi = bookInfoMapper.getBookInfoByIsbn(isbn);
    List<SimpleLib> libs = libBorrowMapper.getLibsWithStatus(
      isbn, 
      LibBookStatusEnum.AVAILABLE.getCode()
    );
    bi.setAvailableLibs(libs);
    setBookInfoCachedFields(bi);
    return bi;
  }
  
  @Override
  public List<BookBrief> getRecoBooks(int userId, int offset, int num) {
    List<String> recoBookIsbns = recoBookProv.getRecoBookIsbns(userId,offset, num);
    return bookInfoMapper.getBookBriefList(recoBookIsbns);
  }

  /* 其中一个实现思路是直接在一个sql中查询出所有的信息(目前的视线较为复杂，且有临时表)
  *  另一个思路是先查询出作者的基本信息，再查询出作者的书籍信息，然后组合在一起
  *  测试下来，第二种思路的性能更好，因此采用第二种思路
  */
  @Override
  public AuthorWithBookLis getAuthorWithBooks(int authorId, int num) {
    Author author = authorMapper.getAuthor(authorId);
    List<BookBrief> bookBriefList = bookInfoMapper.getAuthorBooks(authorId, 0, num);
    return new AuthorWithBookLis(
      author, 
      bookBriefList
    );
  }

  @Override
  public List<BookBrief> getBooksByAuthor(int authorId, int offset, int num) {
    return bookInfoMapper.getAuthorBooks(authorId, offset, num);
  }
}
