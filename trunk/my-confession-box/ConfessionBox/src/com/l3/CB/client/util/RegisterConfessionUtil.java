package com.l3.CB.client.util;

import java.util.Date;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateHPEvent;
import com.l3.CB.client.event.UpdateMenuEvent;
import com.l3.CB.client.presenter.RegisterConfessionPresenter.Display;
import com.l3.CB.client.ui.widgets.Templates;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.UserInfo;

public class RegisterConfessionUtil {

    /**
     * Make preview description
     * @param display - View object
     */
    public static void handlePreview(Display display) {
	String confesee = ConfessionBox.cbText.confessedByAnynName();
	String confessor = ConfessionBox.cbText.confessedToWorld();

	if(display.isIdentityHidden()){
	    confesee = ConfessionBox.cbText.confessedByAnynName();
	} else {
	    confesee = ConfessionBox.getLoggedInUserInfo().getName();
	}

	if(display.isShared()) {
	    if(display.getRelationSuggestBox() != null && display.getRelationSuggestBox().getSelectedRelation() != null) {
		confessor = display.getRelationSuggestBox().getSelectedRelation().getDisplayText();
	    }
	}
	if(display.isShared() && display.getRelationSuggestBox() != null && display.getRelationSuggestBox().getSelectedRelation() != null) {
	    display.getHtmlConfessionPreview().setHTML(Templates.TEMPLATES.confessonPreview(confesee, confessor, CommonUtils.getPronoun(ConfessionBox.loggedInUserInfo.getGender())));
	} else {
	    display.getHtmlConfessionPreview().setHTML(Templates.TEMPLATES.confessonPreview(confesee, confessor, "the"));
	}

	display.getHtmlConfessionPreview().setVisible(true);
    }

    /**
     * Finally Register Confession
     * 
     * @param confession
     */
    public static void finallyRegisterConfession(final Confession confession, final Display display) {
	// Show user confirm message
	if(Window.confirm(ConfessionBox.cbText.confirmMessageWhenSubmittingCOnfession())) {

	    ConfessionBox.confessionService.registerConfession(confession, new AsyncCallback<Void>() {

		// Human points when submitting a confession
		int pointsToBeAdded = Constants.POINTS_ON_CONFESSING;

		@Override
		public void onSuccess(Void result) {
		    if(!display.isIdentityHidden()) {
			pointsToBeAdded += Constants.POINTS_ON_UNHIDING_IDENTITY;
		    }

		    if(display.isShared()) {
			pointsToBeAdded += Constants.POINTS_ON_APPEAL_PARDON;
		    }

		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
		    ConfessionBox.eventBus.fireEvent(new UpdateMenuEvent());
		    ConfessionBox.eventBus.fireEvent(new UpdateHPEvent(pointsToBeAdded));
		}

		@Override
		public void onFailure(Throwable caught) {
		    Error.handleError("RegisterConfessionPresenter", "onFailure", caught);
		}
	    }); 
	} 
    }

    /**
     * Check is user registered - Show FB send dialog
     * 
     * @param confession
     */
    public static void checkIfUserRegistered(Confession confession, Display display) {
	final UserInfo confessedToUser = display.getSharedWith();
	if(confessedToUser != null) {
	    CommonUtils.sendConfessionNotification(confessedToUser.getId(),
		    confessedToUser.getName(),
		    ConfessionBox.getLoggedInUserInfo().getName(),
		    FacebookUtil.getConfessionNotificationUrl(),
		    FacebookUtil.getApplicationImage(),
		    ConfessionBox.cbText.shareConfessionFBWallMessage());
	}
    }

    /**
     * Get Share to user object
     * 
     * @param sharedWithUser
     * @return
     */
    public static ConfessionShare getConfessedShareTO(UserInfo sharedWithUser, Display display) {
	if(sharedWithUser != null) {
	    final ConfessionShare confessToWithCondition = new ConfessionShare();
	    confessToWithCondition.setTimeStamp(new Date());
	    confessToWithCondition.setFbId(sharedWithUser.getId());
	    confessToWithCondition.setUserFullName(sharedWithUser.getName());
	    confessToWithCondition.setRelation(display.getRelationSuggestBox().getSelectedRelation());
	    return confessToWithCondition;
	} 
	return null;
    }

    /**
     * Get confession Object to be sent to server 
     * @return
     */
    public static Confession getConfessionToBeSaved(Display display) {
	if(ConfessionBox.getLoggedInUserInfo() != null) {
	    final Confession confession = new Confession(CommonUtils.checkForNull(display.getConfession()), display.isIdentityHidden());
	    confession.setUserId(ConfessionBox.getLoggedInUserInfo().getUserId());
	    confession.setUserEmailAddress(ConfessionBox.getLoggedInUserInfo().getEmail());
	    confession.setConfessionTitle(CommonUtils.checkForNull(display.getConfessionTitle()));
	    confession.setTimeStamp(new Date());
	    confession.setUsername(ConfessionBox.getLoggedInUserInfo().getUsername());
	    confession.setUserFullName(ConfessionBox.getLoggedInUserInfo().getName());
	    confession.setLocale(ConfessionBox.getLoggedInUserInfo().getLocale());
	    return confession;
	}
	return null;
    }
}