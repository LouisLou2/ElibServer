package com.leo.elib.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HomeDataRefreshReq {
  @JsonProperty("last_readed_announ_id")
  public int lastReadedAnnounId;
  @JsonProperty("viewing_history_page_num")
  public int viewingHistoryPageSize;
  @JsonProperty("reco_book_page_num")
  public int recoBookPageSize;
  @JsonProperty("chart_page_num")
  public int chartPageSize;
}