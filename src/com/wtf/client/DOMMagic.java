package com.wtf.client;

import java.util.Stack;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.RootPanel;

public class DOMMagic {
	private static String _row_format = "";
	private static boolean _computed = false;
	
	public static boolean isComputed() {
		return _computed;
	}

	public static void computeRowFormat() {
		Debug.log_time("start computiong Row Format ");
		//Pair(node, is_closing_tag)
		Stack<Pair<Node, Boolean> > stack = new Stack<Pair<Node, Boolean> >();
		stack.push(new Pair<Node, Boolean>(RootPanel.getBodyElement(), false));
		Pair<Node, Boolean> node = null;	  
		while(!stack.isEmpty()) {
			node = stack.pop();

			translateNode(node);
			
			if(node.second())
				continue;
			
			if(node.first().getNodeType() != Node.TEXT_NODE)
				stack.push(new Pair<Node, Boolean>(node.first(), true));
			
			Node child = node.first().getLastChild();
			while(child != null) {
				if(!ignore(child))
					stack.push(new Pair<Node, Boolean>(child, false));
				child = child.getPreviousSibling();
			}	  
		}
		Debug.log_time("Done ");
		_computed = true;
		debug();
	}

	private static void translateNode(Pair<Node, Boolean> node) {
		if(node.first().getNodeType() == Node.TEXT_NODE) {
			String words[] = node.first().getNodeValue().split("\\s"); //whitespace
			for(String word : words) {
				if(word.length() == 0)
					continue;
				_row_format += word + "\n";
			}
			
		} else {
			_row_format += "<";
			if(node.second())
				_row_format += "/";
			_row_format += node.first().getNodeName() + ">\n";
		}
	}

	private static boolean ignore(Node node) {
		if(node.getNodeType() != Node.ELEMENT_NODE)
			return false;
		if(node.getNodeName().toLowerCase().equals("iframe"))
			return true;
		if(((Element) node).getClassName().equals("wtf_ignore"))
			return true;
		return false;
	}
	
	private static void debug() {
		_row_format = _row_format.replace("<", "&lt;");
		_row_format = _row_format.replace(">", "&gt;");
		_row_format = _row_format.replace("\n", "<br>");
		Debug.log(_row_format);
	}

}
