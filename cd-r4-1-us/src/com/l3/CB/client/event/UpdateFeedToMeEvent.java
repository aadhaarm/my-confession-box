package com.l3.CB.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.l3.CB.shared.TO.Confession;

public class UpdateFeedToMeEvent extends GwtEvent<UpdateFeedToMeEventHandler> {
    public static Type<UpdateFeedToMeEventHandler> TYPE = new Type<UpdateFeedToMeEventHandler>();

    Confession confession;

    public UpdateFeedToMeEvent(Confession confession) {
	super();
	this.confession = confession;
    }

    @Override
    public Type<UpdateFeedToMeEventHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(UpdateFeedToMeEventHandler handler) {
	handler.updateFeedToMeConfessions(this);
    }

    public Confession getConfession() {
        return confession;
    }
}