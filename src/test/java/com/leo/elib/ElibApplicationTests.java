package com.leo.elib;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leo.elib.constant.book.LibBookStatusEnum;
import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.mapper.BookMarkMapper;
import com.leo.elib.mapper.LibBorrowMapper;
import com.leo.elib.service.inter.EmailSevice;
import com.leo.elib.service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.EmailContentProvider;
import com.leo.elib.usecase.inter.AuthUsecase;
import com.leo.elib.usecase.inter.BookInfoProvider;
import com.leo.elib.usecase.inter.BookMarkUsecase;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;

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

    @Test
    void contextLoads() {
        var it = libBorrowMapper.getLibsWithStatus("000100039X", (byte) LibBookStatusEnum.AVAILABLE.getCode());
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
}
