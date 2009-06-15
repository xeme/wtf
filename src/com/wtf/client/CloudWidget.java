package com.wtf.client;

import java.util.HashMap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.wtf.client.CloudPresenter.CloudView;

public class CloudWidget extends Composite implements CloudView {
  private Image _icon = null;
  private Label _value = null;
  
  //number of clouds attached to this place
  private static HashMap<Integer, Integer> _clouds = new HashMap<Integer, Integer>();

  private int _offset = -1;

  public CloudWidget() {
    _icon = StatusBar.wtfImageBundle.new_discussion().createImage();
    _value = new Label();
  }
  
  public com.google.gwt.user.client.Element getClickable() {
    return _value.getElement();
  }
  
  public Label getValue() {
    return _value;
  }

  public void drawIcon(int elem_hash, int elem_top, int elem_left, int thread_size) {
    int top = elem_top - 20;
    int left = elem_left - _icon.getWidth();
    left = Math.max(left, 0);
    top = Math.max(top, 0);

    if (_offset == -1)
      setOffset(elem_hash, _icon.getHeight());
    top += _offset;

    renderIcon(top, left, Integer.toString(thread_size));
  }

  public void drawNewIcon(int elem_top, int elem_left) {
    removeIcon();

    int top = elem_top - 20;
    int left = elem_left - _icon.getWidth();
    left = Math.max(left, 0);
    top = Math.max(top, 0);

    renderIcon(top, left, "?");
  }
  
  public void removeIcon() {
    if (_value.getElement().getParentElement() != null) {
      _value.getElement().getParentElement().removeChild(_value.getElement());
    }
    if (_icon.getElement().getParentElement() != null) {
      _icon.getElement().getParentElement().removeChild(_icon.getElement());
    }
  }

  private void renderIcon(int top, int left, String val) {
    DOM.setStyleAttribute(_icon.getElement(), "position", "absolute");
    DOM.setStyleAttribute(_icon.getElement(), "top", top + "px");
    DOM.setStyleAttribute(_icon.getElement(), "left", left + "px");
    _icon.getElement().setClassName("wtf_icon");

    StatusBar.getIgnoredParent().appendChild(_icon.getElement());
    renderValue(left, top, val);
  }
  
  private void renderValue(int x, int y, String value) {
    Element val = _value.getElement();
    val.setInnerText(value);
    val.setClassName("wtf_icon_text");
    com.google.gwt.user.client.Element val_ = val.cast();

    DOM.setStyleAttribute(val_, "position", "absolute");
    DOM.setStyleAttribute(val_, "top", y + "px");
    DOM.setStyleAttribute(val_, "left", x + "px");
    DOM.setStyleAttribute(val_, "cursor", "hand");
    DOM.setStyleAttribute(val_, "cursor", "pointer");

    StatusBar.getIgnoredParent().appendChild(val);
  }

  private void setOffset(int elem_hash, int height) {
    _offset = getCloudsNumber(elem_hash) * height;
    addCloud(elem_hash);
  }
  
  private static int getCloudsNumber(int elem_hash) {
    if (!_clouds.containsKey(elem_hash))
      return 0;
    return _clouds.get(elem_hash);
  }
  
  private static void addCloud(int elem_hash) {
    int old_val = 0;
    if (_clouds.containsKey(elem_hash)) {
      old_val = _clouds.get(elem_hash);
      _clouds.remove(elem_hash);
    }
    _clouds.put(elem_hash, old_val + 1);
  }
}
