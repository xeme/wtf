package com.wtf.client;

import java.util.HashSet;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class SelectedElement {
	private Element _element;
	private HashSet<Element> _selection_borders = new HashSet<Element>();
	
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
}
