package com.l3.CB.client.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.UserInfo;

public class EditConfessionPresenter implements Presenter {

    public interface Display {
	public HasClickHandlers getSubmitBtn();
	public HasClickHandlers getCbHideIdentity();
	public HasClickHandlers getCbConfessTo();
	public String getConfession();
	public String getConfessionTitle();
	boolean isShared();
	public boolean isIdentityHidden();
	public boolean isFriendsOracleNull();
	public HorizontalPanel gethPanelShare();
	public String getSharedWith();
	public void setFriendsOracle(MultiWordSuggestOracle friendsOracle);
	public void setProfilePictureTag(boolean isAnyn, String gender, String fbId);
	public void setCaptchaHTMLCode(String captchaHTMLCode);
	public TextBox getTxtTitle();
	public RichTextArea getTxtConfession();
	public CheckBox getCbHideIdentityWidget();
	public CheckBox getCbConfessToWidget();
	Widget asWidget();
    }

    private final Display display;
    private final MultiWordSuggestOracle friendsOracle;
    private Map<String, UserInfo> userfriends;


    public EditConfessionPresenter(final Display display, Long confId) {
	super();
	this.display = display;
	display.setProfilePictureTag(true, ConfessionBox.loggedInUserInfo.getGender(), ConfessionBox.loggedInUserInfo.getId());
	friendsOracle = new MultiWordSuggestOracle();

	populateConfessionToBeEdited(confId);
	bind();
    }

    private void populateConfessionToBeEdited(Long confId) {
	ConfessionBox.confessionService.getConfession(confId, new AsyncCallback<Confession>() {
	    @Override
	    public void onSuccess(Confession result) {

	    }
	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("EditConfessionPresenter", "onFailure", caught);
	    }
	});
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
		Error.handleError("EditConfessionPresenter", "onFailure", caught);
	    }
	});
    }

    private void bind() {
	this.display.getSubmitBtn().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		// Get confession to be saved
		final Confession confession = getConfessionToBeSaved();
		//Register Shared-To info
		if(display.isShared()) {
		    final List<ConfessionShare> confessedTo = new ArrayList<ConfessionShare>();
		    String fbIdSharedUser = userfriends.get(display.getSharedWith()).getId();
		    final ConfessionShare confessToWithCondition = getConfessedToWithConditions(fbIdSharedUser);

		    ConfessionBox.facebookService.getUserDetails(fbIdSharedUser, ConfessionBox.accessToken, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
			    UserInfo confessedToUser = CommonUtils.getUserInfo(result);
			    if(confessedToUser != null) {
				confessToWithCondition.setUserFullName(confessedToUser.getName());
				confessToWithCondition.setUsername(confessedToUser.getUsername());
				confessedTo.add(confessToWithCondition);
				confession.setConfessedTo(confessedTo);
				//Finally register confession
				finallyRegisterConfession(confession);
			    }
			}

			@Override
			public void onFailure(Throwable caught) {
			    Error.handleError("EditConfessionPresenter",
				    "onFailure", caught);
			}
		    });
		} else  {
		    //Finally register confession
		    finallyRegisterConfession(confession);
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

    }

    /**
     * @param confession
     */
    private void finallyRegisterConfession(final Confession confession) {
	//		confessionService.registerConfession(confession, new AsyncCallback<Confession>() {
	//			@Override
	//			public void onSuccess(Confession result) {
	//				History.newItem(Constants.HISTORY_ITEM_CONFESSION_FEED);
	//			}
	//			@Override
	//			public void onFailure(Throwable caught) {
	//				logger.log(Constants.LOG_LEVEL, "Exception when registering confession:" + caught.getMessage());
	//			}
	//		});
    }

    /**
     * @param fbIdSharedUser
     * @return
     */
    private ConfessionShare getConfessedToWithConditions(
	    String fbIdSharedUser) {
	final ConfessionShare confessToWithCondition = new ConfessionShare();

	confessToWithCondition.setTimeStamp(new Date());
	confessToWithCondition.setFbId(fbIdSharedUser);
	confessToWithCondition.setUserFullName(userfriends.get(display.getSharedWith()).getName());
	return confessToWithCondition;
    }

    /**
     * @return
     */
    private Confession getConfessionToBeSaved() {
	final Confession confession = new Confession(display.getConfession(), display.isIdentityHidden());
//	confession.setUserId(userInfo.getUserId());
//	confession.setConfessionTitle(display.getConfessionTitle());
//	confession.setTimeStamp(new Date());
//	confession.setUsername(userInfo.getUsername());
//	confession.setUserFullName(userInfo.getName());
//	confession.setLocale(userInfo.getLocale());
	return confession;
    }

    @Override
    public void go(HasWidgets container) {
	RootPanel.get(Constants.DIV_MAIN_CONTENT).clear();
	RootPanel.get(Constants.DIV_MAIN_CONTENT).add(display.asWidget());		
    }

}
