package com.leo.elib.controller.user.book;

import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.comp_struct.TokenInfo;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.usecase.inter.BookInfoProvider;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/book_reco")
public class BookRecoController {

  @Resource
  private BookInfoProvider bookRecoUsecase;

  @GetMapping("/by_user")
  RespWrapper<?> getRecoBooks(int pageNum, int pageSize, HttpServletRequest request) {
    // User从request中获取
    // 从request中获取用户
    TokenInfo tokenInfo = (TokenInfo) request.getAttribute("tokenInfo");
    int userId = tokenInfo.getUserId();
    System.out.println("pageNum: " + pageNum + " pageSize: " + pageSize + " userId: " + userId);
    List<BookBrief> books = bookRecoUsecase.getRecoBooks(userId, pageNum * pageSize, pageSize);
    books.forEach(BookBrief::buildUrl);
    return RespWrapper.success(
      books
    );
  }
}
