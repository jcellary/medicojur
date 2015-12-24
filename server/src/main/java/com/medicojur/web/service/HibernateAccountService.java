package com.medicojur.web.service;

import javax.inject.Inject;

import com.medicojur.web.model.hibernate.Contact;
import com.medicojur.web.model.service.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateAccountService implements AccountService {

  private SessionFactory sessionFactory;

  @Inject
  public HibernateAccountService(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void registerPublisher(Account account, String password) {
    testHibernate();
  }

  public void testHibernate() {
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
