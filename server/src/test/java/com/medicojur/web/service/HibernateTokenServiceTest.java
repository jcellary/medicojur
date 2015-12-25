package com.medicojur.web.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.medicojur.web.model.hibernate.Token;
import com.medicojur.web.model.service.Account;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class HibernateTokenServiceTest {

  private SessionFactory sf = mock(SessionFactory.class);

  private Session session = mock(Session.class);

  private HibernateTokenService hts = new HibernateTokenService(sf);

  @Before
  public void initMocks() {
    when(sf.openSession()).thenReturn(session);
  }

  @Test
  public void shouldCreateTokenEntry() {
    String token = hts.generateToken(1);
    assertThat(token, is(notNullValue()));
    assertThat(token.length(), is(greaterThan(0)));

    ArgumentCaptor<Token> tokenCaptor =
        ArgumentCaptor.forClass(Token.class);
    verify(session).save(tokenCaptor.capture());
    verify(session).flush();
    verify(session).close();

    assertThat(tokenCaptor.getValue().getToken(), is(equalTo(token)));
    assertThat(tokenCaptor.getValue().getUserId(), is(equalTo(1)));
  }

  @Test
  public void shouldVerifyTokenWhenValid() {
    String testToken = "123";
    when(session.get(Token.class, testToken)).thenReturn(
        Token.builder().token(testToken).userId(1).build());

    boolean isTokenValid = hts.isTokenValid(testToken);

    assertThat(isTokenValid, is(equalTo(true)));
    verify(session).get(Token.class, testToken);
    verify(session).close();
  }

  @Test
  public void shouldVerifyTokenWhenInvalid() {
    String testToken = "123";
    when(session.get(Token.class, testToken)).thenReturn(null);

    boolean isTokenValid = hts.isTokenValid(testToken);

    assertThat(isTokenValid, is(equalTo(false)));
    verify(session).get(Token.class, testToken);
    verify(session).close();
  }
}
