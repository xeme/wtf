package com.wtf.client;

import java.util.HashSet;

import com.google.gwt.user.client.Element;

public class Selection {
	private HashSet<SelectedElement> _elements;
	
	public Selection(HashSet<SelectedElement> elements) {
		_elements = elements;
	}
	
	public HashSet<SelectedElement> getElements() {
		return _elements;
	}
	
	public Element getTopElement() {
		Element top_elem = null;
		for(SelectedElement elem : _elements) {
			if(!elem.isSupported())
				continue;
			if(top_elem == null || elem.getElement().getAbsoluteTop() < top_elem.getAbsoluteTop()) {
				top_elem = elem.getElement();
			}
		}
		return top_elem;
	}
	
}
