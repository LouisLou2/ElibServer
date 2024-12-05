package com.leo.elib.service.specific.impl.cache.resource;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
final public class AuthorCacheAccessScriptImpl implements AuthorCacheAccessScript {

  private String putAuthorWithLisScript;
  private String getAuthorScript;
  private String getBookBriefsScript;
  private String getAuthorWithBooksScript;

  @Override
  public String putAuthorWithLisScript() {
    return putAuthorWithLisScript;
  }

  @Override
  public String getAuthorScript() {
    return getAuthorScript;
  }

  @Override
  public String getBookBriefsScript() {
    return getBookBriefsScript;
  }

  @Override
  public String getAuthorWithBooksScript() {
    return getAuthorWithBooksScript;
  }

  @PostConstruct
  void init(){
    putAuthorWithLisScript = """
    local hashName = KEYS[1] -- hash容器名字
    local zsetName = KEYS[2] -- zset容器名字
    local key = KEYS[3] -- id
    local keyAuthor = KEYS[4] -- value的author字段名字
    local keyBooks = KEYS[5] -- value的books字段名字
    
    local maxCapacity = ARGV[1] -- 最大容量
    local authorVal = ARGV[2] -- author的值
    local booksVal = ARGV[3] -- books的值
    
    -- 如果容器已经满了
    if redis.call('ZCARD', zsetName) >= tonumber(maxCapacity) then
        -- 找到最不常用的元素
        local minScoreId = redis.call('ZRANGE', zsetName, 0, 0)[1]
        -- 删除最不常用的元素
        redis.call('ZREM', zsetName, minScoreId)
        -- 删除hash中对应的元素
        redis.call('HDEL', hashName, minScoreId..keyAuthor)
        redis.call('HDEL', hashName, minScoreId..keyBooks)
    end
    
    -- 将新元素存入hash
    redis.call('HSET', hashName, key..keyAuthor, authorVal)
    redis.call('HSET', hashName, key..keyBooks, booksVal)
    
    -- 将新元素存入zset
    redis.call('ZADD', zsetName, 0, key)
    
    return 1
    """;

    getAuthorWithBooksScript = """
    local hashName = KEYS[1] -- hash容器名字
    local zsetName = KEYS[2] -- zset容器名字
    local key = KEYS[3] -- id
    
    local keyAuthor = KEYS[4] -- value的author字段名字
    local keyBooks = KEYS[5] -- value的books字段名字
    
    -- 检查zset中是否存在
    local exist = redis.call('ZSCORE', zsetName, key)
    
    -- 如果不存在，返回空
    if not exist then
      return nil
    end
    
    local author = redis.call('HGET', hashName, key..keyAuthor)
    
    local books = redis.call('HGET', hashName, key..keyBooks)
    
    -- 增加zset中的分数
    redis.call('ZINCRBY', zsetName, 1, key)
    
    return {author, books}
    """;

    getAuthorScript = """
    local hashName = KEYS[1] -- hash容器名字
    local zsetName = KEYS[2] -- zset容器名字
    local key = KEYS[3] -- id
    local keyAuthor = KEYS[4] -- value的author字段名字
    
    -- 检查zset中是否存在
    local exist = redis.call('ZSCORE', zsetName, key)
    
    -- 如果不存在，返回空
    if not exist then
      return nil
    end
    
    -- increase the score
    redis.call('ZINCRBY', zsetName, 1, key)
    
    local author = redis.call('HGET', hashName, key..keyAuthor)
    return author
    """;

    getBookBriefsScript = """
    local hashName = KEYS[1] -- hash容器名字
    local zsetName = KEYS[2] -- zset容器名字
    local key = KEYS[3] -- id
    local keyBooks = KEYS[4] -- value的books字段名字
    
    -- 检查zset中是否存在
    local exist = redis.call('ZSCORE', zsetName, key)
    
    -- 如果不存在，返回空
    if not exist then
      return nil
    end
    
    -- increase the score
    redis.call('ZINCRBY', zsetName, 1, key)
    
    local books = redis.call('HGET', hashName, key..keyBooks)
    return books
    """;
  }
}