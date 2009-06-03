package com.wtf.client;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

/*
 * Selection Manager
 */
public class Selector {
  private static SelectedElement _selected = null; // current highlighted
                                                   // element
  private static boolean _initialized = false;
  private static boolean _selection_mode = false;
  private static CloudWidget _new_cloud = null;
  private static Discussion _new_discussion;

  private static HashMap<Element, SelectedElement> _active_selection = new HashMap<Element, SelectedElement>();
  private static HashSet<Element> _init_done = new HashSet<Element>();
  private static HashSet<Element> _exclude = new HashSet<Element>();

  public static void commitSelected() {
    if (_selected == null)
      return;
    selectionClean(_selected.getElement());
    if (isFlash(_selected.getElement())) {
      drawTab(_selected.getElement(), _selected);
    } else {
      drawRect(_selected.getElement(), _selected);
    }
    _active_selection.put(_selected.getElement(), _selected);
    _selected.createNextLevel();
    drawIcon();
  }

  public static void drawRect(com.google.gwt.user.client.Element elem,
      SelectedElement sel) {
    int thickness_i = Config.getOptionInt("border_thickness", 2);

    String thickness = Integer.toString(thickness_i) + "px";

    Element divl = DOM.createDiv();
    Element divr = DOM.createDiv();
    Element divt = DOM.createDiv();
    Element divb = DOM.createDiv();
    if (sel != null) {
      divl.setClassName("wtf_selection_selected");
      divr.setClassName("wtf_selection_selected");
      divt.setClassName("wtf_selection_selected");
      divb.setClassName("wtf_selection_selected");
      sel.addSelectionBorder((com.google.gwt.user.client.Element) divl);
      sel.addSelectionBorder((com.google.gwt.user.client.Element) divr);
      sel.addSelectionBorder((com.google.gwt.user.client.Element) divt);
      sel.addSelectionBorder((com.google.gwt.user.client.Element) divb);
    } else {
      divl.setId("wtf_selection_l");
      divr.setId("wtf_selection_r");
      divt.setId("wtf_selection_t");
      divb.setId("wtf_selection_b");
      divl.setClassName("wtf_selection");
      divr.setClassName("wtf_selection");
      divt.setClassName("wtf_selection");
      divb.setClassName("wtf_selection");
    }

    com.google.gwt.user.client.Element divl_ = (com.google.gwt.user.client.Element) divl;
    com.google.gwt.user.client.Element divr_ = (com.google.gwt.user.client.Element) divr;
    com.google.gwt.user.client.Element divt_ = (com.google.gwt.user.client.Element) divt;
    com.google.gwt.user.client.Element divb_ = (com.google.gwt.user.client.Element) divb;
    DOM.setStyleAttribute(divl_, "position", "absolute");
    DOM.setStyleAttribute(divr_, "position", "absolute");
    DOM.setStyleAttribute(divt_, "position", "absolute");
    DOM.setStyleAttribute(divb_, "position", "absolute");
    DOM.setStyleAttribute(divl_, "fontSize", "0px"); // for IE6
    DOM.setStyleAttribute(divr_, "fontSize", "0px"); // for IE6
    DOM.setStyleAttribute(divt_, "fontSize", "0px"); // for IE6
    DOM.setStyleAttribute(divb_, "fontSize", "0px"); // for IE6

    String h = Integer.toString(elem.getOffsetHeight() + thickness_i) + "px";
    String w = Integer.toString(elem.getOffsetWidth() + thickness_i) + "px";
    String y = Integer.toString(elem.getAbsoluteTop()) + "px";
    String x = Integer.toString(elem.getAbsoluteLeft() - thickness_i) + "px";
    String w_x = Integer.toString(elem.getOffsetWidth()
        + elem.getAbsoluteLeft() - thickness_i)
        + "px";
    String h_y = Integer.toString(elem.getOffsetHeight()
        + elem.getAbsoluteTop())
        + "px";

    DOM.setStyleAttribute(divl_, "width", thickness);
    DOM.setStyleAttribute(divl_, "height", h);
    DOM.setStyleAttribute(divl_, "top", y);
    DOM.setStyleAttribute(divl_, "left", x);

    DOM.setStyleAttribute(divr_, "width", thickness);
    DOM.setStyleAttribute(divr_, "height", h);
    DOM.setStyleAttribute(divr_, "top", y);
    DOM.setStyleAttribute(divr_, "left", w_x);

    DOM.setStyleAttribute(divt_, "width", w);
    DOM.setStyleAttribute(divt_, "height", thickness);
    DOM.setStyleAttribute(divt_, "top", y);
    DOM.setStyleAttribute(divt_, "left", x);

    DOM.setStyleAttribute(divb_, "width", w);
    DOM.setStyleAttribute(divb_, "height", thickness);
    DOM.setStyleAttribute(divb_, "top", h_y);
    DOM.setStyleAttribute(divb_, "left", x);

    EventListener event_listener = new EventListener() {
      public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
          case Event.ONMOUSEOUT:
            remove_selection();
            break;
        }
      }
    };

    DOM.sinkEvents(divl_, Event.ONMOUSEOUT);
    DOM.sinkEvents(divr_, Event.ONMOUSEOUT);
    DOM.sinkEvents(divt_, Event.ONMOUSEOUT);
    DOM.sinkEvents(divb_, Event.ONMOUSEOUT);
    DOM.setEventListener(divl_, event_listener);
    DOM.setEventListener(divr_, event_listener);
    DOM.setEventListener(divt_, event_listener);
    DOM.setEventListener(divb_, event_listener);

    RootPanel.getBodyElement().appendChild(divl);
    RootPanel.getBodyElement().appendChild(divr);
    RootPanel.getBodyElement().appendChild(divt);
    RootPanel.getBodyElement().appendChild(divb);
  }

  public static void drawTab(com.google.gwt.user.client.Element elem,
      SelectedElement sel) {
    // create 4 borders
    int h = 20;
    String label = "selection";

    String hs = Integer.toString(h + 1) + "px";

    Element div = DOM.createDiv();
    if (sel != null) {
      div.setClassName("wtf_selection_tab_selected");
      sel.addSelectionBorder((com.google.gwt.user.client.Element) div);
    } else {
      div.setClassName("wtf_selection_tab");
      div.setId("wtf_selection_tab");
    }
    div.setInnerText(label);

    RootPanel.getBodyElement().appendChild(div);

    com.google.gwt.user.client.Element div_ = (com.google.gwt.user.client.Element) div;
    DOM.setStyleAttribute(div_, "position", "absolute");

    String x = Integer.toString(elem.getAbsoluteLeft()) + "px";
    String y = Integer.toString(elem.getAbsoluteTop() - h) + "px";

    DOM.setStyleAttribute(div_, "height", hs);
    DOM.setStyleAttribute(div_, "top", y);
    DOM.setStyleAttribute(div_, "left", x);

    DOM.setStyleAttribute(div_, "cursor", "hand");
    DOM.setStyleAttribute(div_, "cursor", "pointer");

    EventListener event_listener = new EventListener() {
      public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
          case Event.ONMOUSEOUT:
            remove_selection();
            break;
          case Event.ONCLICK:
            if (_active_selection.containsKey(_selected.getElement())) {
              unCommitSelected();
            } else {
              commitSelected();
            }
            break;
        }
      }
    };

    DOM.sinkEvents(div_, Event.ONMOUSEOUT | Event.ONCLICK);
    DOM.setEventListener(div_, event_listener);
  }

  public static void endSelectionMode() {
    // Debug.log_time("endSelectionMode");
    StatusBar.setStatus("Leaving Selection Mode...");
    // chunk operations
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        remove_selection();
        for (SelectedElement sel_elem : _active_selection.values()) {
          sel_elem.deleteSelectionBorders();
          sel_elem.removeNextLevel();
        }
        _active_selection.clear();
        _selection_mode = false;
      }
    });
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        StatusBar.setStatus("WTF ready");
        // Debug.log_time("endSelectionMode finished ");
      }
    });
    if (_new_cloud != null)
      _new_cloud.removeIcon();
    if (_new_discussion != null && _new_discussion.isNew())
      _new_discussion.deleteDiscussionWidget();
    StatusBar.setSelectionMode(false);
  }

  public static boolean ignore(com.google.gwt.user.client.Element elem) {
    return elem.getId().equals("wtf_selection_l")
        || elem.getId().equals("wtf_selection_r")
        || elem.getId().equals("wtf_selection_t")
        || elem.getId().equals("wtf_selection_b")
        || elem.getId().equals("wtf_selection_tab") || parentIgnore(elem);
  }

  public static void initNewCloud() {
    // create empty selection
    Command on_close = new Command() {
      public void execute() {
        _selection_mode = true;
        _new_discussion.hide();
      }
    };
    _new_discussion = new Discussion(new Selection(
        new HashSet<SelectedElement>()), 0, on_close);
    _new_discussion.setNew(true);
    Command on_click = new Command() {
      public void execute() {
        if (_selection_mode) {
          Selection sel = new Selection(new HashSet<SelectedElement>(
              _active_selection.values()));
          _new_discussion.setSelection(sel);
          Command cmd = new Command() {
            public void execute() {
              if (!StatusBar.isSelectionMode())
                return;
              _new_discussion.setPoll(DiscussionManager.getNewPoll());
              _selection_mode = false;
              remove_selection();
              _new_discussion.show();
            }
          };
          DiscussionManager.fetchPollInfo(cmd);
        } else {
          _selection_mode = true;
          _new_discussion.hide();
        }
      }
    };
    _new_cloud = new CloudWidget(on_click, null, null);
  }

  public static boolean isFlash(com.google.gwt.user.client.Element elem) {
    return elem.getTagName().toLowerCase().equals("object")
        || elem.getTagName().toLowerCase().equals("embed");
  }

  public static boolean isSelectionMode() {
    return _selection_mode;
  }

  public static void remove_selection() {
    remove_border("l");
    remove_border("r");
    remove_border("t");
    remove_border("b");
    com.google.gwt.user.client.Element sel = DOM.getElementById("wtf_selection_tab");
    if (sel != null)
      RootPanel.getBodyElement().removeChild(sel);

    if (_selected != null)
      DOM.setStyleAttribute(_selected.getElement(), "cursor", "");
    _selected = null;
  }

  public static void select(com.google.gwt.user.client.Element elem) {
    if (elem == null || _selected != null || elem == RootPanel.getBodyElement()
        || ignore(elem)) {
      return;
    }
    _selected = new SelectedElement(elem);

    if (isFlash(elem)) {
      drawTab(elem, null);
    } else {
      // only non-flash can be clicked. flash has special tab
      DOM.setStyleAttribute(elem, "cursor", "hand");
      DOM.setStyleAttribute(elem, "cursor", "pointer");
      drawRect(elem, null);
    }
    // Debug.log_time("select: ");
  }

  public static void selectionClean(com.google.gwt.user.client.Element elem) {
    if (_selected == null || elem.equals(_selected.getElement())
        || ignore(elem))
      return;
    remove_selection();
    // Debug.log_time("clear: ");
  }

  public static void startSelectionMode() {
    DiscussionManager.fetchDiscussionsList(new Command() { public void execute() {} });
    
    DOMMagic.requestComputingRowFormat();

    // chunk operations
    if (!_initialized) {
      StatusBar.setStatus("Initializing DOM...");
      DeferredCommand.addCommand(new Command() { // init chunk
        public void execute() {
          initDOM();
          initResizeRefresh();
          _initialized = true;
        }
      });
    }

    DeferredCommand.addCommand(new Command() { // start_selection chunk
      public void execute() {
        initNewCloud();
      }
    });
    StatusBar.setSelectionMode(true);
    _selection_mode = true;
  }

  public static void unCommitSelected() {
    if (_selected == null
        || !_active_selection.containsKey(_selected.getElement()))
      return;
    _active_selection.get(_selected.getElement()).deleteSelectionBorders();
    _active_selection.get(_selected.getElement()).removeNextLevel();
    _active_selection.remove(_selected.getElement());
    drawIcon();
  }

  private static void addListener(com.google.gwt.user.client.Element elem) {
    if (isFlash(elem)) {
      HashSet<String> possible = new HashSet<String>();
      possible.add("true");
      possible.add("false");
      String support = Config.getOptionString("flash", possible, "false");
      if (support.equals("false"))
        return;
    }
    DOM.sinkEvents(elem, Event.MOUSEEVENTS | Event.ONCLICK);
    DOM.setEventListener(elem, new EventListener() {
      public void onBrowserEvent(Event event) {
        if (!_selection_mode)
          return;
        com.google.gwt.user.client.Element elem = DOM.eventGetTarget(event);
        if (!DOM.eventGetCurrentTarget(event).equals(elem))
          return;
        switch (DOM.eventGetType(event)) {
          case Event.ONMOUSEOVER:
            selectionClean(elem);
            select(elem);
            if (_selected != null) {
              _selected._ie_fail_prevent_click_event = false;
            }
            break;
          case Event.ONMOUSEOUT:
            selectionClean(elem);
            break;
          case Event.ONCLICK:
            if (isFlash(elem) || _selected._ie_fail_prevent_click_event)
              return;
            if (_active_selection.containsKey(elem)) {
              unCommitSelected();
            } else {
              commitSelected();
            }
            break;
        }
        DOM.eventPreventDefault(event);
      }
    });
  }

  // end of interface
  private static void drawIcon() {
    _new_cloud.removeIcon();
    Element top_elem = null;
    for (Element elem : _active_selection.keySet()) {
      if (top_elem == null || elem.getAbsoluteTop() < top_elem.getAbsoluteTop()) {
        top_elem = elem;
      }
    }
    if (top_elem == null)
      return;
    _new_cloud.setTargetElement(top_elem);
    _new_cloud.drawNewIcon();
  }

  private static void initDOM() {
    List<String> exclude_selectors = Config.getExcludeSelectors();
    for (String str : exclude_selectors) {
      NodeList<Element> elems = $(str).get();
      for (int i = 0; i < elems.getLength(); i++) {
        _exclude.add(elems.getItem(i));
      }
    }

    List<String> add_selectors = Config.getAddSelectors();
    for (String str : add_selectors) {
      NodeList<Element> elems = $(str).get();
      for (int i = 0; i < elems.getLength(); i++) {
        initElementByRoot(elems.getItem(i));
      }
    }
    _init_done.clear();
    _exclude.clear();
  }

  private static void initElementByRoot(Element root) {
    Stack<Element> stack = new Stack<Element>();
    stack.push(root);
    Element elem = null;
    while (!stack.isEmpty()) {
      elem = stack.pop();
      if (ignore((com.google.gwt.user.client.Element) elem))
        continue;

      if (_init_done.contains(elem) || _exclude.contains(elem))
        continue;

      addListener((com.google.gwt.user.client.Element) elem);
      _init_done.add(elem);

      Element child = elem.getFirstChildElement();
      while (child != null) {
        stack.push(child);
        child = child.getNextSiblingElement();
      }
    }
  }

  private static void initResizeRefresh() {
    Window.addResizeHandler(new ResizeHandler() {
      public void onResize(ResizeEvent event) {
        remove_selection();
        for (Element elem : _active_selection.keySet()) {
          _active_selection.get(elem).deleteSelectionBorders();
          if (isFlash((com.google.gwt.user.client.Element) elem)) {
            drawTab((com.google.gwt.user.client.Element) elem,
                _active_selection.get(elem));
          } else {
            drawRect((com.google.gwt.user.client.Element) elem,
                _active_selection.get(elem));
          }
        }
        drawIcon();
        _new_discussion.reposition();
      }
    });
  }

  private static boolean parentIgnore(Element elem) {
    while (elem != null && elem != RootPanel.getBodyElement()) {
      if (elem.getClassName().equals("wtf_ignore"))
        return true;
      elem = elem.getParentElement();
    }
    return false;
  }

  private static void remove_border(String type) {
    com.google.gwt.user.client.Element sel = DOM.getElementById("wtf_selection_"
        + type);
    if (sel != null)
      RootPanel.getBodyElement().removeChild(sel);
  }
}
