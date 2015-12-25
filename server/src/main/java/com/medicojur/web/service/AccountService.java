package com.medicojur.web.service;

import com.medicojur.web.model.service.Account;

import java.util.Optional;

public interface AccountService {

  void registerPublisher(Account account, String password);

  Optional<Integer> getUserIdWithUserNameAndPassword(String userName, String password);
}
