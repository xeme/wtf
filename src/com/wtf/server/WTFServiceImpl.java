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

import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

@SuppressWarnings("serial")
public class WTFServiceImpl extends RemoteServiceServlet implements WTFService {

  @Override
  public Boolean addPost(String discussionKey, PostDTO post) {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    DiscussionJDO d = pm.getObjectById(DiscussionJDO.class, discussionKey);
    PostJDO p = new PostJDO(post.getAuthor(), post.getContent(), post.getDate());
    d.addPost(p);

    pm.close();

    return true;
  }

  @Override
  public String createDiscussion(String url, LineNumbers lines) {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    PageJDO p;
    DiscussionJDO d;

    Transaction tx = pm.currentTransaction();

    try {
      tx.begin();

      try {
        p = pm.getObjectById(PageJDO.class, url);
      } catch (Exception e) {
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
    } catch (NucleusObjectNotFoundException e) {
      return null;
    }

    List<DiscussionDTO> discussions = new LinkedList<DiscussionDTO>();

    for (DiscussionJDO d : p.getDiscussions()) {
      discussions.add(new DiscussionDTO(d.getKey(), d.getLines(),
          d.getPosts().size()));
    }

    return discussions;
  }

  @Override
  public List<PostDTO> getPosts(String discussionKey) {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    List<PostDTO> posts = new LinkedList<PostDTO>();
    DiscussionJDO d = pm.getObjectById(DiscussionJDO.class, discussionKey);
    for (PostJDO p : d.getPosts()) {
      posts.add(new PostDTO(p.getAuthor(), p.getContent(), p.getDate()));
    }

    return posts;
  }
}