package com.wtf.server.jdo;

import com.wtf.client.dto.PostDTO;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PostJDO {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private DiscussionJDO discussion;

  @Persistent
  private String author;

  @Persistent
  private String content;

  @Persistent
  private Date date;

  public PostJDO(String author, String content, Date date) {
    this.author = author;
    this.content = content;
    this.date = date;
  }

  public String getAuthor() {
    return author;
  }

  public String getContent() {
    return content;
  }

  public Date getDate() {
    return date;
  }

  public Long getId() {
    return id;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public PostDTO toPostDTO() {
    return new PostDTO(author, content, date);
  }
}