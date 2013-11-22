package com.l3.CB.client.view.misc;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.UserInfo;

public class FriendsSuggestBoxUI extends Composite {

    private static FriendsSuggestBoxUIUiBinder uiBinder = GWT
	    .create(FriendsSuggestBoxUIUiBinder.class);

    interface FriendsSuggestBoxUIUiBinder extends
    UiBinder<Widget, FriendsSuggestBoxUI> {
    }

    private Map<String, UserInfo> userfriends = null;
    private final MultiWordSuggestOracle friendsOracle = new MultiWordSuggestOracle();

    @UiField(provided = true)
    SuggestBox friendsSuggestBox;

    @UiField
    ListBox friendsSuggestList;

    @UiField
    Image imgFriend;

    @UiField
    SpanElement spanMessage;

    /**
     * Constructor
     */
    public FriendsSuggestBoxUI() {
	friendsSuggestBox = new SuggestBox(friendsOracle);
	initWidget(uiBinder.createAndBindUi(this));

	if(Navigator.isJavaEnabled()) {
	    friendsSuggestList.removeFromParent();
	} else {
	    friendsSuggestBox.removeFromParent();
	}

	imgFriend.setVisible(false);

	initializeFriends(null);
    }

    public void initializeFriends(final String preselectedFriend) {
	userfriends = CommonUtils.friendsMap;
	if(userfriends != null && !userfriends.isEmpty()) {
	    setFriendsInWidget(userfriends);
	    if(preselectedFriend != null) {
		setSelectedUser(preselectedFriend);	
	    }
	} else {
	    ConfessionBox.facebookService.getFriends(ConfessionBox.accessToken, new AsyncCallback<String>() {
		@Override
		public void onSuccess(String result) {
		    if(result != null) {
			userfriends = CommonUtils.getFriendsUserInfo(result);
			setFriendsInWidget(userfriends);
			if(preselectedFriend != null) {
			    setSelectedUser(preselectedFriend);	
			}
		    }
		}
		@Override
		public void onFailure(Throwable caught) {
		    Error.handleError("RegisterConfessionPresenter", "onFailure", caught);
		}
	    });
	}
    }


    private void setFriendsInWidget(Map<String, UserInfo> userfriends) {
	if(friendsSuggestBox != null){
	    if(userfriends != null && !userfriends.isEmpty()) {
		setupWidget(userfriends);
	    } else {
		spanMessage.setInnerText("No friends details available.");
	    }
	}
    }

    private void setupWidget(final Map<String, UserInfo> userfriends) {
	this.userfriends = userfriends;

	if(Navigator.isJavaEnabled()) {
	    setupFriendSuggestBox();
	} else {
	    setFriendSuggestListBox();
	}
    }

    private void setFriendSuggestListBox() {
	if(userfriends != null && !userfriends.isEmpty()) {
	    friendsSuggestList.addItem("Choose person you are confessing to");
	    //iterating over keys only
	    for (String friendsName : userfriends.keySet()) {
		friendsSuggestList.addItem(friendsName);
	    }
	}
    }

    private void setupFriendSuggestBox() {
	if(userfriends != null && !userfriends.isEmpty()) {
	    //iterating over keys only
	    for (String friendsName : userfriends.keySet()) {
		friendsOracle.add(friendsName);
	    }
	}
    }

    private void setSelectedUser(String userFullName) {
	friendsSuggestBox.setValue(userFullName, true);
    }

    public UserInfo getSelectedUser() {
	if(Navigator.isJavaEnabled()) {
	    return userfriends.get(friendsSuggestBox.getValue());
	} else {
	    return userfriends.get(friendsSuggestList.getValue(friendsSuggestList.getSelectedIndex()));
	}
    }

    public boolean validate() {
	if(Navigator.isJavaEnabled()) {
	    if(userfriends.get(friendsSuggestBox.getValue()) == null) {
		showErrorText();
		friendsSuggestBox.addStyleName(Constants.STYLE_CLASS_DANGER);
		return false;
	    } 
	    friendsSuggestBox.removeStyleName(Constants.STYLE_CLASS_DANGER);
	    hideErrorText();
	} else {
	    if(userfriends.get(friendsSuggestList.getValue(friendsSuggestList.getSelectedIndex())) == null) {
		showErrorText();
		friendsSuggestList.addStyleName(Constants.STYLE_CLASS_DANGER);
		return false;
	    } 
	    friendsSuggestList.removeStyleName(Constants.STYLE_CLASS_DANGER);
	    hideErrorText();
	}
	return true;
    }

    private void showErrorText() {
	spanMessage.setInnerText(ConfessionBox.cbText.shareConfessionSuggestBoxErrorMessage());
    }

    private void hideErrorText() {
	spanMessage.setInnerText(ConfessionBox.cbText.friendsSuggestionBoxLabel());
    }

    @UiHandler("friendsSuggestBox")
    void onSuggestBoxSelection(SelectionEvent<Suggestion> event) {
	validate();

	String selection = event.getSelectedItem().getReplacementString();
	if(selection != null && !"".equals(selection)) {
	    final UserInfo friend = userfriends.get(selection);

	    if(friend !=null) {
		imgFriend.setUrl(FacebookUtil.getUserImageUrl(friend.getId()));
		imgFriend.setVisible(true);
	    } else {
		imgFriend.setVisible(false);
	    }
	} else {
	    imgFriend.setVisible(false);
	}
    }

    @UiHandler("friendsSuggestBox")
    void onSuggestBoxChange(ValueChangeEvent<String> event) {
	validate();

	String selection = event.getValue();
	if(selection != null && !"".equals(selection)) {
	    final UserInfo friend = userfriends.get(selection);

	    if(friend !=null) {
		imgFriend.setUrl(FacebookUtil.getUserImageUrl(friend.getId()));
		imgFriend.setVisible(true);
	    } else {
		imgFriend.setVisible(false);
	    }
	} else {
	    imgFriend.setVisible(false);
	}
    }

    @UiHandler("imgFriend")
    void onImageCLick(ClickEvent event) {
	UserInfo friend = userfriends.get(friendsSuggestBox.getText());
	Window.open(FacebookUtil.getProfileFBLink(friend.getId()).asString(), friend.getName(), "");
    }

    @UiHandler("friendsSuggestList")
    void onListChange(ChangeEvent event) {
	validate();

	String selection = friendsSuggestList.getValue(friendsSuggestList.getSelectedIndex());
	if(selection != null && !"".equals(selection)) {
	    final UserInfo friend = userfriends.get(selection);

	    if(friend !=null) {
		imgFriend.setUrl(FacebookUtil.getUserImageUrl(friend.getId()));
		imgFriend.setVisible(true);
	    } else {
		imgFriend.setVisible(false);
	    }
	} else {
	    imgFriend.setVisible(false);
	}
    }

    public SuggestBox getFriendsSuggestBox() {
        return friendsSuggestBox;
    }

    public ListBox getFriendsSuggestList() {
        return friendsSuggestList;
    }
}