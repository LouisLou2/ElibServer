package com.leo.elib;

import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.service.base_service.inter.RCacheManager;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@SpringBootTest
public class SetBookCoverColor {

  @Resource
  BookInfoMapper bookInfoMapper;

  @Resource
  private RCacheManager rCacheManager;

  @Test
  void readAndSetColor(){
    String colorTxtPath = "D:/resources/project_resource/elib_pic/book_cover_color.txt";
    // 第一行是isbn，第二行是颜色
    // 读取文件
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(colorTxtPath));
      String isbn;
      String color;
      while ((isbn = reader.readLine()) != null && (color = reader.readLine()) != null) {
        bookInfoMapper.debug_setColor(isbn, Long.parseLong(color));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Test
  void testGetBookInfo(){
    var it = bookInfoMapper.getBookInfoWithoutLibs("000100039X");
    System.out.println(it);
  }

  @Test
  void testInsertAuthorWithBooks(){
    var it = bookInfoMapper.getAuthorWithBooks(5778, 10);
    System.out.println(it);
    String script = """
-- Lua 脚本：将 map 存入 Redis 哈希表
local hash = KEYS[1]
local key = KEYS[2]
local authorKey = KEYS[3]
local booksKey = KEYS[4]
local author = ARGV[1]
local books = ARGV[2]

-- 通过 HSET 将 map 存入 Redis 哈希表
redis.call("HSET", hash, key..authorKey, author)
redis.call("HSET", hash, key..booksKey, books)
return 1
        """;
    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
    // 存储到redis
    List<String> keys = List.of("elib:hash:author", "5784", ":author", ":books");
    rCacheManager.debug_getRedisTemplate().execute(redisScript, keys, it.getAuthor(), it.getBooks());

    // 从redis中取出
    var res = rCacheManager.getOpsForHash().get("elib:hash:author", "5778:author");
    var res2 = rCacheManager.getOpsForHash().get("elib:hash:author", "5778:books");
    System.out.println(res);
  }

  @Test
  void testLFU(){
    String LUA_PUT_SCRIPT = """
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

    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_PUT_SCRIPT, Long.class);

    var it = bookInfoMapper.getAuthorWithBooks(5779, 10);
    // 存储到redis
    List<String> keys = List.of("elib:hash:author", "elib:zset:author", "5779", ":author", ":books");
    rCacheManager.debug_getRedisTemplate().execute(redisScript, keys, 2, it.getAuthor(), it.getBooks());
  }

  @Test
  void testMultiGet(){
    String script = """
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
      
     return books
     """;

    DefaultRedisScript<Object> redisScript = new DefaultRedisScript<>(script, Object.class);
    List<String> keys = List.of("elib:hash:author", "elib:zset:author","5779", ":author", ":books");
    Object res = rCacheManager.debug_getRedisTemplate().execute(redisScript, keys);
    List<String> resList = (List<String>) res;
    System.out.println(res);
  }
}
