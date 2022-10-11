package com.java.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "post")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private Long userId;

  @JsonIgnore
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<Categories> categoryIds = new ArrayList<Categories>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

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

  public List<Categories> getCategoryIds() {
    return categoryIds;
  }

  public void addCategoryIds(Categories categoryIds) {
    this.categoryIds.add(categoryIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoryIds, content, id, title, userId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Post other = (Post) obj;
    return Objects.equals(categoryIds, other.categoryIds)
        && Objects.equals(content, other.content)
        && Objects.equals(id, other.id)
        && Objects.equals(title, other.title)
        && Objects.equals(userId, other.userId);
  }
}
