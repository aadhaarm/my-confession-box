package com.l3.CB.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class UpdateMenuEvent extends GwtEvent<UpdateMenuEventHandler> {
	public static Type<UpdateMenuEventHandler> TYPE = new Type<UpdateMenuEventHandler>();

	@Override
	public Type<UpdateMenuEventHandler> getAssociatedType() {
	    return TYPE;
	}

	@Override
	protected void dispatch(UpdateMenuEventHandler handler) {
	    handler.updateMenuCount(this);
	}
}
