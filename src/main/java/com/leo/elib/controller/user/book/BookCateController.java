package com.leo.elib.controller.user.book;


import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.entity.BookCate;
import com.leo.elib.usecase.inter.HitCateRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/book_cate")
public class BookCateController {

  @Resource
  private HitCateRepository hitCateRepository;

  @GetMapping("/hit_sub")
  public RespWrapper<List<BookCate>> getHitSubCategory(int num) {
    System.out.println("getHitSubCategory with num: " + num);
    List<BookCate> lis = hitCateRepository.getHitSubCategory(num);
    BookCate.buildUrlForLis(lis);
    return RespWrapper.success(lis);
  }

  @GetMapping("/categories")
  public RespWrapper<?> getBookInfo(String isbn, int relatedBookNum) {
    return null;
  }
}
