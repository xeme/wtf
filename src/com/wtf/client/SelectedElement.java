package com.wtf.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Text;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.RootPanel;

public class SelectedElement {
	private Selector _selector;
	private Element _element;
	private HashSet<Element> _selection_borders = new HashSet<Element>();
	
	private List<Node> _text_nodes = new LinkedList<Node>();
	private HashMap<Node, List<Node> > _origin = new HashMap<Node, List<Node> >(); 
	

	public SelectedElement(Selector selector, Element element) {
		_selector = selector;
		_element = element;
	}

	public Element getElement() {
		return _element;
	}

	public void addSelectionBorder(Element elem) {
		_selection_borders.add(elem);
	}

	public void deleteSelectionBorders() {
		for(Element elem : _selection_borders) {
			if(elem != null)
				RootPanel.getBodyElement().removeChild(elem);
		}
		_selection_borders.clear();
	}

	public void createNextLevel() {
		Debug.log_time("createNextLevel ");
		Node node = _element;
		NodeList<Node> children = node.getChildNodes();
		//check if its enough text to add spans
		int length = 0;
		for(int i = 0; i < children.getLength(); i++) {
			if(children.getItem(i).getNodeType() == Node.TEXT_NODE) {
				Text text_node =  children.getItem(i).cast();
				length += text_node.getData().split(" ").length;
				if(length >= 10)
					break;
			}
		}
		if(length < 10)
			return;

		for(int i = 0; i < children.getLength(); i++) {
			if(children.getItem(i).getNodeType() == Node.TEXT_NODE) {
				Text text_node =  children.getItem(i).cast();
				String text_node_data = text_node.getData();
				String split[] = text_node_data.split(" ");
				List<Node> new_nodes = new LinkedList<Node>();
				for(int j = 0; j < split.length; j++) {
					String s = split[j];
					Element span = DOM.createSpan();
					if(j == split.length - 1
						&& text_node_data.charAt(text_node_data.length() -1) != ' ') {
						span.setInnerText(s);
					} else {
						span.setInnerText(s + " ");
					}
					DOM.setStyleAttribute(span, "cursor", "hand");
					DOM.setStyleAttribute(span, "cursor", "pointer");
					addListener(span);
					text_node.getParentNode().insertBefore(span, text_node);
					new_nodes.add(span);
				}		
				_text_nodes.add(text_node);
				_origin.put(text_node, new_nodes);
				text_node.getParentNode().removeChild(text_node);
			}
		}
		Debug.log_time("createNextLevel finished ");
	}

	public void removeNextLevel() {
		for(Node key : _origin.keySet()) {
			List<Node> new_nodes = _origin.get(key);
			Text tmp = key.cast();
			boolean first = true;
			for(Node new_node : new_nodes) {
				if(first) {
					new_node.getParentNode().insertBefore(key, new_node);
					first = false;
				}
				new_node.getParentNode().removeChild(new_node);
			}
		}
		_text_nodes.clear();
		_origin.clear();
	}
	
	private void select(com.google.gwt.user.client.Element elem) {
		elem.setClassName("wtf_selection_word");
	}
	
	private void selectionClean(com.google.gwt.user.client.Element elem) {
		elem.setClassName("");
	}
	
	private void addListener(com.google.gwt.user.client.Element elem){
		DOM.sinkEvents(elem, Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONCLICK);
		DOM.setEventListener(elem, new EventListener() {
			public void onBrowserEvent(Event event) {
				if(!_selector.isSelectionMode())
					return;
				com.google.gwt.user.client.Element elem = DOM.eventGetTarget(event);
				if(!DOM.eventGetCurrentTarget(event).equals(elem))
					return;
				switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEOVER:
					selectionClean(elem);
					select(elem);
					break;
				case Event.ONMOUSEOUT:
					selectionClean(elem);
					break;
				case Event.ONCLICK:
							
					break;
				}
				DOM.eventPreventDefault(event);
			}
		});
	}
}
