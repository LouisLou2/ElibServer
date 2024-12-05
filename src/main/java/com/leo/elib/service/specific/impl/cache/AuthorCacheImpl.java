package com.leo.elib.service.specific.impl.cache;

import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.dto.dao.Author;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.service.base_service.inter.RCacheManager;
import com.leo.elib.service.specific.impl.cache.resource.AuthorCacheAccessScript;
import com.leo.elib.service.specific.inter.cache.AuthorCache;
import com.leo.elib.util.ListUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorCacheImpl implements AuthorCache {

  @Value("${container.redis.author.hash-cont-name}")
  private String hashContName; // elib:hash:author

  @Value("${container.redis.author.zset-cont-name}")
  private String zsetContName; // elib:zset:author

  @Value("${container.redis.author.author-part-suffix}")
  private String authorPartSuffix; // author

  @Value("${container.redis.author.book-part-suffix}")
  private String bookPartSuffix; // book

  @Value("${container.redis.author.max_capacity}")
  private int maxCapacity; // 1000

  @Value("${container.redis.author.cache-author-books-num}")
  private int cacheAuthorBooksNum; // 20

  @Resource
  private RCacheManager rCacheManager;

  @Resource
  private AuthorCacheAccessScript accessScripts;

  private RedisTemplate<String, Object> redisTemplate;

  private DefaultRedisScript<Long> redisLongResScript;

  private DefaultRedisScript<List> redisListResScript;

  private DefaultRedisScript<Author> redisAuthorResScript;

  private DefaultRedisScript<Object> redisObjectResScript;


  @PostConstruct
  void init(){
    redisTemplate = rCacheManager.getRedisObjTemplate();
    authorPartSuffix = ':' + authorPartSuffix;
    bookPartSuffix = ':' + bookPartSuffix;
    redisLongResScript = new DefaultRedisScript<>(accessScripts.putAuthorWithLisScript(), Long.class);
    redisListResScript = new DefaultRedisScript<>(accessScripts.getAuthorWithBooksScript(), List.class);
    redisAuthorResScript = new DefaultRedisScript<>(accessScripts.getAuthorScript(), Author.class);
    redisObjectResScript = new DefaultRedisScript<>(accessScripts.getBookBriefsScript(), Object.class);
  }

//  void safeShrinkList(List<BookBrief> bookBriefs, int offset, int num) {
//    // 将offset 之后的num个元素移动至开头
//    int realBegin = Math.min(offset, bookBriefs.size());
//    int realEnd = Math.min(offset + num, bookBriefs.size());
//    for (int i = realBegin; i < realEnd; ++i) {
//      bookBriefs.set(i - realBegin, bookBriefs.get(i));
//    }
//    // 删除多余的元素
//    int needDel = realEnd - realBegin;
//    int count = 0;
//    while (count < needDel) {
//      bookBriefs.remove(bookBriefs.size() - 1);
//      ++count;
//    }
//  }

  @Override
  public Author getAuthorById(int authorId) {
    return redisTemplate.execute(
      redisAuthorResScript,
      List.of(
        hashContName,
        zsetContName,
        String.valueOf(authorId),
        authorPartSuffix
      )
    );
  }

  @Override
  public List<BookBrief> getBookBriefsByAuthorIdLazy(int authorId, int num, int offset) {
    assert num > 0 && offset >= 0;
    if (offset >= cacheAuthorBooksNum) return new ArrayList<>();
    Pair<Boolean, List<BookBrief>> res = getBookBriefsByAuthorId(authorId, num, offset);
    return res.getSecond();
  }

  @Override
  public Pair<Boolean, List<BookBrief>> getBookBriefsByAuthorId(int authorId, int num, int offset) {
    Object res = redisTemplate.execute(
      redisObjectResScript,
      List.of(
          hashContName,
          zsetContName,
          String.valueOf(authorId),
          bookPartSuffix
      )
    );
    if (res == null) return Pair.of(false, new ArrayList<>());
    List<BookBrief> bookBriefs = (List<BookBrief>) res;
    return Pair.of(true, ListUtil.safeSubList(offset, num,bookBriefs));
  }

  @Override
  public AuthorWithBookLis getAuthorWithBookList(int authorId, int booksNum) {
    List<Object> res = redisTemplate.execute(
      redisListResScript,
      List.of(
        hashContName,
        zsetContName,
        String.valueOf(authorId),
        authorPartSuffix,
        bookPartSuffix
      )
    );
    assert res.size() == 2 && ((res.get(0) == null && res.get(1) == null) || (res.get(0) != null && res.get(1) != null));
    if (res.get(0) == null) return null;
    Author author = (Author) res.get(0);
    List<BookBrief> bookBriefs = (List<BookBrief>) res.get(1);
    // 将多余的书籍删除，直接在原有的list上操作
    int needDel = bookBriefs.size() - booksNum;
    int count = 0;
    while (count < needDel) {
      bookBriefs.remove(bookBriefs.size() - 1);
      ++count;
    }
    return AuthorWithBookLis.from(author, bookBriefs);
  }

  @Async("taskExecutor")
  @Override
  public void insertAuthorWithBookLis(AuthorWithBookLis authorWithBookLis) {
    redisTemplate.execute(
      redisLongResScript,
      List.of(
        hashContName,
        zsetContName,
        String.valueOf(authorWithBookLis.getAuthorId()),
        authorPartSuffix,
        bookPartSuffix
      ),
      maxCapacity,
      authorWithBookLis.getAuthor(),
      authorWithBookLis.getBooks()
    );
  }

  @Override
  public int numOfBooksCachedPerAuthor() {
    return cacheAuthorBooksNum;
  }
}