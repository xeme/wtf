package com.wtf.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Label;

public class CloudPresenter {
  interface CloudView {
    Element getClickable();
    Label getValue();
    void drawIcon(int elem_hash, int elem_top, int elem_left, int thread_size);
    void drawNewIcon(int elem_top, int elem_left);
    void removeIcon();
  }

  private CloudView _widget = null;

  private Command _on_click = null;
  private Command _on_mouse_over = null;
  private Command _on_mouse_out = null;

  public CloudPresenter(Command on_click, Command on_mouse_over, Command on_mous_out) {
    _on_click = on_click;
    _on_mouse_over = on_mouse_over;
    _on_mouse_out = on_mous_out;
  }

  public void bindWidget(CloudView w) {
    _widget = w;
    Element val = w.getClickable();
    DOM.sinkEvents(val, Event.MOUSEEVENTS | Event.ONCLICK);
    DOM.setEventListener(val, new EventListener() {
      public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
          case Event.ONMOUSEOVER:
            if (_on_mouse_over == null)
              return;
            _on_mouse_over.execute();
            break;
          case Event.ONMOUSEOUT:
            if (_on_mouse_out == null)
              return;
            _on_mouse_out.execute();
            break;
          case Event.ONCLICK:
            if (_on_click == null)
              return;
            _on_click.execute();
            break;
        }
        DOM.eventPreventDefault(event);
      }
    });
  }

  public void updateValue(int val) {
    if(_widget == null)
      return;
    Label value = _widget.getValue();
    if (value != null) {
      value.setText(Integer.toString(val));
    }
  }

  public void drawIcon(Element elem, int thread_size) {
    if(_widget == null || elem == null)
      return;
    _widget.drawIcon(elem.hashCode(), elem.getAbsoluteTop(), elem.getAbsoluteLeft(), thread_size);
  }
  
  public void drawNewIcon(Element elem) {
    if(_widget == null || elem == null)
      return;
    _widget.drawNewIcon(elem.getAbsoluteTop(), elem.getAbsoluteLeft());
  }

  public void removeIcon() {
    _widget.removeIcon();
  }
}
