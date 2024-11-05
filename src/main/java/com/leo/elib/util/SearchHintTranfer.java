package com.leo.elib.util;

import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public class SearchHintTranfer {
  public static <T> List<T> getList(SearchHits<T> hints) {
    return hints.getSearchHits().stream().map(SearchHit::getContent).toList();
  }
}
