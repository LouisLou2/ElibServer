package com.leo.elib.controller.user.shelf;

import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.comp_struct.TokenInfo;
import com.leo.elib.entity.SimpleUserOwnedBook;
import com.leo.elib.usecase.inter.BookInfoProvider;
import com.leo.elib.usecase.inter.BookshelfUsecase;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/shelf")
public class BookShelfController {
  @Resource
  private BookInfoProvider bookInfoProv;
  @Resource
  private BookshelfUsecase bookshelfUsecase;

  @GetMapping("/get_books")
  RespWrapper<?> getBooksFromShelf(int offset, int num, HttpServletRequest request) {
    // 从request中获取用户
    TokenInfo tokenInfo = (TokenInfo) request.getAttribute("tokenInfo");
    int userId = tokenInfo.getUserId();
    List<SimpleUserOwnedBook> lis = bookshelfUsecase.getBooksFromShelf(userId, offset, num);
    lis.forEach(SimpleUserOwnedBook::buildUrl);
    return RespWrapper.success(lis);
  }
}
