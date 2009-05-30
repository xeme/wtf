package com.wtf.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;

public class Discussion {

	private DiscussionWidget _discussion_widget = null;
	private Poll _poll;
	private List<Post> _thread = new LinkedList<Post>();
	private int _thread_size;
	private Selection _selection;
	private CloudWidget _icon;

	private boolean _fetching = false;
	private boolean _fetched = false;

	public Discussion(Selection selection, int thread_size) {
		_selection = selection;
		_thread_size = thread_size;

		Command on_click = new Command() {
			public void execute() {
				if(_discussion_widget == null || !_discussion_widget.isVisible()) {
					show();
				} else {
					hide();
				}
			}
		};
		Command on_mouse_over = new Command() {
			public void execute() {
				for(SelectedElement elem : _selection.getElements()) {
					elem.showSelection();
				}
			}
		};
		Command on_mouse_out = new Command() {
			public void execute() {
				for(SelectedElement elem : _selection.getElements()) {
					elem.deleteSelectionBorders();
				}
			}

		};
		_icon = new CloudWidget(on_click, on_mouse_over, on_mouse_out);
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
		updateThreadSize();
	}

	public Selection getSelection() {
		return _selection;
	}

	public void setPoll(Poll poll) {
		_poll = poll;
	}

	public void setThread(List<Post> thread) {
		_thread = thread;
		updateThreadSize();
	}

	public void show() {
		final Discussion this_ref = this;
		Command show_discussion = new Command () {
			public void execute() {
				if(_discussion_widget == null) {
					_discussion_widget = new DiscussionWidget(this_ref);
					RootPanel.get().add(_discussion_widget);
				}
				_discussion_widget.setVisible(true);
				_discussion_widget.setFormVisibility(false);
				_discussion_widget.position(_selection.getTopElement());
			}
		};
		DiscussionManager.fetchDiscussionDetails(this, show_discussion);
	}

	public void hide() {
		if(_discussion_widget != null)
			_discussion_widget.setVisible(false);
	}

	public void showIcon() {
		Element elem = _selection.getTopElement();
		_icon.drawIcon(elem, _thread_size);
	}

	public void removeIcon() {
		_icon.removeIcon();
	}

	public void reposition() {
		if(_discussion_widget != null)
			_discussion_widget.position(_selection.getTopElement());
	}

	private void updateThreadSize() {
		_thread_size = _thread.size();
		_icon.updateValue(_thread_size);
	}
}
