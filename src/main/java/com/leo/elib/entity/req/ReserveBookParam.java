package com.leo.elib.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
public class ReserveBookParam {
  @JsonProperty("lib_id")
  @NotNull(message = "lib_id is required")
  public int libId;

  @NotNull(message = "isbn is required")
  public String isbn;

  @JsonProperty("pick_up_time")
  @NotNull(message = "pick_up_time is required")
  public LocalDateTime pickUpTime;
}
