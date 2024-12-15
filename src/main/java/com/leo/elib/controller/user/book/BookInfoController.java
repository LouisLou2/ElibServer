package com.leo.elib.controller.user.book;

import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.constant.user.UserViewBookBehavior;
import com.leo.elib.entity.BookInfo;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.usecase.inter.BookInfoProvider;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/book_info")
public class BookInfoController {

  @Resource
  private BookInfoProvider bookInfoProvider;

  // 此方法不要求认证
  @GetMapping("/info_one_isbn")
  RespWrapper<?> getBookInfo(String isbn, int relatedBookNum) {
    BookInfo bookInfo = bookInfoProvider.getBookInfo(isbn,relatedBookNum);
    if (bookInfo == null) {
      return RespWrapper.error(ResCodeEnum.ResourceNotFound);
    }
    bookInfo.buildUrl();
    return RespWrapper.success(bookInfo);
  }

  // 此方法要求认证, 用户的信息虽说不会在这里用到，但是在Filter中要保证用户是合法的状态
  @GetMapping("/info_one_isbn_cb")
  @UserViewBookBehavior
  RespWrapper<?> getBookInfoCountBehavior(String isbn, int relatedBookNum) {
    BookInfo bookInfo = bookInfoProvider.getBookInfo(isbn,relatedBookNum);
    if (bookInfo == null) {
      return RespWrapper.error(ResCodeEnum.ResourceNotFound);
    }
    bookInfo.buildUrl();
    return RespWrapper.success(bookInfo);
  }

  @GetMapping("/sub_cate_books")
  public RespWrapper<List<BookBrief>> getBookCate(int subCateId, int offset, int num) {
    System.out.println("getBookCate with subCateId: " + subCateId + " offset: " + offset + " num: " + num);
    List<BookBrief> lis = bookInfoProvider.getBooksBySubCate(subCateId, offset, num);
    lis.forEach(BookBrief::buildUrl);
    return RespWrapper.success(lis);
  }
}
