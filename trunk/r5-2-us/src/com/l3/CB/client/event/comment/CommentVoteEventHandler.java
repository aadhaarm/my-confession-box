package com.l3.CB.client.event.comment;

import com.google.gwt.event.shared.EventHandler;

public interface CommentVoteEventHandler extends EventHandler {
  void registerVote(CommentVoteEvent event);
}
