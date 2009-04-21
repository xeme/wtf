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

  
  public void elemOver(com.google.gwt.user.client.Element elem){
	  if(DOM.getElementById("wtf_selection") != null || elem == RootPanel.getBodyElement()
			  || elem.equals(_selected)) {
		  GWT.log("elemOver [return]" + elem.toString(), null);
		  return;
	  }
	  GWT.log("elemOver" + elem.toString(), null);	
	  
	  Element div = DOM.createDiv();
	  div.setId("wtf_selection");
	  RootPanel.getBodyElement().appendChild(div);
	  _selected = elem;
	  

	  com.google.gwt.user.client.Element div_ = (com.google.gwt.user.client.Element) div;
	  DOM.setStyleAttribute(div_, "border", "solid 2px #3c7bd9");
	  DOM.setStyleAttribute(div_, "height", Integer.toString(elem.getClientHeight())+"px");
	  DOM.setStyleAttribute(div_, "width", Integer.toString(elem.getClientWidth())+"px");
	  DOM.setStyleAttribute(div_, "position", "absolute");
	  DOM.setStyleAttribute(div_, "top", Integer.toString(elem.getAbsoluteTop())+"px");
	  DOM.setStyleAttribute(div_, "left", Integer.toString(elem.getAbsoluteLeft())+"px");
	
	  DOM.sinkEvents(div_, Event.ONMOUSEOUT);
	  DOM.setEventListener(div_, new EventListener() {
		  public void onBrowserEvent(Event event) {
			  com.google.gwt.user.client.Element elem = DOM.eventGetTarget(event);
			  switch (DOM.eventGetType(event)) {
		      case Event.ONMOUSEOUT:
		    	  RootPanel.getBodyElement().removeChild(elem);
		    	  GWT.log("AutoDestruction" + elem.toString(), null);
		        break;
		    }
		  }
	  });
  }
  
  public void selectionClean(com.google.gwt.user.client.Element elem){
		  com.google.gwt.user.client.Element sel = DOM.getElementById("wtf_selection");
		  if(sel != null && !elem.equals(_selected) && !sel.equals(elem))
		  {
			  GWT.log("selectionClean" + elem.toString(), null);
			  RootPanel.getBodyElement().removeChild(sel);
		  }
  }
  
  private void elemInit(com.google.gwt.user.client.Element elem){
//	  DOM.setStyleAttribute(elem, "backgroundColor", "#0f0"); //troche niefajnie
  }
  
  private void addListener(com.google.gwt.user.client.Element elem){
	  DOM.sinkEvents(elem, Event.ONMOUSEOVER | Event.ONMOUSEOUT);
	  GWT.log("**ADD_LISTENER" + elem.toString() + "id:" + elem.getId(), null);
	  DOM.setEventListener(elem, new EventListener() {
		  public void onBrowserEvent(Event event) {
			  com.google.gwt.user.client.Element elem = DOM.eventGetTarget(event);
			  switch (DOM.eventGetType(event)) {
		      case Event.ONMOUSEOVER:
		    	  GWT.log("_over_ " + elem.toString(), null);
		    	  selectionClean(elem);
		    	  elemOver(elem);
		        break;
		      case Event.ONMOUSEOUT:
		    	  GWT.log("_out_ " + elem.toString(), null);
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
		  elemInit((com.google.gwt.user.client.Element) elem);
		  addListener((com.google.gwt.user.client.Element) elem);
  
		  Element child = elem.getFirstChildElement();
		  while(child != null) {
			  stack.push(child);
			  child = child.getNextSiblingElement();
		  }	  
	  }
  }
}
