package com.wtf.client.dto;

import com.wtf.client.LineNumbers;

import java.io.Serializable;

public class DiscussionDTO implements Serializable {
  private String key;
  private LineNumbers lines;
  private int postsCount;

  public DiscussionDTO(String key, LineNumbers lines, int posts) {
    this.key = key;
    this.lines = lines;
    this.postsCount = posts;
  }

  protected DiscussionDTO() {
  }

  public String getKey() {
    return key;
  }

  public LineNumbers getLines() {
    return lines;
  }

  public int getPostsCount() {
    return postsCount;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setLines(LineNumbers lines) {
    this.lines = lines;
  }

  public void setPostsCount(int count) {
    this.postsCount = count;
  }
}