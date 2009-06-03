package com.wtf.client.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.wtf.client.LineNumbers;
import com.wtf.client.dto.DiscussionDTO;
import com.wtf.client.dto.PostDTO;

import java.util.List;

public interface WTFServiceAsync {
  Request addPost(String discussionKey, PostDTO post,
      AsyncCallback<Boolean> callback);

  Request createDiscussion(String url, LineNumbers lines,
      AsyncCallback<String> callback);

  Request getDiscussions(String url, AsyncCallback<List<DiscussionDTO>> callback);

  Request getPosts(String discussionKey, AsyncCallback<List<PostDTO>> callback);
}