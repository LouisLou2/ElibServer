package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.entity.dto.dao.BookDaoForList;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
public class BookInfoForList {
    private String isbn;
    private String title;
    @JsonProperty("original_title")
    private String originalTitle;
    @JsonProperty("publisher_id")
    private int publisherId;
    @JsonProperty("publisher_name")
    private String publisherName;
    @JsonProperty("pub_date")
    private Date pubDate;
    @JsonProperty("word_count")
    private short wordCount;
    @JsonProperty("lang_id")
    private short langId;
    @JsonProperty("lang_name")
    private String langName;
    @JsonProperty("cover_m_url")
    private String coverMUrl;
    private short rating;
    @JsonProperty("has_ebook")
    private boolean hasEbook;
    private int category1;
    @JsonProperty("category1_name")
    private String category1Name;
    private int category2;
    @JsonProperty("category2_name")
    private String category2Name;
    @JsonProperty("author_ids")
    private List<Integer> authorIds;
    @JsonProperty("author_names")
    private List<String> authorNames;
    @JsonProperty("tag_ids")
    private List<Short> tagIds;
    @JsonProperty("tag_names")
    private List<String> tagNames;
    
    public BookInfoForList(
      BookDaoForList dao,
       String langName, 
       String category1Name, 
       String category2Name, 
       List<String> tagNames
    ){
        this.isbn = dao.getIsbn();
        this.title = dao.getTitle();
        this.originalTitle = dao.getOriginalTitle();
        this.publisherId = dao.getPublisherId();
        this.publisherName = dao.getPublisherName();
        this.pubDate = dao.getPubDate();
        this.wordCount = dao.getWordCount();
        this.langId = dao.getLangId();
        this.langName = langName;
        this.coverMUrl = dao.getCoverMUrl();
        this.rating = dao.getRating();
        this.hasEbook = dao.isHasEbook();
        this.category1 = dao.getCategory1();
        this.category1Name = category1Name;
        this.category2 = dao.getCategory2();
        this.category2Name = category2Name;
        this.authorIds = dao.getAuthorIds();
        this.authorNames = dao.getAuthorNames();
        this.tagIds = dao.getTagIds();
        this.tagNames = tagNames;
    }
}