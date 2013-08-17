package com.l3.CB.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ActivityEvent extends GwtEvent<ActivityEventHandler> {
    public static Type<ActivityEventHandler> TYPE = new Type<ActivityEventHandler>();

    private final Long confId;

    public ActivityEvent(Long confId) {
	super();
	this.confId = confId;
    }

    @Override
    public Type<ActivityEventHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(ActivityEventHandler handler) {
	handler.registerVote(this);
    }

    public Long getConfId() {
        return confId;
    }
}