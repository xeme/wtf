package com.wtf.client;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.wtf.client.DiscussionPresenter.DiscussionView;

public class DiscussionWidget extends Composite implements DiscussionView {

  private FlexTable _thread = new FlexTable();
  final FlexTable _form = new FlexTable();
  private ToggleButton _show_button;
  private ToggleButton _submit;
  private InlineLabel _close;
  private TextBox _author;
  private TextArea _content;
  
  public Poll _poll; //TODO: support polls
  private HorizontalPanel _bar; //TODO: support polls

  public DiscussionWidget() {
    DockPanel dock = new DockPanel();
    initWidget(dock);
    getElement().setClassName("wtf_ignore");
    dock.setStyleName("wtf_discussion");
    dock.setSpacing(0);

    HorizontalPanel bar = new HorizontalPanel();
    bar.setStyleName("wtf_discussion_bar");
    bar.setSpacing(0);
    _bar = bar;

    dock.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    dock.add(bar, DockPanel.NORTH);

    dock.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
    ScrollPanel scroller = new ScrollPanel();
    scroller.setStyleName("wtf_discussion_thread");
    scroller.setSize("450px", "200px");
    scroller.add(_thread);
    _thread.setCellSpacing(0);
    _thread.setCellPadding(0);
    _thread.setWidth("100%");
    DOM.setStyleAttribute(_thread.getElement(), "margin", "0px");

    dock.add(scroller, DockPanel.CENTER);

    _form.setVisible(false);

    _show_button = new ToggleButton("Add post", "Hide");
    _show_button.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        _form.setVisible(!_form.isVisible());
      }
    });
    dock.add(_show_button, DockPanel.SOUTH);
    _show_button.setWidth("440px");
    DOM.setStyleAttribute(_show_button.getElement(), "margin", "0px");
    DOM.setStyleAttribute(_show_button.getElement(), "padding", "4px");

    fillForm(_form);
    dock.add(_form, DockPanel.SOUTH);

    DOM.setStyleAttribute(getElement(), "position", "absolute");
  }

  public void detach() {
    RootPanel.get().remove(this);
  }
  
  public void attach() {
    RootPanel.get().add(this);
  }
  
  //TODO: support polls
  public void setPoll(Poll poll) {
    _poll = poll;
    addPollTo(_bar);
    addCloseTo(_bar);
  }
  
  public HasClickHandlers getSubmitButton() {
    return _submit;
  }
  
  public HasClickHandlers getClose() {
    return _close;
  }

  public String getContent() {
    return _content.getText();
  }

  public String getAuthor() {
    return _author.getText();
  }

  public void position(int elem_top, int elem_left) {
    int top = elem_top - this.getOffsetHeight();
    int left = elem_left;
    left = Math.max(left, 0);
    top = Math.max(top, 0);

    if (left + this.getOffsetWidth() > Window.getClientWidth())
      left = Window.getClientWidth() - this.getOffsetWidth();

    DOM.setStyleAttribute(getElement(), "top", top + "px");
    DOM.setStyleAttribute(getElement(), "left", left + "px");
  }

  public void setFormVisibility(boolean visibility) {
    _form.setVisible(visibility);
    _show_button.setDown(visibility);
  }

  private void addCloseTo(Panel parent) {
    _close = new InlineLabel("X");
    _close.setStyleName("wtf_discussion_close");
    DOM.setStyleAttribute(_close.getElement(), "padding", "2px");
    DOM.setStyleAttribute(_close.getElement(), "cursor", "hand");
    DOM.setStyleAttribute(_close.getElement(), "cursor", "pointer");
    parent.add(_close);
  }

  private void addPollTo(Panel parent) {
    for (final Poll.Answer a : _poll.getAnswers()) {
      InlineLabel al = new InlineLabel(a.getLabel() + "(" + a.getVotes() + ")");
      al.setStyleName(a.getClassAttr());
      DOM.setStyleAttribute(al.getElement(), "padding", "2px");
      DOM.setStyleAttribute(al.getElement(), "cursor", "hand");
      DOM.setStyleAttribute(al.getElement(), "cursor", "pointer");
      parent.add(al);
      al.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          a.clicked();
        }
      });
    }
  }

  public void addPost(String author, String content, Date date) {
    int numRows = _thread.getRowCount();
    Label cl = new Label(content);

    Label al = new Label(author);
    al.setStyleName("wtf_discussion_author");
    al.setWidth("90px");
    DOM.setStyleAttribute(al.getElement(), "overflow", "hidden");

    DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");
    Label datel = new Label(fmt.format(date));
    datel.setStyleName("wtf_discussion_date");

    al.getElement().appendChild(datel.getElement());
    _thread.setWidget(numRows, 0, al);
    _thread.setWidget(numRows, 1, cl);
    if (numRows % 2 == 0) {
      cl.getElement().getParentElement().setClassName(
      "wtf_discussion_post_even");
      al.getElement().getParentElement().setClassName(
      "wtf_discussion_post_even");
      al.getElement().getParentElement().setAttribute("width", "90px");
    } else {
      cl.getElement().getParentElement().setClassName("wtf_discussion_post_odd");
      al.getElement().getParentElement().setClassName("wtf_discussion_post_odd");
      al.getElement().getParentElement().setAttribute("width", "90px");
    }
    cl.getElement().getParentElement().scrollIntoView();
  }

  private void fillForm(FlexTable form) {
    form.setCellSpacing(5);
    form.setStyleName("wtf_discussion_form");
    DOM.setStyleAttribute(form.getElement(), "margin", "0px");

    _author = new TextBox();
    Label author_l = new Label("Author: ");
    form.setWidget(0, 0, author_l);
    form.setWidget(0, 1, _author);

    _content = new TextArea();
    Label content_l = new Label("Content: ");
    _content.setSize("350px", "100px");
    form.setWidget(1, 0, content_l);
    form.setWidget(1, 1, _content);

    _submit = new ToggleButton("Add");
    _submit.setWidth("50px");
    _submit.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (_submit.isDown()) {
          _submit.setDown(false);    
        }
      }
    });

    form.setWidget(2, 1, _submit);
  }
}
