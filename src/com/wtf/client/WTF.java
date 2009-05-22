package com.wtf.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;

public class WTF implements EntryPoint {

	public void onModuleLoad() {   
		if(GWT.isScript()) {
			try {
				doLoad();
			}catch(Exception e){
				Debug.log("[M]Caught Exception: " + e.getMessage());
				e.fillInStackTrace();
				StackTraceElement stack[] = e.getStackTrace();
				Debug.log("Stack Trace:" + stack.length);
				for(StackTraceElement s : stack) {
					Debug.log(s.getMethodName());
				}
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
		StatusBar.setStatus("WTF loading...");
		Command after_config_load = new Command() {
			public void execute() {
				//things that need configuration file
				StatusBar.setOrientation();
				StatusBar.setStatus("WTF ready");
			}
		};
		Config.init("../config.xml", after_config_load);
	}
}

