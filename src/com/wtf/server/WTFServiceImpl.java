package com.wtf.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import com.wtf.client.LineNumbers;
import com.wtf.client.dto.DiscussionDTO;
import com.wtf.client.dto.PostDTO;
import com.wtf.client.rpc.WTFService;
import com.wtf.server.jdo.DiscussionJDO;
import com.wtf.server.jdo.PageJDO;
import com.wtf.server.jdo.PostJDO;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.Extent;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

@SuppressWarnings("serial")
public class WTFServiceImpl extends RemoteServiceServlet implements WTFService {
  private static final Logger log = Logger.getLogger(WTFServiceImpl.class.getName());

  @Override
  public Boolean addPost(String discussionKey, PostDTO post) {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    DiscussionJDO d;
    try {
      d = pm.getObjectById(DiscussionJDO.class, discussionKey);
    } catch (JDOObjectNotFoundException e) {
      log.severe("Adding post to " + discussionKey + " failed.");
      return false;
    }

    PostJDO p = new PostJDO(post.getAuthor(), post.getContent(), new Date());
    d.addPost(p);

    pm.close();

    log.severe("Adding post to " + discussionKey + " succeeded.");

    return true;
  }

  @Override
  public String createDiscussion(String url, LineNumbers lines) {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    PageJDO p;
    DiscussionJDO d = null;

    Transaction tx = pm.currentTransaction();

    try {
      tx.begin();

      try {
        p = pm.getObjectById(PageJDO.class, url);
      } catch (JDOObjectNotFoundException e) {
        p = new PageJDO(url);
        pm.makePersistent(p);
      }

      d = new DiscussionJDO(lines);

      p.getDiscussions().add(d);

      pm.makePersistent(p);

      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
        log.severe("Creating discussion failed.");
      } else {
        log.severe("Creating discussion succeeded: " + d.getKey());
      }
    }

    return d.getKey();
  }

  @Override
  public List<DiscussionDTO> getDiscussions(String url) {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    PageJDO p;

    try {
      p = pm.getObjectById(PageJDO.class, url);
    } catch (JDOObjectNotFoundException e) {
      log.severe("No PageJDO for " + url);
      return null;
    }

    List<DiscussionDTO> discussions = new LinkedList<DiscussionDTO>();

    for (DiscussionJDO d : p.getDiscussions()) {
      discussions.add(new DiscussionDTO(d.getKey(), d.getLines(),
          d.getPosts().size()));
    }

    log.severe("Returning " + discussions.size() + " discussions.");

    return discussions;
  }

  @Override
  public List<PostDTO> getPosts(String discussionKey) {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    DiscussionJDO d;
    try {
      d = pm.getObjectById(DiscussionJDO.class, discussionKey);
    } catch (JDOObjectNotFoundException e) {
      log.severe("No discussion: " + discussionKey);
      return null;
    }

    List<PostDTO> posts = new LinkedList<PostDTO>();

    for (PostJDO p : d.getPosts()) {
      posts.add(new PostDTO(p.getAuthor(), p.getContent(), p.getDate()));
    }

    log.severe("Returning " + posts.size() + " posts.");

    return posts;
  }
}
