package com.leo.elib;

import com.leo.elib.entity.BookViewingHistory;
import com.leo.elib.usecase.inter.BookViewingHistoryManager;
import com.leo.elib.util.SearchHintTranfer;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class BookViewingHistoryTests {

  @Autowired
  private BookViewingHistoryManager bookVHManager;

  @Resource
  private ElasticsearchTemplate esTemp;

  @Autowired
  private ElasticsearchOperations esOperations;

  @Test
  void deleteIndexAndCreate() {
    IndexOperations indexOps = esTemp.indexOps(BookViewingHistory.class);
    boolean exists = indexOps.exists();
    if (exists){
      boolean delete = indexOps.delete();
      if (delete){
        System.out.println("delete index success");
      }else{
        throw new RuntimeException("delete index failed");
      }
    }
    boolean create = indexOps.createWithMapping();
    if (create) {
      System.out.println("create index success");
    }else{
      throw new RuntimeException("create index failed");
    }
  }

  @Test
  void insertViewingHistory() {
     BookViewingHistory history1 = new BookViewingHistory(10003, "000100039X", "Dart Apprentice", List.of("Kahlil Gibran","Robin Hobb"), "开发区世创科技有限公司出版社", LocalDateTime.now());
     BookViewingHistory history2 = new BookViewingHistory(10003, "000649885X", "Ship of Magic (Liveship Traders, #1)", List.of("Robin Hobb"), "诺依曼软件传媒有限公司出版社", LocalDateTime.now().minusDays(100));
     BookViewingHistory history3 = new BookViewingHistory(10002, "000649885X", "Ship of Magic (Liveship Traders, #1)", List.of("Robin Hobb"), "诺依曼软件传媒有限公司出版社", LocalDateTime.now());

      bookVHManager.addViewingHistory(history1);
      bookVHManager.addViewingHistory(history2);

      // sleep
      try {
        Thread.sleep(7000); //上面的异步方法需要时间，所以这里等待一下，实际生产环境不需要
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
  }

  @Test
  void getViewingHistory() {
    List<BookViewingHistory> viewingHistory = bookVHManager.getViewingHistory(10003, 0, 10);
    viewingHistory.forEach(System.out::println);
  }

  @Test
  void testSearch(){
    // 创建查询条件：根据userId和viewingTime查询
    Criteria criteria = new Criteria(BookViewingHistory.userIdFieldName).is(10003);
    Query query = new CriteriaQuery(criteria);
    query.addSort(Sort.by(Sort.Order.desc(BookViewingHistory.timeFieldName)));

    SearchHits<BookViewingHistory> searchHits = esOperations.search(query, BookViewingHistory.class, IndexCoordinates.of("book_viewing_history"));
    // 执行查询
    var res = SearchHintTranfer.getList(searchHits);
    res.forEach(System.out::println);
  }

  @Test
  void testSearchWithKeyword(){
    List<BookViewingHistory> viewingHistory = bookVHManager.getViewingHistory(10003, "Robin", 0, 10);
    viewingHistory.forEach(System.out::println);
  }
}
