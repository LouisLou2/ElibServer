package com.leo.elib.controller.user.book;


import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.BookCate;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.service.specific.inter.cache.static_type.BookCache;
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

  @Resource
  private BookCache bookCache;

  @GetMapping("/hit_sub")
  public RespWrapper<List<BookCate>> getHitSubCategory(int num) {
    System.out.println("getHitSubCategory with num: " + num);
    List<BookCate> lis = hitCateRepository.getHitSubCategory(num);
    BookCate.buildUrlForLis(lis);
    return RespWrapper.success(lis);
  }

  @GetMapping("/categories")
  public RespWrapper<?> getAllCates() {
    List<BookCate> lis = bookCache.getAllCates();
    BookCate.buildUrlForLis(lis);
    return RespWrapper.success(lis);
  }

  @GetMapping("/sub_cates")
  public RespWrapper<?> getSubCates(int cateId) {
    List<BookCate> lis = bookCache.getSubCates(cateId);
    if (lis == null) {
      return RespWrapper.error(ResCodeEnum.ResourceNotFound);
    }
    BookCate.buildUrlForLis(lis);
    return RespWrapper.success(lis);
  }
}
