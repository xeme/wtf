package com.wtf.client;

import com.google.gwt.core.client.EntryPoint;

public class WTF implements EntryPoint {

	public void onModuleLoad() {   
		Debug.log(">> WTF console <<");
		Selector selector_manager = new Selector();
		StatusBar.init(selector_manager);
		StatusBar.setStatus("WTF ready");
	}
}

