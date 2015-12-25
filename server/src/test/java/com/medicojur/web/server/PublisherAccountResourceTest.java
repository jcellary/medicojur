package com.medicojur.web.server;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.ServletModule;
import com.medicojur.web.MainContextListener;
import com.medicojur.web.model.server.Authenticate;
import com.medicojur.web.model.server.AuthenticationToken;
import com.medicojur.web.model.server.RegisterPublisher;
import com.medicojur.web.model.service.Account;
import com.medicojur.web.service.AccountService;
import com.medicojur.web.service.TokenService;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.guice.spi.container.GuiceComponentProviderFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

public class PublisherAccountResourceTest {

  private static final URI BASE_URI = getBaseURI();

  private static final int PORT = 9998;

  private HttpServer server;

  private AccountService accountService = mock(AccountService.class);

  private TokenService tokenService = mock(TokenService.class);

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://localhost/").port(PORT).build();
  }

  @Before
  public void startServer() throws IOException {
    Injector injector = Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        bind(new TypeLiteral<AccountService>() {
        }).toInstance(accountService);
        bind(new TypeLiteral<TokenService>() {
        }).toInstance(tokenService);
      }
    });

    ResourceConfig rc = new PackagesResourceConfig(
        MainContextListener.RESOURCE_PACKAGE_NAMESPACE);
    IoCComponentProviderFactory ioc = new GuiceComponentProviderFactory(rc, injector);
    server = GrizzlyServerFactory.createHttpServer(BASE_URI + "services/", rc, ioc);
  }

  @After
  public void stopServer() {
    server.stop();
  }

  @Test
  public void shouldRegisterPublisherAccount() throws IOException {
    WebResource service = Utils.createWebResource(getBaseURI());

    RegisterPublisher registerPublisher = RegisterPublisher.builder()
        .firstName("testFirstName")
        .lastName("testLastName")
        .userName("testUserName")
        .password("testPassword")
        .build();

    ClientResponse resp = service.path("services/publisherAccount/register")
        .accept(MediaType.APPLICATION_JSON)
        .entity(registerPublisher, MediaType.APPLICATION_JSON)
        .post(ClientResponse.class);

    assertThat(resp.getStatus(), is(equalTo(Response.Status.NO_CONTENT.getStatusCode())));

    ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
    verify(accountService).registerPublisher(accountCaptor.capture(), eq("testPassword"));
    assertThat(accountCaptor.getValue().getFirstName(), is(equalTo("testFirstName")));
    assertThat(accountCaptor.getValue().getLastName(), is(equalTo("testLastName")));
    assertThat(accountCaptor.getValue().getUserName(), is(equalTo("testUserName")));
    assertThat(accountCaptor.getValue().getRole(), is(equalTo(Account.Role.Publisher)));
  }

  @Test
  public void shouldGenerateTokenWhenAuthenticationOk() throws IOException {
    WebResource service = Utils.createWebResource(getBaseURI());

    when(accountService.getUserIdWithUserNameAndPassword(anyString(), anyString())).thenReturn(
        Optional.of(1));
    when(tokenService.generateToken(eq(1))).thenReturn("123");

    Authenticate registerPublisher = Authenticate.builder()
        .userName("testUserName")
        .password("testPassword")
        .build();

    AuthenticationToken resp = service.path("services/publisherAccount/authenticate")
        .accept(MediaType.APPLICATION_JSON)
        .entity(registerPublisher, MediaType.APPLICATION_JSON)
        .post(AuthenticationToken.class);

    assertThat(resp.getToken(), is(notNullValue()));
    assertThat(resp.getToken().length(), is(greaterThan(0)));

    verify(accountService).getUserIdWithUserNameAndPassword(eq("testUserName"), eq("testPassword"));
  }
}