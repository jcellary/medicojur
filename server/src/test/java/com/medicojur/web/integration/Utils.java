package com.medicojur.web.integration;

import com.google.inject.servlet.GuiceFilter;
import com.medicojur.web.MainContextListener;
import com.medicojur.web.model.hibernate.Account;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class Utils {

  public static SessionFactory getSessionFactory() throws HibernateException {

    ServiceRegistry serviceRegistry =
        new StandardServiceRegistryBuilder().configure().build();

    return new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
  }

  public static void clearDb(Session session) {
    Query q = session.createQuery("delete from Account");
    q.executeUpdate();
    q = session.createQuery("delete from Token");
    q.executeUpdate();
  }

  public static Account createTestAccountEntity() {
    return Account.builder()
        .firstName("testFirstName")
        .lastName("testLastName")
        .userName("testUserName")
        .role(com.medicojur.web.model.hibernate.Account.PUBLISHER)
        .password(DigestUtils.md5Hex("testPassword"))
        .build();
  }

  public static void createTestPublisher(Session session) {
    session.save(createTestAccountEntity());
    session.flush();
  }

  public static Server createJettyServer(int port) throws Exception {
    MainContextListener contextListener = new MainContextListener();

    Server server = new Server(port);

    ServletContextHandler servletContextHandler = new ServletContextHandler();
    servletContextHandler.addEventListener(contextListener);
    server.setHandler(servletContextHandler);

    servletContextHandler.addFilter(GuiceFilter.class, "/*", null);
    servletContextHandler.addServlet(DefaultServlet.class, "/");
    server.start();

    return server;
  }
}
