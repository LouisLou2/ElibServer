package com.leo.elib.controller.user.search;


import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.constant.SearchInType;
import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.SearchedBook;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.entity.req.SearchReq;
import com.leo.elib.usecase.inter.AuthorInfoProvider;
import com.leo.elib.usecase.inter.search.SearchUsecase;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.leo.elib.constant.SearchInType.*;

@RestController
@RequestMapping("/api/user/search")
public class SearchController {

  @Resource
  private SearchUsecase searchUsecase;

  @PostMapping("/keyword")
  RespWrapper<?> getSearchRes(@RequestBody SearchReq searchReq) {
    SearchInType type = searchReq.type;
    String keyword = searchReq.keyword;
    int pageNum = searchReq.pageNum;
    int pageSize = searchReq.pageSize;
    List<?> result = null;
    switch (type){
      case AUTHORS:
        result = searchUsecase.searchInAuthors(keyword, pageNum, pageSize);
        break;
      case BOOKS:
        List<SearchedBook> books = searchUsecase.searchInBooks(keyword, pageNum, pageSize);
        books.forEach(SearchedBook::buildUrl);
        result = books;
        break;
      case PUBLISHERS:
        result = searchUsecase.searchInPublishers(keyword, pageNum, pageSize);
        break;
    };
    if (result == null) {
      return RespWrapper.error(ResCodeEnum.ResourceNotFound);
    }
    return RespWrapper.success(result);
  }
}
