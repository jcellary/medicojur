package com.medicojur.web.service;

import com.medicojur.web.model.service.Account;

public interface TokenService {

  boolean isTokenValid(String token);

  String generateToken(int userId);
}
