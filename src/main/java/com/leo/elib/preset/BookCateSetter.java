//package com.leo.elib.preset;
//
//import com.leo.elib.service.base_service.inter.RCacheManager;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.Resource;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.util.Pair;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//import java.util.List;
//@Service
//public class BookCateSetter {
//  @Value("${container.redis.book-cache.book-cate-hash}")
//  private String bookCateHashCont;
//  @Resource
//  private RCacheManager rCacheManager;
//  private HashOperations<String, String, Object> opsForHash;
//  private static List<Pair<Short, String>> bookCates;
//  static {
//    bookCates = Arrays.asList(
//      Pair.of((short) 1000, "健康与生活"),
//      Pair.of((short) 2000, "历史"),
//      Pair.of((short) 3000, "哲学"),
//      Pair.of((short) 4000, "技术与工程"),
//      Pair.of((short) 5000, "文学"),
//      Pair.of((short) 6000, "社会科学"),
//      Pair.of((short) 7000, "科学"),
//      Pair.of((short) 8000, "自然科学"),
//      Pair.of((short) 9000, "艺术"),
//      Pair.of((short) 10000, "计算机科学"),
//
//      Pair.of((short) 1001, "亲子育儿"),
//      Pair.of((short) 1002, "健康养生"),
//      Pair.of((short) 1003, "心理健康"),
//      Pair.of((short) 1004, "旅行与探险"),
//      Pair.of((short) 1005, "饮食营养"),
//      Pair.of((short) 2001, "世界历史"),
//      Pair.of((short) 2002, "历史传记"),
//      Pair.of((short) 2003, "历史地理"),
//      Pair.of((short) 2004, "历史文化"),
//      Pair.of((short) 2005, "国别历史"),
//      Pair.of((short) 3001, "伦理学"),
//      Pair.of((short) 3002, "形而上学"),
//      Pair.of((short) 3003, "政治哲学"),
//      Pair.of((short) 3004, "美学"),
//      Pair.of((short) 3005, "逻辑学"),
//      Pair.of((short) 4001, "化学工程"),
//      Pair.of((short) 4002, "建筑工程"),
//      Pair.of((short) 4003, "机械工程"),
//      Pair.of((short) 4004, "材料科学"),
//      Pair.of((short) 4005, "电气工程"),
//      Pair.of((short) 5001, "小说"),
//      Pair.of((short) 5002, "戏剧"),
//      Pair.of((short) 5003, "散文"),
//      Pair.of((short) 5004, "文集"),
//      Pair.of((short) 5005, "诗歌"),
//      Pair.of((short) 6001, "人类学"),
//      Pair.of((short) 6002, "心理学"),
//      Pair.of((short) 6003, "政治学"),
//      Pair.of((short) 6004, "社会学"),
//      Pair.of((short) 6005, "经济学"),
//      Pair.of((short) 7001, "化学"),
//      Pair.of((short) 7002, "地球科学"),
//      Pair.of((short) 7003, "天文学"),
//      Pair.of((short) 7004, "物理学"),
//      Pair.of((short) 7005, "生物学"),
//      Pair.of((short) 8001, "地理"),
//      Pair.of((short) 8002, "地质学"),
//      Pair.of((short) 8003, "气象学"),
//      Pair.of((short) 8004, "生态学"),
//      Pair.of((short) 8005, "生物科学"),
//      Pair.of((short) 9001, "摄影"),
//      Pair.of((short) 9002, "绘画"),
//      Pair.of((short) 9003, "舞蹈"),
//      Pair.of((short) 9004, "雕塑"),
//      Pair.of((short) 9005, "音乐"),
//      Pair.of((short) 10001, "人工智能"),
//      Pair.of((short) 10002, "数据库"),
//      Pair.of((short) 10003, "算法"),
//      Pair.of((short) 10004, "编程"),
//      Pair.of((short) 10005, "网络安全")
//    );
//  }
//  @PostConstruct
//  void init() {
//    opsForHash = rCacheManager.getOpsForHash();
//  }
//  public void setBookCatesForRedis() {
//    for (Pair<Short, String> bookCate : bookCates) {
//      opsForHash.put(bookCateHashCont, String.valueOf(bookCate.getFirst()), bookCate.getSecond());
//    }
//  }
//}
