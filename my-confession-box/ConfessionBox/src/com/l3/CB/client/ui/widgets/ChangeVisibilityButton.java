package com.l3.CB.client.ui.widgets;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateHPEvent;
import com.l3.CB.client.event.UpdateIdentityVisibilityEvent;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionPackage;

public class ChangeVisibilityButton extends FlowPanel {

    public ChangeVisibilityButton(final Confession confession, Image buttonImage, final boolean shareAnyn) {
	PushButton btn = new PushButton(buttonImage);
	buttonImage.setStyleName("link8");
	this.addStyleName(Constants.DIV_USER_CONTROL_BUTTON);

	if(shareAnyn) {
	    this.setTitle(ConfessionBox.cbText.unHideIdentityButtonTitleUserControl());
	} else {
	    this.setTitle(ConfessionBox.cbText.hideIdentityButtonTitleUserControl());
	}

	this.add(btn);

	btn.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if(ConfessionBox.isLoggedIn) {
		    final Image loaderImage = CommonUtils.getMeLoaderImage();
		    add(loaderImage);

		    ConfessionPackage confessionPackagea = new ConfessionPackage();
		    confessionPackagea.setUserId(ConfessionBox.getLoggedInUserInfo().getUserId());
		    confessionPackagea.setFbId(ConfessionBox.getLoggedInUserInfo().getId());
		    confessionPackagea.setConfId(confession.getConfId());
		    confessionPackagea.setVisible(!shareAnyn);
		    confessionPackagea.setUpdateTimeStamp(new Date());
		    
		    ConfessionBox.confessionService.changeIdentityVisibility(confessionPackagea, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
			    if(result) {
				if(shareAnyn) {
				    // Give human points
				    ConfessionBox.eventBus.fireEvent(new UpdateHPEvent(Constants.POINTS_ON_UNHIDING_IDENTITY));
				} else {
				    //Deduct human points
				    ConfessionBox.eventBus.fireEvent(new UpdateHPEvent(-1*Constants.POINTS_ON_UNHIDING_IDENTITY));
				    setTitle(ConfessionBox.cbText.unHideIdentityButtonTitleUserControl());
				    setTitle(ConfessionBox.cbText.hideIdentityButtonTitleUserControl());
				}

				confession.setShareAsAnyn(!confession.isShareAsAnyn());
				ConfessionBox.eventBus.fireEvent(new UpdateIdentityVisibilityEvent(confession));
			    }
			    Timer timer = new Timer() {
				@Override
				public void run() {
				    remove(loaderImage);
				}
			    };
			    timer.schedule(3000);
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