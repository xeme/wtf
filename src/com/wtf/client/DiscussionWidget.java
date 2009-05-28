package com.wtf.client;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class DiscussionWidget extends Composite{

	private Discussion _discussion;
	private FlexTable _thread = new FlexTable();

	public DiscussionWidget(Discussion discusion) {
		_discussion = discusion;
		DockPanel dock = new DockPanel();
		initWidget(dock);
		addStyleName("wtf_ignore");
		dock.setStyleName("wtf_discussion");


		dock.setSpacing(0);
		HorizontalPanel bar = new HorizontalPanel();
		bar.setStyleName("wtf_discussion_bar");

		bar.setSpacing(0);

		addPollTo(bar);

		dock.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		dock.add(bar, DockPanel.NORTH);

		dock.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
		ScrollPanel scroller = new ScrollPanel();
		scroller.setStyleName("wtf_discussion_thread");
		scroller.setSize("450px", "200px");

		scroller.add(_thread);
		_thread.setCellSpacing(0);
		_thread.setCellPadding(0);
		
		DOM.setStyleAttribute(_thread.getElement(), "margin", "0px");

		dock.add(scroller, DockPanel.SOUTH);

		DOM.setStyleAttribute(getElement(), "position", "absolute");
		DOM.setStyleAttribute(getElement(), "top", "20px");
		DOM.setStyleAttribute(getElement(), "left", "20px");
		
		fillThread();
	}

	private void addPollTo(Panel parent) {
		Poll poll = _discussion.getPoll();
		for(final Poll.Answer a : poll.getAnswers()) {
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

	private void fillThread() {
		List<Post> thread = _discussion.getThread();
		for(Post p : thread) {
			addPost(p);
		}
	}

	private void addPost(Post p) {
		int numRows = _thread.getRowCount();
		Label cl = new Label(p.getContent());

		Label al = new Label(p.getAuthor());
		al.setStyleName("wtf_discussion_author");
		al.setWidth("90px");
		DOM.setStyleAttribute(al.getElement(), "overflow", "hidden");

		DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");
		Label date = new Label(fmt.format(p.getDate()));
		date.setStyleName("wtf_discussion_date");

		al.getElement().appendChild(date.getElement());
		_thread.setWidget(numRows, 0, al);
		_thread.setWidget(numRows, 1, cl);
		if(numRows % 2 == 0) {
			cl.getElement().getParentElement().setClassName("wtf_discussion_post_even");
			al.getElement().getParentElement().setClassName("wtf_discussion_post_even");
		} else {
			cl.getElement().getParentElement().setClassName("wtf_discussion_post_odd");
			al.getElement().getParentElement().setClassName("wtf_discussion_post_odd");
		}
		cl.getElement().scrollIntoView();
	}

}


