package com.wtf.server;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.wtf.client.LineNumbers;
import com.wtf.client.dto.DiscussionDTO;
import com.wtf.client.dto.PageDTO;
import com.wtf.client.dto.PostDTO;
import com.wtf.client.rpc.WTFService;
import com.wtf.server.jdo.DiscussionJDO;
import com.wtf.server.jdo.PageJDO;
import com.wtf.server.jdo.PostJDO;

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
  public PageDTO getPage(String url) {
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

    //log.severe("Returning content ["+ p.getContent().length() + "] '" + p.getContent() + "'");
    //log.severe("Returning content: " + p.getContent());
    log.severe("Returning " + discussions.size() + " discussions.");

    return new PageDTO(p.getContent(), discussions);
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

  @Override
  public String getContent(String url) {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    PageJDO p;
    try {
      p = pm.getObjectById(PageJDO.class, url);
      return p.getContent();
    } catch (JDOObjectNotFoundException e) {
      log.severe("No PageJDO for " + url);
      return null;
    }
  }

  @Override
  public Boolean updateContent(String url, String content) {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    PageJDO p;
    try {
      p = pm.getObjectById(PageJDO.class, url);
      //log.severe("Updating content of " + url + " with ["+ content.length() + "] '" + content + "'");
      log.severe("Updating content of " + url + " with ["+ content.length() + "]");
      p.setContent(content);
      pm.close();
      return true;
    } catch (JDOObjectNotFoundException e) {
      log.severe("No PageJDO for " + url);
      return false;
    }
  }

  @Override
  public Boolean updateLineNumbers(String key,
      LineNumbers lineNumbersFromSelection) {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    DiscussionJDO d;
    try {
      d = pm.getObjectById(DiscussionJDO.class, key);
      d.setLines(lineNumbersFromSelection);
      pm.close();
      return true;
    } catch (JDOObjectNotFoundException e) {
      log.severe("No discussion: " + key);
      return false;
    }
  }

  @Override
  public Integer[] computeDiff(String url, String new_content) {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    PageJDO p;
    String old_content;
    Integer[] old_to_new;
    try {
      p = pm.getObjectById(PageJDO.class, url);
      old_content = p.getContent();
      if(old_content == null || old_content.equals(new_content)) {
        return null;
      }
      log.severe("DIFF: start\n");
      String[] old_splitted = old_content.split("__wtf__");
      String[] new_splitted = new_content.split("__wtf__");

      int n = old_splitted.length;

      old_to_new = new Integer[n];
      for(int i = 0; i < n; i++)
        old_to_new[i] = -1;
      
      log.severe("DIFF: splitted and after init\n");
      
      Diff diff = new Diff(old_splitted, new_splitted);
      Diff.change script = diff.diff_2(false);
      
      log.severe("DIFF: after diff!\n");
      
      int line_old = 0, moved = 0;
      while(script != null) {
        for(int i = line_old; i < script.line0; i++)
          old_to_new[i] = i - moved;

        int delta = script.deleted - script.inserted;
        moved += delta;
        line_old = script.line0 + script.deleted;

        script = script.link;
      }
      for(int i = line_old; i < n; i++)
        old_to_new[i] = i - moved;
      
      log.severe("DIFF: done!\n");
      
      /*String tmp = "";
      log.severe("DIFF: new: " + new_splitted.length + " old: " + n);
      for(int i = 0; i < n; i++) {
        tmp += "[" + i + "] = " + old_to_new[i] + "\n";
      }
      log.severe("DIFF: \n" + tmp);*/

      return old_to_new;
    } catch (JDOObjectNotFoundException e) {
      log.severe("No PageJDO for " + url);
      return null;
    }
  }
}
