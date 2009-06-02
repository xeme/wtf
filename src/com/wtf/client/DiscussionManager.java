package com.wtf.client;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.wtf.client.Poll.Answer;

public class DiscussionManager {
	private static boolean _fetched = false;
	private static boolean _fetching = false;
	private static boolean _icons_visible = false;
	private static Poll _poll = null;
	private static boolean _poll_fetching = false;
	private static HashSet<Discussion> _discussions = new HashSet<Discussion>();

	//number of clouds attached to element
	private static HashMap<Element, Integer> _clouds_attached = new HashMap<Element, Integer>();

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
		if(_fetching) {
			callback.execute();
			return;
		}
		_fetching = true;
		StatusBar.setStatus("Fetching discussions...");	
		Debug.log("Fetching discussions...");

		//TODO (peper): to z RPC: zbior par (obiekt LineNumbers, liczba postow w watku)
		final HashSet<Pair<LineNumbers, Integer> > discussions = new HashSet<Pair<LineNumbers, Integer> >();

		//simulator
		LineNumbers lines1 = new LineNumbers();
		LineNumbers lines2 = new LineNumbers();
		//This line numbers match elements from wtf.html
		lines1.addElement(new Pair<Integer, Integer>(77, 78));

		lines2.addElement(new Pair<Integer, Integer>(98, 148));
		lines2.addElement(new Pair<Integer, Integer>(149, 150));

		discussions.add(new Pair<LineNumbers, Integer>(lines1, 22));
		discussions.add(new Pair<LineNumbers, Integer>(lines2, 45));


		//after fetching do this:
		Command after_fetching = new Command() {
			public void execute() {
				for(Pair<LineNumbers, Integer> d : discussions) {
					Selection sel = DOMMagic.getSelectionFromLineNumbers(d.first());
					if(sel != null)
						_discussions.add(new Discussion(sel, d.second(), null));
				}

				StatusBar.setStatus("Discussions fetched");
				_fetching = false;
				_fetched = true;
				callback.execute();
			}
		};
		//DOMMagic must be computed before applying fetched discussions
		if(DOMMagic.isComputed()) {
			after_fetching.execute();
		} else {
			DOMMagic.requestComputingRowFormat();
			DeferredCommand.addCommand(after_fetching);
		}		
	}

	public static void fetchDiscussionDetails(Discussion discussion, Command callback) {
		//do we want to fetch every time discussion is viewed??
		if(discussion.isFetched()) {
			callback.execute();
			return;
		}

		if(discussion.isFetching())
			return;
		discussion.setFetching(true);		

		StatusBar.setStatus("Fetching details...");
		Debug.log("Fetching details...");

		//TODO (peper): to z RPC: ankieta z wynikami i tresc dyskusji
		List<Answer> answers = new LinkedList<Answer>();
		List<Post> thread = new LinkedList<Post>();

		//simulator	
		answers.add(new Answer("OK", "a1", "wtf_poll_green", 23));
		answers.add(new Answer("NIEJASNE", "a2", "wtf_poll_gray", 3));
		answers.add(new Answer("BLAD", "a3", "wtf_poll_red", 56));
		Poll poll = new Poll(answers);		

		thread.add(new Post("janek", "no to jest bez sensu", new Date()));
		thread.add(new Post("zenon", "jakto bez sensu? przeciez wszystko jest OK.", new Date()));
		thread.add(new Post("janek", "jest calkowicie bez sensu dlatego ze jest bez sensu - bez skladu, ladu i porzadku, dluzszy post bez sensu", new Date()));
		thread.add(new Post("iwonka", "WPISUJCIE MIASTA!!", new Date()));
		thread.add(new Post("kononowicz", "Białystok", new Date()));
		thread.add(new Post("cygan", "Toruń", new Date()));
		thread.add(new Post("Isabell", "Brwinów", new Date()));
		thread.add(new Post("admin", "Wy wszyscy jesteście zbanowani!!", new Date()));
		thread.add(new Post("iwonka", "Twoja stara jest zbanowana...", new Date()));
		thread.add(new Post("dlugi_nick_ktory_jest_dlugi", "ale nie mam co powiedziec", new Date()));

		//after fetching do this:
		discussion.setPoll(poll);
		discussion.setThread(thread);

		StatusBar.setStatus("Details fetched");
		discussion.setFetching(false);
		discussion.setFetched(true);		
		callback.execute();
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

	public static void createDiscussion(final Discussion discussion, final Command callback) {
		StatusBar.setStatus("Creating discussion...");
		Debug.log("Creating discussion...");
		//create discussion in the backend and update it's id

		//create LineNumbres object
		LineNumbers line_numbers = DOMMagic.getLineNumbersFromSelection(discussion.getSelection());

		//debug
		line_numbers.debug();

		//TODO (peper): tutaj utworzenie nowej dyskusji w backendzie i sciagniecie jej id
		//wyslac trzeba jej line_numbers i tresc dyskusji

		//TODO (filip): dodac id do dyskusji

		//after creating do this:
		StatusBar.setStatus("Discussion created");
		callback.execute();
	}

	public static void addPost(Discussion discussion, Post post, Command callback) {
		StatusBar.setStatus("Adding post...");
		Debug.log("Adding post...");
		//add post

		//TODO (peper): tutaj wyslanie tresci posta dla tej konkretnej dyskusji
		//TODO (filip): dodac id do dyskusji

		//after adding do this:
		StatusBar.setStatus("Post added");
		callback.execute();
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
