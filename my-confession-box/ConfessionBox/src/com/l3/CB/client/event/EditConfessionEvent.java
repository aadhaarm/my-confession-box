package com.l3.CB.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.l3.CB.shared.TO.Confession;

public class EditConfessionEvent extends GwtEvent<EditConfessionEventHandler> {
    public static Type<EditConfessionEventHandler> TYPE = new Type<EditConfessionEventHandler>();

    Confession confessionToBeEdited;

    public EditConfessionEvent(Confession confession) {
	super();
	this.confessionToBeEdited = confession;
    }

    @Override
    public Type<EditConfessionEventHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(EditConfessionEventHandler handler) {
	handler.editConfession(this);
    }

    public Confession getConfessionToBeEdited() {
	return confessionToBeEdited;
    }

    public void setConfessionToBeEdited(Confession confessionToBeEdited) {
	this.confessionToBeEdited = confessionToBeEdited;
    }
}
