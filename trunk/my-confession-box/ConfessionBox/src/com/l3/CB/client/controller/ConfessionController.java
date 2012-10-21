package com.l3.CB.client.controller;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.FacebookServiceAsync;
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.client.presenter.ConfessionForMeFeedPresenter;
import com.l3.CB.client.presenter.MenuPresenter;
import com.l3.CB.client.presenter.MyConfessionFeedPresenter;
import com.l3.CB.client.presenter.Presenter;
import com.l3.CB.client.presenter.RegisterConfessionPresenter;
import com.l3.CB.client.view.ConfessionFeedView;
import com.l3.CB.client.view.MenuView;
import com.l3.CB.client.view.RegisterConfessionView;
import com.l3.CB.shared.CBText;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionController implements Presenter, ValueChangeHandler<String> {
	
	private final HandlerManager eventBus;
	private final ConfessionServiceAsync confessionService; 
	private static UserInfo loggesInUserInfo;
	private HasWidgets container;
	private String accessToken;
	private FacebookServiceAsync facebookService;
	private CBText cbText;	

	public ConfessionController(HandlerManager eventBus,
			ConfessionServiceAsync rpcService,
			FacebookServiceAsync facebookService, UserInfo userInfo, String confId, String accessToken, CBText cbText) {
		super();
		this.eventBus = eventBus;
		this.confessionService = rpcService;
		ConfessionController.loggesInUserInfo = userInfo;
		this.accessToken = accessToken;
		this.facebookService = facebookService;
		this.cbText = cbText;
		rpcService.registerUser(userInfo, new AsyncCallback<UserInfo>() {
			
			@Override
			public void onSuccess(UserInfo result) {
				ConfessionController.loggesInUserInfo = result;
				
				if(null != ConfessionBox.confId) {
					History.newItem(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID);
				} else {
					History.newItem(Constants.HISTORY_ITEM_CONFESSION_FEED);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();

		if (token != null) {
			Presenter presenter = null;
			if (token.equals(Constants.HISTORY_ITEM_REGISTER_CONFESSION)) {
				presenter = new RegisterConfessionPresenter(eventBus, confessionService, facebookService, loggesInUserInfo, new RegisterConfessionView(cbText, loggesInUserInfo), accessToken);
			} else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FEED)) {
				presenter = new ConfessionFeedPresenter(eventBus, confessionService, loggesInUserInfo, new ConfessionFeedView(confessionService, loggesInUserInfo, facebookService, accessToken, cbText));
			} else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID)) {
				presenter = new ConfessionFeedPresenter(eventBus, confessionService, loggesInUserInfo, new ConfessionFeedView(confessionService, loggesInUserInfo, facebookService, accessToken, cbText), ConfessionBox.confId);
			} else if(token.equals(Constants.HISTORY_ITEM_MY_CONFESSION_FEED)) {
				presenter = new MyConfessionFeedPresenter(eventBus, confessionService, loggesInUserInfo, new ConfessionFeedView(confessionService, loggesInUserInfo, facebookService, accessToken, cbText));
			} else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED)) {
				presenter = new ConfessionForMeFeedPresenter(eventBus, confessionService, loggesInUserInfo, new ConfessionFeedView(confessionService, loggesInUserInfo, facebookService, accessToken, cbText));
			}
			if (presenter != null) {
				presenter.go(container);
			}
		}
	}

	@Override
	public void go(HasWidgets container) {
		this.container = container;
		Presenter presenter = new MenuPresenter(eventBus, confessionService, loggesInUserInfo, new MenuView(cbText));
		presenter.go(container);
	}
}
