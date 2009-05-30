package com.wtf.client;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.wtf.client.Poll.Answer;

public class DiscussionManager {
	private static boolean _fetched = false;
	private static boolean _fetching = false;
	private static boolean _icons_visible = false;
	private static HashSet<Discussion> _discussions = new HashSet<Discussion>();

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

	public static void fetchDiscussionsList(Command callback) {
		if(_fetching)
			return;
		_fetching = true;
		StatusBar.setStatus("Fetching discussions...");	
		
		//simulator
		HashSet<SelectedElement> elements1 = new HashSet<SelectedElement>();
		HashSet<SelectedElement> elements2 = new HashSet<SelectedElement>();
		HashSet<SelectedElement> elements3 = new HashSet<SelectedElement>();
		Element body = RootPanel.getBodyElement();
		elements1.add(new SelectedElement(
				(com.google.gwt.user.client.Element) body.getElementsByTagName("img").getItem(0)
		));

		elements2.add(new SelectedElement(
				(com.google.gwt.user.client.Element) body.getElementsByTagName("p").getItem(0)
		));
		elements2.add(new SelectedElement(
				(com.google.gwt.user.client.Element) body.getElementsByTagName("p").getItem(1)
		));
		
		elements3.add(new SelectedElement(
				(com.google.gwt.user.client.Element) body.getElementsByTagName("p").getItem(2)
		));
		
		Selection sel1 = new Selection(elements1);
		Selection sel2 = new Selection(elements2);
		Selection sel3 = new Selection(elements3);

		_discussions.add(new Discussion(sel1, 23));
		_discussions.add(new Discussion(sel2, 4));
		_discussions.add(new Discussion(sel3, 42));

		//after fetching do this:
		StatusBar.setStatus("Discussions fetched");
		_fetching = false;
		_fetched = true;
		callback.execute();
	}

	public static void fetchDiscussionDetails(final Discussion discussion, Command callback) {
		//do we want to fetch every time discussion is viewed??
		if(discussion.isFetched()) {
			callback.execute();
			return;
		}
		
		if(discussion.isFetching())
			return;
		discussion.setFetching(true);		
		
		StatusBar.setStatus("Fetching discussion's details...");
		
		//simulator
		List<Answer> answers = new LinkedList<Answer>();	
		answers.add(new Answer("OK", "a1", "wtf_poll_green", 23));
		answers.add(new Answer("NIEJASNE", "a2", "wtf_poll_gray", 3));
		answers.add(new Answer("BLAD", "a3", "wtf_poll_red", 56));
		Poll poll = new Poll(answers);		

		List<Post> thread = new LinkedList<Post>();
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

		StatusBar.setStatus("Discussion's details fetched");
		discussion.setFetching(false);
		discussion.setFetched(true);		
		callback.execute();
	}

	public static void showIcons() {
		final Command cmd = new Command() {
			public void execute() {
				for(Discussion elem : _discussions) {
					elem.showIcon();
				}
				_icons_visible = true;
			}
		};
		if(!_fetched) {
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
}
