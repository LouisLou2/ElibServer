package com.leo.elib;


import com.leo.elib.usecase.inter.BookReserveUsecase;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ReserveBookTests {

  @Resource
  private BookReserveUsecase bookReserveUsecase;

  @Test
  void testsReserveBook() {
    // test reserveBook
    var res = bookReserveUsecase.reserveBook(10003, 1, "000100039X", LocalDateTime.now());
    int x = 0;
    System.out.println(res);
  }
}
