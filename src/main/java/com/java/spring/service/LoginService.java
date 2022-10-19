package com.java.spring.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.java.spring.dto.LoginUserDto;
import com.java.spring.exception.EmailNotFoundException;
import com.java.spring.exception.EmptyEmailException;
import com.java.spring.exception.EmptyPasswordException;
import com.java.spring.model.User;
import com.java.spring.repository.LoginRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  @Autowired
  LoginRepository repository;

  public String findByEmail(LoginUserDto user) {
    if (user.getEmail() == null || user.getPassword() == null) {
      throw new NullPointerException("all values is required");
    }
    if (user.getEmail() == "") throw new EmptyEmailException();
    if (user.getPassword() == "") throw new EmptyPasswordException();
    Optional<User> findEmail = repository.findByEmailAndPassword(user.getEmail(), user.getPassword());
    if (findEmail.isEmpty()) throw new EmailNotFoundException();
    String secret = System.getenv("SECRET");
    if (secret == null) {
      secret = "BH&2&@2f3%#6qPt5B";
    }
    Algorithm algorithm = Algorithm.HMAC256(secret); 
    Map<String, Object> payloadClaims = new HashMap<>();
    User userFound = repository.findByEmail(user.getEmail());
    payloadClaims.put("id", userFound.getId());
    payloadClaims.put("displayName", userFound.getDisplayName());
    payloadClaims.put("email", userFound.getEmail());
    payloadClaims.put("image", userFound.getImage());
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
