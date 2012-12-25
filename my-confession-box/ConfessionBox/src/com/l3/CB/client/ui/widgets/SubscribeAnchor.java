package com.l3.CB.client.ui.widgets;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;

public class SubscribeAnchor extends Anchor {

    boolean isSubscribed = false;

    public SubscribeAnchor(Long confId) {
	super();

	setTitle(ConfessionBox.cbText.subscribeLinkToolTipText());
	if(ConfessionBox.isTouchEnabled) {
	    this.setStyleName(Constants.STYLE_CLASS_SUBSCRIBE_LINK_TO_BTN_U);
	}
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
		setTitle(ConfessionBox.cbText.unSubscribeLinkToolTipText());
		if(ConfessionBox.isTouchEnabled) {
		    this.setStyleName(Constants.STYLE_CLASS_SUBSCRIBE_LINK_TO_BTN_S);
		}
	    } else {
		setText(ConfessionBox.cbText.subscribeAnchorLabel());
		setTitle(ConfessionBox.cbText.subscribeLinkToolTipText());
		if(ConfessionBox.isTouchEnabled) {
		    this.setStyleName(Constants.STYLE_CLASS_SUBSCRIBE_LINK_TO_BTN_U);
		}
	    }
	} else {
	    setText(ConfessionBox.cbText.subscribeAnchorLabel());
	}
    }

    private void bind(final Long confId) {
	if(ConfessionBox.isTouchEnabled) {
	    this.addTouchEndHandler(new TouchEndHandler() {

		@Override
		public void onTouchEnd(TouchEndEvent event) {
		    if(ConfessionBox.isLoggedIn) {
			registerSubscription(confId);
		    } else {
			CommonUtils.login(0);
		    }
		}
	    });
	} else {
	    this.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
		    if(ConfessionBox.isLoggedIn) {
			registerSubscription(confId);
		    } else {
			CommonUtils.login(0);
		    }
		}

	    });
	}
    }

    /**
     * @param confId
     */
     private void registerSubscription(final Long confId) {
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
     }
}