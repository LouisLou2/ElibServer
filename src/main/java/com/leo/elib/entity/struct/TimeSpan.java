package com.leo.elib.entity.struct;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TimeSpan {
  private Date date;
  @JsonProperty("hour_begin")
  private byte hourBegin;
  @JsonProperty("hour_end")
  private byte hourEnd;
}
