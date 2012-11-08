package com.l3.CB.client.ui.widgets;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateIdentityVisibilityEvent;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.UserInfo;

public class ShareConfessionPopup extends PopupPanel {

    private List<UserInfo> userfriends;
    private final Button btnSubmit; 
    private FriendsListBox friendsListBox;
    private final FlowPanel fPnlConfessionShare;
    private final Confession confession;

    public ShareConfessionPopup(Confession confession) {
	super(true);
	this.confession = confession;
	addStyleName(Constants.STYLE_CLASS_PARDON_MODAL);
	fPnlConfessionShare = new FlowPanel();
//	populateFriendsList();
	btnSubmit = new Button("Send Confession");
	fPnlConfessionShare.add(btnSubmit);
	add(fPnlConfessionShare);
	bind(confession.getConfId());
    }

    private void bind(final Long confId) {
	btnSubmit.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(friendsListBox != null) {

		    UserInfo selectedUser = friendsListBox.getSelectedUser();
		    if(selectedUser != null) {
			// Open a fb notification message dialog
			CommonUtils.sendConfessionNotification(selectedUser.getId(), selectedUser.getName(), ConfessionBox.loggedInUserInfo.getName(), FacebookUtil.getConfessionNotificationUrl(), FacebookUtil.getApplicationImage());

			final ConfessionShare confessTo = getConfessedShareTO(selectedUser);

			if(confessTo != null) {
			    ConfessionBox.confessionService.createConfessedToUser(confId, ConfessionBox.loggedInUserInfo.getUserId(), ConfessionBox.loggedInUserInfo.getId(), confessTo, new AsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
				    ConfessionBox.confEventBus.fireEvent(new UpdateIdentityVisibilityEvent(confession));
				    hide();
				}

				@Override
				public void onFailure(Throwable caught) {
				    Error.handleError("ShareConfessionButton", "onFailure", caught);
				}
			    });
			}
		    }
		}
	    };
	});
    }

    /**
     * @param sharedWithUser
     * @return
     */
    private ConfessionShare getConfessedShareTO(UserInfo sharedWithUser) {
	if(sharedWithUser != null) {
	    final ConfessionShare confessToWithCondition = new ConfessionShare();
	    confessToWithCondition.setTimeStamp(new Date());
	    confessToWithCondition.setFbId(sharedWithUser.getId());
	    confessToWithCondition.setUserFullName(sharedWithUser.getName());
	    return confessToWithCondition;
	} 
	return null;
    }

    /**
     * Get user friends
     */
    public void populateFriendsList() {
	if(friendsListBox == null) {

	    ConfessionBox.facebookService.getFriends(ConfessionBox.accessToken, new AsyncCallback<String>() {
		@Override
		public void onSuccess(String result) {
		    if(result != null) {
			userfriends = CommonUtils.getFriendsUserInfo(result);
			if(userfriends != null && !userfriends.isEmpty()) {
			    friendsListBox = new FriendsListBox(userfriends);
			    fPnlConfessionShare.add(friendsListBox);
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
}