package com.leo.elib.usecase.inter;

import com.leo.elib.entity.AuthedUserWithData;
import com.leo.elib.entity.UserHomeData;

public interface AggregationUserDataProvider {

  UserHomeData getHomeData(
    int userId,
    int lastReadedAnnounId,
    int viewingHistoryPageSize,
    int recoBookPageSize,
    int chartPageSize
  );
}
