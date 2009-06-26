package com.wtf.client;

import static com.google.gwt.query.client.GQuery.$;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

public class DOMMagic {
  private static String _row_format = "";
  private static boolean _computed = false;
  private static int _line_counter;

  // maps for relations between Elements and lines
  private static HashMap<Element, TagLines> _elem_to_lines = new HashMap<Element, TagLines>();
  private static HashMap<Integer, Element> _line_to_elem = new HashMap<Integer, Element>();
  

  private static HashSet<Element> _done = new HashSet<Element>();
  private static HashSet<Element> _exclude = new HashSet<Element>();

  public static String getRowFormat() {
    return _row_format;
  }  
  
  public static LineNumbers getLineNumbersFromSelection(Selection selection) {
    LineNumbers line_numbers = new LineNumbers();

    HashSet<SelectedElement> elements = selection.getElements();
    for (SelectedElement s_elem : elements) {
      Element elem = s_elem.getElement();

      if (!_elem_to_lines.containsKey(elem)) {
        // this can happen if Row Format is not computed
        Debug.log("Error in getLineNumbersFromSelection");
        StatusBar.setError("Internal Error");
        return null;
      }

      line_numbers.addElement(_elem_to_lines.get(elem));
      line_numbers.addNextLevelWords(_elem_to_lines.get(elem), s_elem.getSelectedWords());
    }

    return line_numbers;
  }

  public static Selection getSelectionFromLineNumbers(LineNumbers line_numbers) {
    HashSet<SelectedElement> selected_elements = new HashSet<SelectedElement>();

    HashSet<TagLines> elements_lines = line_numbers.getElements();
    HashMap<TagLines, SelectedElement> tmp = new HashMap<TagLines, SelectedElement>();

    for (TagLines element_lines : elements_lines) {
      int line = 0;
      if (_line_to_elem.containsKey(element_lines.getOpenLine())) {
        line = element_lines.getOpenLine();
      } else {
        if (_line_to_elem.containsKey(element_lines.getCloseLine())) {
          line = element_lines.getCloseLine();
        } else {
          Debug.log("Error: There is no element in relation with lines: "
              + element_lines.getOpenLine() + " and " + element_lines.getCloseLine());
          StatusBar.setError("Incorect discussion location");
          return null;
        }
      }

      com.google.gwt.user.client.Element element = (com.google.gwt.user.client.Element) _line_to_elem.get(line);

      SelectedElement selected_element = new SelectedElement(element);
      tmp.put(element_lines, selected_element);
      selected_elements.add(selected_element);
    }

    // next level
    HashSet<WordsLines> next_levels = line_numbers.getNextLevelWords();
    for (WordsLines next_level : next_levels) {
      SelectedElement selected_element = tmp.get(next_level.getParentTag());
      if (selected_element == null) {
        Debug.log("Error: Incorrect next level description");
        //StatusBar.setError("Incorect discussion location");
        return null;
      }
      selected_element.setSelectedWords(next_level.getLines());
    }

    return new Selection(selected_elements);
  }

  public static boolean isComputed() {
    return _computed;
  }

  public static void requestComputingRowFormat() {
    StatusBar.setStatus("DOMMagic...");
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        if (!isComputed()) {
          DOMMagic.computeRowFormat();
        }
      }
    });
  }

  private static void computeRowFormat() {
    Debug.log_time("start computiong Row Format ");

    _line_counter = 0;
  
    NodeList<Element> elems;
    elems = $(Config.getOptionString("exclude_roots",  "")).get();
    for (int i = 0; i < elems.getLength(); i++) {
      _exclude.add(elems.getItem(i));
    }

    elems = $(Config.getOptionString("include_roots",  "")).get();
    for (int i = 0; i < elems.getLength(); i++) {
      computeRoot(elems.getItem(i));
    }

    _done.clear();
    _exclude.clear();
        
    Debug.log_time("Done ");
    _computed = true;

    // this may slow down every other logging so use this wisely
    //debug();
  }
  
  private static void computeRoot(Node root) {
    // Pair(node, is_closing_tag)
    Stack<Pair<Node, Boolean>> stack = new Stack<Pair<Node, Boolean>>();
    stack.push(new Pair<Node, Boolean>(root, false));
    Pair<Node, Boolean> node = null;
    while (!stack.isEmpty()) {
      node = stack.pop();
      
      if (_done.contains(node.first()) || _exclude.contains(node.first()))
        continue;

      // add node to row_format
      printNode(node);

      // save relations
      if (node.first().getNodeType() != Node.TEXT_NODE) {
        _line_to_elem.put(_line_counter - 1, (Element) node.first());
        // opening tag
        if (!node.second()) {
          _elem_to_lines.put((Element) node.first(), new TagLines(_line_counter - 1, -1));
        } else {
          TagLines tmp = _elem_to_lines.remove((Element) node.first());
          tmp.setCloseLine(_line_counter - 1);
          _elem_to_lines.put((Element) node.first(), tmp);
        }
      }

      // if closing tag
      if (node.second())
        continue;

      // create closing tag
      if (node.first().getNodeType() != Node.TEXT_NODE)
        stack.push(new Pair<Node, Boolean>(node.first(), true));

      // DFS
      Node child = node.first().getLastChild();
      while (child != null) {
        if (!ignore(child))
          stack.push(new Pair<Node, Boolean>(child, false));
        child = child.getPreviousSibling();
      }
    }
  }

  private static void debug() {
    _row_format = _row_format.replace("<", "&lt;");
    _row_format = _row_format.replace(">", "&gt;");
    //_row_format = _row_format.replace("\n", "<br>");
    _row_format = _row_format.replace("\n", "\\n");
    Debug.log(_row_format);
  }

  private static boolean ignore(Node node) {
    if (node.getNodeType() != Node.ELEMENT_NODE)
      return false;
    if (node.getNodeName().toLowerCase().equals("iframe"))
      return true;
    if (((Element) node).getClassName().equals("wtf_ignore"))
      return true;
    return false;
  }

  private static void printNode(Pair<Node, Boolean> node) {
    String sep = "__wtf__";
    if (node.first().getNodeType() == Node.TEXT_NODE) {
      //String words[] = node.first().getNodeValue().split("\\s"); // whitespace
      String val = node.first().getNodeValue();//.replaceAll("\n", "&nbsp;");
      String words[] = val.split(" ");
      for (String word : words) {
        if (word.length() == 0)
          continue;

        /* debug only */
        //_row_format += _line_counter + ": ";
        /* debug only */

        _row_format += word + sep;
        _line_counter++;
      }

    } else {
      /* debug only */
      //_row_format += _line_counter + ": ";
      /* debug only */

      _row_format += "<";
      if (node.second())
        _row_format += "/";
      _row_format += node.first().getNodeName() + ">" + sep;

      _line_counter++;
    }
  }

}
