package com.wtf.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.wtf.client.LineNumbers;
import com.wtf.client.SPair;
import com.wtf.client.dto.DiscussionDTO;
import com.wtf.client.dto.PostDTO;

import java.util.List;

@RemoteServiceRelativePath("rpc")
public interface WTFService extends RemoteService {
  Boolean createStuff(SPair<Integer, Integer> p);
  String createDiscussion(String url, LineNumbers lines);
  List<DiscussionDTO> getDiscussions(String url);
  Boolean addPost(String discussionKey, PostDTO post);
  List<PostDTO> getPosts(String discussionKey);
}