package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.entity.dto.dao.BookBrief;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserHomeData {
  @JsonProperty("has_new_announ")
  private boolean hasNewAnnoun;

  @JsonProperty("viewing_history")
  private List<BookViewingHistory> viewingHistory;

  @JsonProperty("reco_books")
  private List<BookBrief> recoBooks;

  @JsonProperty("trending_books")
  private List<BookBrief> trendingBooks;

  @JsonProperty("highly_rated_books")
  private List<BookBrief> highlyRatedBooks;

  @JsonIgnore
  boolean viewingHistoryUrlSet = false;
  @JsonIgnore
  boolean recoBooksUrlSet = false;
  @JsonIgnore
  boolean trendingBooksUrlSet = false;
  @JsonIgnore
  boolean highlyRatedBooksUrlSet = false;

  public void buildUrl() {
    if (!viewingHistoryUrlSet) {
      viewingHistory.forEach(BookViewingHistory::buildUrl);
      viewingHistoryUrlSet = true;
    }
    if (!recoBooksUrlSet) {
      recoBooks.forEach(BookBrief::buildUrl);
      recoBooksUrlSet = true;
    }
    if (!trendingBooksUrlSet) {
      trendingBooks.forEach(BookBrief::buildUrl);
      trendingBooksUrlSet = true;
    }
    if (!highlyRatedBooksUrlSet) {
      highlyRatedBooks.forEach(BookBrief::buildUrl);
      highlyRatedBooksUrlSet = true;
    }
  }
}
