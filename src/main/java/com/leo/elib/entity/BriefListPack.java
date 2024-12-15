package com.leo.elib.entity;

import com.leo.elib.entity.dto.dao.BookBrief;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BriefListPack {
  List<BookBrief> bookBriefs;
}
