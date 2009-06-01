package com.wtf.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
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
	
	private Command _on_click = null;
	private Command _on_mouse_over = null;
	private Command _on_mouse_out = null;
	
	private int _offset = -1;

	public CloudWidget(Command on_click, Command on_mouse_over, Command on_mous_out) {
		_on_click = on_click;
		_on_mouse_over = on_mouse_over;
		_on_mouse_out = on_mous_out;
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
		icon.getElement().setClassName("wtf_icon");

		RootPanel.getBodyElement().appendChild(icon.getElement());
		icon.getElement().setClassName("wtf_ignore");
		_icon = icon.getElement();
		drawValue(left, top, "?", _target_element);
	}
	
	public void drawIcon(Element elem, int thread_size) {
		if(elem == null)
			return;

		Image icon = StatusBar.wtfImageBundle.new_discussion().createImage();

		int top = elem.getAbsoluteTop() - 20;
		int left = elem.getAbsoluteLeft() - icon.getWidth();
		left = Math.max(left, 0);
		top = Math.max(top, 0);
		
		if(_offset == -1)
			setOffset(elem, icon.getHeight());
		top += _offset;

		DOM.setStyleAttribute(icon.getElement(), "position", "absolute");
		DOM.setStyleAttribute(icon.getElement(), "top", top + "px");
		DOM.setStyleAttribute(icon.getElement(), "left", left + "px");
		icon.getElement().setClassName("wtf_icon");

		RootPanel.getBodyElement().appendChild(icon.getElement());
		icon.getElement().setClassName("wtf_ignore");
		_icon = icon.getElement();
		drawValue(left, top, Integer.toString(thread_size), elem);
	}

	public void removeIcon() {
		if(_value != null) {
			_value.getParentElement().removeChild(_value);
			_value = null;
		}
		if(_icon != null) {
			_icon.getParentElement().removeChild(_icon);
			_icon = null;
		}
	}
	
	public void updateValue(int val) {
		if(_value != null) {
			_value.setInnerText(Integer.toString(val));
		}
	}

	private void drawValue(int x, int y, String value, Element parent) {
		Element val = DOM.createDiv();
		val.setInnerText(value);
		val.setClassName("wtf_icon_text");
		com.google.gwt.user.client.Element val_ = val.cast();

		DOM.setStyleAttribute(val_, "position", "absolute");
		DOM.setStyleAttribute(val_, "top", y + "px");
		DOM.setStyleAttribute(val_, "left", x + "px");
		DOM.setStyleAttribute(val_, "cursor", "hand");
		DOM.setStyleAttribute(val_, "cursor", "pointer");

		parent.appendChild(val);
		_value = val;

		DOM.sinkEvents(val_, Event.MOUSEEVENTS | Event.ONCLICK);
		DOM.setEventListener(val_, new EventListener() {
			public void onBrowserEvent(Event event) {
				switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEOVER:
					if(_on_mouse_over == null)
						return;
					_on_mouse_over.execute();
					break;
				case Event.ONMOUSEOUT:
					if(_on_mouse_out == null)
						return;
					_on_mouse_out.execute();
					break;
				case Event.ONCLICK:
					if(_on_click == null)
						return;
					_on_click.execute();
					break;
				}
				DOM.eventPreventDefault(event);
			}
		});
	}
	
	private void setOffset(Element elem, int height) {
		if(elem == null)
			_offset = 0;
		_offset = DiscussionManager.getCloudsNumber(elem) * height;
		DiscussionManager.addCloud(elem);
	}
}
