package com.l3.CB.client.ui.widgets;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;

public class DeleteConfessionButton extends PushButton {

    boolean isVisibleToWorld;
    
    public DeleteConfessionButton(final Confession confession, Image buttonImage, final boolean isVisible) {
	super(buttonImage);
	isVisibleToWorld = isVisible;
	
	this.addStyleName(Constants.DIV_USER_CONTROL_BUTTON);
	if(isVisibleToWorld) {
	    this.setTitle(ConfessionBox.cbText.hideConfessionButtonTitleUserControl());
	} else {
	    this.setTitle(ConfessionBox.cbText.unhideConfessionButtonTitleUserControl());
	}

	this.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if(ConfessionBox.isLoggedIn) {
		    ConfessionBox.confessionService.changeConfessionVisibility(
			    ConfessionBox.getLoggedInUserInfo().getUserId(),
			    ConfessionBox.getLoggedInUserInfo().getId(),
			    confession.getConfId(), !isVisibleToWorld, new Date(),
			    new AsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
				    if(result) {
					changeVisibility();
					if(isVisibleToWorld) {
					    setTitle(ConfessionBox.cbText.hideConfessionButtonTitleUserControl());
					} else {
					    setTitle(ConfessionBox.cbText.unhideConfessionButtonTitleUserControl());
					}
				    }
				}


				@Override
				public void onFailure(Throwable caught) {
				    Error.handleError("DeleteConfessionButton", "onFailure", caught);
				}
			    });
		}
	    }
	});
    }
    private void changeVisibility() {
	isVisibleToWorld = !isVisibleToWorld;
    }
}