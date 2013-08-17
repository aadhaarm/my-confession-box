package com.l3.CB.client.ui.widgets;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;

public class SubscribeAnchor extends FlowPanel {

    boolean isSubscribed = false;
    Anchor ancSub;

    public SubscribeAnchor(Long confId) {
	super();
	this.setStyleName("subscribeAnchorBar");
	ancSub = new Anchor();
	this.add(ancSub);
	
	setTitle(ConfessionBox.cbText.subscribeLinkToolTipText());
	if(ConfessionBox.isTouchEnabled) {
	    ancSub.setStyleName(Constants.STYLE_CLASS_SUBSCRIBE_LINK_TO_BTN_U);
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
		ancSub.setText(ConfessionBox.cbText.unSubscribeAnchorLabel());
		setTitle(ConfessionBox.cbText.unSubscribeLinkToolTipText());
		if(ConfessionBox.isTouchEnabled) {
		    ancSub.setStyleName(Constants.STYLE_CLASS_SUBSCRIBE_LINK_TO_BTN_S);
		}
	    } else {
		ancSub.setText(ConfessionBox.cbText.subscribeAnchorLabel());
		setTitle(ConfessionBox.cbText.subscribeLinkToolTipText());
		if(ConfessionBox.isTouchEnabled) {
		    ancSub.setStyleName(Constants.STYLE_CLASS_SUBSCRIBE_LINK_TO_BTN_U);
		}
	    }
	} else {
	    ancSub.setText(ConfessionBox.cbText.subscribeAnchorLabel());
	}
    }

    private void bind(final Long confId) {
	if(ConfessionBox.isTouchEnabled) {
	    ancSub.addTouchEndHandler(new TouchEndHandler() {

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
	    ancSub.addClickHandler(new ClickHandler() {
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
	 ancSub.setVisible(false);
	 final Image loderImage = CommonUtils.getMeLoaderImage(); 
	 this.add(loderImage);
	 ConfessionBox.confessionService.subscribe(confId, ConfessionBox.getLoggedInUserInfo().getUserId(), new Date(), new AsyncCallback<Boolean>() {
	     @Override
	     public void onSuccess(Boolean result) {
		 isSubscribed = !isSubscribed;						
		 setLinkText();
		 remove(loderImage);
		 ancSub.setVisible(true);
	     }
	     @Override
	     public void onFailure(Throwable caught) {
		 Error.handleError("SubscribeAnchor", "onFailure", caught);
	     }
	 });
     }
}