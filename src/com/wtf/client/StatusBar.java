package com.wtf.client;

import java.util.HashSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StatusBar {
	public static WTFImageBundle wtfImageBundle = (WTFImageBundle) GWT.create(WTFImageBundle.class);
	
	private static StatusBarWidget _status_bar = null;
	private static boolean _error = false;


	public static void init() {
		_status_bar = new StatusBarWidget();
		RootPanel.get().add(_status_bar);
	}
	
	public static void setOrientation() {
		HashSet<String> possible = new HashSet<String>();
		possible.add("left");
		possible.add("right");
		String horizontal = Config.getOptionString("orientation", possible, "left");
		_status_bar.setOrientation(horizontal);
	}

	public static void setStatus(String s) {
		if(!_error) 
			_status_bar.setStatus(s);
	}
	
	public static void setError(String s) {
		_status_bar.setStatus("(!) " + s);
		_error = true;
		_status_bar.setOrientation("left");
	}

	/*
	 *  Status Bar Widget
	 */
	private static class StatusBarWidget extends Composite implements ClickHandler {

		private Label _status = new Label();
		private HorizontalPanel menu_panel = new HorizontalPanel();
		//menu buttons
		private ToggleButton _b_start_selection;
		private ToggleButton _b_show_discussions;
		private Image _b_about;

		public StatusBarWidget() {		

			HorizontalPanel status_panel = new HorizontalPanel();
			VerticalPanel v_panel = new VerticalPanel();
			// All composites must call initWidget() in their constructors.
			initWidget(v_panel);				

			//set styles
			getElement().setId("wtf_status_bar");
			if(!GWT.isScript()) {
				//prevent strange position fixed fail in hosted mode
				DOM.setStyleAttribute(getElement(), "position", "absolute");
			}
			addStyleName("wtf_ignore");
			
			menu_panel.getElement().setId("wtf_menu_panel");
			status_panel.getElement().setId("wtf_status_panel");
			v_panel.setSpacing(0);

			//compose all
			v_panel.add(menu_panel);
			v_panel.add(status_panel);
			status_panel.add(_status);

			//menu panel	
			_b_start_selection = new ToggleButton(wtfImageBundle.select().createImage(),
					new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(_error)
						return;
					_b_start_selection.setFocus(false);
					if (_b_start_selection.isDown()) {
						Selector.startSelectionMode();
					} else {
						Selector.endSelectionMode();
					}
				}
			});	
			_b_start_selection.setTitle("Start Selection Mode");

			_b_show_discussions = new ToggleButton(wtfImageBundle.discussions().createImage(),
					new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(_error)
						return;
					_b_show_discussions.setFocus(false);
					Debug.log("show discussions pressed");
				}
			});
			_b_show_discussions.setTitle("Show Discussions");

			_b_about = wtfImageBundle.wtf().createImage();
			_b_about.setTitle("About WTF");

			menu_panel.add(_b_start_selection);
			menu_panel.add(_b_show_discussions);
			menu_panel.add(_b_about);
		}

		public void setOrientation(String orientation) {
			if(orientation.equals("left")) {
				DOM.setStyleAttribute(getElement(), "left", "0px");
				menu_panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			} else {
				DOM.setStyleAttribute(getElement(), "right", "0px");
				menu_panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			}
		}
		
		public void setStatus(String s) {
			_status.setText(s);
		}

		public void onClick(ClickEvent event) {
			/*	Object sender = event.getSource();
			if (sender == )*/
		}
	}

}
