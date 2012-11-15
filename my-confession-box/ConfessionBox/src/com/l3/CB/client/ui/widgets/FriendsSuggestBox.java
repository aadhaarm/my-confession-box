package com.l3.CB.client.ui.widgets;

import java.util.Map;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.UserInfo;

public class FriendsSuggestBox extends FlowPanel {

    private Map<String, UserInfo> userfriends = null;
    private MultiWordSuggestOracle friendsOracle;
    private final Label errMsg = new Label(ConfessionBox.cbText.shareConfessionSuggestBoxErrorMessage());
    private SuggestBox friendsSuggestBox;
    private Image friendsImage;

    public FriendsSuggestBox(final Map<String, UserInfo> userfriends) {
	super();
	this.userfriends = userfriends;

	this.setStyleName("appeal");

	if(userfriends != null && !userfriends.isEmpty()) {
	    friendsOracle = new MultiWordSuggestOracle();
	    //iterating over keys only
	    for (String friendsName : userfriends.keySet()) {
		friendsOracle.add(friendsName);
	    }
	    friendsSuggestBox = new SuggestBox(friendsOracle);
	    this.add(friendsSuggestBox);
	}
	friendsSuggestBox.setWidth("150px");

	friendsSuggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<String> event) {
		validate();
		String selection = event.getValue();
		if(selection != null && !"".equals(selection)) {
		    UserInfo friend = userfriends.get(selection);
		    if(friend !=null) {
			if(friendsImage != null) {
			    remove(friendsImage);
			}
			friendsImage = new Image(FacebookUtil.getUserImageUrl(friend.getId()));
			friendsImage.setStyleName("friend_image");
			add(friendsImage);
		    } else {
			if(friendsImage != null) {
			    remove(friendsImage);
			}
		    }
		} else {
		    if(friendsImage != null) {
			remove(friendsImage);
		    }
		}
	    }
	});
    }

    public UserInfo getSelectedUser() {
	return userfriends.get(friendsSuggestBox.getValue());
    }

    public boolean validate() {
	if(userfriends.get(friendsSuggestBox.getValue()) == null) {
	    this.add(errMsg);
	    return false;
	} 
	this.remove(errMsg);
	return true;
    }

    public void setFocus() {
	friendsSuggestBox.setFocus(true);
    }
}