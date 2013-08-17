package com.l3.CB.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class EditHappenEvent extends GwtEvent<EditHappenEventHandler> {
    public static Type<EditHappenEventHandler> TYPE = new Type<EditHappenEventHandler>();

    private final Long confId;

    public EditHappenEvent(Long confId) {
	super();
	this.confId = confId;
    }

    @Override
    public Type<EditHappenEventHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(EditHappenEventHandler handler) {
	handler.changeStatusToEdit(this);
    }

    public Long getConfId() {
        return confId;
    }
}