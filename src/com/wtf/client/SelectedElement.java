package com.wtf.client;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Text;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.RootPanel;

public class SelectedElement {
	private Element _element;
	private HashSet<Element> _selection_borders = new HashSet<Element>();
	
	private List<Pair<Node, List<Node> > > _origin = new LinkedList<Pair<Node, List<Node> > >();
	
	private HashSet<Node> _selected = new HashSet<Node>();
	private HashSet<Node> _tmp_selected = new HashSet<Node>();
	private int _start_selection = -1;
	private boolean _selecting = true; //false - removing selection
	
	//in IE if onmousedown and onmouseup targets are not equal, onclick is generated
	//(targeting common ancestor)
	public boolean _ie_fail_prevent_click_event = false;

	public SelectedElement(Element element) {
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

		int counter = 0;
		for(int i = 0; i < children.getLength(); i++) {
			if(children.getItem(i).getNodeType() == Node.TEXT_NODE) {
				Text text_node = children.getItem(i).cast();

				String text_node_data = text_node.getData();
				String split[] = text_node_data.split(" ");
				List<Node> new_nodes = new LinkedList<Node>();
				for(int j = 0; j < split.length; j++) {
					String s = split[j];
					Element span = DOM.createSpan();
					//hashCode may differ among page views 
					span.setId("wtf_span_" + _element.hashCode() + "_" + counter);
					counter++;
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
				_origin.add(new Pair<Node, List<Node> >(text_node, new_nodes));
				text_node.getParentNode().removeChild(text_node);		
			}
		}
		Debug.log_time("createNextLevel finished ");
	}

	public void removeNextLevel() {
		for(Pair<Node, List<Node> > pair : _origin) {
			List<Node> new_nodes = pair.second();
			Node key = pair.first();
			boolean first = true;
			for(Node new_node : new_nodes) {
				if(first) {
					new_node.getParentNode().insertBefore(key, new_node);
					first = false;
				}
				new_node.getParentNode().removeChild(new_node);
			}
		}
		_origin.clear();
	}
	
	private void over(com.google.gwt.user.client.Element elem) {
		clearSelection();
		drawSelection(elem);
		if(_start_selection != -1)
			return;
		if(_selected.contains(elem)) {
			elem.setClassName("");
		} else {
			elem.setClassName("wtf_selection_word");
		}
	}
	
	private void out(com.google.gwt.user.client.Element elem) {
		if(_start_selection != -1)
			return;
		if(_selected.contains(elem)) {
			elem.setClassName("wtf_selection_word_selected");
		} else {
			elem.setClassName("");
		}
	}
	
	private void down(com.google.gwt.user.client.Element elem) {
		_start_selection = getCounterFromId(elem);
		if(_selected.contains(elem)) {
			_selecting = false;
		} else {
			_selecting = true;
		}
		_tmp_selected.add(elem);
	}
	
	private void up(com.google.gwt.user.client.Element elem) {
		if(_selecting) {
			_selected.addAll(_tmp_selected);
			for(Node node : _tmp_selected) {
				Element el = node.cast();
				el.setClassName("wtf_selection_word_selected");
			}
		} else {			
			_selected.removeAll(_tmp_selected);
		}
		clearSelection();
		_start_selection = -1;
	}
	
	private void clearSelection() {
		for(Node node : _tmp_selected) {
			Element el = node.cast();
			if(_selected.contains(el)) {
				el.setClassName("wtf_selection_word_selected");
			} else {
				el.setClassName("");
			}
		}
		_tmp_selected.clear();
	}
	
	private void drawSelection(com.google.gwt.user.client.Element elem) {
		if(_start_selection != -1) {
			int start = Math.min(getCounterFromId(elem), _start_selection);
			int end = Math.max(getCounterFromId(elem), _start_selection);
			for(int i = start; i <= end; i++) {
				String id = "wtf_span_" + _element.hashCode() + "_" + i;
				Element elem_i = DOM.getElementById(id);
				if(_selecting) {
					elem_i.setClassName("wtf_selection_word");
				} else {
					elem_i.setClassName("");
				}
				_tmp_selected.add(elem_i);
			}
		}
	}
	
	private int getCounterFromId(com.google.gwt.user.client.Element elem) {
		String id = elem.getId();
		return Integer.parseInt(id.substring(id.lastIndexOf('_')+1));
	}
	
	private void addListener(com.google.gwt.user.client.Element elem){
		DOM.sinkEvents(elem, Event.MOUSEEVENTS);
		DOM.setEventListener(elem, new EventListener() {
			public void onBrowserEvent(Event event) {
				if(!Selector.isSelectionMode())
					return;
				com.google.gwt.user.client.Element elem = DOM.eventGetTarget(event);
				if(!DOM.eventGetCurrentTarget(event).equals(elem))
					return;
				switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEOVER:
					out(elem);
					over(elem);		
					break;
				case Event.ONMOUSEOUT:
					out(elem);
					break;
				case Event.ONMOUSEDOWN:
					down(elem);
					_ie_fail_prevent_click_event = true;
					break;
				case Event.ONMOUSEUP:
					up(elem);
					break;
				}
				DOM.eventPreventDefault(event);
				NativeEvent nevent = event.cast();
				nevent.stopPropagation();
			}
		});
	}
}
