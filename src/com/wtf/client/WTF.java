package com.wtf.client;

import com.google.gwt.core.client.EntryPoint;

public class WTF implements EntryPoint {

	public void onModuleLoad() {   
		Debug.log("test");
		StatusBar status_bar = new StatusBar();
		Selector selector_manager = new Selector();
		
		
		selector_manager.newSelection();  
	}
}

