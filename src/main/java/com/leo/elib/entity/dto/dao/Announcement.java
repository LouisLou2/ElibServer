package com.leo.elib.entity.dto.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.constant.AnnounType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Announcement {
  @JsonProperty("announcement_id")
  private int announcementId;  // 对应 announcement_id
  private LocalDateTime sendingTime;               // 对应 time
  @JsonProperty("expiry_time")
  private LocalDateTime expiryTime;         // 对应 expiry_time
  private String title;            // 对应 title
  private String content;          // 对应 content
  private AnnounType category;        // 对应 category
  @JsonProperty("editor_id")
  private int editorId;        // 对应 editor_id
  private String cover;            // 对应 cover
}