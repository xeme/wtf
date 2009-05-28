package com.wtf.client;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.RootPanel;

public class Discussion {

	private DiscussionWidget _discussion_widget;
	private Poll _poll;
	private List<Post> _thread = new LinkedList<Post>();
	
	public Discussion() {
		_poll = new Poll();
		_thread.add(new Post("janek", "no to jest bez sensu", new Date()));
		_thread.add(new Post("zenon", "jakto bez sensu? przeciez wszystko jest OK.", new Date()));
		_thread.add(new Post("janek", "jest calkowicie bez sensu dlatego ze jest bez sensu - bez skladu, ladu i porzadku, dluzszy post bez sensu", new Date()));
		_thread.add(new Post("iwonka", "WPISUJCIE MIASTA!!", new Date()));
		_thread.add(new Post("kononowicz", "Białystok", new Date()));
		_thread.add(new Post("cygan", "Toruń", new Date()));
		_thread.add(new Post("Isabell", "Brwinów", new Date()));
		_thread.add(new Post("admin", "Wy wszyscy jesteście zbanowani!!", new Date()));
		_thread.add(new Post("iwonka", "Twoja stara jest zbanowana...", new Date()));
		_thread.add(new Post("dlugi_nick_ktory_jest_dlugi", "ale nie mam co powiedziec", new Date()));
		
		_discussion_widget = new DiscussionWidget(this);
	}
	
	public void show() {
		RootPanel.get().add(_discussion_widget);
	}
	
	public Poll getPoll() {
		return _poll;
	}
	
	public List<Post> getThread() {
		return _thread;
	}
}
