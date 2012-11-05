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
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
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
	public HorizontalPanel gethPanelShare();
	public String getSharedWith();
	public boolean isIdentityHidden();
	public boolean isFriendsOracleNull();
	public void setFriendsOracle(MultiWordSuggestOracle friendsOracle);
	public void setProfilePictureTag(boolean isAnyn, String gender, String fbId);
	boolean isShared();
	public CBTextBox getTxtTitle();
	public CBTextArea getTxtConfession();
	public CheckBox getCbHideIdentityWidget();
	public CheckBox getCbConfessToWidget();
	Widget asWidget();
    }

    private final Display display;
    private final MultiWordSuggestOracle friendsOracle;
    private Map<String, UserInfo> userfriends;

    public RegisterConfessionPresenter(final Display display) {
	super();
	this.display = display;
	display.setProfilePictureTag(true, ConfessionBox.loggedInUserInfo.getGender(), ConfessionBox.loggedInUserInfo.getId());
	friendsOracle = new MultiWordSuggestOracle();

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
		    if(userfriends != null && !userfriends.isEmpty()) {
			//iterating over keys only
			for (String friendsName : userfriends.keySet()) {
			    if(friendsName != null) {
				friendsOracle.add(friendsName);
			    }
			}
			display.setFriendsOracle(friendsOracle);
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
	this.display.getSubmitBtn().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if(display.getTxtTitle().validate() && display.getTxtConfession().validate()) {

		    // Get confession to be saved
		    final Confession confession = getConfessionToBeSaved();

		    if(confession != null) {

			//Register Shared-To info
			if(display.isShared()) {
			    final List<ConfessionShare> lstConfessTo = new ArrayList<ConfessionShare>();
			    UserInfo fbSharedUser = userfriends.get(display.getSharedWith());
			    if(fbSharedUser != null) {
				final ConfessionShare confessTo = getConfessedShareTO(fbSharedUser.getId());
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
		    if(display.isFriendsOracleNull()) {
			getMyFriends();
		    }
		    display.gethPanelShare().setVisible(true);
		} else {
		    display.gethPanelShare().setVisible(false);
		}
	    }
	});

	display.getTxtTitle().addBlurHandler(new BlurHandler() {

	    @Override
	    public void onBlur(BlurEvent event) {
		display.getTxtTitle().validate();
	    }
	});

	display.getTxtConfession().addBlurHandler(new BlurHandler() {

	    @Override
	    public void onBlur(BlurEvent event) {
		display.getTxtConfession().validate();
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
    }

    /**
     * @param confession
     */
    private void finallyRegisterConfession(final Confession confession) {
	if(Window.confirm("Your confession is now being submitted. Press OK if you want to proceed with submitting the confession or press 'Cancel' if you want to recheck and be sure about what you confession.")) {
	    ConfessionBox.confessionService.registerConfession(confession, new AsyncCallback<Confession>() {
		@Override
		public void onSuccess(Confession result) {
		    History.newItem(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
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
     * @param fbIdSharedUser
     * @return
     */
    private ConfessionShare getConfessedShareTO(String fbIdSharedUser) {
	if(fbIdSharedUser != null) {
	    final ConfessionShare confessToWithCondition = new ConfessionShare();
	    confessToWithCondition.setTimeStamp(new Date());
	    confessToWithCondition.setFbId(fbIdSharedUser);
	    confessToWithCondition.setUserFullName(userfriends.get(display.getSharedWith()).getName());
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

    @Override
    public void go(HasWidgets container) {
	RootPanel.get(Constants.DIV_MAIN_CONTENT).clear();
	RootPanel.get(Constants.DIV_MAIN_CONTENT).add(display.asWidget());		
    }
}