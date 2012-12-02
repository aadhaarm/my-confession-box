package com.l3.CB.client.ui.widgets;

import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.UserInfo;

public class FriendsSuggestBox extends FlowPanel {

    private Map<String, UserInfo> userfriends = null;
    private MultiWordSuggestOracle friendsOracle;
    private final Label errMsg = new Label(ConfessionBox.cbText.shareConfessionSuggestBoxErrorMessage());
    private final Label label = new Label(ConfessionBox.cbText.friendsSuggestionBoxLabel());
    private SuggestBox friendsSuggestBox;
    private Image friendsImage;

    public FriendsSuggestBox(final Map<String, UserInfo> userfriends) {
	super();
	errMsg.setStyleName("errorMessage");
	
	this.userfriends = userfriends;

	this.setStyleName(Constants.DIV_FRIENDS_SUGGEST_BOX);

	if(userfriends != null && !userfriends.isEmpty()) {
	    friendsOracle = new MultiWordSuggestOracle();
	    //iterating over keys only
	    for (String friendsName : userfriends.keySet()) {
		friendsOracle.add(friendsName);
	    }
	    friendsSuggestBox = new SuggestBox(friendsOracle);
	    this.add(label);
	    this.add(friendsSuggestBox);
	}
	friendsSuggestBox.setWidth("150px");

	friendsSuggestBox.addSelectionHandler(new SelectionHandler<MultiWordSuggestOracle.Suggestion>() {
	    
	    @Override
	    public void onSelection(SelectionEvent<Suggestion> event) {
		validate();
		
		String selection = event.getSelectedItem().getReplacementString();
		if(selection != null && !"".equals(selection)) {
		    final UserInfo friend = userfriends.get(selection);
		    if(friend !=null) {
			if(friendsImage != null) {
			    remove(friendsImage);
			}
			friendsImage = new Image(FacebookUtil.getUserImageUrl(friend.getId()));
			friendsImage.setStyleName(Constants.DIV_FRIENDS_SUGGEST_IMAGE);
			// Friends image as link
			friendsImage.addClickHandler(new ClickHandler() {
			    
			    @Override
			    public void onClick(ClickEvent event) {
				Window.open(FacebookUtil.getProfileFBLink(friend.getId()).asString(), friend.getName(), "");
			    }
			});
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
	
	friendsSuggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<String> event) {
		validate();
		String selection = event.getValue();
		if(selection != null && !"".equals(selection)) {
		    final UserInfo friend = userfriends.get(selection);
		    if(friend !=null) {
			if(friendsImage != null) {
			    remove(friendsImage);
			}
			friendsImage = new Image(FacebookUtil.getUserImageUrl(friend.getId()));
			friendsImage.setStyleName(Constants.DIV_FRIENDS_SUGGEST_IMAGE);
			// Friends image as link
			friendsImage.addClickHandler(new ClickHandler() {
			    
			    @Override
			    public void onClick(ClickEvent event) {
				Window.open(FacebookUtil.getProfileFBLink(friend.getId()).asString(), friend.getName(), "");
			    }
			});
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