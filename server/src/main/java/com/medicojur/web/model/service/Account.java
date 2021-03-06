package com.medicojur.web.model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Account {

  public enum Role {
    Publisher,
    Subscriber
  }

  @JsonProperty
  private String userId;

  @JsonProperty
  private Role role;

  @JsonProperty
  private String userName;

  @JsonProperty
  private String firstName;

  @JsonProperty
  private String lastName;
}
