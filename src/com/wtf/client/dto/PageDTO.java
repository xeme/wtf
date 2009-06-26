package com.wtf.client.dto;

import java.io.Serializable;
import java.util.List;

public class PageDTO implements Serializable {
  String content;
  List<DiscussionDTO> discussions;
  
  protected PageDTO() {
  }
  
  public PageDTO(String content, List<DiscussionDTO> discussions) {
    this.content = content;
    this.discussions = discussions;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public List<DiscussionDTO> getDiscussions() {
    return discussions;
  }

  public void setDiscussions(List<DiscussionDTO> discussions) {
    this.discussions = discussions;
  }
}
