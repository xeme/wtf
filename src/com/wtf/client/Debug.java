package com.wtf.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Debug {
	static private HTML _status = new HTML();
	static private Queue<String> _logs = new LinkedList<String>();
	static private boolean _initialized = false; 
	
	public static void init() {
		RootPanel.get("wtf_status_console").add(_status);
		DOM.getElementById("wtf_status_console").setClassName("wtf_ignore");
	}
	
	public static void log(String s) {
		if(!_initialized)
			init();
		
		_logs.add(s);
		if(_logs.size() > 10)
			_logs.poll();
		String out = "";
		Iterator<String> iter = _logs.iterator();
		while(iter.hasNext()){
			out += iter.next() + "<br>";
		}
		_status.setHTML(out);
	}
}
