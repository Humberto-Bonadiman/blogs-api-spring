package com.java.spring.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "categories")
public class Categories {
 
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JoinColumn(name = "post_id")
  @ManyToOne(targetEntity = Post.class, fetch = FetchType.EAGER)
  private Post post;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Categories other = (Categories) obj;
    return Objects.equals(id, other.id) && Objects.equals(name, other.name);
  }

  @Override
  public String toString() {
    return "Categories [id=" + id + ", name=" + name + ", post=" + post + "]";
  }
}
