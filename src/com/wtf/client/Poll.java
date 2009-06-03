package com.wtf.client;

import java.util.LinkedList;
import java.util.List;

public class Poll {
  public static class Answer {
    public String _label;
    public String _id;
    public String _class_attr;
    public int _votes;

    public Answer(String label, String id, String class_attr) {
      _label = label;
      _id = id;
      _class_attr = class_attr;
      _votes = 0;
    }

    public Answer(String label, String id, String class_attr, int votes) {
      _label = label;
      _id = id;
      _class_attr = class_attr;
      _votes = votes;
    }

    public void clicked() {
      Debug.log("clicked " + _id);
    }

    public String getClassAttr() {
      return _class_attr;
    }

    public String getId() {
      return _id;
    }

    public String getLabel() {
      return _label;
    }

    public int getVotes() {
      return _votes;
    }
  }

  private List<Answer> _answers = new LinkedList<Answer>();

  public Poll(List<Answer> answers) {
    _answers = answers;
  }

  public List<Answer> getAnswers() {
    return _answers;
  }
}
