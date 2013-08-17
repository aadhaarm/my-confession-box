package com.l3.CB.client.event.comment;

import com.google.gwt.event.shared.GwtEvent;

public class CommentEvent extends GwtEvent<CommentEventHandler> {
    public static Type<CommentEventHandler> TYPE = new Type<CommentEventHandler>();

    private Long confId;
    
    public CommentEvent(Long confId) {
	super();
	this.confId = confId;
    }

    @Override
    public Type<CommentEventHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(CommentEventHandler handler) {
	handler.commentAdded(this);
    }

    public Long getConfId() {
        return confId;
    }

    public void setConfId(Long confId) {
        this.confId = confId;
    }
}