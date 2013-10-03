package com.l3.CB.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class UpdateMyFeedEvent extends GwtEvent<UpdateMyFeedEventHandler> {
    public static Type<UpdateMyFeedEventHandler> TYPE = new Type<UpdateMyFeedEventHandler>();


    public UpdateMyFeedEvent(int pointsToBeAdded) {
	super();
    }

    @Override
    public Type<UpdateMyFeedEventHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(UpdateMyFeedEventHandler handler) {
	handler.updateMyFeedConfessions(this);
    }
}