package com.leo.elib.controller.user.borrow;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.comp_struct.TokenInfo;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.req.ReserveBookParam;
import com.leo.elib.usecase.inter.BookReserveUsecase;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/borrow")
public class ReservationController {

  @Resource
  private BookReserveUsecase bookReserveUsecase;

  @PostMapping("/reserve")
  RespWrapper<?> reserveBook(@Valid @RequestBody ReserveBookParam body, HttpServletRequest request) {
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
}
