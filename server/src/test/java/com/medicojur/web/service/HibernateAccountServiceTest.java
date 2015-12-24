package com.medicojur.web.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.medicojur.web.model.service.Account;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;

public class HibernateAccountServiceTest {

  private SessionFactory sf = mock(SessionFactory.class);

  private Session session = mock(Session.class);

  private HibernateAccountService has = new HibernateAccountService(sf);

  @Before
  public void initMocks() {
    when(sf.openSession()).thenReturn(session);
  }

  @Test
  public void shouldCreateAccountEntryWhenRegisteringPublisher() {
    Account account = Account.builder()
        .userName("testUsername")
        .firstName("testFirname")
        .lastName("testLastname")
        .role(Account.Role.Publisher)
        .build();
    has.registerPublisher(account, "password");

    ArgumentCaptor<com.medicojur.web.model.hibernate.Account> accountCaptor =
        ArgumentCaptor.forClass(com.medicojur.web.model.hibernate.Account.class);
    verify(session).save(accountCaptor.capture());
    verify(session).flush();
    verify(session).close();

    assertThat(accountCaptor.getValue().getUserName(), is(equalTo("testUsername")));
    assertThat(accountCaptor.getValue().getPassword(), is(equalTo(DigestUtils.md5Hex("password"))));
  }

  @Test
  public void shouldAuthenticateIfUserNameAndPasswordValid() {
    Query query = mock(Query.class);
    when(session.createQuery(anyString())).thenReturn(query);
    when(query.list()).thenReturn(Arrays.asList(
        new com.medicojur.web.model.hibernate.Account()));

    assertThat(has.isUserNamePasswordValid("testUserName", "testPassword"), is(true));

    verify(session).createQuery(
        "from Account where userName = :userName and password = :password");
    verify(query).setParameter("userName", "testUserName");
    verify(query).setParameter("password", DigestUtils.md5Hex("testPassword"));
    verify(session).close();
  }

  @Test
  public void shouldFailAuthenticationIfUserNameAndPasswordNotValid() {
    Query query = mock(Query.class);
    when(session.createQuery(anyString())).thenReturn(query);
    when(query.list()).thenReturn(new ArrayList());

    assertThat(has.isUserNamePasswordValid("testUserName", "testPassword"), is(false));

    verify(session).createQuery(
        "from Account where userName = :userName and password = :password");
    verify(query).setParameter("userName", "testUserName");
    verify(query).setParameter("password", DigestUtils.md5Hex("testPassword"));
    verify(session).close();
  }
}
