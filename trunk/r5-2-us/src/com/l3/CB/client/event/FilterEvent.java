package com.l3.CB.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.l3.CB.shared.TO.Filters;

public class FilterEvent extends GwtEvent<FilterEventHandler> {
    public static Type<FilterEventHandler> TYPE = new Type<FilterEventHandler>();

    private final Filters selectedFilter;
    
    public FilterEvent(Filters selectedFilter) {
	super();
	this.selectedFilter = selectedFilter;
    }

    @Override
    public Type<FilterEventHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(FilterEventHandler handler) {
	handler.filter(this);
    }

    public Filters getSelectedFilter() {
        return selectedFilter;
    }
}