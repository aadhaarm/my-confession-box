package com.l3.CB.client.ui.widgets;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateHPEvent;
import com.l3.CB.client.event.UpdateIdentityVisibilityEvent;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;

public class ChangeVisibilityButton extends PushButton {

    public ChangeVisibilityButton(final Confession confession, Image buttonImage, final boolean shareAnyn) {
	super();
	this.addStyleName(Constants.DIV_USER_CONTROL_BUTTON);
	if(shareAnyn) {
	    this.setTitle(ConfessionBox.cbText.unHideIdentityButtonTitleUserControl());
	} else {
	    this.setTitle(ConfessionBox.cbText.hideIdentityButtonTitleUserControl());
	}

	this.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if(ConfessionBox.isLoggedIn) {

		    ConfessionBox.confessionService.changeIdentityVisibility(
			    ConfessionBox.getLoggedInUserInfo().getUserId(),
			    ConfessionBox.getLoggedInUserInfo().getId(),
			    confession.getConfId(), !shareAnyn, new Date(),
			    new AsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
				    if(result) {
					if(shareAnyn) {
					    // Give human points
					    ConfessionBox.confEventBus.fireEvent(new UpdateHPEvent(Constants.POINTS_ON_UNHIDING_IDENTITY));
					    setTitle(ConfessionBox.cbText.unHideIdentityButtonTitleUserControl());
					} else {
					    //Deduct human points
					    ConfessionBox.confEventBus.fireEvent(new UpdateHPEvent(-1*Constants.POINTS_ON_UNHIDING_IDENTITY));
					    setTitle(ConfessionBox.cbText.hideIdentityButtonTitleUserControl());
					}

					confession.setShareAsAnyn(!confession.isShareAsAnyn());
					ConfessionBox.confEventBus.fireEvent(new UpdateIdentityVisibilityEvent(confession));
				    }
				}
				@Override
				public void onFailure(Throwable caught) {
				    Error.handleError("ChangeVisibilityButton", "onFailure", caught);
				}
			    });
		}
	    }
	});
    }
}
