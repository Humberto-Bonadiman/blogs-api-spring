package com.java.spring.service;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.spring.dto.CreateUserDto;
import com.java.spring.model.User;
import com.java.spring.repository.UserRepository;

@Service
public class UserService implements ServiceInterface<CreateUserDto, User> {

  @Autowired
  UserRepository repository;

  @Override
  public User create(CreateUserDto object) {
    User user = new User();
    user.setDisplayName(object.getDisplayName());
    user.setEmail(object.getEmail());
    user.setPassword(object.getPassword());
    user.setImage(object.getImage());
    return repository.save(user);
  }

  public static boolean isValidEmailAddress(String email) {
    boolean valid = EmailValidator.getInstance().isValid(email);
    return valid;
  }
}
