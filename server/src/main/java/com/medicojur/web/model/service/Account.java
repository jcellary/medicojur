package com.medicojur.web.model.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Account {

  public enum Role {
    Publisher,
    Subscriber
  }

  private String userId;

  private String userName;

  private Role role;
}
