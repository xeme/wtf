package com.wtf.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StatusBar {
  /*
   * Status Bar Widget
   */
  private static class StatusBarWidget extends Composite implements
  ClickHandler {

    private Label _status = new Label();
    private HorizontalPanel menu_panel = new HorizontalPanel();
    // menu buttons
    public ToggleButton _b_start_selection;
    public ToggleButton _b_show_discussions;
    private Image _b_about;

    public StatusBarWidget() {

      HorizontalPanel status_panel = new HorizontalPanel();
      VerticalPanel v_panel = new VerticalPanel();
      // All composites must call initWidget() in their constructors.
      initWidget(v_panel);

      // set styles
      getElement().setId("wtf_status_bar");
      if (!GWT.isScript()) {
        // prevent strange position fixed fail in hosted mode
        DOM.setStyleAttribute(getElement(), "position", "absolute");
      }
      addStyleName("wtf_ignore");

      menu_panel.getElement().setId("wtf_menu_panel");
      status_panel.getElement().setId("wtf_status_panel");
      v_panel.setSpacing(0);

      // compose all
      v_panel.add(menu_panel);
      v_panel.add(status_panel);
      status_panel.add(_status);

      // menu panel
      _b_start_selection = new ToggleButton(
          wtfImageBundle.select().createImage(), new ClickHandler() {
            public void onClick(ClickEvent event) {
              if (_error)
                return;
              _b_start_selection.setFocus(false);
              if (_b_start_selection.isDown()) {
                if (_b_show_discussions.isDown()) {
                  _b_show_discussions.setDown(false);
                  DiscussionsManager.removeIcons();
                }
                Selector.startSelectionMode();
                DeferredCommand.addCommand(new Command() { 
                  public void execute() {
                    StatusBar.setStatus("Selection Mode");
                  }
                });
              } else {
                Selector.endSelectionMode();
                DeferredCommand.addCommand(new Command() { 
                  public void execute() {
                    StatusBar.setStatus("WTF ready");
                  }
                });
              }
            }
          });
      _b_start_selection.setTitle("Start Selection Mode");

      _b_show_discussions = new ToggleButton(
          wtfImageBundle.discussions().createImage(), new ClickHandler() {
            public void onClick(ClickEvent event) {
              if (_error)
                return;
              _b_show_discussions.setFocus(false);
              if (_b_show_discussions.isDown()) {
                if (_b_start_selection.isDown()) {
                  _b_start_selection.setDown(false);
                  Selector.endSelectionMode();
                }
                DiscussionsManager.showIcons();
              } else {
                DiscussionsManager.removeIcons();
              }
              DeferredCommand.addCommand(new Command() { 
                public void execute() {
                  StatusBar.setStatus("WTF ready");
                }
              });
            }
          });
      _b_show_discussions.setTitle("Show Discussions");

      _b_about = wtfImageBundle.wtf().createImage();
      _b_about.setTitle("About WTF");

      menu_panel.add(_b_start_selection);
      menu_panel.add(_b_show_discussions);
      menu_panel.add(_b_about);
    }

    public void onClick(ClickEvent event) {
    }

    public void setOrientation(String orientation) {
      if (orientation.equals("left")) {
        DOM.setStyleAttribute(getElement(), "left", "0px");
        menu_panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
      } else {
        DOM.setStyleAttribute(getElement(), "right", "0px");
        menu_panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
      }
    }

    public void setStatus(String s) {
      _status.setText(s);
      _status.setTitle(s);
    }
  }

  public static WTFImageBundle wtfImageBundle = (WTFImageBundle) GWT.create(WTFImageBundle.class);
  private static StatusBarWidget _status_bar = null;

  private static boolean _error = false;
  private static boolean _selection_mode = false;

  private static boolean _discussion_mode = false;

  // blocks interface
  public static void blockWTF() {
    if (_status_bar._b_start_selection.isDown())
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          Selector.endSelectionMode();
        }
      });
    if (_status_bar._b_show_discussions.isDown())
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          DiscussionsManager.removeIcons();
        }
      });
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        setButtons(false, false);
        _status_bar._b_show_discussions.setEnabled(false);
        _status_bar._b_start_selection.setEnabled(false);
      }
    });
  }

  // if you want to be ignored element be child of this element
  public static Element getIgnoredParent() {
    return _status_bar.getElement();
  }

  public static void init() {
    _status_bar = new StatusBarWidget();
    RootPanel.get().add(_status_bar);
    
    String[] possible = {"left", "right"};
    _status_bar.setOrientation(Config.getOptionString("statusbar_orientation", possible, "left"));
  }

  public static boolean isDiscussionMode() {
    return _discussion_mode;
  }

  public static boolean isSelectionMode() {
    return _selection_mode;
  }

  public static void setButtons(boolean select, boolean discussions) {
    _status_bar._b_start_selection.setDown(select);
    _status_bar._b_show_discussions.setDown(discussions);
  }

  public static void setDiscussionMode(boolean b) {
    _discussion_mode = b;
    if (b)
      _selection_mode = false;
  }

  public static void setError(String s) {
    _status_bar.setStatus("(!) " + s);
    _error = true;
    blockWTF();
    _status_bar.setOrientation("left");
  }

  public static void setSelectionMode(boolean b) {
    _selection_mode = b;
    if (b)
      _discussion_mode = false;
  }

  public static void setStatus(String s) {
    if (!_error)
      _status_bar.setStatus(s);
  }

}
