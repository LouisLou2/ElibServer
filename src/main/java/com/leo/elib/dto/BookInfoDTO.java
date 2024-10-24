package com.leo.elib.dto;

import java.util.List;

public class BookInfoDTO {
    private String isbn;
    private String title;
    private String originalTitle;
    private String publisher;
    private String pubDate;
    private int wordCount;
    private String desc;
    private String coverLUrl;
    private String coverMUrl;
    private String coverSUrl;
    private String ebookUrl;
    private int category1;
    private int category2;
    private List<Integer> authorIds;
    private List<String> authorNames;
    private String author;
    private List<String> tags;
    private List<String> className;
    private int available;
    private int count;
}