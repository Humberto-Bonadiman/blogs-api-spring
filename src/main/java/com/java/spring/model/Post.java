package com.java.spring.model;

import java.time.Instant;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

  @Column
  private Instant published;

  @Column
  private Instant updated;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "users")
  private User user;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<Categories> categories = new ArrayList<Categories>();

  public Post() {
    super();
    this.categories = new ArrayList<Categories>();
  }

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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Categories> getCategories() {
    return categories;
  }

  public void addCategories(Categories categories) {
    categories.setPost(this);
    this.categories.add(categories);
  }

  public Instant getPublished() {
    return published;
  }

  public void setPublished(Instant published) {
    this.published = published;
  }

  public Instant getUpdated() {
    return updated;
  }

  public void setUpdated(Instant updated) {
    this.updated = updated;
  }

  @Override
  public int hashCode() {
    return Objects.hash(categories, content, id, title, userId, updated);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Post other = (Post) obj;
    return Objects.equals(categories, other.categories)
        && Objects.equals(content, other.content)
        && Objects.equals(id, other.id)
        && Objects.equals(title, other.title)
        && Objects.equals(userId, other.userId)
        && Objects.equals(updated, other.updated);
  }
}
