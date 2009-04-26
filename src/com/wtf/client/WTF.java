package com.wtf.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class WTF implements EntryPoint {

	public void onModuleLoad() {   
		if(GWT.isScript()) {
			try {
				doLoad();
			}catch(Exception e){
				Debug.log("[M]Caught Exception: " + e.getMessage());
			}	
		} else {
			doLoad();
		}	
	}

	private void doLoad()
	{
		Debug.log(">> WTF console <<");		
		Selector selector_manager = new Selector();
		StatusBar.init(selector_manager);
		StatusBar.setStatus("WTF ready");
	}
}

