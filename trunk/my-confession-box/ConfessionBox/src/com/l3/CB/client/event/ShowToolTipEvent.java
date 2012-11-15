package com.l3.CB.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ShowToolTipEvent extends GwtEvent<ShowToolTipEventHandler> {
    public static Type<ShowToolTipEventHandler> TYPE = new Type<ShowToolTipEventHandler>();

    private final Long confId;
    
    public ShowToolTipEvent(Long confId) {
	super();
	this.confId = confId;
    }

    @Override
    public Type<ShowToolTipEventHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(ShowToolTipEventHandler handler) {
	handler.showToolTip(this);
    }

    public Long getConfId() {
        return confId;
    }
}