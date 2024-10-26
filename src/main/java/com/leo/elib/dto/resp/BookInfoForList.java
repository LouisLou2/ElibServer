package com.leo.elib.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.dto.dao.BookDaoForList;
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
    
    public BookInfoForList(String isbn, String title, String originalTitle, int publisherId, String publisherName, Date pubDate, short wordCount, short langId, String coverMUrl, short rating, boolean hasEbook, int category1, int category2, List<Integer> authorIds, List<String> authorNames, List<Short> tagIds) {
        this.isbn = isbn;
        this.title = title;
        this.originalTitle = originalTitle;
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.pubDate = pubDate;
        this.wordCount = wordCount;
        this.langId = langId;
        this.coverMUrl = coverMUrl;
        this.rating = rating;
        this.hasEbook = hasEbook;
        this.category1 = category1;
        this.category2 = category2;
        this.authorIds = authorIds;
        this.authorNames = authorNames;
        this.tagIds = tagIds;
    }
    
    public static BookInfoForList fromBookDaoForList(BookDaoForList dao) {
        return new BookInfoForList(
          dao.getIsbn(),
          dao.getTitle(), 
          dao.getOriginalTitle(), 
          dao.getPublisherId(), 
          dao.getPublisherName(), 
          dao.getPubDate(), 
          dao.getWordCount(), 
          dao.getLangId(), 
          dao.getCoverMUrl(), 
          dao.getRating(), 
          dao.isHasEbook(), 
          dao.getCategory1(), 
          dao.getCategory2(), 
          dao.getAuthorIds(), 
          dao.getAuthorNames(), 
          dao.getTagIds()
        );
    }
}