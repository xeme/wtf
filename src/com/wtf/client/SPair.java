package com.wtf.client;

import java.io.Serializable;

public class SPair <T extends Serializable, U extends Serializable>
    implements Serializable {
  private T first;
  private U second;
  
  protected SPair() { }

  public SPair(T f, U s) {
    this.first = f;
    this.second = s;
  }

  public T first()  {
    return first;
  }
  public U second() {
    return second;
  }
  
  public void setFirst(T f) {
    first = f;
  }
  public void setSecond(U s) {
    second = s;
  }
  
  public int hashCode() {
    return first.hashCode() * 13 + second.hashCode();
  }
} 
