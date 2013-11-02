package com.l3.CB.client.event.confession;

import com.google.gwt.event.shared.GwtEvent;
import com.l3.CB.shared.TO.Filters;

public class FilterConfessionsEvent extends GwtEvent<FilterConfessionsEventHandler> {
    public static Type<FilterConfessionsEventHandler> TYPE = new Type<FilterConfessionsEventHandler>();

    private final Filters filter;

    public FilterConfessionsEvent(Filters filter) {
	super();
	this.filter = filter;
    }

    @Override
    public Type<FilterConfessionsEventHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(FilterConfessionsEventHandler handler) {
	handler.filter(this);
    }

    public Filters getFilter() {
        return filter;
    }
}