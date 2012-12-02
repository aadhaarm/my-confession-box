package com.l3.CB.client.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateHPEvent;
import com.l3.CB.client.event.UpdateMenuEvent;
import com.l3.CB.client.ui.widgets.CBTextArea;
import com.l3.CB.client.ui.widgets.CBTextBox;
import com.l3.CB.client.ui.widgets.RelationSuggestBox;
import com.l3.CB.client.ui.widgets.Templates;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.util.HelpInfo;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.UserInfo;

public class RegisterConfessionPresenter implements Presenter {

    public interface Display {
	public boolean isIdentityHidden();
	public boolean isFriendsListNull();
	public boolean isShared();
	public HTML getHtmlConfessionPreview();
	public RelationSuggestBox getRelationSuggestBox();

	public void setFriends(Map<String, UserInfo> userfriends);
	public void setProfilePictureTag(boolean isAnyn, String gender, String fbId);

	/*
	 * Input widgets
	 */
	public CBTextBox getTxtTitle();
	public CBTextArea getTxtConfession();
	public CheckBox getCbHideIdentityWidget();
	public CheckBox getCbConfessToWidget();
	public UserInfo getSharedWith();

	public HasClickHandlers getSubmitBtn();
	public Widget gethFriendSuggestBox();
	public HasClickHandlers getCbHideIdentity();
	public HasClickHandlers getCbConfessTo();

	/*
	 * Draft
	 */
	public String getConfession();
	public String getConfessionTitle();
	public Button getBtnSave();
	public Button getBtnDeleteDraft();
	public void enableDeleteDraftButton(boolean isVisible);

	Widget asWidget();
    }

    private final Display display;
    private Map<String, UserInfo> userfriends;

    public RegisterConfessionPresenter(final Display display) {
	super();
	this.display = display;
	display.setProfilePictureTag(true, ConfessionBox.getLoggedInUserInfo().getGender(), ConfessionBox.getLoggedInUserInfo().getId());
	checkForDraft();
	bind();
    }

    private void checkForDraft() {
	ConfessionBox.confessionService.getConfessionDraft(ConfessionBox.getLoggedInUserInfo().getUserId(), ConfessionBox.getLoggedInUserInfo().getId(), new AsyncCallback<Confession>() {
	    @Override
	    public void onSuccess(Confession result) {
		if(result != null) {
		    display.getTxtTitle().getTxtTitle().setText(result.getConfessionTitle());
		    display.getTxtConfession().getCbTextArea().setText(result.getConfession());
		    display.enableDeleteDraftButton(true);
		    display.getBtnSave().setText(ConfessionBox.cbText.saveDraftButtonText());
		}
	    }
	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("RegisterConfessionPresenter", "onFailure", caught);
	    }
	});
    }

    public RegisterConfessionPresenter(final Display display, Confession confession) {
	super();
	this.display = display;
	display.setProfilePictureTag(confession.isShareAsAnyn(), ConfessionBox.getLoggedInUserInfo().getGender(), ConfessionBox.getLoggedInUserInfo().getId());
	bind();
    }

    /**
     * @param facebookService
     * @param display
     * @param accessToken
     */
    private void getMyFriends() {
	userfriends = CommonUtils.friendsMap;
	if(userfriends != null && !userfriends.isEmpty()) {
	    display.setFriends(userfriends);
	} else {
	    ConfessionBox.facebookService.getFriends(ConfessionBox.accessToken, new AsyncCallback<String>() {
		@Override
		public void onSuccess(String result) {
		    if(result != null) {
			userfriends = CommonUtils.getFriendsUserInfo(result);
			if(userfriends != null) {
			    display.setFriends(userfriends);
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

    /**
     * Bind events
     */
    private void bind() {
	/*
	 * Bind Confession Submit 
	 */
	onConfessionSubmit();

	/*
	 * RelationSuggestBox - Add Selection Handler
	 * 
	 * CbHideIdentity - AddClickHandler
	 * 
	 * CbConfessTo - AddClickHandler
	 */
	onOptionSelection();

	/*
	 * TxtTitle - AddBlurHandler
	 * 
	 * CbTextArea - AddBlurHandler
	 */
	processField();

	/*
	 * BtnSave - AddClickHandler
	 * 
	 * BtnDeleteDraft - AddClickHandler
	 */
	onDraftConfessions();
    }

    /**
     * BtnSave - AddClickHandler
     * 
     * BtnDeleteDraft - AddClickHandler
     */
    private void onDraftConfessions() {
	display.getBtnSave().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if(display.getTxtTitle().validate() && display.getTxtConfession().validate(Constants.CONF_MIN_CHARS, Constants.CONF_MAX_CHARS)) {
		    // Get confession to be saved
		    final Confession confession = new Confession();
		    confession.setConfessionTitle(display.getConfessionTitle());
		    confession.setConfession(display.getConfession());
		    confession.setUserId(ConfessionBox.getLoggedInUserInfo().getUserId());
		    ConfessionBox.confessionService.registerConfessionDraft(confession, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
			    display.enableDeleteDraftButton(true);
			    display.getBtnSave().setText("Saved");
			}

			@Override
			public void onFailure(Throwable caught) {
			    Error.handleError("RegisterConfessionPresenter",
				    "onFailure", caught);
			}
		    });
		}
	    }
	});

	display.getBtnDeleteDraft().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		ConfessionBox.confessionService.clearConfessionDraft(ConfessionBox.getLoggedInUserInfo().getUserId(), ConfessionBox.getLoggedInUserInfo().getId(), new AsyncCallback<Void>() {

		    @Override
		    public void onSuccess(Void result) {
			display.getTxtTitle().getTxtTitle().setText("");
			display.getTxtConfession().getCbTextArea().setText("");
			display.enableDeleteDraftButton(false);
			display.getBtnSave().setText("Save as draft");
		    }

		    @Override
		    public void onFailure(Throwable caught) {
			Error.handleError("RegisterConfessionPresenter", "onFailure", caught);
		    }
		});
	    }
	});
    }

    /**
     * TxtTitle - AddBlurHandler
     * 
     * CbTextArea - AddBlurHandler
     */
    private void processField() {
	display.getTxtTitle().getTxtTitle().addBlurHandler(new BlurHandler() {
	    @Override
	    public void onBlur(BlurEvent event) {
		display.getTxtTitle().validate();
	    }
	});

	display.getTxtConfession().getCbTextArea().addBlurHandler(new BlurHandler() {
	    @Override
	    public void onBlur(BlurEvent event) {
		display.getTxtConfession().validate(Constants.CONF_MIN_CHARS, Constants.CONF_MAX_CHARS);
	    }
	});

	display.getTxtTitle().getTxtTitle().addBlurHandler(new BlurHandler() {
	    @Override
	    public void onBlur(BlurEvent event) {
		display.getTxtTitle().validate();
	    }
	});
	
    }

    /**
     * Make preview description
     */
    private void handlePreview() {
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
     * RelationSuggestBox - Add Selection Handler
     * 
     * CbHideIdentity - AddClickHandler
     * 
     * CbConfessTo - AddClickHandler
     */
    private void onOptionSelection() {

	display.getRelationSuggestBox().getRelationSuggestBox().addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
	    @Override
	    public void onSelection(SelectionEvent<Suggestion> event) {
		handlePreview();
	    }
	});

	display.getCbHideIdentity().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		handlePreview();
		display.setProfilePictureTag(display.isIdentityHidden(), ConfessionBox.getLoggedInUserInfo().getGender(), ConfessionBox.getLoggedInUserInfo().getId());
		HelpInfo.showHelpInfo(HelpInfo.type.REGISTER_CONF_HIDE_ID_CHECKBOX);
	    }
	});

	display.getCbConfessTo().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		handlePreview();
		HelpInfo.showHelpInfo(HelpInfo.type.REGISTER_CONF_SHARE_WITH_CHECKBOX);
		if(display.isShared()) {
		    if(display.isFriendsListNull()) {
			getMyFriends();
		    }
		    if(display.gethFriendSuggestBox() != null) {
			display.gethFriendSuggestBox().setVisible(true);
			display.getRelationSuggestBox().setVisible(true);
		    }
		} else {
		    if(display.gethFriendSuggestBox() != null) {
			display.gethFriendSuggestBox().setVisible(false);
			display.getRelationSuggestBox().setVisible(false);
		    }
		}
	    }
	});
    }

    /**
     * Bind Confession Submit 
     */
    private void onConfessionSubmit() {
	// On Submit confession
	this.display.getSubmitBtn().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {

		if(display.getTxtTitle().validate() && display.getTxtConfession().validate(Constants.CONF_MIN_CHARS, Constants.CONF_MAX_CHARS)) {

		    // Get confession to be saved
		    final Confession confession = getConfessionToBeSaved();

		    if(confession != null) {

			//Register Shared-To info
			if(display.isShared()) {

			    final List<ConfessionShare> lstConfessTo = new ArrayList<ConfessionShare>();
			    UserInfo fbSharedUser = display.getSharedWith();
			    if(fbSharedUser != null && display.getRelationSuggestBox().validate()) {

				// Check if the user confessed to is a member and send a fb notification message
				checkIfUserRegistered(confession);

				final ConfessionShare confessTo = getConfessedShareTO(fbSharedUser);
				if(confessTo != null) {
				    lstConfessTo.add(confessTo);
				    confession.setConfessedTo(lstConfessTo);
				    //Finally register confession
				    finallyRegisterConfession(confession);
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
     * Finally Register Confession
     * 
     * @param confession
     */
    private void finallyRegisterConfession(final Confession confession) {
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

		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED);
		    ConfessionBox.confEventBus.fireEvent(new UpdateMenuEvent());
		    ConfessionBox.confEventBus.fireEvent(new UpdateHPEvent(pointsToBeAdded));
		}

		@Override
		public void onFailure(Throwable caught) {
		    Error.handleError("RegisterConfessionPresenter", "onFailure", caught);
		}
	    }); 
	} 
    }

    /**
     * Get Share to user object
     * 
     * @param sharedWithUser
     * @return
     */
    private ConfessionShare getConfessedShareTO(UserInfo sharedWithUser) {
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
    private Confession getConfessionToBeSaved() {
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

    /**
     * Check is user registered - Show FB send dialog
     * 
     * @param confession
     */
    private void checkIfUserRegistered(Confession confession) {
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
     * Kick start Presenter
     */
    @Override
    public void go(HasWidgets container) {
	RootPanel.get(Constants.DIV_MAIN_CONTENT).clear();
	RootPanel.get(Constants.DIV_MAIN_CONTENT).add(display.asWidget());		
    }
}