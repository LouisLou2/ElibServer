package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.entity.struct.TimeSpan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LibTimeSpan {
  @JsonProperty("lib_id")
  private int libId;
  @JsonProperty("lib_name")
  private String libName;
  @JsonProperty("time_spans")
  private List<TimeSpan> timeSpans;
}
