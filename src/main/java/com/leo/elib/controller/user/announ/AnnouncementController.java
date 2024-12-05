package com.leo.elib.controller.user.announ;


import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.constant.ResCodeEnum;
import com.leo.elib.entity.AnnounceBrief;
import com.leo.elib.entity.dto.dao.Announcement;
import com.leo.elib.usecase.inter.AnnounUsecase;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/announ")
public class AnnouncementController {

  @Resource
  private AnnounUsecase announUsecase;

  @Validated
  @GetMapping("/latest")
  RespWrapper<List<AnnounceBrief>> getLatestAnnoun(
    @RequestParam @Min(1) @Max(30) int num,
    @RequestParam @Min(0) int offset
  ){
    System.out.println("getLatestAnnoun: num=" + num + ", offset=" + offset);
    List<AnnounceBrief> announs = announUsecase.getLatestAnnounBrief(num, offset);
    return RespWrapper.success(announs);
  }


  // 获取一个具体的Announcement
  @GetMapping("/detail")
  RespWrapper<?> getAnnounDetail(int id){
    System.out.println("getAnnounDetail: id=" + id);
    Announcement announ = announUsecase.getAnnounDetail(id);
    if (announ == null) {
      return RespWrapper.error(ResCodeEnum.AnnouncementNotFound);
    }
    return RespWrapper.success(announ);
  }
}