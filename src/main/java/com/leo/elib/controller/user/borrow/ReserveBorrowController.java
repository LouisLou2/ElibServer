package com.leo.elib.controller.user.borrow;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.comp_struct.TokenInfo;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.constant.book.ReservationStatus;
import com.leo.elib.constant.book.ReserveBorrowStatus;
import com.leo.elib.entity.ReserveBorrowBrief;
import com.leo.elib.entity.req.ReserveBookParam;
import com.leo.elib.mapper.ReserveBorrowMapper;
import com.leo.elib.usecase.inter.BookReserveUsecase;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/reserve_borrow")
public class ReserveBorrowController {

  @Resource
  private BookReserveUsecase bookReserveUsecase;

  @Resource
  private ReserveBorrowMapper reserveBorrowMapper;

  @PostMapping("/reserve")
  RespWrapper<?> reserveBook(@Valid @RequestBody ReserveBookParam body, HttpServletRequest request) {
    // 注意需要重写此方法，因为数据库更改，有错误
    // 从request中获取用户
    TokenInfo tokenInfo = (TokenInfo) request.getAttribute("tokenInfo");
    int userId = tokenInfo.getUserId();
    NullablePair<ResCodeEnum, String> res = bookReserveUsecase.reserveBook(
      userId,
      body.libId,
      body.isbn,
      body.pickUpTime
    );
    assert res != null;
    return new RespWrapper<>(res.getFirst().getCode(), res.getSecond(), null);
  }

  @GetMapping("/records")
  RespWrapper<?> getRecords(int offset, int num, Byte status, HttpServletRequest request) {
    // 注意需要重写此方法，因为数据库更改，有错误
    // 从request中获取用户
    TokenInfo tokenInfo = (TokenInfo) request.getAttribute("tokenInfo");
    int userId = tokenInfo.getUserId();
    System.out.println("userId: " + userId + " offset: " + offset + " num: " + num + " status: " + status);
    List<ReserveBorrowBrief> res = reserveBorrowMapper.getBriefsByUserId(userId, status, num, offset);
    res.forEach(ReserveBorrowBrief::buildUrl);
    return RespWrapper.success(res);
  }
}
