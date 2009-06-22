package com.wtf.client;

import java.io.Serializable;
import java.util.HashSet;

public class WordsLines implements Serializable {
  private HashSet<Integer> _lines;
  private TagLines _parent_tag;
  
  public WordsLines(TagLines parent_tag, HashSet<Integer> lines) {
    _parent_tag = parent_tag;
    _lines = lines;
  }
  
  protected WordsLines() {
  }
  
  public HashSet<Integer> getLines() {
    return _lines;
  }
  
  public TagLines getParentTag() {
    return _parent_tag;
  }
}
