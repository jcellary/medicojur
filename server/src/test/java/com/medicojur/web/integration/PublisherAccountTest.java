package com.medicojur.web.integration;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.eclipse.jetty.server.Server;
import org.hibernate.SessionFactory;
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

  private static final SessionFactory sessionFactory = Utils.getSessionFactory();

  private Server server;

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://localhost/").port(PORT).build();
  }

  @Before
  public void startServer() throws Exception {
    server = Utils.createJettyServer(PORT);

    Utils.clearDb(sessionFactory);
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
