package com.leo.elib.usecase.inter;

import java.util.List;

public interface RecoBookProvider {
  List<String> getRecoBookIsbns(int userId,int offset, int num);
}
