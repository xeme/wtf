package com.wtf.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.wtf.client.Poll.Answer;
import com.wtf.client.dto.DiscussionDTO;
import com.wtf.client.dto.PostDTO;
import com.wtf.client.rpc.WTFService;
import com.wtf.client.rpc.WTFServiceAsync;

public class DiscussionManager {
	private static boolean _fetched = false;
	private static boolean _fetching = false;
	private static boolean _icons_visible = false;
	private static Poll _poll = null;
	private static boolean _poll_fetching = false;
	private static HashSet<Discussion> _discussions = new HashSet<Discussion>();

	//number of clouds attached to element
	private static HashMap<Element, Integer> _clouds_attached = new HashMap<Element, Integer>();

	private static WTFServiceAsync wtfService = GWT.create(WTFService.class);
	static {
		//ServiceDefTarget sdt = (ServiceDefTarget) wtfService;
		//sdt.setServiceEntryPoint("http://wtf-review.appspot.com/wtf/magic");
		//sdt.setRpcRequestBuilder(new RpcRequestBuilderWN());
	}
	private static String pageUrl = GWT.getHostPageBaseURL();

	public static void init() {
		Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {				
				DiscussionManager.redrawIcons();
				DiscussionManager.redrawDiscussions();
			}
		});
	}

	public static HashSet<Discussion> getDiscussions() {
		return _discussions;
	}

	public static void fetchDiscussionsList(final Command callback) {
		if(_fetching || _fetched) {
			return;
		}
		_fetching = true;
		StatusBar.setStatus("Fetching discussions...");	
		Debug.log("Fetching discussions...");

		wtfService.getDiscussions(pageUrl, new AsyncCallback<List<DiscussionDTO>>() {
			@Override
			public void onFailure(Throwable caught) {
				StatusBar.setStatus("Fetching discussions fail:" + caught.getMessage());
			}

			@Override
			public void onSuccess(final List<DiscussionDTO> ds) {
				StatusBar.setStatus("Fetching " + ds.size() + " discussions win");

				Command add_discussions = new Command() {
					@Override
					public void execute() {
						for (DiscussionDTO d : ds) {
							Selection sel = DOMMagic.getSelectionFromLineNumbers(d.getLines());
							if(sel != null) {
								Discussion dis = new Discussion(sel, d.getPostsCount(), null);
								dis.setKey(d.getKey());
								_discussions.add(dis);
							}   
						}
						StatusBar.setStatus("Discussions fetched");
						_fetching = false;
						_fetched = true;
						callback.execute();	
					}
				};

				//DOMMagic must be computed before applying fetched discussions
				if (DOMMagic.isComputed()) {
					add_discussions.execute();
				} else {
					DOMMagic.requestComputingRowFormat();
					DeferredCommand.addCommand(add_discussions);
				}
			}
		});
	}

	public static void fetchDiscussionDetails(final Discussion discussion,
			final Command callback) {
		//do we want to fetch every time discussion is viewed??
		if(discussion.isFetched()) {
			callback.execute();
			return;
		}

		if (discussion.isFetching())
			return;
		discussion.setFetching(true);		

		StatusBar.setStatus("Fetching details...");
		Debug.log("Fetching details...");

		//TODO (peper): to z RPC: ankieta z wynikami i tresc dyskusji
		List<Answer> answers = new LinkedList<Answer>();

		//simulator	
		answers.add(new Answer("OK", "a1", "wtf_poll_green", 23));
		answers.add(new Answer("NIEJASNE", "a2", "wtf_poll_gray", 3));
		answers.add(new Answer("BLAD", "a3", "wtf_poll_red", 56));
		final Poll poll = new Poll(answers);		

		final List<PostDTO> thread = new LinkedList<PostDTO>();

		wtfService.getPosts(discussion.getKey(), new AsyncCallback<List<PostDTO>>() {
			@Override
			public void onFailure(Throwable caught) {
				StatusBar.setStatus("Fetching details failed: " + caught.getMessage());
			}

			@Override
			public void onSuccess(List<PostDTO> posts) {
				thread.addAll(posts);

				//after fetching do this:
				discussion.setPoll(poll);
				discussion.setThread(thread);

				StatusBar.setStatus("Details fetched");
				discussion.setFetching(false);
				discussion.setFetched(true);		
				callback.execute();
			}
		});
	}

	public static void fetchPollInfo(Command callback) {
		if(_poll_fetching || _poll != null) {
			callback.execute();
			return;
		}
		_poll_fetching = true;
		StatusBar.setStatus("Fetching poll...");
		Debug.log("Fetching poll...");

		//TODO (peper): to z RPC: zbior odpowiedzi (same dane potrzebne do wyswietlenia - bez wynikow)
		List<Answer> answers = new LinkedList<Answer>();

		//simulator	
		answers.add(new Answer("OK", "a1", "wtf_poll_green"));
		answers.add(new Answer("NIEJASNE", "a2", "wtf_poll_gray"));
		answers.add(new Answer("BLAD", "a3", "wtf_poll_red"));

		_poll = new Poll(answers);
		//after creating do this:
		StatusBar.setStatus("Poll fetched");
		_poll_fetching = false;
		callback.execute();
	}	

	public static void createDiscussion(final Discussion discussion,
			final Command callback) {
		StatusBar.setStatus("Creating discussion...");
		Debug.log("Creating discussion...");
		//create LineNumbres object
		LineNumbers line_numbers = DOMMagic.getLineNumbersFromSelection(discussion.getSelection());

		//debug
		//line_numbers.debug();

		//TODO (peper): tutaj utworzenie nowej dyskusji w backendzie i sciagniecie jej id
		//wyslac trzeba jej line_numbers i tresc dyskusji

		Request r = wtfService.createDiscussion(pageUrl, line_numbers, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Debug.log("Creating discussion fail: " + caught.getMessage()); 
				StatusBar.setStatus("Creating discussion fail: " + caught.getMessage()); 
			}

			@Override
			public void onSuccess(String key) {
				// after creating do this:
				discussion.setKey(key);
				StatusBar.setStatus("Discussion created");
				Debug.log("Discussion created");
				callback.execute();
			}
		});
	}

	public static void addPost(Discussion discussion, PostDTO post,
			final Command callback) {
		StatusBar.setStatus("Adding post...");
		Debug.log("Adding post...");

		//TODO (peper): tutaj wyslanie tresci posta dla tej konkretnej dyskusji
		//TODO (filip): dodac id do dyskusji
		wtfService.addPost(discussion.getKey(), post, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Boolean result) {
				//after adding do this:
				StatusBar.setStatus("Post added");
				callback.execute();
			}
		});

	}

	public static void showIcons() {
		StatusBar.setDiscussionMode(true);
		final Command cmd = new Command() {
			public void execute() {
				if(!StatusBar.isDiscussionMode())
					return;
				for(Discussion elem : _discussions) {
					elem.showIcon();
				}
				_icons_visible = true;	
			}
		};
		if(!_fetched) {
			//Fetch Discussions (it triggers computing Row Format)
			fetchDiscussionsList(new Command() {
				public void execute() {
					cmd.execute();
				}
			});
		} else {
			cmd.execute();
		}
	}

	public static void removeIcons() {
		if(!_fetched)
			return;
		for(Discussion elem : _discussions) {
			elem.removeIcon();
			elem.hide();
		}
		_icons_visible = false;
		StatusBar.setDiscussionMode(false);
	}

	public static void redrawIcons() {
		if(_icons_visible) {
			removeIcons();
			showIcons();
		}
	}

	public static void redrawDiscussions() {
		for(Discussion d : _discussions) {
			d.reposition();
		}
	}

	public static Poll getNewPoll() {
		return _poll;
	}

	public static void addDiscussion(Discussion d) {
		_discussions.add(d);
		d.setFetched(true);
	}

	public static void addCloud(Element elem) {
		int old_val = 0;
		if(_clouds_attached.containsKey(elem)) {
			old_val = _clouds_attached.get(elem);
			_clouds_attached.remove(elem);
		}
		_clouds_attached.put(elem, old_val + 1);
	}

	public static int getCloudsNumber(Element elem) {
		if(!_clouds_attached.containsKey(elem))
			return 0;
		return _clouds_attached.get(elem);
	}
}
