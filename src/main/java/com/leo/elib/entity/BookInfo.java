package com.leo.elib.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BookInfo {
    private String isbn;                // 图书的 ISBN，主键
    private String title;               // 图书标题
    private String originalTitle;       // 原版标题（可选）
    private Date pubDate;               // 出版日期
    private String publisher;           // 出版社（可选）
    private int wordCount;              // 字数
    private String desc;                // 图书描述（可选）
    private String coverLUrl;           // 大封面 URL（可选）
    private String coverMUrl;           // 中封面 URL（可选）
    private String coverSUrl;           // 小封面 URL（可选）
    private String eBookUrl;            // 电子书链接（可选）
    private int categoryId;             // 分类 ID
    private int subcategoryId;          // 子分类 ID
}