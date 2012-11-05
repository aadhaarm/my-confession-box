package com.l3.CB.client.ui.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.TO.Confession;

public class DeleteConfessionButton extends PushButton {

    public DeleteConfessionButton(final Confession confession, Image buttonImage, final boolean isVisible) {
	super();
	this.addStyleName("userControlButtonContainer");
	if(isVisible) {
	    this.setTitle("Hide your confession from anonymous wall.");
	} else {
	    this.setTitle("Un-Hide your confession from anonymous wall.");
	}

	this.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		ConfessionBox.confessionService.changeConfessionVisibility(ConfessionBox.loggedInUserInfo.getUserId(), ConfessionBox.loggedInUserInfo.getId(), confession.getConfId(), !isVisible, new AsyncCallback<Boolean>() {
		    @Override
		    public void onSuccess(Boolean result) {
			if(result) {
			    if(isVisible) {
				setTitle("Hide your confession from anonymous wall.");
			    } else {
				setTitle("Un-Hide your confession from anonymous wall.");
			    }
			}
		    }

		    @Override
		    public void onFailure(Throwable caught) {
			Error.handleError("DeleteConfessionButton",
				"onFailure", caught);
		    }
		});
	    }
	});
    }
}