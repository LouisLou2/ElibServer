package com.leo.elib.service.specific.impl.cache;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.BriefListPack;
import com.leo.elib.entity.dto.dao.Author;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.service.base_service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.cache.AuthorCacheNew;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AuthorCacheRedisImpl implements AuthorCacheNew {

  @Resource
  private RCacheManager rCacheManager;

  private RedisTemplate<String, Object> redisTemplate;

  // 配置项注入
  @Value("${container.redis.author_cache.hash-cont-name}")
  private String hashContName;

  @Value("${container.redis.author_cache.zset-cont-name}")
  private String zsetContName;

  @Value("${container.redis.author_cache.author-book-list-prefix}")
  private String authorBookListPrefix;

  @Value("${container.redis.author_cache.max_capacity}")
  private int maxCapacity;

  final private String getAuthorBooksScript = """
  -- Check if wordId exists in the zset
  local zset_name = KEYS[1]
  local list_prefix = KEYS[2]
  
  local authorId = tonumber(ARGV[1])
  local num = tonumber(ARGV[2])
  local offset = tonumber(ARGV[3])
  
  -- Check if authorId exists in the zset
  local exists = redis.call("ZSCORE", zset_name, authorId)
  
  if not exists then
      -- If authorId does not exist in zset, return null
      return nil
  end
  -- If wordId exists in zset, return the list, and update the zset
  redis.call("ZINCRBY", zset_name, 1, authorId)
  
  -- Get elements from list:wordId with offset and num
  local result = redis.call("LRANGE", list_prefix .. ":" .. authorId, offset, offset + num - 1)
  
  return result
  """;

  final private String getAuthorAndBooksScript = """
  -- Check if wordId exists in the zset
  local zset_name = KEYS[1]
  local list_prefix = KEYS[2]
  local hash_name = KEYS[3]
  
  local authorId = ARGV[1]
  local num = tonumber(ARGV[2])
  
  -- Check if authorId exists in the zset
  local exists = redis.call("ZSCORE", zset_name, authorId)
  
  if not exists then
      -- If wordId does not exist in zset, return null
      return nil
  end
  -- If wordId exists in zset, return the list, and update the zset
  redis.call("ZINCRBY", zset_name, 1, authorId)
  
  -- Get elements from list:wordId with offset and num
  local list_result = redis.call("LRANGE", list_prefix .. ":" .. authorId, 0, num - 1)
  local author_result = redis.call("HGET", hash_name, authorId)
  
  return {author_result, list_result}
  """;

  final private String insertAuthorWithBooksScript = """
  -- 获取 zset 中的大小
  local zset_name = KEYS[1]
  local list_prefix = KEYS[2]
  local hash_name = KEYS[3]
  
  local max_num = tonumber(ARGV[1])
  local authorId = ARGV[2]
  local authorInitialScore = tonumber(ARGV[3])
  local author = ARGV[4]
  
  -- 检查 zset 的大小
  local zset_size = redis.call("ZCARD", zset_name)
  
  if zset_size >= max_num then
      -- 如果 zset 已经满了，找到最小的时间戳和对应的 wordId（LRU 策略）
      local min_entry = redis.call("ZRANGE", zset_key, 0, 0, "WITHSCORES")
      local min_score = tonumber(min_entry[2])
      local min_authorId = min_entry[1]
  
      -- 移除最小时间戳的成员及对应的 list
      redis.call("ZREM", zset_name, min_authorId)
      redis.call("DEL", list_prefix .. ":" .. min_authorId)
  
      -- 返回移除的 wordId 和对应的时间戳
      return min_authorId
  end
  
  -- 如果 zset 没有满，插入新的数据
  redis.call("ZADD", zset_name, authorInitialScore, authorId)
  local listName = list_prefix .. ":" .. authorId
  -- 向hash中插入新的数据
  redis.call("HSET", hash_name, authorId, author)
  -- 向 list 中插入新的数据（RecoComp 对象列表）
  for i = 5, #ARGV do
      -- ARGV 从第 5 项开始是 book 数据，这里假设每个 book 被序列化为 JSON 格式
      redis.call("RPUSH", listName, ARGV[i])
  end
  
  return authorId
  """;

  DefaultRedisScript<List> redisObjectPairResScript;
  DefaultRedisScript<List> redisAuthorBooksResScript;
  DefaultRedisScript<Long> redisInsertAuthorWithBooksScript;


  @PostConstruct
  private void init() {
    redisTemplate = rCacheManager.getRedisObjTemplate();
    redisObjectPairResScript = new DefaultRedisScript<>(getAuthorAndBooksScript, List.class);
    redisAuthorBooksResScript = new DefaultRedisScript<>(getAuthorBooksScript, List.class);
    redisInsertAuthorWithBooksScript = new DefaultRedisScript<>(insertAuthorWithBooksScript, Long.class);
  }

  @Override
  public NullablePair<Boolean,List<BookBrief>> getBookBriefsByAuthorId(int authorId, int num, int offset) {
    List result = redisTemplate.execute(
      redisAuthorBooksResScript,
      List.of(
        zsetContName,
        authorBookListPrefix
      ),
      authorId,
      num,
      offset
    );
    assert result != null;
    if (!result.isEmpty() && result.get(0) == null) {
      return new NullablePair<>(false, null);
    }
    List<BookBrief> bookBriefs = (List<BookBrief>) result;
    return new NullablePair<>(true, bookBriefs);
  }

  @Override
  public NullablePair<Boolean,AuthorWithBookLis> getAuthorAndBooksByAuthorId(int authorId, int num) {
    List result = redisTemplate.execute(
      redisObjectPairResScript,
      List.of(
        zsetContName,
        authorBookListPrefix,
        hashContName
      ),
      authorId,
      num
    );
    assert result != null;
    if (!result.isEmpty() && result.get(0) == null) {
      return new NullablePair<>(false, null);
    }
    Author author = (Author) result.get(0);
    List<BookBrief> bookBriefs = (List<BookBrief>) result.get(1);
    return new NullablePair<>(true, new AuthorWithBookLis(authorId, author, bookBriefs,false));
  }

  @Override
  public void insertAuthorWithBookLis(AuthorWithBookLis authorWithBookLis) {
    List<Object> args = new ArrayList<>();
    args.add(maxCapacity);
    args.add(authorWithBookLis.getAuthorId());
    args.add(0);
    args.add(authorWithBookLis.getAuthor());
    args.addAll(authorWithBookLis.getBooks());
    Long res = redisTemplate.execute(
      redisInsertAuthorWithBooksScript,
      List.of(
        zsetContName,
        authorBookListPrefix,
        hashContName
      ),
      args.toArray()
    );
    System.out.println(res);
  }
}