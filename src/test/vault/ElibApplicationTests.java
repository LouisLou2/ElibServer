package com.leo.elib;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leo.elib.constant.DeviceTypeEnum;
import com.leo.elib.constant.book.LibBookStatus;
import com.leo.elib.entity.BookInfo;
import com.leo.elib.entity.BookViewingHistory;
import com.leo.elib.entity.dto.dao.Author;
import com.leo.elib.entity.dto.dao.Publisher;
import com.leo.elib.entity.elastic.BookDetailedInfo;
import com.leo.elib.mapper.*;
import com.leo.elib.service.base_service.inter.EmailSevice;
import com.leo.elib.service.base_service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.EmailContentProvider;
import com.leo.elib.usecase.inter.AuthUsecase;
import com.leo.elib.usecase.inter.BookInfoProvider;
import com.leo.elib.usecase.inter.BookMarkUsecase;
import com.leo.elib.usecase.inter.search.SearchUsecase;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ElibApplicationTests {
    @Resource
    private AuthUsecase authUsecase;
    @Resource
    private BookInfoMapper bookInfoMapper;
    @Resource
    private LibBorrowMapper libBorrowMapper;
    @Resource
    private BookInfoProvider bookInfoProvider;
    @Resource
    private RCacheManager rCacheManager;
    @Resource
    private EmailContentProvider emailContentProvider;
    @Resource
    private EmailSevice emailSevice;
    @Resource
    private BookMarkMapper bookMarkMapper;
    @Resource
    private BookMarkUsecase bookMarkUsecase;
    @Autowired
    private ReservationMapper reservationMapper;
    @Resource
    private ElasticsearchTemplate esTemp;
    @Resource
    private SearchUsecase searchUsecase;
    @Autowired
    private PublisherMapper publisherMapper;

  @Autowired
  private AuthorMapper authorMapper;

    @Test
    void tes(){
      List<Object> lis = new ArrayList<>();
      lis.add("123");
      lis.add(null);
      lis.add("789");

      List<String> res = (List<String>) (List<?>) lis;
      System.out.println(res);
    }

    @Test
    void contextLoads() {
        var it = libBorrowMapper.getLibsWithStatus("000100039X", LibBookStatus.Available.getCode());
        var now = System.currentTimeMillis();
        for (int i=0;i<1;++i){
            var bi1 = bookInfoProvider.getBookInfo("000100039X");
            int x=0;
        }
        var end = System.currentTimeMillis();
        now = System.currentTimeMillis();
        for (int i=0;i<1;++i){
            var authorWithBookList = bookInfoProvider.getAuthorWithBooks(5778, 10);
        }
        end = System.currentTimeMillis();
        System.out.println(end-now);
        int x=0;
    }

    @Test
    void insertEmailTemplate() throws FileNotFoundException {
        var opsForHash = rCacheManager.getOpsForHash();
        var filePath = "D:\\SourceCode\\elib\\src\\main\\resources\\template\\email_code.html";
        var fileReader = new BufferedReader(new FileReader(filePath));
        var sb = new StringBuilder();
        fileReader.lines().forEach(sb::append);
        int index = sb.indexOf("678567");
        opsForHash.put("elib:hash:content", "verify_code_email", sb.toString());
        opsForHash.put("elib:hash:content", "verify_code_email_index", index);
        opsForHash.put("elib:hash:content", "need_update_email", true);
    }

    @Test
    void testEmailTemplate() {
        var emailCodeTemForLogin = emailContentProvider.getEmailCodeTemForLogin("123456");
        // System.out.println(emailCodeTemForLogin);
        emailSevice.sendHtmlEmail("865113609@qq.com", "test", emailCodeTemForLogin);
    }

    @Test
    void testBookInfo() {
        var bookInfo = bookInfoProvider.getBookInfo("000100039X");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            var s = objectMapper.writeValueAsString(bookInfo);
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAuthorWithBooks() {
        var authorWithBooks = bookInfoProvider.getAuthorWithBooks(5778, 10);
        var it = bookInfoProvider.getBooksByAuthor(5778, 0, 6);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            var s = objectMapper.writeValueAsString(authorWithBooks);
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBookMarkMapper() {
        var it = bookMarkMapper.getBookMarks(10003, 0, 10);
        bookMarkMapper.deleteBookMark(10003, "000100039X");
        bookMarkMapper.insertBookMark(10003, "000100039X", LocalDateTime.now());
        it = bookMarkMapper.getBookMarks(10003, 0, 10);
        int x=0;
    }

    @Test
    void testBookMark() {
        var it = bookMarkUsecase.getBookMarks(10003, 0, 10);
        var it1 = bookMarkUsecase.getBookMarkInfo(10003, 10);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            var s = objectMapper.writeValueAsString(it1);
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testReserve() {
      var it = reservationMapper.getReserveRecord(1);
//        var now = System.currentTimeMillis();
//        for (int i=0;i<1;++i){
//          var it = reservationMapper.getReserved(10003, null, 0, 10);
//          int x=0;
//        }
//        var end = System.currentTimeMillis();
//        System.out.println(end-now);
        int x=0;
    }

    @Test
    void testEs() {
      IndexOperations indexOps = esTemp.indexOps(BookDetailedInfo.class);
      boolean exists = indexOps.exists();
      if (!exists) {
        boolean create = indexOps.createWithMapping();
        if (create) {
          System.out.println("create index success");
        }
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
          esTemp.save(detailedInfos);
          offset += infos.size();
      }
      System.out.println("inserted "+ offset + " records");
    }

    @Test
    void testInsertEsAuthor(){
      IndexOperations indexOps = esTemp.indexOps(Author.class);
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
      // 插入数据
      int offset =0;
      int page = 100;
      List<Author> authors;
      while (true){
          authors = authorMapper.debug_getAuthor(page,offset);
          if (authors.isEmpty()){
              break;
          }
          esTemp.save(authors);
          offset += authors.size();
          System.out.println("inserted "+ offset + " records");
      }
      System.out.println("total: inserted "+ offset + " records");
    }

  @Test
  void testSearchEs() {
    var now = System.currentTimeMillis();
    for (int i=0;i<50;++i){
      // var infos = esTemp.get("000100039X", BookDetailedInfo.class);
      var it = searchUsecase.searchInBooks("twisted", 0, 10);
//    var it1 = esTemp.get("10149", Author.class);
//    int x=0;
    }
    var end = System.currentTimeMillis();
    System.out.println(end-now);
  }

  @Test
  void testInsertPublisher(){
    IndexOperations indexOps = esTemp.indexOps(Publisher.class);
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
    // 插入数据
    int offset =0;
    int page = 100;
    while (true){
      List<Publisher> pubs = publisherMapper.debug_getPublishers(page, offset);
      if (pubs.isEmpty()){
        break;
      }
      esTemp.save(pubs);
      offset += pubs.size();
      System.out.println("inserted "+ offset + " records");
    }
    System.out.println("total: inserted "+ offset + " records");
  }

  @Test
  void createBookViewingHistoryIndex(){
    IndexOperations indexOps = esTemp.indexOps(BookViewingHistory.class);
    boolean exists = indexOps.exists();
    if (!exists) {
      boolean create = indexOps.createWithMapping();
      if (create) {
        System.out.println("create index success");
      }
    }
  }

  @Test
  void testLogin(){
    var res = authUsecase.loginEmailPwd(
      "lskleo@163.com",
        "abc123456",
        DeviceTypeEnum.Mobile
    );
  }
}
