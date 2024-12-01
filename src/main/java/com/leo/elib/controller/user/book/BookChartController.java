package com.leo.elib.controller.user.book;

import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.usecase.inter.chart.HighRatingBookChartManager;
import com.leo.elib.usecase.inter.chart.TrendingBookManager;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/book_chart")
public class BookChartController {

  @Resource
  private HighRatingBookChartManager highRatingBookChartManager;

  @Resource
  private TrendingBookManager trendingBookManager;


  @GetMapping("/high_rating")
  public RespWrapper<?> getHighRatingBooks(int pageNum, int pageSize) {
    List<BookBrief> books = highRatingBookChartManager.getHighRatingBooks(pageNum, pageSize);
    books.forEach(BookBrief::buildUrl);
    System.out.println("high rating books with PageNum: " + pageNum + " and PageSize: " + pageSize + "actually size: " + books.size());
    return RespWrapper.success(books);
  }

  @GetMapping("/trending")
  public RespWrapper<?> getTrendingBooks(int pageNum, int pageSize) {
    List<BookBrief> books = trendingBookManager.getTrendingBooks(pageNum, pageSize);
    books.forEach(BookBrief::buildUrl);
    System.out.println("trending books with PageNum: " + pageNum + " and PageSize: " + pageSize + "actually size: " + books.size());
    return RespWrapper.success(books);
  }
}
