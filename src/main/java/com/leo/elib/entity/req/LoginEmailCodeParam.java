package com.leo.elib.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.constant.DeviceTypeEnum;

public class LoginEmailCodeParam {
  public String email;
  public String code;
  @JsonProperty("device_type")
  public DeviceTypeEnum deviceType;
  @JsonProperty("last_readed_announ_id")
  public int lastReadedAnnounId;
  @JsonProperty("viewing_history_page_num")
  public int viewingHistoryPageSize;
  @JsonProperty("reco_book_page_num")
  public int recoBookPageSize;
  @JsonProperty("chart_page_num")
  public int chartPageSize;

  public boolean allSet() {
    return email != null &&
        code != null &&
        deviceType != null &&
        lastReadedAnnounId >= 0 &&
        viewingHistoryPageSize > 0 &&
        recoBookPageSize > 0 && chartPageSize > 0;
  }
}