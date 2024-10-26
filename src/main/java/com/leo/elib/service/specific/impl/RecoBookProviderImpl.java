package com.leo.elib.service.specific.impl;

import com.leo.elib.usecase.inter.RecoBookProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecoBookProviderImpl implements RecoBookProvider {
  @Override
  public List<String> getRecoBookIsbns(int userId, int offset, int num) {
    return List.of("000100039X","000649885X","000675399X");
  }
}
