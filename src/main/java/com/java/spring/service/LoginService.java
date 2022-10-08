package com.java.spring.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.java.spring.model.User;
import com.java.spring.repository.LoginRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  @Autowired
  LoginRepository repository;

  public String findByEmail(String email) {
    Algorithm algorithm = Algorithm.HMAC256(System.getenv("SECRET"));
    Map<String, Object> payloadClaims = new HashMap<>();
    User user = repository.findByEmail(email);
    payloadClaims.put("id", user.getId());
    payloadClaims.put("displayName", user.getDisplayName());
    payloadClaims.put("email", user.getEmail());
    payloadClaims.put("image", user.getImage());
    String token = JWT.create()
		        .withPayload(payloadClaims)
		        .withExpiresAt(localDateNowMoreSeven())
		        .sign(algorithm);
    return token;
  }

  public Date localDateNowMoreSeven() {
    LocalDate todayMoreSeven =  LocalDate.now().plusDays(7);
    Date date = Date.from(todayMoreSeven.atStartOfDay(ZoneId.systemDefault()).toInstant());
    return date;
  }

  
  
  
  
  
  
  
  
  
  
}
