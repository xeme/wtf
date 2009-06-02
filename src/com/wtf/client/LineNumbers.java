package com.wtf.client;

import java.io.Serializable;
import java.util.HashSet;

import com.google.gwt.user.client.rpc.IsSerializable;

/*
 * This class stores line numbers and line ranges which serialize relation
 * between row_format and DOM.
 * Objects of this class are used in RPC as selection definition 
 */
public class LineNumbers implements Serializable {
	private HashSet<SPair<Integer, Integer> > _elements = new HashSet<SPair<Integer, Integer> >();
	
	private HashSet<SPair<SPair<Integer, Integer>, HashSet<Integer> > > _next_level =
			new HashSet<SPair<SPair<Integer, Integer>, HashSet<Integer> > >();
	
	public void addElement(SPair<Integer, Integer> elem) {
		_elements.add(elem);
	}
	
	public void addNextLevelWords(SPair<Integer, Integer> elem, HashSet<Integer> words) {
		_next_level.add(new SPair<SPair<Integer, Integer>, HashSet<Integer> >(elem, words));
	}
	
	public HashSet<SPair<Integer, Integer> > getElements() {
		return _elements;
	}
	
	public HashSet<SPair<SPair<Integer, Integer>, HashSet<Integer> > > getNextLevelWords() {
		return _next_level;
	}
	
	public void debug() {
		for(SPair<Integer, Integer> e : _elements) {
			Debug.log("(" + e.first() + "," + e.second() + ")");
		}
	}
}
