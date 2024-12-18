package com.leo.elib.controller.user.author;

import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.usecase.inter.AuthorInfoProvider;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/author_info")
public class AuthorInfoController {

  @Resource
  private AuthorInfoProvider authorInfoProvider;

  @GetMapping("/info_one_author")
  RespWrapper<?> getAuthorInfo(int authorId, int bookNum) {
    AuthorWithBookLis authorWithBookLis = authorInfoProvider.getAuthorWithBooks(authorId, bookNum);
    if (authorWithBookLis == null) {
      return RespWrapper.error(ResCodeEnum.ResourceNotFound);
    }
    authorWithBookLis.buildUrl();
    return RespWrapper.success(authorWithBookLis);
  }

  @GetMapping("/author_books")
  RespWrapper<?> getAuthorInfo(int authorId, int num, int offset) {
    List<BookBrief> cacheRes = authorInfoProvider.getBooksByAuthor(authorId, offset, num);
    if (cacheRes == null) {
      return RespWrapper.error(ResCodeEnum.ResourceNotFound);
    }
    cacheRes.forEach(BookBrief::buildUrl);
    return RespWrapper.success(cacheRes);
  }
}
