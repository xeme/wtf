package com.wtf.client;

import java.io.Serializable;

public class Pair<T, U> implements Serializable {
  private T first;
  private U second;

  public Pair(T f, U s) {
    this.first = f;
    this.second = s;
  }

  protected Pair() {
  }

  public T first() {
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
}
