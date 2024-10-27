package com.leo.elib.service.specific.impl;

import com.leo.elib.service.specific.inter.RecoBookProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecoBookProviderImpl implements RecoBookProvider {
  // TODO: 还没有实现推荐功能
  @Override
  public List<String> getRecoBookIsbns(int userId, int offset, int num) {
    return List.of("000100039X","000649885X","000675399X");
  }
}
