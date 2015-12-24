package com.medicojur.web.integration;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.google.inject.servlet.GuiceFilter;
import com.medicojur.web.MainContextListener;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class PublisherAccountTest {

  private static final URI BASE_URI = getBaseURI();

  private static final int PORT = 9998;

  private Server server;

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://localhost/").port(PORT).build();
  }

  @Before
  public void startServer() throws Exception {

    MainContextListener contextListener = new MainContextListener();

    server = new Server(PORT);
    ServletContextHandler servletContextHandler = new ServletContextHandler();
    servletContextHandler.addEventListener(contextListener);
    server.setHandler(servletContextHandler);
    servletContextHandler.addFilter(GuiceFilter.class, "/*", null);
    servletContextHandler.addServlet(DefaultServlet.class, "/");
    server.start();
  }

  @After
  public void stopServer() throws Exception {
    server.stop();
  }

  @Test
  public void shouldRegisterPublisherAccount() throws IOException {
    Client client = Client.create(new DefaultClientConfig());
    WebResource service = client.resource(BASE_URI);

    ClientResponse resp = service.path("services").path("publisherAccount")
        .accept(MediaType.TEXT_HTML)
        .get(ClientResponse.class);

    String text = resp.getEntity(String.class);

    assertThat(resp.getStatus(), is(equalTo(200)));
    assertEquals("<h2>All stuff</h2><ul></ul>", text);
  }
}
