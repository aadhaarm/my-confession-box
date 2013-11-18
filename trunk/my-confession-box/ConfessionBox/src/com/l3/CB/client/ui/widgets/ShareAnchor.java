package com.l3.CB.client.ui.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.UserInfo;

public class ShareAnchor extends FlowPanel {

    private Confession confession;
    private UserInfo confessedByUserInfo;
    private Anchor ancShare;

    public ShareAnchor(final Confession confession, UserInfo confessedByUserInfo) {
	super();
	this.setStyleName("shareAnchorBar");
	this.confession = confession;
	this.confessedByUserInfo = confessedByUserInfo;
	ancShare = new Anchor("Share");
	if(ConfessionBox.isTouchEnabled) {
	    ancShare.setStyleName(Constants.STYLE_CLASS_SHARE_LINK_TO_BTN);
	}

	
	ancShare.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(ConfessionBox.isLoggedIn) {
		    CommonUtils.postOnWall(FacebookUtil.getActivityUrl(confession.getConfId()), getImageUrl(FacebookUtil.getFaceIconImage(confession.getGender())), getShareTitle(), getCaption(), getDescription(), 5);
		} else {
		    CommonUtils.login(0);
		}
	    }
	});
	this.add(ancShare);
    }    
    
    private String getImageUrl(String url) {
	return Location.getHost() + url;
    }
    
    private String getDescription() {
	return ConfessionBox.getLoggedInUserInfo().getFirst_name() + " earned 5 human points for sharing.";
    }

    private String getCaption() {
	return confession.getConfessionTitle();
    }

    private String getShareTitle() {
	
	String shareTitle = "Anonymous confessed to World";
	
	// Get confessed to text if any
	String confesee = "Anonymous";
	if(confession.isShareAsAnyn()) {
	    confesee = ConfessionBox.cbText.confessedByAnynName();
	} else if(confessedByUserInfo != null){
	    confesee = confessedByUserInfo.getName();
	}

	if(confession.getConfessedTo() != null && !confession.getConfessedTo().isEmpty()) {
	    for (ConfessionShare confessionShare : confession.getConfessedTo()) {
		if(confessionShare != null && confessionShare.getRelation() != null) {
		    shareTitle = confesee + " confessed to " + confessionShare.getRelation().getDisplayText();		    
		}
	    }
	} else {
	    shareTitle = confesee + " confessed to World";
	}


	return shareTitle;
    }
}
