package com.l3.CB.client.ui.widgets;

import java.awt.Label;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateHPEvent;
import com.l3.CB.client.event.UpdateIdentityVisibilityEvent;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.PardonStatus;
import com.l3.CB.shared.TO.UserInfo;


public class AppealPardonWidget extends FlowPanel {

    private Map<String, UserInfo> userfriends;

    private final Button btnSubmit; 
    private FriendsSuggestBox friendsSuggestBox;
    private RelationSuggestBox relationSuggestBox;
    private final Confession confession;
    private Boolean isPardonerUserOnCB;


    public AppealPardonWidget(Confession confession) {
	super();
	this.confession = confession;
	addStyleName(Constants.STYLE_CLASS_PARDON_MODAL);

	btnSubmit = new Button("Proceed");
	btnSubmit.setStyleName("appealButton");

	if(friendsSuggestBox != null) {
	    add(friendsSuggestBox);
	    add(relationSuggestBox);
	    add(btnSubmit);
	} else {
	    populateFriendsList();
	}
	bind(confession.getConfId());
    }

    private void bind(final Long confId) {
	btnSubmit.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(isPardonerUserOnCB == null) {
		    final UserInfo selectedUser = friendsSuggestBox.getSelectedUser();
		    if(selectedUser != null) {
			ConfessionBox.confessionService.isUserRegistered(selectedUser.getId(), new AsyncCallback<UserInfo>() {

			    @Override
			    public void onSuccess(UserInfo result) {
				isPardonerUserOnCB = (result == null);
				setupConfirmPanel(isPardonerUserOnCB, selectedUser.getName());
			    }

			    @Override
			    public void onFailure(Throwable caught) {
				Error.handleError("RegisterConfessionUtil", "onFailure", caught);
			    }
			});
		    }
		} else {
		    submitConfessionPardonRequest(confId);
		}
	    }

	    /**
	     * @param confId
	     */
	    private void submitConfessionPardonRequest(final Long confId) {
		if(friendsSuggestBox != null && friendsSuggestBox.validate() && relationSuggestBox.validate()) {

		    UserInfo selectedUser = friendsSuggestBox.getSelectedUser();
		    if(selectedUser != null) {
			// Open a fb notification message dialog
			CommonUtils.sendConfessionNotification(
				selectedUser.getId(), selectedUser.getName(),
				ConfessionBox.getLoggedInUserInfo().getName(),
				FacebookUtil.getConfessionNotificationUrl(),
				FacebookUtil.getApplicationImage(),
				ConfessionBox.cbText.shareConfessionFBWallMessage());

			final ConfessionShare confessTo = getConfessedShareTO(selectedUser);

			if(confessTo != null) {
			    ConfessionBox.confessionService.createConfessedToUser(confId, ConfessionBox.getLoggedInUserInfo().getUserId(),
				    ConfessionBox.getLoggedInUserInfo().getId(), confessTo, new Date(),new AsyncCallback<Void>() {
				@Override
				public void onSuccess(Void result) {
				    List<ConfessionShare> aa = new ArrayList<ConfessionShare>();
				    confessTo.setPardonStatus(PardonStatus.AWAITING_PARDON);
				    aa.add(confessTo);
				    confession.setConfessedTo(aa);
				    ConfessionBox.eventBus.fireEvent(new UpdateIdentityVisibilityEvent(confession));
				    ConfessionBox.eventBus.fireEvent(new UpdateHPEvent(Constants.POINTS_ON_APPEAL_PARDON));
				    setVisible(false);
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

     public void setupConfirmPanel(boolean isFriendNotRegistered, String pardonerName) {
	 remove(btnSubmit);
	 if(isFriendNotRegistered) {
	     add(new HTML(Templates.TEMPLATES.confessorNotRegisteredMessage(pardonerName)));
	 }
	 add(new HTML("Press 'REQUEST' to send a pardon request now."));
	 btnSubmit.setText(ConfessionBox.cbText.shareConfessionButtonShareConfessionPopup());
	 add(btnSubmit);
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
	    confessToWithCondition.setRelation(relationSuggestBox.getSelectedRelation());
	    return confessToWithCondition;
	} 
	return null;
    }

    /**
     * Get user friends
     */
    public void populateFriendsList() {
	if(friendsSuggestBox == null) {
	    relationSuggestBox = new RelationSuggestBox();
	    userfriends = CommonUtils.friendsMap;
	    if(userfriends != null && !userfriends.isEmpty()) {
		friendsSuggestBox = new FriendsSuggestBox(userfriends);
		friendsSuggestBox.setStyleName("friendsSuggestBoxMyConfPage");
		add(friendsSuggestBox);
		add(relationSuggestBox);
		add(btnSubmit);
	    } else {
		ConfessionBox.facebookService.getFriends(ConfessionBox.accessToken, new AsyncCallback<String>() {
		    @Override
		    public void onSuccess(String result) {
			if(result != null) {
			    userfriends = CommonUtils.getFriendsUserInfo(result);
			    if(userfriends != null && !userfriends.isEmpty()) {
				friendsSuggestBox = new FriendsSuggestBox(userfriends);
				friendsSuggestBox.setStyleName("friendsSuggestBoxMyConfPage");
				add(friendsSuggestBox);
				add(relationSuggestBox);
				add(btnSubmit);
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
}