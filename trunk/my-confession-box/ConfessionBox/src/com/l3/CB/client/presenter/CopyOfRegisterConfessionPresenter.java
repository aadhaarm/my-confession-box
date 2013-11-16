package com.l3.CB.client.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateHPEvent;
import com.l3.CB.client.event.UpdateMenuEvent;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.view.misc.CBButton;
import com.l3.CB.client.view.misc.CBTextAreaUI;
import com.l3.CB.client.view.misc.CBTextBoxUI;
import com.l3.CB.client.view.misc.FriendsSuggestBoxUI;
import com.l3.CB.client.view.misc.RelationSuggestBoxUI;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.UserInfo;

public class CopyOfRegisterConfessionPresenter implements Presenter {

    public interface Display {
	Widget asWidget();
	public CBButton getBtnHideIdentity();
	public CBButton getBtnDedicateConfession();
	public CBButton getBtnRequestPardon();
	public FriendsSuggestBoxUI getFriendsSuggestBox();
	public RelationSuggestBoxUI getRelationSuggestBox();
	public CBTextBoxUI getTxtTitle();
	public CBTextAreaUI getTxtConfession();
	public HasClickHandlers getBtnSubmit();
	public boolean isFriendNotRegistered();
    }

    private final Display display;

    /**
     * Constructor
     * @param display
     */
    public CopyOfRegisterConfessionPresenter(final Display display) {
	super();
	this.display = display;
	
	// Check for draft on load
	checkForDraft();
	
	// Save confessions draft
	saveTimer.schedule(5000);
	
	bind();
    }

    Timer saveTimer = new Timer() {
	@Override
	public void run() {
	    saveConfessionDraft();
	}
    };

    private void bind() {
	display.getBtnSubmit().addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {


		if(display.getTxtTitle().validate() && display.getTxtConfession().validate(Constants.CONF_MIN_CHARS, Constants.CONF_MAX_CHARS)) {

		    // Get confession to be saved
		    final Confession confession = getConfessionToBeSaved();

		    if(confession != null) {

			//Register Shared-To info
			if(display.getBtnDedicateConfession().getValue()) {

			    if(display.getBtnRequestPardon().getValue()) {
				confession.setOnlyDedicate(false);
			    } else {
				confession.setOnlyDedicate(true);
			    }

			    if(display.getFriendsSuggestBox().validate() && display.getRelationSuggestBox().validate()) {
				final List<ConfessionShare> lstConfessTo = new ArrayList<ConfessionShare>();
				UserInfo fbSharedUser = display.getFriendsSuggestBox().getSelectedUser();
				if(fbSharedUser != null && display.getRelationSuggestBox().validate()) {

				    final ConfessionShare confessTo = getConfessedShareTO(fbSharedUser);
				    if(confessTo != null) {
					lstConfessTo.add(confessTo);
					confession.setConfessedTo(lstConfessTo);
					//Finally register confession
					finallyRegisterConfession(confession);
				    }
				}
			    }
			} else  {
			    //Finally register confession
			    finallyRegisterConfession(confession);
			}
		    }
		}

	    }
	});
    }

    /**
     * Get confession Object to be sent to server 
     * @return
     */
    public Confession getConfessionToBeSaved() {
	if(ConfessionBox.getLoggedInUserInfo() != null) {
	    final Confession confession = new Confession(CommonUtils.checkForNull(display.getTxtConfession().getValue()), 
		    display.getBtnHideIdentity().getValue());
	    confession.setUserId(ConfessionBox.getLoggedInUserInfo().getUserId());
	    confession.setUserEmailAddress(ConfessionBox.getLoggedInUserInfo().getEmail());
	    confession.setConfessionTitle(CommonUtils.checkForNull(display.getTxtTitle().getValue()));
	    confession.setTimeStamp(new Date());
	    confession.setUsername(ConfessionBox.getLoggedInUserInfo().getUsername());
	    confession.setUserFullName(ConfessionBox.getLoggedInUserInfo().getName());
	    confession.setLocale(ConfessionBox.getLoggedInUserInfo().getLocale());
	    return confession;
	}
	return null;
    }

    /**
     * Get Share to user object
     * 
     * @param sharedWithUser
     * @return
     */
    public ConfessionShare getConfessedShareTO(UserInfo sharedWithUser) {
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
     * Finally Register Confession
     * 
     * @param confession
     */
    public void finallyRegisterConfession(final Confession confession) {
	ConfessionBox.confessionService.registerConfession(confession, new AsyncCallback<Void>() {

	    // Human points when submitting a confession
	    int pointsToBeAdded = Constants.POINTS_ON_CONFESSING;

	    @Override
	    public void onSuccess(Void result) {
		if(display.isFriendNotRegistered()) {
		    final UserInfo confessedToUser = display.getFriendsSuggestBox().getSelectedUser();
		    openConfessionNotificationMessageDialog(confessedToUser);
		}

		if(!display.getBtnHideIdentity().getValue()) {
		    pointsToBeAdded += Constants.POINTS_ON_UNHIDING_IDENTITY;
		}

		if(display.getBtnDedicateConfession().getValue()) {
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

    /**
     * @param confessedToUser
     */
    private static void openConfessionNotificationMessageDialog(final UserInfo confessedToUser) {
	CommonUtils.sendConfessionNotification(confessedToUser.getId(),
		confessedToUser.getName(),
		ConfessionBox.getLoggedInUserInfo().getName(),
		FacebookUtil.getConfessionNotificationUrl(),
		FacebookUtil.getApplicationImage(),
		ConfessionBox.cbText.shareConfessionFBWallMessage());
    }

    /**
     * Kick start Presenter
     */
    @Override
    public void go(HasWidgets container) {
	RootPanel.get(Constants.DIV_MAIN_CONTENT).clear();
	RootPanel.get(Constants.DIV_MAIN_CONTENT).add(display.asWidget());		
    }

    /**
     * Save confession
     */
    private void saveConfessionDraft() {
	// Get confession to be saved
	final Confession confession = getConfessionToBeSaved();
	confession.setUserId(ConfessionBox.getLoggedInUserInfo().getUserId());

	confession.setShareAsAnyn(display.getBtnHideIdentity().getValue());

	if(display.getBtnDedicateConfession() != null && display.getFriendsSuggestBox().getSelectedUser() != null) {
	    confession.setShareToUserIDForSave(display.getFriendsSuggestBox().getSelectedUser().getId());
	}

	if(display.getBtnDedicateConfession() != null && display.getRelationSuggestBox().getSelectedRelation() != null) {
	    confession.setShareToRelationForSave(display.getRelationSuggestBox().getSelectedRelation().getDisplayText());
	}

	ConfessionBox.confessionService.registerConfessionDraft(confession, new AsyncCallback<Void>() {
	    @Override
	    public void onSuccess(Void result) {
		saveTimer.schedule(5000);
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("RegisterConfessionPresenter", "onFailure", caught);
	    }
	});
    }
    
    /**
     * Check for draft on-load
     */
    private void checkForDraft() {
	ConfessionBox.confessionService.getConfessionDraft(ConfessionBox.getLoggedInUserInfo().getUserId(), ConfessionBox.getLoggedInUserInfo().getId(), new AsyncCallback<Confession>() {
	    @Override
	    public void onSuccess(Confession result) {
		if(result != null) {
		    
		    display.getTxtTitle().setText(result.getConfessionTitle());
		    display.getTxtTitle().validate();

		    display.getTxtConfession().setText(result.getConfession());
		    display.getTxtConfession().validate(Constants.CONF_MIN_CHARS, Constants.CONF_MAX_CHARS);
		}
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("RegisterConfessionPresenter", "onFailure", caught);
	    }
	});
    }
}