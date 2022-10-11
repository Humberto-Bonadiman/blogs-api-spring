package com.java.spring.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.java.spring.dto.PostDto;
import com.java.spring.exception.CategoryNotFoundException;
import com.java.spring.model.Categories;
import com.java.spring.model.Post;
import com.java.spring.repository.CategoriesRepository;
import com.java.spring.repository.PostRepository;
import java.util.Optional;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class PostServices {

  @Autowired
  PostRepository repository;

  @Autowired
  CategoriesRepository categoriesRepository;

  @Autowired
  UserService userService;

  public Post create(PostDto object, String token) {
    try {
      DecodedJWT decoded = userService.verifyToken(token);
      Long numberId = returnIdToken(decoded);
      userService.verifyToken(token);
      if (object.getTitle() == null
          || object.getContent() == null
          || object.getCategoryIds() == null) {
        throw new NullPointerException("all values is required");
      }
      Post newPost = new Post();
      newPost.setTitle(object.getTitle());
      newPost.setContent(object.getContent());
      newPost.setUserId(numberId);
      for(Long id: object.getCategoryIds()) {
        Optional<Categories> category = categoriesRepository.findById(id);
        if (category.isEmpty()) throw new CategoryNotFoundException();
        Categories categoryid = categoriesRepository.findById(id).get();
        newPost.addCategoryIds(categoryid);
      }
      return repository.save(newPost);
    } catch (JWTVerificationException exception){
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
