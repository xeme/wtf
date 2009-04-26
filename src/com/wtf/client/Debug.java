package com.wtf.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Debug {
	private static boolean _debug_mode = true;

	private static HTML _status = new HTML();
	private static Queue<String> _logs = new LinkedList<String>();
	private static boolean _initialized = false; 	
	private static Duration _timer = new Duration();
	private static int _last_call = 0;

	public static void init() {	
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				Debug.log("Caught Exception: " + e.getMessage());		
			}
		});
		if(!_debug_mode)
			return;
		Element div = DOM.createDiv();
		div.setId("wtf_status_console");
		div.setClassName("wtf_ignore");
		RootPanel.getBodyElement().appendChild(div);
		RootPanel.get("wtf_status_console").add(_status);
		_initialized = true;
	}

	public static void log(String s) {
		if(!_debug_mode)
			return;
		if(!_initialized)
			init();

		_logs.add(s);
		if(_logs.size() > 10)
			_logs.poll();
		String out = "";
		Iterator<String> iter = _logs.iterator();
		while(iter.hasNext()) {
			out += iter.next() + "<br>";
		}
		_status.setHTML(out);
	}

	/*
	 * logs milliseconds elapsed from last log_time call (first call logs time elapsed from WTF load) 
	 */
	public static void log_time(String s)
	{
		if(!_debug_mode)
			return;
		if(!_initialized)
			init();

		int now = _timer.elapsedMillis();
		log(s + Integer.toString(now - _last_call)+" ms");
		_last_call = now;
	}
}
