package com.wtf.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class Discussion {

	private DiscussionWidget _discussion_widget = null;
	private Poll _poll;
	private List<Post> _thread = new LinkedList<Post>();
	private int _thread_size;
	private Selection _selection;
	private CloudWidget _icon = new CloudWidget();
	
	private boolean _fetching = false;
	private boolean _fetched = false;
	
	public Discussion(Selection selection, int thread_size) {
		_selection = selection;
		_thread_size = thread_size;
	}
	
	public boolean isFetching() {
		return _fetching;
	}
	
	public boolean isFetched() {
		return _fetched;
	}
	
	public void setFetching(boolean b) {
		_fetching = b;
	}
	
	public void setFetched(boolean b) {
		_fetched = b;
	}
	
	public Poll getPoll() {
		return _poll;
	}
	
	public List<Post> getThread() {
		return _thread;
	}
	
	public void addPost(Post p) {
		_thread.add(p);
	}
	
	public Selection getSelection() {
		return _selection;
	}
	
	public int getThreadSize() {
		return _thread_size;
	}
	
	public void setPoll(Poll poll) {
		_poll = poll;
	}
	
	public void setThread(List<Post> thread) {
		_thread = thread;
		_thread_size = thread.size();
	}
	
	public void show() {
	//	DiscussionManager.fetchDiscussionDetails(this, callback)
	//	_discussion_widget = new DiscussionWidget(this);
	//	RootPanel.get().add(_discussion_widget);
	}
	
	public void showIcon() {
		Element elem = _selection.getTopElement();
		_icon.drawIcon(elem, _thread_size);
	}
	
	public void removeIcon() {
		_icon.removeIcon();
	}
	
}
