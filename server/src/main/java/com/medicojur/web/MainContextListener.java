package com.medicojur.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.medicojur.web.model.hibernate.Contact;
import com.medicojur.web.service.AccountService;
import com.medicojur.web.service.HibernateAccountService;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

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

    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {

        //Bind services
        bind(new TypeLiteral<AccountService>() {}).to(HibernateAccountService.class);

        //Bind single hibernate session factory object, it's thread safe.
        bind(new TypeLiteral<SessionFactory>() {}).toInstance(sessionFactory);

        for (Class<?> resource : rc.getClasses()) {
          bind(resource);
        }

        serve("/services/*").with(GuiceContainer.class);
      }
    });
  }

  public static void testHibernate() {
    Session session = null;
    Transaction tx = null;

    try {
      session = sessionFactory.openSession();
      tx = session.beginTransaction();

      // Creating Contact entity that will be save to the sqlite database
      Contact myContact = new Contact(3, "My Name", "my_email@email.com");
      Contact yourContact = new Contact(24, "Your Name", "your_email@email.com");

      // Saving to the database
      session.save(myContact);
      session.save(yourContact);

      // Committing the change in the database.
      session.flush();
      tx.commit();

      // Fetching saved data
      List<Contact> contactList = session.createQuery("from Contact").list();

      for (Contact contact : contactList) {
        System.out.println(
            "Id: " + contact.getId() + " | Name:" + contact.getName() + " | Email:" + contact
                .getEmail());
      }

    } catch (Exception ex) {
      ex.printStackTrace();

      // Rolling back the changes to make the data consistent in case of any failure
      // in between multiple database write operations.
      tx.rollback();
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }
}
