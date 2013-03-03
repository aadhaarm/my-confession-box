package com.l3.CB.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class URLEvent extends GwtEvent<URLEventHandler> {
    public static Type<URLEventHandler> TYPE = new Type<URLEventHandler>();

    private final String urlType;

    public URLEvent(String urlType) {
	super();
	this.urlType = urlType;
    }

    @Override
    public Type<URLEventHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(URLEventHandler handler) {
	handler.loadPage(this);
    }

    public String getUrlType() {
        return urlType;
    }
}