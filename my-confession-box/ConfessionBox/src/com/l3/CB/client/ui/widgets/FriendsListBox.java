package com.l3.CB.client.ui.widgets;

import java.util.List;

import com.google.gwt.user.client.ui.ListBox;
import com.l3.CB.shared.TO.UserInfo;

public class FriendsListBox extends ListBox{

    List<UserInfo> userfriends = null;

    public FriendsListBox(List<UserInfo> userfriends) {
	super();
	this.userfriends = userfriends;
	if(userfriends != null && !userfriends.isEmpty()) {
	    for (UserInfo userInfo : userfriends) {
		addItem(userInfo.getName());
	    }
	}
    }

    public UserInfo getSelectedUser() {
	return userfriends.get(getSelectedIndex());
    }
}