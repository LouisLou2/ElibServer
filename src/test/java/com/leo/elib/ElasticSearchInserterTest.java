package com.leo.elib;

import com.leo.elib.entity.BookInfo;
import com.leo.elib.entity.elastic.BookDetailedInfo;
import com.leo.elib.usecase.inter.BookInfoProvider;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;

import java.util.List;

@SpringBootTest
public class ElasticSearchInserterTest {

  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;

  @Resource
  private BookInfoProvider bookInfoProvider;

  @Test
  void refreshBookDetailInfo(){
    IndexOperations indexOps = elasticsearchTemplate.indexOps(BookDetailedInfo.class);
    boolean exists = indexOps.exists();
    if (exists) {
      boolean delete = indexOps.delete();
      if (delete) {
        System.out.println("delete index success");
      }else {
        System.out.println("delete index failed");
        return;
      }
    }
    boolean create = indexOps.createWithMapping();
    if (create) {
      System.out.println("create index success");
    }else {
      System.out.println("create index failed");
      return;
    }
    // 插入数据
    int offset =0;
    int page = 100;
    while (true){
      List<BookInfo> infos = bookInfoProvider.debug_getBooksByIsbn(offset, page);
      if (infos.isEmpty()){
        break;
      }
      List<BookDetailedInfo> detailedInfos = infos.stream().map(BookDetailedInfo::new).toList();
      elasticsearchTemplate.save(detailedInfos);
      offset += infos.size();
    }
    System.out.println("inserted "+ offset + " records");
  }
}
