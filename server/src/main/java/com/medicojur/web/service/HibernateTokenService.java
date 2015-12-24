package com.medicojur.web.service;

import com.medicojur.web.model.hibernate.Token;
import com.medicojur.web.model.service.Account;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.UUID;

import javax.inject.Inject;

public class HibernateTokenService implements TokenService {

  private SessionFactory sessionFactory;

  @Inject
  public HibernateTokenService(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public boolean isTokenValid(String token) {
    Session session = null;

    try {
      session = sessionFactory.openSession();

      Token tokenEntity = session.get(Token.class, token);

      return tokenEntity != null;
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }


  public String generateToken(int userId) {
    Session session = null;

    try {
      session = sessionFactory.openSession();

      Token token = Token.builder()
          .token(UUID.randomUUID().toString())
          .userId(userId)
          .build();

      session.flush();
      session.save(token);
      return token.getToken();
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }
}
