//package com.leo.elib;
//
//import com.leo.elib.entity.BookCate;
//import com.leo.elib.entity.CateBookNum;
//import com.leo.elib.mapper.BookInfoMapper;
//import com.leo.elib.service.base_service.inter.RCacheManager;
//import com.leo.elib.service.specific.inter.cache.static_type.BookCache;
//import jakarta.annotation.Resource;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.*;
//
//@SpringBootTest
//public class BookCateCoverInserter {
//  @Resource
//  private BookCache bookCache;
//
//  @Resource
//  private RCacheManager rCacheManager;
//
//  @Resource
//  private BookInfoMapper bookInfoMapper;
//
//  @Test
//  void testInsert(){
//    String colorStat = """
//10001
//4279115092
//9005
//4294374112
//9002
//4293438017
//4004
//4292884232
//5001
//4290502098
//2002
//4292138196
//6002
//4293980400
//6005
//4293330720
//3005
//4278800260
//10004
//4292895697
//5005
//4290535951
//1003
//4294411578
//2001
//4279768589
//1004
//4282348494
//8005
//4285300783
//4001
//4278414695
//1000
//4293651690
//2000
//4279768589
//3000
//4278800260
//4000
//4278414695
//5000
//4290535951
//6000
//4293330720
//7000
//4290487243
//8000
//4285300783
//9000
//4293438017
//10000
//4292895697""";
//
//    List<CateBookNum> firstLevelCate = bookInfoMapper.debug_getCateBookNum();
//    List<CateBookNum> secondLevelCate = bookInfoMapper.debug_getSubCateBookNum();
//
//    Map<Integer,Integer> cateBookNumMap = new HashMap<>();
//    for (CateBookNum cateBookNum : firstLevelCate) {
//      cateBookNumMap.put(cateBookNum.getCateId(),cateBookNum.getNum());
//    }
//    for (CateBookNum cateBookNum : secondLevelCate) {
//      cateBookNumMap.put(cateBookNum.getCateId(),cateBookNum.getNum());
//    }
//
//
//    Map<Integer,BookCate> cate = new HashMap<>();
//
//    List<String> colorStatList = Arrays.asList(colorStat.split("\n"));
//    Map<Integer,Long> colorMap = new HashMap<>();
//    for (int i = 0; i < colorStatList.size(); i += 2) {
//      colorMap.put(Integer.parseInt(colorStatList.get(i)), Long.parseLong(colorStatList.get(i + 1)));
//    }
//    Map<Integer,String> cateMap = bookCache.debug_GetAllCategoryNames();
//    for (var entry : cateMap.entrySet()) {
//      Integer bookNum = cateBookNumMap.get(entry.getKey());
//      assert bookNum != null;
//      int key = entry.getKey();
//      BookCate bookCate = new BookCate();
//      bookCate.setBookNum(bookNum);
//      bookCate.setCateId(key);
//      bookCate.setCateName(entry.getValue());
//      Long domColor = colorMap.get(key);
//      if (domColor!=null){
//        bookCate.setCoverUrl("elib/cate_cover/pics/" + key +".jpg");
//        bookCate.setDomColor(domColor);
//      }else{
//        bookCate.setCoverUrl("elib/cate_cover/pics/9005.jpg");
//        bookCate.setDomColor(4294374112L);
//      }
//      cate.put(key,bookCate);
//    }
//    for (var entry : cate.entrySet()) {
//      rCacheManager.getOpsForHash().put("elib:hash:book_cate_info",entry.getKey().toString(),entry.getValue());
//    }
//  }
//}
