package com.wtf.client;

import java.io.Serializable;
import java.util.HashSet;

/*
 * This class stores line numbers and line ranges which serialize relation
 * between row_format and DOM. Objects of this class are used in RPC as
 * selection definition
 */
public class LineNumbers implements Serializable {
  private HashSet<TagLines> _elements = new HashSet<TagLines>();
  private HashSet<WordsLines> _next_level = new HashSet<WordsLines>();

  public void addElement(TagLines elem) {
    _elements.add(elem);
  }

  public void addNextLevelWords(TagLines elem, HashSet<Integer> words) {
    _next_level.add(new WordsLines(elem, words));
  }

  public void debug() {
    for (TagLines e : _elements) {
      Debug.log("(" + e.getOpenLine() + "," + e.getCloseLine() + ")");
    }
  }

  public HashSet<TagLines> getElements() {
    return _elements;
  }

  public HashSet<WordsLines> getNextLevelWords() {
    return _next_level;
  }
}
