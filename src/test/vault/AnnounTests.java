package com.leo.elib;


import com.leo.elib.mapper.AnnounMapper;
import com.leo.elib.service.specific.inter.cache.AnnounCache;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AnnounTests {
  @Resource
  private AnnounMapper amapper;

  @Resource
  private AnnounCache announCache;

  @Test
  public void testSelectAnnoun() {
    var it = amapper.getLatestAnnouns(1, 0);
    for (var announ : it) {
      System.out.println(announ);
    }
  }

  @Test
  public void testInsertAnnoun() {
    var it = amapper.getLatestAnnouns(5, 0);
//    for (var announ : it) {
//      announCache.insertAnnounAsLatest(announ);
//    }
//    // test get
//    var it2 = announCache.getLatestAnnoun(5, 0);
//    int x=0;
  }
}
