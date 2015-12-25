package com.medicojur.web.server;

import com.medicojur.web.model.server.Authenticate;
import com.medicojur.web.model.server.AuthenticationToken;
import com.medicojur.web.model.server.RegisterPublisher;
import com.medicojur.web.model.service.Account;
import com.medicojur.web.service.AccountService;
import com.medicojur.web.service.TokenService;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("publisherAccount")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PublisherAccountResource {

  private AccountService accountService;

  private TokenService tokenService;

  @Inject
  public PublisherAccountResource(AccountService accountService, TokenService tokenService) {
    this.accountService = accountService;
    this.tokenService = tokenService;
  }

  @POST
  @Path("register")
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

  @POST
  @Path("authenticate")
  public Response authenticate(Authenticate authenticate) {

    Optional<Integer> userId = accountService.getUserIdWithUserNameAndPassword(
        authenticate.getUserName(), authenticate.getPassword());

    if (userId.isPresent()) {
      String token = tokenService.generateToken(userId.get());
      AuthenticationToken authenticationToken =
          AuthenticationToken.builder().token(token).build();
      return Response.ok(authenticationToken).build();
    } else {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
  }
}