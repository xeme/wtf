package com.wtf.client;

import java.util.HashSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
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

	//blocks interface
	public static void blockWTF() {
		if(_status_bar._b_start_selection.isDown())
			DeferredCommand.addCommand(new Command() {
				public void execute() {
					Selector.endSelectionMode();
				}
			});
		if(_status_bar._b_show_discussions.isDown())
			DeferredCommand.addCommand(new Command() {
				public void execute() {
					DiscussionManager.removeIcons();
				}
			});
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				setButtons(false, false);
				_status_bar._b_show_discussions.setEnabled(false);
				_status_bar._b_start_selection.setEnabled(false);
			}
		});	
	}

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
		blockWTF();
		_status_bar.setOrientation("left");
	}

	public static void setButtons(boolean select, boolean discussions) {
		_status_bar._b_start_selection.setDown(select);
		_status_bar._b_show_discussions.setDown(discussions);
	}

	/*
	 *  Status Bar Widget
	 */
	private static class StatusBarWidget extends Composite implements ClickHandler {

		private Label _status = new Label();
		private HorizontalPanel menu_panel = new HorizontalPanel();
		//menu buttons
		public ToggleButton _b_start_selection;
		public ToggleButton _b_show_discussions;
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
						if(_b_show_discussions.isDown()) {
							_b_show_discussions.setDown(false);
							DiscussionManager.removeIcons();
						}
						Selector.startSelectionMode();
					} else {
						Selector.endSelectionMode();
						StatusBar.setStatus("WTF ready");
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
					if (_b_show_discussions.isDown()) {
						if(_b_start_selection.isDown()) {
							_b_start_selection.setDown(false);
							Selector.endSelectionMode();
						}
						DiscussionManager.showIcons();
					} else {
						DiscussionManager.removeIcons();
						StatusBar.setStatus("WTF ready");
					}
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
		}
	}

}
