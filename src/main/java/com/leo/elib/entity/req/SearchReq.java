package com.leo.elib.entity.req;

import com.leo.elib.constant.SearchInType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class SearchReq {
  public String keyword;
  public SearchInType type;
  public int pageNum;
  public int pageSize;
}
