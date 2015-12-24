package com.medicojur.web.server;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.ServletModule;
import com.medicojur.web.MainContextListener;
import com.medicojur.web.model.service.Account;
import com.medicojur.web.service.AccountService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.guice.spi.container.GuiceComponentProviderFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class PublisherAccountResourceTest {

  private static final URI BASE_URI = getBaseURI();

  private static final int PORT = 9998;

  private HttpServer server;

  private AccountService accountService = mock(AccountService.class);

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
    Client client = Client.create(new DefaultClientConfig());
    WebResource service = client.resource(getBaseURI());

    ClientResponse resp = service.path("services").path("publisherAccount")
        .accept(MediaType.TEXT_HTML)
        .get(ClientResponse.class);

    String text = resp.getEntity(String.class);

    assertThat(resp.getStatus(), is(equalTo(200)));
    assertEquals(200, resp.getStatus());
    assertEquals("<h2>All stuff</h2><ul></ul>", text);

    verify(accountService).registerPublisher(any(Account.class), eq("password"));
  }
}