package com.wtf.client;

import java.util.HashSet;

import com.google.gwt.user.client.rpc.IsSerializable;

/*
 * This class stores line numbers and line ranges which serialize relation
 * between row_format and DOM.
 * Objects of this class are used in RPC as selection definition 
 */
public class LineNumbers implements IsSerializable {
	private HashSet<Pair<Integer, Integer> > _elements = new HashSet<Pair<Integer, Integer> >();
	
	private HashSet<Pair<Pair<Integer, Integer>, HashSet<Integer> > > _next_level =
			new HashSet<Pair<Pair<Integer, Integer>, HashSet<Integer> > >();
	
	public void addElement(Pair<Integer, Integer> elem) {
		_elements.add(elem);
	}
	
	public void addNextLevelWords(Pair<Integer, Integer> elem, HashSet<Integer> words) {
		_next_level.add(new Pair<Pair<Integer, Integer>, HashSet<Integer> >(elem, words));
	}
	
	public HashSet<Pair<Integer, Integer> > getElements() {
		return _elements;
	}
	
	public HashSet<Pair<Pair<Integer, Integer>, HashSet<Integer> > > getNextLevelWords() {
		return _next_level;
	}
	
	public void debug() {
		for(Pair<Integer, Integer> e : _elements) {
			Debug.log("(" + e.first() + "," + e.second() + ")");
		}
	}
}
