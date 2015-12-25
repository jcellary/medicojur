package com.medicojur.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.medicojur.web.service.AccountService;
import com.medicojur.web.service.HibernateAccountService;
import com.medicojur.web.service.HibernateTokenService;
import com.medicojur.web.service.TokenService;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class MainContextListener extends GuiceServletContextListener {

  public static final String RESOURCE_PACKAGE_NAMESPACE = "com.medicojur.web.server";

  private static SessionFactory sessionFactory;

  private static SessionFactory configureSessionFactory() throws HibernateException {

    ServiceRegistry serviceRegistry =
        new StandardServiceRegistryBuilder().configure().build();

    sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();

    return sessionFactory;
  }

  @Override
  protected Injector getInjector() {
    configureSessionFactory();

    final ResourceConfig rc = new PackagesResourceConfig(RESOURCE_PACKAGE_NAMESPACE);
    rc.getProperties().put(
        "com.sun.jersey.spi.container.ContainerResponseFilters",
        "com.sun.jersey.api.container.filter.LoggingFilter;com.medicojur.web.filters.CORSFilter"
    );

    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {

        //Bind services
        bind(new TypeLiteral<AccountService>() {}).to(HibernateAccountService.class);
        bind(new TypeLiteral<TokenService>() {
        }).to(HibernateTokenService.class);

        //Bind single hibernate session factory object, it's thread safe.
        bind(new TypeLiteral<SessionFactory>() {}).toInstance(sessionFactory);

        for (Class<?> resource : rc.getClasses()) {
          bind(resource);
        }

        serve("/services/*").with(GuiceContainer.class);
      }
    });
  }
}
