package com.wtf.client;

import java.util.Stack;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WTF implements EntryPoint {

	private com.google.gwt.user.client.Element _selected = null;
	
	
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    HTML status = new HTML();
    status.setText("tutaj status wtf..");

    RootPanel.get("wtf_status").add(status);  
    initDOM();  
    
  }

  
  public void select(com.google.gwt.user.client.Element elem){
	  if(elem == null || _selected != null || elem == RootPanel.getBodyElement() || elem.equals(_selected)) {
		  GWT.log("elemOver [return]" + elem.toString(), null);
		  return;
	  }
	  GWT.log("elemOver" + elem.toString(), null);
	  _selected = elem;
	  
	  //create 4 borders
	  int tickness_i = 2; 
	  String color = "#3c7bd9";
	  
	  String tickness = Integer.toString(tickness_i) + "px";
	  
	  Element divl = DOM.createDiv();
	  Element divr = DOM.createDiv();
	  Element divt = DOM.createDiv();
	  Element divb = DOM.createDiv();
	  divl.setId("wtf_selection_l");
	  divr.setId("wtf_selection_r");
	  divt.setId("wtf_selection_t");
	  divb.setId("wtf_selection_b");
	  RootPanel.getBodyElement().appendChild(divl);
	  RootPanel.getBodyElement().appendChild(divr);
	  RootPanel.getBodyElement().appendChild(divt);
	  RootPanel.getBodyElement().appendChild(divb);
	  
	  
	  com.google.gwt.user.client.Element divl_ = (com.google.gwt.user.client.Element) divl;
	  com.google.gwt.user.client.Element divr_ = (com.google.gwt.user.client.Element) divr;
	  com.google.gwt.user.client.Element divt_ = (com.google.gwt.user.client.Element) divt;
	  com.google.gwt.user.client.Element divb_ = (com.google.gwt.user.client.Element) divb;
	  DOM.setStyleAttribute(divl_, "backgroundColor", color);
	  DOM.setStyleAttribute(divr_, "backgroundColor", color);
	  DOM.setStyleAttribute(divt_, "backgroundColor", color);
	  DOM.setStyleAttribute(divb_, "backgroundColor", color);
	  DOM.setStyleAttribute(divl_, "position", "absolute");
	  DOM.setStyleAttribute(divr_, "position", "absolute");
	  DOM.setStyleAttribute(divt_, "position", "absolute");
	  DOM.setStyleAttribute(divb_, "position", "absolute");
	  
	  String h = Integer.toString(elem.getClientHeight())+"px";
	  String w = Integer.toString(elem.getClientWidth())+"px";  
	  String y = Integer.toString(elem.getAbsoluteTop())+"px";
	  String x = Integer.toString(elem.getAbsoluteLeft())+"px";
	  String w_x = Integer.toString(elem.getClientWidth() + elem.getAbsoluteLeft() - tickness_i)+"px";
	  String h_y = Integer.toString(elem.getClientHeight() + elem.getAbsoluteTop() - tickness_i)+"px";
	  
	  DOM.setStyleAttribute(divl_, "width", tickness);
	  DOM.setStyleAttribute(divl_, "height", h);
	  DOM.setStyleAttribute(divl_, "top", y);
	  DOM.setStyleAttribute(divl_, "left", x);
	  
	  DOM.setStyleAttribute(divr_, "width", tickness);
	  DOM.setStyleAttribute(divr_, "height", h);
	  DOM.setStyleAttribute(divr_, "top", y);
	  DOM.setStyleAttribute(divr_, "left", w_x);
	  
	  DOM.setStyleAttribute(divt_, "width", w);
	  DOM.setStyleAttribute(divt_, "height", tickness);
	  DOM.setStyleAttribute(divt_, "top", y);
	  DOM.setStyleAttribute(divt_, "left", x);
	  
	  DOM.setStyleAttribute(divb_, "width", w);
	  DOM.setStyleAttribute(divb_, "height", tickness);
	  DOM.setStyleAttribute(divb_, "top", h_y);
	  DOM.setStyleAttribute(divb_, "left", x);
	  
	  EventListener event_listener = new EventListener() {
		  public void onBrowserEvent(Event event) {
			 // com.google.gwt.user.client.Element elem = DOM.eventGetTarget(event);
			  switch (DOM.eventGetType(event)) {
		      case Event.ONMOUSEOUT:
		    	  remove_selection_rect();
		        break;
		    }
		  }
	  };
	
	  DOM.sinkEvents(divl_, Event.ONMOUSEOUT);
	  DOM.sinkEvents(divr_, Event.ONMOUSEOUT);
	  DOM.sinkEvents(divt_, Event.ONMOUSEOUT);
	  DOM.sinkEvents(divb_, Event.ONMOUSEOUT);
	  DOM.setEventListener(divl_, event_listener);
	  DOM.setEventListener(divr_, event_listener);
	  DOM.setEventListener(divt_, event_listener);
	  DOM.setEventListener(divb_, event_listener);
  }
  
  private void remove_border(String type) {
	  com.google.gwt.user.client.Element sel = DOM.getElementById("wtf_selection_" + type);
	  if(sel != null)
		  RootPanel.getBodyElement().removeChild(sel);
  }
  
  public void remove_selection_rect(){
	  remove_border("l");
	  remove_border("r");
	  remove_border("t");
	  remove_border("b");
	  _selected = null;
  }
  
  private boolean ignore(com.google.gwt.user.client.Element elem) {
	  return elem.getId().equals("wtf_selection_l") ||
	  		 elem.getId().equals("wtf_selection_r") ||
	  		 elem.getId().equals("wtf_selection_t") ||
	  		 elem.getId().equals("wtf_selection_b");
  }
  
  public void selectionClean(com.google.gwt.user.client.Element elem) {
	  if(elem.equals(_selected) || ignore(elem))
		  return;
	  remove_selection_rect();
  }
  
  
  private void addListener(com.google.gwt.user.client.Element elem){
	  DOM.sinkEvents(elem, Event.ONMOUSEOVER | Event.ONMOUSEOUT);
	  DOM.setEventListener(elem, new EventListener() {
		  public void onBrowserEvent(Event event) {
			  com.google.gwt.user.client.Element elem = DOM.eventGetTarget(event);
			  switch (DOM.eventGetType(event)) {
		      case Event.ONMOUSEOVER:
		 //   	  GWT.log("_over_ " + elem.toString(), null);
		    	  selectionClean(elem);
		    	  select(elem);
		        break;
		      case Event.ONMOUSEOUT:
		 //   	  GWT.log("_out_ " + elem.toString(), null);
		    	  selectionClean(elem);
		        break;
		    }
		  }
	  });
  }
  
  private void initDOM(){

	  Element body = RootPanel.getBodyElement();
	  Stack<Element> stack = new Stack<Element>();
	  stack.push(body);
	  Element elem = null;	  
	  while(!stack.isEmpty()) {
		  elem = stack.pop();
		  addListener((com.google.gwt.user.client.Element) elem);
  
		  Element child = elem.getFirstChildElement();
		  while(child != null) {
			  stack.push(child);
			  child = child.getNextSiblingElement();
		  }	  
	  }
  }
}
