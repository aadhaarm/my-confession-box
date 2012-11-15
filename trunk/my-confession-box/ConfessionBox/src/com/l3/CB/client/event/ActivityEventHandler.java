package com.l3.CB.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface ActivityEventHandler extends EventHandler {
  void registerVote(ActivityEvent event);
}
