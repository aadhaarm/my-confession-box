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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateMenuEvent;
import com.l3.CB.client.ui.widgets.CBTextArea;
import com.l3.CB.client.ui.widgets.CBTextBox;
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
	public HasClickHandlers getSubmitBtn();
	public String getConfession();
	public String getConfessionTitle();
	public HasClickHandlers getCbHideIdentity();
	public HasClickHandlers getCbConfessTo();
	public Widget gethFriendSuggestBox();

	public boolean isIdentityHidden();
	public boolean isFriendsListNull();
	public UserInfo getSharedWith();
	public void setFriends(Map<String, UserInfo> userfriends);
	boolean isShared();

	public void setProfilePictureTag(boolean isAnyn, String gender, String fbId);
	public CBTextBox getTxtTitle();
	public CBTextArea getTxtConfession();
	public CheckBox getCbHideIdentityWidget();
	public CheckBox getCbConfessToWidget();

	public Button getBtnSave();
	
	Widget asWidget();
    }

    private final Display display;
    private Map<String, UserInfo> userfriends;
    private Confession confession = null;

    public RegisterConfessionPresenter(final Display display) {
	super();
	this.display = display;
	display.setProfilePictureTag(true, ConfessionBox.loggedInUserInfo.getGender(), ConfessionBox.loggedInUserInfo.getId());
	checkForDraft();
	bind();
    }

    private void checkForDraft() {
	ConfessionBox.confessionService.getConfessionDraft(ConfessionBox.loggedInUserInfo.getUserId(), ConfessionBox.loggedInUserInfo.getId(), new AsyncCallback<Confession>() {
	    @Override
	    public void onSuccess(Confession result) {
		if(result != null) {
		    display.getTxtTitle().getTxtTitle().setText(result.getConfessionTitle());
		    display.getTxtConfession().getCbTextArea().setText(result.getConfession());
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
	this.confession = confession;
	//	setupConfessionForEdit();
	display.setProfilePictureTag(confession.isShareAsAnyn(), ConfessionBox.loggedInUserInfo.getGender(), ConfessionBox.loggedInUserInfo.getId());
	bind();
    }

    /**
     * @param facebookService
     * @param display
     * @param accessToken
     */
    private void getMyFriends() {
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

    private void bind() {
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
			    // Check if the user confessed to is a member and send a fb notification message
			    checkIfUserRegistered(confession);

			    final List<ConfessionShare> lstConfessTo = new ArrayList<ConfessionShare>();
			    UserInfo fbSharedUser = display.getSharedWith();
			    if(fbSharedUser != null) {
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

	display.getCbHideIdentity().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		display.setProfilePictureTag(display.isIdentityHidden(), ConfessionBox.loggedInUserInfo.getGender(), ConfessionBox.loggedInUserInfo.getId());
	    }
	});

	display.getCbConfessTo().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if(display.isShared()) {
		    if(display.isFriendsListNull()) {
			getMyFriends();
		    }
		    if(display.gethFriendSuggestBox() != null) {
			display.gethFriendSuggestBox().setVisible(true);
		    }
		} else {
		    if(display.gethFriendSuggestBox() != null) {
			display.gethFriendSuggestBox().setVisible(false);
		    }
		}
	    }
	});

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

	display.getCbHideIdentity().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		HelpInfo.showHelpInfo(HelpInfo.type.REGISTER_CONF_HIDE_ID_CHECKBOX);
	    }
	});

	display.getCbConfessToWidget().addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		HelpInfo.showHelpInfo(HelpInfo.type.REGISTER_CONF_SHARE_WITH_CHECKBOX);
	    }
	});
	
	display.getBtnSave().addClickHandler(new ClickHandler() {
	    
	    @Override
	    public void onClick(ClickEvent event) {
		if(display.getTxtTitle().validate() && display.getTxtConfession().validate(Constants.CONF_MIN_CHARS, Constants.CONF_MAX_CHARS)) {
		    // Get confession to be saved
		    final Confession confession = new Confession();
		    confession.setConfessionTitle(display.getConfessionTitle());
		    confession.setConfession(display.getConfession());
		    confession.setUserId(ConfessionBox.loggedInUserInfo.getUserId());
		    ConfessionBox.confessionService.registerConfessionDraft(confession, new AsyncCallback<Void>() {
		        
		        @Override
		        public void onSuccess(Void result) {
		    	// TODO Auto-generated method stub
		    	
		        }
		        
		        @Override
		        public void onFailure(Throwable caught) {
		    	// TODO Auto-generated method stub
		    	
		        }
		    });
		}
	    }
	});

    }

    /**
     * @param confession
     */
    private void finallyRegisterConfession(final Confession confession) {
	if(Window.confirm("Your confession is now being submitted. Press OK if you want to proceed with submitting the confession or press 'Cancel' if you want to recheck and be sure about what you confession.")) {
	    ConfessionBox.confessionService.registerConfession(confession, new AsyncCallback<Void>() {
		@Override
		public void onSuccess(Void result) {
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
		    ConfessionBox.confEventBus.fireEvent(new UpdateMenuEvent());
		}

		@Override
		public void onFailure(Throwable caught) {
		    Error.handleError("RegisterConfessionPresenter", "onFailure", caught);
		}
	    }); 
	} 
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
     * @return
     */
    private Confession getConfessionToBeSaved() {
	if(ConfessionBox.loggedInUserInfo != null) {
	    final Confession confession = new Confession(CommonUtils.checkForNull(display.getConfession()), display.isIdentityHidden());
	    confession.setUserId(ConfessionBox.loggedInUserInfo.getUserId());
	    confession.setUserEmailAddress(ConfessionBox.loggedInUserInfo.getEmail());
	    confession.setConfessionTitle(CommonUtils.checkForNull(display.getConfessionTitle()));
	    confession.setTimeStamp(new Date());
	    confession.setUsername(ConfessionBox.loggedInUserInfo.getUsername());
	    confession.setUserFullName(ConfessionBox.loggedInUserInfo.getName());
	    confession.setLocale(ConfessionBox.loggedInUserInfo.getLocale());
	    return confession;
	}
	return null;
    }

    private void checkIfUserRegistered(Confession confession) {
	final UserInfo confessedToUser = display.getSharedWith();
	if(confessedToUser != null) {
	    CommonUtils.sendConfessionNotification(confessedToUser.getId(),
		    confessedToUser.getName(),
		    ConfessionBox.loggedInUserInfo.getName(),
		    FacebookUtil.getConfessionNotificationUrl(),
		    FacebookUtil.getApplicationImage(),
		    ConfessionBox.cbText.shareConfessionFBWallMessage());
	}
    }

    @Override
    public void go(HasWidgets container) {
	RootPanel.get(Constants.DIV_MAIN_CONTENT).clear();
	RootPanel.get(Constants.DIV_MAIN_CONTENT).add(display.asWidget());		
    }
}