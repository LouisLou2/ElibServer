package com.leo.elib.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.config.ServiceNetConfig;
import com.leo.elib.constant.book.ReserveBorrowStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RBDetail {
  @JsonProperty("reserve_id")
  private int reserveId;           // reserve_id

  @JsonProperty("user_id")
  private int userId;              // user_id

  @JsonProperty("reserve_time")
  private LocalDateTime reserveTime; // reserve_time

  private LocalDateTime deadline;    // deadline

  private String isbn;             // isbn

  private ReserveBorrowStatus status;            // status

  @JsonProperty("book_unq_id")
  private int bookUnqId;           // book_unq_id

  @JsonProperty("lib_id")
  private int libId; // lib_id

  @JsonProperty("pickup_time")
  private LocalDateTime pickUpTime; // pickup_time

  @JsonProperty("return_time")
  private LocalDateTime returnTime; // return_time

  @JsonProperty("due_time")
  private LocalDateTime dueTime;    // due_time

  @JsonProperty("librarian_id")
  private Integer librarianId;      // ibrarain_id (nullable)

  @JsonProperty("fee_id")
  private Integer feeId;           // fee_id (nullable)

  /*----------非reservation表中内容------------*/
  private String title;             // title

  private String location;

  @JsonProperty("lib_name")
  private String libName;

  @JsonProperty("lib_phone")
  private String libPhone;

  @JsonProperty("lib_email")
  private String libEmail;

  @JsonProperty("one_author")
  private String oneAuthorName;

  @JsonProperty("cover_m_url")
  private String coverMUrl;

  @JsonIgnore
  private boolean urlSet = false;

  public void buildUrl() {
    if (!urlSet) {
      coverMUrl = ServiceNetConfig.equip(coverMUrl);
      urlSet = true;
    }
  }
}
