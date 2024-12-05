package com.leo.elib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.elib.config.ServiceNetConfig;
import com.leo.elib.constant.AnnounType;
import com.leo.elib.entity.dto.dao.Announcement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@NoArgsConstructor
@Getter
public class AnnounceBrief {
  @JsonProperty("announcement_id")
  private int announcementId;  // 对应 announcement_id

  @JsonProperty("sending_time")
  private LocalDateTime sendingTime;               // 对应 time

  @JsonProperty("expiry_time")
  private LocalDateTime expiryTime;         // 对应 expiry_time

  private String title;            // 对应 title

  private AnnounType category;        // 对应 category

  @JsonProperty("editor_id")
  private int editorId;        // 对应 editor_id

  private String cover;            // 对应 cover

  @JsonIgnore
  private boolean urlSet = false;

  public boolean urlBuildOrNull(){
    return cover == null || urlSet;
  }

  public boolean urlUnBuildOrNull(){
    return cover == null && !urlSet;
  }

  public void buildUrl() {
    if (cover == null) return;
    if (urlSet) return;
    this.cover = ServiceNetConfig.equip(cover);
    urlSet = true;
  }

  // all args constructor
  private AnnounceBrief(int announcementId, LocalDateTime sendingTime, LocalDateTime expiryTime, String title, AnnounType category, int editorId, String cover) {
    this.announcementId = announcementId;
    this.sendingTime = sendingTime;
    this.expiryTime = expiryTime;
    this.title = title;
    this.category = category;
    this.editorId = editorId;
    this.cover = cover;
  }

  public static AnnounceBrief fromAnnouncementUrlBuildOrNull(Announcement announcement) {
    assert announcement.urlBuildOrNull();
    AnnounceBrief brief =  new AnnounceBrief(
        announcement.getAnnouncementId(),
        announcement.getSendingTime(),
        announcement.getExpiryTime(),
        announcement.getTitle(),
        announcement.getCategory(),
        announcement.getEditorId(),
        announcement.getCover()
    );
    if (brief.cover != null) {
      brief.urlSet = true;
    }
    return brief;
  }
}