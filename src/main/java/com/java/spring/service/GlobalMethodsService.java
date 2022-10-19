package com.java.spring.service;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.java.spring.exception.TokenNotFoundException;

@Service
public class GlobalMethodsService {

  public static boolean isValidEmailAddress(String email) {
    if (email == null) throw new NullPointerException("all values is required");
    boolean valid = EmailValidator.getInstance().isValid(email);
    return valid;
  }

  public boolean isValidDisplayNameLength(String displayName) {
    if (displayName.length() >= 8) return true;
    return false;
  }

  public boolean isValidPasswordLength(String password) {
    if(password.length() >= 6) return true;
    return false;
  }

  public DecodedJWT verifyToken(String token) {
    try {
      if (token.equals("")) {
        throw new TokenNotFoundException();
      }
      String secret = System.getenv("SECRET");
      if (secret == null) {
        secret = "BH&2&@2f3%#6qPt5B";
      }
      Algorithm algorithm = Algorithm.HMAC256(secret);  
      JWTVerifier verifier = JWT.require(algorithm).build();
      return verifier.verify(token);
    } catch (JWTVerificationException e) {
      throw new JWTVerificationException("Expired or invalid token");
    }
  }

  public Long returnIdToken(DecodedJWT decoded) {
    String encPayload = decoded.getPayload();
    String payload = decode(encPayload);
    String firstPartPayload = payload.substring(payload.indexOf("id") + 4);
    return Long.parseLong(
        firstPartPayload.substring(0, firstPartPayload.indexOf(","))); 
  }

  public String decode(final String base64) {
    return StringUtils.newStringUtf8(Base64.decodeBase64(base64));
  }
}
