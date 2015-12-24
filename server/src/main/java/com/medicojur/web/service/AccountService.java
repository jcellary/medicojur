package com.medicojur.web.service;

import com.medicojur.web.model.service.Account;

public interface AccountService {

  void registerPublisher(Account account, String password);

  boolean isUserNamePasswordValid(String userName, String password);
}
