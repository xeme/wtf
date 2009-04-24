package com.wtf.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class StatusBar {
	private static StatusBarWidget _status_bar = null;

		
	public static void init() {
		String horizontal = "left";
		_status_bar = new StatusBarWidget(horizontal);
	    RootPanel.get().add(_status_bar);
	}

	/*
	 *  Status Bar Widget
	 */
	private static class StatusBarWidget extends Composite implements ClickHandler {

		private Label label = new Label();

		public StatusBarWidget(String orientation) {		
			HorizontalPanel panel = new HorizontalPanel();
			initWidget(panel);
			if(orientation.equals("left")) {
				panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
				DOM.setStyleAttribute(getElement(), "left", "0px");
			} else {
				panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				DOM.setStyleAttribute(getElement(), "right", "0px");
			}
			
			label.setText("dupa");
			
			panel.add(label);
			
			


			// All composites must call initWidget() in their constructors.
			

			getElement().setId("wtf_status_bar");
			addStyleName("wtf_ignore");
		}

		public void onClick(ClickEvent event) {
			Object sender = event.getSource();
			if (sender == label) {
				
			}
		}
	}

}
