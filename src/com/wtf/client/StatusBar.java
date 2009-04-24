package com.wtf.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class StatusBar {
	private Element _status_bar = null;
	
	public StatusBar() {		
		_status_bar = DOM.createDiv();	
		_status_bar.setId("wtf_status_bar");
		_status_bar.setClassName("wtf_ignore");
		RootPanel.getBodyElement().appendChild(_status_bar);	
	}
}
