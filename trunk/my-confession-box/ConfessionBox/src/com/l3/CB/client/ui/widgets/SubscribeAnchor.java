package com.l3.CB.client.ui.widgets;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;

public class SubscribeAnchor extends Anchor {

    boolean isSubscribed = false;

    public SubscribeAnchor(Long confId) {
	super();

	setTitle(ConfessionBox.cbText.subscribeLinkToolTipText());

	if(ConfessionBox.isLoggedIn) {
	    ConfessionBox.confessionService.isSubscribed(confId, ConfessionBox.getLoggedInUserInfo().getUserId(), new AsyncCallback<Boolean>() {

		@Override
		public void onSuccess(Boolean result) {
		    isSubscribed = result;
		    setLinkText();
		}


		@Override
		public void onFailure(Throwable caught) {
		    Error.handleError("SubscribeAnchor", "onFailure", caught);
		}
	    });
	} else {
	    setLinkText();
	}

	bind(confId);
    }

    /**
     * Set anchor text
     */
    private void setLinkText() {
	if(ConfessionBox.isLoggedIn) {
	    if(isSubscribed){
		setText(ConfessionBox.cbText.unSubscribeAnchorLabel());
	    } else {
		setText(ConfessionBox.cbText.subscribeAnchorLabel());
	    }
	} else {
	    setText(ConfessionBox.cbText.subscribeAnchorLabel());
	}
    }

    private void bind(final Long confId) {
	this.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(ConfessionBox.isLoggedIn) {

		    ConfessionBox.confessionService.subscribe(confId, ConfessionBox.getLoggedInUserInfo().getUserId(), new Date(), new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
			    isSubscribed = !isSubscribed;						
			    setLinkText();
			}

			@Override
			public void onFailure(Throwable caught) {
			    Error.handleError("SubscribeAnchor", "onFailure", caught);
			}
		    });
		} else {
		    CommonUtils.login(0);
		}
	    }
	});
    }
}