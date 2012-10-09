package com.l3.CB.client.presenter;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.FacebookServiceAsync;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class RegisterConfessionPresenter implements Presenter {

	Logger logger = Logger.getLogger("CBLogger");

	public interface Display {
		HasClickHandlers getSubmitBtn();
		String getConfession();
		boolean isAnynShare();
		Widget asWidget();
		public void setFriendsOracle(MultiWordSuggestOracle friendsOracle);
		public RadioButton getRbIdentityDiscloseToSome();
		public HorizontalPanel gethPanelShare();
		public boolean isFriendsOracleNull();
		public RadioButton getRbIdentityAnyn();
		public RadioButton getRbIdentityDisclose();
	}

	@SuppressWarnings("unused")
	private final HandlerManager eventBus;
	private final ConfessionServiceAsync rpcService; 
	private UserInfo userInfo;
	private final Display display;
	private MultiWordSuggestOracle friendsOracle;
	@SuppressWarnings("unused")
	private List<UserInfo> userfriends;
	private FacebookServiceAsync facebookService;
	private String accessToken;

	
	public RegisterConfessionPresenter(HandlerManager eventBus,
			ConfessionServiceAsync rpcService, FacebookServiceAsync facebookService, UserInfo userInfo,
			final Display display, String accessToken) {
		super();
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		this.userInfo = userInfo;
		this.display = display;
		this.accessToken = accessToken;
		this.facebookService = facebookService;
		
		friendsOracle = new MultiWordSuggestOracle();
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
				logger.log(Constants.LOG_LEVEL,
						"In RegisterConfessionPresenter.onSuccess():"
								+ result);
				if(result != null) {
					List<UserInfo> friends = CommonUtils.getFriendsUserInfo(result);
					if(friends != null) {
						userfriends = friends;
						for (UserInfo userInfo : friends) {
							friendsOracle.add(userInfo.getName());
						}
						display.setFriendsOracle(friendsOracle);
					}
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Constants.LOG_LEVEL,
						"Exception in RegisterConfessionPresenter.onFailure()"
								+ caught.getCause());
			}
		});
	}

	private void bind() {
		this.display.getSubmitBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Confession confession = new Confession(display.getConfession(), display.isAnynShare());
				confession.setUserId(userInfo.getUserId());
				confession.setTimeStamp(new Date());
				rpcService.registerConfession(confession, new AsyncCallback<Confession>() {

					@Override
					public void onSuccess(Confession result) {
						History.newItem(Constants.HISTORY_ITEM_CONFESSION_FEED);
					}

					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, "Exception when registering confession:" + caught.getMessage());
					}
				});
			}
		});
		
		this.display.getRbIdentityDiscloseToSome().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(display.getRbIdentityDiscloseToSome().isEnabled()) {
					if(display.isFriendsOracleNull()) {
						getMyFriends();
					}
					display.gethPanelShare().setVisible(true);
				}
			}
		});
		this.display.getRbIdentityAnyn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				display.gethPanelShare().setVisible(false);
			}
		});
		this.display.getRbIdentityDisclose().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				display.gethPanelShare().setVisible(false);
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		RootPanel.get(Constants.DIV_MAIN_CONTENT).clear();
		RootPanel.get(Constants.DIV_MAIN_CONTENT).add(display.asWidget());		
	}

}
