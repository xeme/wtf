package com.wtf.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

import com.wtf.client.LineNumbers;

public class DiscussionDTO implements IsSerializable {
  private String key;
  private LineNumbers lines;
  private int postsCount;
  
  protected DiscussionDTO() {} 
  
  public DiscussionDTO(String key, LineNumbers lines, int posts) {
    this.key = key;
    this.lines = lines;
    this.postsCount = posts;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public LineNumbers getLines() {
    return lines;
  }

  public void setLines(LineNumbers lines) {
    this.lines = lines;
  }

  public int getPostsCount() {
    return postsCount;
  }

  public void setPostsCount(int count) {
    this.postsCount = count;
  }
}