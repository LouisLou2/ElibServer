package com.leo.elib.controller.user.book;

import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.BookInfo;
import com.leo.elib.usecase.inter.BookInfoProvider;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/book_info")
public class BookInfoController {

  @Resource
  private BookInfoProvider bookInfoProvider;

  @GetMapping("/info")
  RespWrapper<?> getBookInfo(String isbn) {
    BookInfo bookInfo = bookInfoProvider.getBookInfo(isbn);
    if (bookInfo == null) {
      return RespWrapper.error(ResCodeEnum.ResourceNotFound);
    }
    bookInfo.buildUrl();
    return RespWrapper.success(bookInfo);
  }
}
