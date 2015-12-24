package com.medicojur.web.model.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthenticationToken {

  public AuthenticationToken() {
  }

  @JsonProperty
  private String token;
}
