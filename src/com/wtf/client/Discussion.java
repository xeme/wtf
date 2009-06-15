package com.wtf.client;

import java.util.LinkedList;
import java.util.List;

import com.wtf.client.dto.PostDTO;

public class Discussion {
  private Poll _poll;
  private List<PostDTO> _thread = new LinkedList<PostDTO>();
  private int _thread_size;
  private Selection _selection;
  
  private String key;

  private boolean _new = false; // new discussion exists only locally

  private boolean _fetching = false;
  private boolean _fetched = false;

  public Discussion(Selection selection, int thread_size) {
    _selection = selection;
    _thread_size = thread_size;
  }

  public void addPost(final PostDTO p) {
    _thread.add(p);
    updateThreadSize();
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Poll getPoll() {
    return _poll;
  }

  public void setPoll(Poll poll) {
    _poll = poll;
  }

  public Selection getSelection() {
    return _selection;
  }

  public void setSelection(Selection selection) {
    _selection = selection;
  }

  public List<PostDTO> getThread() {
    return _thread;
  }

  public void setThread(List<PostDTO> thread) {
    _thread = thread;
    updateThreadSize();
  }

  public int getThreadSize() {
    return _thread_size;
  }

  public boolean isNew() {
    return _new;
  }

  public void setNew(boolean b) {
    _new = b;
  }

  public boolean isFetched() {
    return _fetched;
  }
  
  public void setFetched(boolean b) {
    _fetched = b;
  }

  public boolean isFetching() {
    return _fetching;
  }

  public void setFetching(boolean b) {
    _fetching = b;
  }
  
  private void updateThreadSize() {
    _thread_size = _thread.size();
  }
}
