package com.l3.CB.client.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.FacebookServiceAsync;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
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
		public String getCaptchaField();
		public boolean isIdentityHidden();
		public boolean isFriendsOracleNull();
		public void setFriendsOracle(MultiWordSuggestOracle friendsOracle);
		public void setProfilePictureTag(boolean isAnyn, String gender, String fbId);
		public void reloadCaptchaImage(String uId);
		public void setCaptchaHTMLCode(String captchaHTMLCode);
		boolean isShared();
		Widget asWidget();
	}

	@SuppressWarnings("unused")
	private final HandlerManager eventBus;
	private final ConfessionServiceAsync confessionService; 
	private final UserInfo userInfo;
	private final Display display;
	private final MultiWordSuggestOracle friendsOracle;
	private Map<String, UserInfo> userfriends;
	private FacebookServiceAsync facebookService;
	private String accessToken;
	
	public RegisterConfessionPresenter(HandlerManager eventBus,
			ConfessionServiceAsync rpcService, FacebookServiceAsync facebookService, UserInfo userInfo,
			final Display display, String accessToken) {
		super();
		this.eventBus = eventBus;
		this.confessionService = rpcService;
		this.userInfo = userInfo;
		this.display = display;
		this.accessToken = accessToken;
		this.facebookService = facebookService;
		display.setProfilePictureTag(true, userInfo.getGender(), userInfo.getId());
		friendsOracle = new MultiWordSuggestOracle();
		display.reloadCaptchaImage(Integer.toString(new Random().nextInt()));
		
		confessionService.getCaptchaString(new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if(result != null) {
					display.setCaptchaHTMLCode(result);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				Error.handleError("RegisterConfessionPresenter", "onFailure", caught);
			}
		});
		
		bind();
	}

	/**
	 * @param facebookService
	 * @param display
	 * @param accessToken
	 */
	private void getMyFriends() {
		facebookService.getFriends(accessToken, new AsyncCallback<String>() {
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
				// Get confession to be saved
				final Confession confession = getConfessionToBeSaved();
				if(confession != null) {
					//Register Shared-To info
					if(display.isShared()) {
						final List<ConfessionShare> confessedTo = new ArrayList<ConfessionShare>();
						String fbIdSharedUser = userfriends.get(display.getSharedWith()).getId();
						if(fbIdSharedUser != null) {
							final ConfessionShare confessToWithCondition = getConfessedToWithConditions(fbIdSharedUser);
							if(confessToWithCondition != null) {
								facebookService.getUserDetails(fbIdSharedUser, accessToken, new AsyncCallback<String>() {
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
										Error.handleError("RegisterConfessionPresenter", "bind", caught);
									}
								});
							}
						}
					} else  {
						//Finally register confession
						finallyRegisterConfession(confession);
					}
				}
			}

		});
		
		display.getCbHideIdentity().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				display.setProfilePictureTag(display.isIdentityHidden(), userInfo.getGender(), userInfo.getId());
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
		confessionService.registerConfession(confession, display.getCaptchaField(), new AsyncCallback<Confession>() {
			@Override
			public void onSuccess(Confession result) {
				History.newItem(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
			}
			@Override
			public void onFailure(Throwable caught) {
				Error.handleError("RegisterConfessionPresenter", "onFailure", caught);
			}
		});
	}

	/**
 	 * @param fbIdSharedUser
	 * @return
	 */
	private ConfessionShare getConfessedToWithConditions(String fbIdSharedUser) {
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
		if(userInfo != null) {
			final Confession confession = new Confession(CommonUtils.checkForNull(display.getConfession()), display.isIdentityHidden());
			confession.setUserId(userInfo.getUserId());
			confession.setConfessionTitle(CommonUtils.checkForNull(display.getConfessionTitle()));
			confession.setTimeStamp(new Date());
			confession.setUsername(userInfo.getUsername());
			confession.setUserFullName(userInfo.getName());
			confession.setLocale(userInfo.getLocale());
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
