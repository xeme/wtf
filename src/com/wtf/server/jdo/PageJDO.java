package com.wtf.server.jdo;

import com.google.appengine.api.datastore.Text;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PageJDO {
  // @PrimaryKey
  // @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  // private Long id;

  // @Persistent
  // private SiteJDO site;

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
  private String encodedKey;

  @Persistent
  @Extension(vendorName = "datanucleus", key = "gae.pk-name", value = "true")
  private String url;
  
  @Persistent
  private Text content;

  @Persistent(mappedBy = "page")
  private List<DiscussionJDO> discussions = new ArrayList<DiscussionJDO>();

  public PageJDO(String url) {
    this.url = url;
  }

  public List<DiscussionJDO> getDiscussions() {
    return discussions;
  }

  public String getEncodedKey() {
    return encodedKey;
  }

  public String getUrl() {
    return url;
  }

  public String getContent() {
    if (content != null) {
      return content.getValue();
    } else {
      return null;
    }
  }

  public void setContent(String content) {
    this.content = new Text(content);
  }
  
  public void setDiscussions(List<DiscussionJDO> discussions) {
    this.discussions = discussions;
  }

  public void setEncodedKey(String encodedKey) {
    this.encodedKey = encodedKey;
  }
}