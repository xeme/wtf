package com.wtf.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public class CloudWidget extends Composite {
	private Element _target_element = null;
	private Element _icon = null;
	private Element _value = null;

	public void CludWidget() {
	}

	public void setTargetElement(Element terget_element) {
		_target_element = terget_element;
	}

	public void drawNewIcon() {
		if(_target_element == null)
			return;
		if(_icon != null) 
			removeIcon();

		Image icon = StatusBar.wtfImageBundle.new_discussion().createImage();

		int top = _target_element.getAbsoluteTop() - 20;
		int left = _target_element.getAbsoluteLeft() - icon.getWidth();
		left = Math.max(left, 0);
		top = Math.max(top, 0);

		DOM.setStyleAttribute(icon.getElement(), "position", "absolute");
		DOM.setStyleAttribute(icon.getElement(), "top", top + "px");
		DOM.setStyleAttribute(icon.getElement(), "left", left + "px");

		RootPanel.getBodyElement().appendChild(icon.getElement());
		_icon = icon.getElement();
		drawValue(left, top, "?");
	}
	
	public void drawIcon(Element elem, int thread_size) {
		if(elem == null)
			return;

		Image icon = StatusBar.wtfImageBundle.new_discussion().createImage();

		int top = elem.getAbsoluteTop() - 20;
		int left = elem.getAbsoluteLeft() - icon.getWidth();
		left = Math.max(left, 0);
		top = Math.max(top, 0);

		DOM.setStyleAttribute(icon.getElement(), "position", "absolute");
		DOM.setStyleAttribute(icon.getElement(), "top", top + "px");
		DOM.setStyleAttribute(icon.getElement(), "left", left + "px");

		RootPanel.getBodyElement().appendChild(icon.getElement());
		_icon = icon.getElement();
		drawValue(left, top, Integer.toString(thread_size));
	}

	public void removeIcon() {
		if(_icon != null) {
			_icon.getParentElement().removeChild(_icon);
			_icon = null;
		}
		if(_value != null) {
			_value.getParentElement().removeChild(_value);
			_value = null;
		}
	}

	private void drawValue(int x, int y, String value) {
		Element val = DOM.createDiv();
		val.setInnerText(value);
		val.setClassName("wtf_icon_text");
		com.google.gwt.user.client.Element val_ = val.cast();

		DOM.setStyleAttribute(val_, "position", "absolute");
		DOM.setStyleAttribute(val_, "top", y + "px");
		DOM.setStyleAttribute(val_, "left", x + "px");
		DOM.setStyleAttribute(val_, "cursor", "hand");
		DOM.setStyleAttribute(val_, "cursor", "pointer");

		RootPanel.getBodyElement().appendChild(val);
		_value = val;

		DOM.sinkEvents(val_, Event.ONCLICK);
		DOM.setEventListener(val_, new EventListener() {
			public void onBrowserEvent(Event event) {
				Debug.log("? clicked");
			}
		});
	}
}
