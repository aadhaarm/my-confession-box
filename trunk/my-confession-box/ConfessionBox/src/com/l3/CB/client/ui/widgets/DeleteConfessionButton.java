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
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionPackage;

public class DeleteConfessionButton extends FlowPanel {

    boolean isVisibleToWorld;
    
    public DeleteConfessionButton(final Confession confession, Image buttonImage, final boolean isVisible) {
	super();
	PushButton btn = new PushButton(buttonImage);
	buttonImage.setStyleName("link9");

	isVisibleToWorld = isVisible;
	
	this.addStyleName(Constants.DIV_USER_CONTROL_BUTTON);
	if(isVisibleToWorld) {
	    this.setTitle(ConfessionBox.cbText.hideConfessionButtonTitleUserControl());
	} else {
	    this.setTitle(ConfessionBox.cbText.unhideConfessionButtonTitleUserControl());
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
		    confessionPackagea.setVisible(!isVisibleToWorld);
		    confessionPackagea.setUpdateTimeStamp(new Date());
		    
		    ConfessionBox.confessionService.changeConfessionVisibility(confessionPackagea , new AsyncCallback<Boolean>() {
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
		    Timer timer = new Timer() {
			@Override
			public void run() {
			    remove(loaderImage);
			}
		    };
		    timer.schedule(3000);
		}
	    }
	});
    }
    private void changeVisibility() {
	isVisibleToWorld = !isVisibleToWorld;
    }
}