package com.leo.elib;


import com.leo.elib.mapper.LibBorrowMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ReserveBookTests {

  @Resource
  private BookReserveUsecase bookReserveUsecase;

  @Resource
  private LibBorrowMapper libBorrowMapper;

  @Test
  void testsReserveBook() {
    // test reserveBook
    var res = bookReserveUsecase.reserveBook(10003, 1, "000100039X", LocalDateTime.now().plusDays(7));
    int x = 0;
    System.out.println(res);
  }

  @Test
  void testsReserveBook2() {
//    AC ac = new AC("000100039X", 1, (byte) 0, (byte) 1, -1);
//    Integer it = libBorrowMapper.debug_lockABook("000100039X", 1, (byte) 0, (byte) 1, -1);
//    int x = 0;
  }
}
