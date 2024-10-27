package com.leo.elib.entity.dto.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/*
* 类层级：DAO，数据库直接查出的，但是没有经过任何处理的数据
* 信息等级：较为简略的数据，只是为了列表展示
*/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDaoForList {
  private String isbn;
  private String title;
  private String originalTitle;
  private int publisherId;
  private String publisherName;
  private Date pubDate;
  private short wordCount;
  private short langId;
  private String coverMUrl;
  private short rating;
  private boolean hasEbook;
  private int category1;
  private int category2;
  private List<Integer> authorIds;
  private List<String> authorNames;
  private List<Short> tagIds;
}