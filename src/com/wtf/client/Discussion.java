package com.wtf.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.RootPanel;

public class Discussion {

	private DiscussionWidget _discussion_widget = null;
	private Poll _poll;
	private List<Post> _thread = new LinkedList<Post>();
	private int _thread_size;
	private Selection _selection;
	private CloudWidget _icon;
	private boolean _over_icon = false;
	private Command _on_close;

	private boolean _fetching = false;
	private boolean _fetched = false;

	private boolean _new = false; //new discussion exists only locally

	public Discussion(Selection selection, int thread_size, Command on_close) {
		_selection = selection;
		_thread_size = thread_size;
		if(on_close == null) {
			_on_close = getNormalOnClose();
		} else {
			_on_close = on_close;
		}


		Command on_click = new Command() {
			public void execute() {
				if(!isVisible()) {
					show();
				} else {
					getNormalOnClose().execute();
				}
			}
		};
		Command on_mouse_over = new Command() {
			public void execute() {
				_over_icon = true;
				showSelection();
			}
		};
		Command on_mouse_out = new Command() {
			public void execute() {
				_over_icon = false;
				if(!isVisible())
					hideSelection();
			}

		};
		_icon = new CloudWidget(on_click, on_mouse_over, on_mouse_out);
	}

	public void setNew(boolean b) {
		_new = b;
	}

	public boolean isNew() {
		return _new;
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

	public Selection getSelection() {
		return _selection;
	}

	public void setSelection(Selection selection) {
		_selection = selection;
	}

	public void setPoll(Poll poll) {
		_poll = poll;
	}

	public void setThread(List<Post> thread) {
		_thread = thread;
		updateThreadSize();
	}

	public void addPost(final Post p) {
		final Discussion this_ref = this;
		final Command add_post = new Command() {
			public void execute() {
				DiscussionManager.addPost(this_ref, p, new Command() {
					public void execute() {
						_thread.add(p);
						updateThreadSize();
					}
				});
			}
		};

		if(isNew()) {
			DiscussionManager.createDiscussion(this, new Command() {
				public void execute() {
					setNew(false);
					DiscussionManager.addDiscussion(this_ref);

					//toggle modes
					Selector.endSelectionMode();

					DiscussionManager.showIcons();
					StatusBar.setButtons(false, true);
					_discussion_widget.setOnClose(getNormalOnClose());

					add_post.execute();
					show();
					_discussion_widget.setFormVisibility(true);
					//this must be after ending selection mode
					DeferredCommand.addCommand(new Command() {
						public void execute() {
							showSelection();
						}
					});
				}
			});
		} else {
			add_post.execute();
		}
	}

	public void show() {
		final Discussion this_ref = this;
		Command show_discussion = new Command () {
			public void execute() {
				if(_discussion_widget == null) {
					_discussion_widget = new DiscussionWidget(this_ref, _on_close);
					RootPanel.get().add(_discussion_widget);
				}
				_discussion_widget.setVisible(true);
				_discussion_widget.setFormVisibility(false);
				_discussion_widget.position(_selection.getTopElement());
			}
		};
		if(isNew()) {
			show_discussion.execute();
		} else {
			DiscussionManager.fetchDiscussionDetails(this, show_discussion);
		}
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
		hideSelection();
	}

	public void reposition() {
		if(_discussion_widget != null)
			_discussion_widget.position(_selection.getTopElement());
	}

	private void updateThreadSize() {
		_thread_size = _thread.size();
		_icon.updateValue(_thread_size);
	}

	public void deleteDiscussionWidget() {
		if(_discussion_widget != null)
			RootPanel.get().remove(_discussion_widget);
		_discussion_widget = null;
		_on_close = getNormalOnClose();
	}

	private Command getNormalOnClose() {
		return new Command() {
			public void execute() {
				hide();
				if(!_over_icon)
					hideSelection();
			}
		};
	}

	public void showSelection() {
		for(SelectedElement elem : _selection.getElements()) {
			elem.showSelection();
		}
	}

	public void hideSelection() {
		for(SelectedElement elem : _selection.getElements()) {
			elem.deleteSelectionBorders();
		}
	}

	private boolean isVisible() {
		return _discussion_widget != null && _discussion_widget.isVisible();
	}
}