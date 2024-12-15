package com.leo.elib.entity;

import com.leo.elib.constant.book.ReserveBorrowStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReserveParam {
  private Integer reserveId;
  private int userId;
  private LocalDateTime reserveTime;
  private LocalDateTime deadline;
  private String isbn;
  private ReserveBorrowStatus status;
  private int bookUnqId;
  private int libId;
  private LocalDateTime pickUpTime;
  private LocalDateTime returnTime;
  private LocalDateTime dueTime;
  private Integer librarianId;
  private Integer feeId;
}
