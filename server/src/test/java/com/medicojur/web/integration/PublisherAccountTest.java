package com.medicojur.web.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.medicojur.web.model.server.Authenticate;
import com.medicojur.web.model.server.AuthenticationToken;
import com.medicojur.web.model.server.RegisterPublisher;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.eclipse.jetty.server.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

public class PublisherAccountTest {

  private static final URI BASE_URI = getBaseURI();

  private static final int PORT = 9998;

  private SessionFactory sessionFactory;

  private Server server;

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://localhost/").port(PORT).build();
  }

  @Before
  public void startServer() throws Exception {
    sessionFactory = Utils.getSessionFactory();
    server = Utils.createJettyServer(PORT);

    Session session = sessionFactory.openSession();
    Transaction transaction = session.beginTransaction();

    Utils.clearDb(session);
    Utils.createTestPublisher(session);

    transaction.commit();
    session.close();
  }

  @After
  public void stopServer() throws Exception {
    server.stop();
    sessionFactory.close();
  }

  @Test
  public void shouldRegisterPublisherAccount() throws IOException {
    Client client = Client.create(new DefaultClientConfig());
    WebResource service = client.resource(BASE_URI);

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
  }

  @Test
  public void shouldGenerateTokenWhenAuthenticationOk() throws IOException {
    Client client = Client.create(new DefaultClientConfig());
    WebResource service = client.resource(BASE_URI);

    Authenticate authenticate = Authenticate.builder()
        .userName("testUserName")
        .password("testPassword")
        .build();

    AuthenticationToken resp = service.path("services/publisherAccount/authenticate")
        .accept(MediaType.APPLICATION_JSON)
        .entity(authenticate, MediaType.APPLICATION_JSON)
        .post(AuthenticationToken.class);

    assertThat(resp.getToken(), is(notNullValue()));
    assertThat(resp.getToken().length(), is(greaterThan(0)));
  }
}
