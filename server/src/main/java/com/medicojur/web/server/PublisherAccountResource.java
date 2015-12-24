package com.medicojur.web.server;

import com.medicojur.web.model.server.RegisterPublisher;
import com.medicojur.web.model.service.Account;
import com.medicojur.web.service.AccountService;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("publisherAccount")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PublisherAccountResource {

  private AccountService accountService;

  @Inject
  public PublisherAccountResource(AccountService accountService) {
    this.accountService = accountService;
  }

  @POST
  public void register(RegisterPublisher registerPublisher) {

    accountService.registerPublisher(
        Account.builder()
            .userId(UUID.randomUUID().toString())
            .role(Account.Role.Publisher)
            .userName(registerPublisher.getUserName())
            .firstName(registerPublisher.getFirstName())
            .lastName(registerPublisher.getLastName())
            .build(),
        registerPublisher.getPassword());
  }

  @GET
  public String getAll() {
    System.out.println("Requested to get All");

    accountService.registerPublisher(
        Account.builder()
            .userId("1")
            .role(Account.Role.Publisher)
            .userName("name")
            .build(),
        "password");

    String html = "<h2>All stuff</h2><ul>";
    html += "</ul>";
    return html;
  }
}