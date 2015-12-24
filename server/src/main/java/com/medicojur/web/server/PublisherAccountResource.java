package com.medicojur.web.server;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

import com.medicojur.web.model.service.Account;
import com.medicojur.web.service.AccountService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("publisherAccount")
public class PublisherAccountResource {

  private AccountService accountService;

  @Inject
  public PublisherAccountResource(AccountService accountService) {
    this.accountService = accountService;
  }

  @GET
  @Produces(TEXT_HTML)
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