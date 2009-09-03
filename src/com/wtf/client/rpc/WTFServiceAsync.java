package com.wtf.client.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.wtf.client.LineNumbers;
import com.wtf.client.dto.PageDTO;
import com.wtf.client.dto.PostDTO;

import java.util.List;

public interface WTFServiceAsync {
  Request addPost(String discussionKey, PostDTO post,
      AsyncCallback<Boolean> callback);

  Request createDiscussion(String url, LineNumbers lines,
      AsyncCallback<String> callback);

  Request getPage(String url, AsyncCallback<PageDTO> callback);

  Request getPosts(String discussionKey, AsyncCallback<List<PostDTO>> callback);

  Request updateLineNumbers(String key, LineNumbers lineNumbersFromSelection,
      AsyncCallback<Boolean> callback);

  Request getContent(String url, AsyncCallback<String> callback);
  Request updateContent(String url, String content, AsyncCallback<Boolean> callback);

  Request computeDiff(String url, String new_content, AsyncCallback<Integer[]> callback);
}