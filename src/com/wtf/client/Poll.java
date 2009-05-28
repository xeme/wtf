package com.wtf.client;

import java.util.LinkedList;
import java.util.List;

public class Poll {
	public class Answer {
		public String _label;
		public String _id;
		public String _class_attr;
		public int _votes;
		
		public Answer(String label, String id, String class_attr, int votes) {
			_label = label;
			_id = id;
			_class_attr = class_attr;
			_votes = votes;
		}

		public String getLabel() {
			return _label;
		}

		public String getId() {
			return _id;
		}

		public String getClassAttr() {
			return _class_attr;
		}

		public int getVotes() {
			return _votes;
		}
		
		public void clicked() {
			Debug.log("clicked " + _id);
		}
	}
	
	private List<Answer> _answers = new LinkedList<Answer>();
	
	public Poll() {
		_answers.add(new Answer("OK", "a1", "wtf_poll_green", 23));
		_answers.add(new Answer("NIEJASNE", "a2", "wtf_poll_gray", 3));
		_answers.add(new Answer("BLAD", "a3", "wtf_poll_red", 56));
	}
	
	public List<Answer> getAnswers() {
		return _answers;
	}	
}
