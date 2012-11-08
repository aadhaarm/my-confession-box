package com.l3.CB.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.l3.CB.shared.TO.Confession;

public class UpdateIdentityVisibilityEvent extends GwtEvent<UpdateIdentityVisibilityEventHandler> {
    public static Type<UpdateIdentityVisibilityEventHandler> TYPE = new Type<UpdateIdentityVisibilityEventHandler>();

    private final Confession confession;
    
    public UpdateIdentityVisibilityEvent(Confession confession) {
	super();
	this.confession = confession;
    }

    @Override
    public Type<UpdateIdentityVisibilityEventHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(UpdateIdentityVisibilityEventHandler handler) {
	handler.updateIdentityVisibility(this);
    }

    public Confession getConfession() {
        return confession;
    }
}