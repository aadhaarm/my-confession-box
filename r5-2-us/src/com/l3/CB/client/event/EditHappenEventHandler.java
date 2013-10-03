package com.l3.CB.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface EditHappenEventHandler extends EventHandler {
  void changeStatusToEdit(EditHappenEvent event);
}