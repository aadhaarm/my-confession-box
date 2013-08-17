package com.l3.CB.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class CancelActivityEvent extends GwtEvent<CancelActivityEventHandler> {
    public static Type<CancelActivityEventHandler> TYPE = new Type<CancelActivityEventHandler>();

    private final Long confId;

    public CancelActivityEvent(Long confId) {
	super();
	this.confId = confId;
    }

    @Override
    public Type<CancelActivityEventHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(CancelActivityEventHandler handler) {
	handler.cancelActivity(this);
    }

    public Long getConfId() {
        return confId;
    }
}