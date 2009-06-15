package com.wtf.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.wtf.client.CloudPresenter.CloudView;
import com.wtf.client.dto.PostDTO;

public class DiscussionPresenter {
  interface DiscussionView {
    void addPost(String author, String content, Date date);
    void position(int top, int left);
    HasClickHandlers getSubmitButton();
    HasClickHandlers getClose();
    String getContent();
    String getAuthor();
    void setFormVisibility(boolean visibility);
    void setVisible(boolean visibility);
    boolean isVisible();
    void attach();
    void detach();

    void setPoll(Poll poll); //TODO: support polls
  }

  private Discussion _discussion;

  private DiscussionView _discussion_widget = null;

  private CloudPresenter _icon;
  private boolean _over_icon = false;
  private Command _on_close;

  public DiscussionPresenter(Discussion discussion, Command on_close) {
    _discussion = discussion;

    if (on_close == null) {
      _on_close = getNormalOnClose();
    } else {
      _on_close = on_close;
    }

    Command on_click = new Command() {
      public void execute() {
        if (!isVisible()) {
          show();
        } else {
          getNormalOnClose().execute();
        }
      }
    };
    Command on_mouse_over = new Command() {
      public void execute() {
        _over_icon = true;
        showSelection();
      }
    };
    Command on_mouse_out = new Command() {
      public void execute() {
        _over_icon = false;
        if (!isVisible())
          hideSelection();
      }

    };

    CloudView cloud_view = new CloudWidget();
    _icon = new CloudPresenter(on_click, on_mouse_over, on_mouse_out);
    _icon.bindWidget(cloud_view);
  }

  public void bindWidget(DiscussionView w) {
    _discussion_widget = w;

    _discussion_widget.setPoll(_discussion.getPoll()); //TODO: support polls

    _discussion_widget.getSubmitButton().addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        addPost(new PostDTO(_discussion_widget.getAuthor(), _discussion_widget.getContent(), new Date()));        
      }
    });

    _discussion_widget.getClose().addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        _on_close.execute();
      }
    });

    //fill thread
    List<PostDTO> thread = _discussion.getThread();
    for (PostDTO p : thread) {
      _discussion_widget.addPost(p.getAuthor(), p.getContent(), p.getDate());
    }
  }

  public void addPost(final PostDTO p) {
    final Command add_post = new Command() {
      public void execute() {
        DiscussionsManager.addPost(_discussion, p, new Command() {
          public void execute() {
            _discussion.addPost(p);
            _icon.updateValue(_discussion.getThreadSize());
            _discussion_widget.addPost(p.getAuthor(), p.getContent(), p.getDate());
          }
        });
      }
    };

    if(_discussion.isNew()) {
      DiscussionsManager.createDiscussion(_discussion, new Command() {
        public void execute() {
          _discussion.setNew(false);
          DiscussionsManager.addDiscussion(DiscussionPresenter.this);
          _discussion.setFetched(true);

          Selector.endSelectionMode();

          _discussion_widget.getClose().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
              getNormalOnClose().execute();
            }
          });

          add_post.execute();

          DiscussionsManager.showIcons();
          StatusBar.setButtons(false, true);

          if (!StatusBar.isDiscussionMode())
            return;

          show();
          _discussion_widget.setFormVisibility(true);
          // this must be after ending selection mode
          DeferredCommand.addCommand(new Command() {
            public void execute() {
              showSelection();
            }
          });
        }
      });
    } else {
      add_post.execute();
    }
  }

  public void deleteDiscussionWidget() {
    if (_discussion_widget != null)
      _discussion_widget.detach();
    _discussion_widget = null;
    _on_close = getNormalOnClose();
  }

  public void hide() {
    if (_discussion_widget != null)
      _discussion_widget.setVisible(false);
  }

  public void hideSelection() {
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        for (final SelectedElement elem : _discussion.getSelection().getElements()) {
          elem.hideNextLevelSelection();
          elem.deleteSelectionBorders();
        }
      }
    });
  }

  public void removeIcon() {
    _icon.removeIcon();
    hideSelection();
  }

  public void reposition() {
    if (_discussion_widget != null) {
      Element top_elem = _discussion.getSelection().getTopElement();
      _discussion_widget.position(top_elem.getAbsoluteTop(), top_elem.getAbsoluteLeft());
    }
  }

  public void show() {
    Command show_discussion = new Command() {
      public void execute() {
        if (!_discussion.isNew() && !StatusBar.isDiscussionMode())
          return;
        if (_discussion_widget == null) {
          bindWidget(new DiscussionWidget());
          Debug.log("dupa");
          _discussion_widget.attach();
        }
        _discussion_widget.setVisible(true);
        _discussion_widget.setFormVisibility(false);
        Element top_elem = _discussion.getSelection().getTopElement();
        _discussion_widget.position(top_elem.getAbsoluteTop(), top_elem.getAbsoluteLeft());
      }
    };
    if (_discussion.isNew()) {
      show_discussion.execute();
    } else {
      DiscussionsManager.fetchDiscussionDetails(_discussion, show_discussion);
    }
  }

  public void showIcon() {
    Element elem = _discussion.getSelection().getTopElement();
    _icon.drawIcon((com.google.gwt.user.client.Element) elem, _discussion.getThreadSize());
  }

  public void showSelection() {
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        for (final SelectedElement elem : _discussion.getSelection().getElements()) {
          elem.showSelection();
        }
      }
    });
    for (final SelectedElement elem : _discussion.getSelection().getElements()) {
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          elem.showNextLevelSelection();
        }
      });
    }
  }

  public boolean isNew() {
    return _discussion.isNew();
  }
  
  public void setSelection(Selection selection) {
    _discussion.setSelection(selection);
  }
  
  public void setPoll(Poll poll) {
    _discussion.setPoll(poll);
  }

  private Command getNormalOnClose() {
    return new Command() {
      public void execute() {
        hide();
        if (!_over_icon)
          hideSelection();
      }
    };
  }

  private boolean isVisible() {
    return _discussion_widget != null && _discussion_widget.isVisible();
  }
}
