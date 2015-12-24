package com.medicojur.web.model.hibernate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account {

  public static final String PUBLISHER = "P";

  public static final String SUBSCRIBER = "S";

  public Account() {
  }

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Integer id;

  private String role;

  @Column(name = "user_name")
  private String userName;

  private String password;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;
}