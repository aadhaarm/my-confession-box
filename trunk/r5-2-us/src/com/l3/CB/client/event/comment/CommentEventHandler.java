package com.l3.CB.client.event.comment;

import com.google.gwt.event.shared.EventHandler;

public interface CommentEventHandler extends EventHandler {
  void commentAdded(CommentEvent event);
}
