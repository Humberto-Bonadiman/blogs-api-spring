package com.java.spring.dto;

import java.util.List;

import com.java.spring.model.Categories;

public class UpdatePostResultDto {

  private String title;

  private String content;

  private Long userId;

  private List<Categories> categories;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public List<Categories> getCategories() {
    return categories;
  }

  public void setCategories(List<Categories> list) {
    this.categories = list;
  }
}
