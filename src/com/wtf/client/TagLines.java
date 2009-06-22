package com.wtf.client;

import java.io.Serializable;

public class TagLines implements Serializable {
  private Integer _open_line, _close_line;
  
  public TagLines(Integer open_line, Integer close_line) {
    _open_line = open_line;
    _close_line = close_line;
  }
  
  protected TagLines() {
  }
  
  public Integer getOpenLine() {
    return _open_line;
  }
  
  public void setOpenLine(Integer open_line) {
    _open_line = open_line;
  }
  
  public Integer getCloseLine() {
    return _close_line;
  }
  
  public void setCloseLine(Integer close_line) {
    _close_line = close_line;
  }
}