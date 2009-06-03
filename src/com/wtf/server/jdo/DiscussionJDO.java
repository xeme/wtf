package com.wtf.server.jdo;

import com.wtf.client.LineNumbers;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class DiscussionJDO {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
  private String key;

  @Persistent
  private PageJDO page;

  @Persistent(serialized = "true")
  private LineNumbers lines;

  @Persistent(mappedBy = "discussion")
  private List<PostJDO> posts = new ArrayList<PostJDO>();

  public DiscussionJDO(LineNumbers lines) {
    this.lines = lines;
  }

  public void addPost(PostJDO post) {
    this.posts.add(post);
  }

  public String getKey() {
    return key;
  }

  public LineNumbers getLines() {
    return lines;
  }

  public PageJDO getPage() {
    return page;
  }

  public List<PostJDO> getPosts() {
    return posts;
  }

  public void setLines(LineNumbers lines) {
    this.lines = lines;
  }

  public void setPage(PageJDO page) {
    this.page = page;
  }

  public void setPosts(List<PostJDO> posts) {
    this.posts = posts;
  }
}