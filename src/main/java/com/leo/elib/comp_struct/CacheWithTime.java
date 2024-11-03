package com.leo.elib.comp_struct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
// 方便jackjson序列化
@JsonIgnoreProperties(ignoreUnknown = true)
public class CacheWithTime {
  private String value;
  private Long expireAt;
}
