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
	  if(DOM.getElementById("wtf_selection") != null | elem == RootPanel.getBodyElement()) {
//		  GWT.log("elemOver return" + elem.toString(), null);
		  return;
	  }
//	  GWT.log("elemOver" + elem.toString(), null);	
	  
	  //add border INSIDE element
	  
//	  Node cloned_elem = elem.cloneNode(true);
//	  ((Element) cloned_elem).setId("wtf_selection");
	  
	  
	  
	  Element div = DOM.createDiv();
	  div.setId("wtf_selection");
	  elem.getParentElement().insertBefore(div, elem);
	  div.appendChild(elem);
	  
	  
	  
	 // elem.getParentElement().removeChild(elem);
	 // div.appendChild(cloned_elem);
	//  DOM.setStyleAttribute((com.google.gwt.user.client.Element) cloned_elem, "border", "solid 1px #00f");

	  DOM.setStyleAttribute((com.google.gwt.user.client.Element) div, "backgroundColor", "#fdd");
  }
  
  public void elemOut(com.google.gwt.user.client.Element elem){
//	  GWT.log("elemOut" + elem.toString(), null);
	  
	  com.google.gwt.user.client.Element sel;
	  while(true) {		  
		  sel = DOM.getElementById("wtf_selection");
		  if(sel != null){
			  sel.getParentElement().insertBefore(elem, sel);
			  sel.getParentElement().removeChild(sel);
		  }
		  else
			  break;
	  }

//	  DOM.setStyleAttribute(elem, "border", "solid 1px #0f0");
  }
  
  private void elemInit(com.google.gwt.user.client.Element elem){
//	  DOM.setStyleAttribute(elem, "border", "solid 1px #0f0"); //troche niefajnie
  }
  
  private void addListener(com.google.gwt.user.client.Element elem){
	  DOM.sinkEvents(elem, Event.ONMOUSEOUT | Event.ONMOUSEOVER);
	  DOM.setEventListener(elem, new EventListener() {
		  public void onBrowserEvent(Event event) {
			  com.google.gwt.user.client.Element elem = DOM.eventGetTarget(event);
			  switch (DOM.eventGetType(event)) {
		      case Event.ONMOUSEOVER:
		    	  elemOver(elem);
		        break;
		      case Event.ONMOUSEOUT:
		    	  elemOut(elem);
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
