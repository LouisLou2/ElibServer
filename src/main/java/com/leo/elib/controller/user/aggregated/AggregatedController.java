package com.leo.elib.controller.user.aggregated;

import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.comp_struct.TokenInfo;
import com.leo.elib.entity.UserHomeData;
import com.leo.elib.entity.req.HomeDataRefreshReq;
import com.leo.elib.usecase.inter.AggregationUserDataProvider;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/aggregated")
public class AggregatedController {

  @Resource
  private AggregationUserDataProvider prov;

  @PostMapping("/home_data")
  RespWrapper<?> getHomeData(@RequestBody HomeDataRefreshReq params, HttpServletRequest request) {
    // User从request中获取
    // 从request中获取用户
    TokenInfo tokenInfo = (TokenInfo) request.getAttribute("tokenInfo");
    int userId = tokenInfo.getUserId();
    UserHomeData data = prov.getHomeData(
      userId,
      params.lastReadedAnnounId,
      params.viewingHistoryPageSize,
      params.recoBookPageSize,
      params.chartPageSize
    );
    data.buildUrl();
    System.out.println("getHomeData request, userId: " + userId);
    return RespWrapper.success(data);
  }
}
