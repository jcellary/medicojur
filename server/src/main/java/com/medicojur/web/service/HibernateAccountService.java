package com.medicojur.web.service;

import javax.inject.Inject;

import com.medicojur.web.model.service.Account;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateAccountService implements AccountService {

  private SessionFactory sessionFactory;

  @Inject
  public HibernateAccountService(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void registerPublisher(Account account, String password) {
    Session session = null;

    try {
      session = sessionFactory.openSession();

      com.medicojur.web.model.hibernate.Account dbAccount =
          com.medicojur.web.model.hibernate.Account.builder()
              .firstName(account.getFirstName())
              .lastName(account.getLastName())
              .userName(account.getUserName())
              .role(com.medicojur.web.model.hibernate.Account.PUBLISHER)
              .password(DigestUtils.md5Hex(password))
              .build();

      session.save(dbAccount);
      session.flush();
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  public boolean isUserNamePasswordValid(String userName, String password) {
    Session session = null;

    try {
      session = sessionFactory.openSession();

      Query query = session.createQuery(
          "from Account where userName = :userName and password = :password");
      query.setParameter("userName", userName);
      query.setParameter("password", DigestUtils.md5Hex(password));

      return query.list().size() > 0;
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }
}
