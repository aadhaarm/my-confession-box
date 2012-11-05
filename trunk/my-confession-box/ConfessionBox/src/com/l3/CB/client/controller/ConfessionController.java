package com.l3.CB.client.controller;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.FacebookServiceAsync;
import com.l3.CB.client.event.UpdateHPEvent;
import com.l3.CB.client.event.UpdateHPEventHandler;
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.client.presenter.ConfessionForMeFeedPresenter;
import com.l3.CB.client.presenter.HumanPointPresenter;
import com.l3.CB.client.presenter.MenuPresenter;
import com.l3.CB.client.presenter.MyConfessionFeedPresenter;
import com.l3.CB.client.presenter.Presenter;
import com.l3.CB.client.presenter.RegisterConfessionPresenter;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.view.ConfessionFeedView;
import com.l3.CB.client.view.HumanPointView;
import com.l3.CB.client.view.MenuView;
import com.l3.CB.client.view.RegisterConfessionView;
import com.l3.CB.shared.CBText;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionController implements Presenter, ValueChangeHandler<String> {
	
	private final FacebookServiceAsync facebookService;
	private final ConfessionServiceAsync confessionService; 
	private final HandlerManager eventBus;
	private HasWidgets container;
	private String accessToken;
	private CBText cbText;
	private HumanPointPresenter hPPresenter = null;

	public ConfessionController(final HandlerManager eventBus,
			ConfessionServiceAsync rpcService,
			FacebookServiceAsync facebookService, String confId, String accessToken, final CBText cbText) {
		super();
		this.eventBus = eventBus;
		this.confessionService = rpcService;
		this.accessToken = accessToken;
		this.facebookService = facebookService;
		this.cbText = cbText;
		
		rpcService.registerUser(ConfessionBox.loggedInUserInfo, new AsyncCallback<UserInfo>() {
			
			@Override
			public void onSuccess(UserInfo result) {
				if(result != null) {
					ConfessionBox.loggedInUserInfo = result;
					Presenter presenter = new MenuPresenter(eventBus, confessionService, ConfessionBox.loggedInUserInfo, new MenuView(cbText, confessionService));
					presenter.go(container);
					hPPresenter = new HumanPointPresenter(new HumanPointView());
					hPPresenter.go(container);
					if(null != ConfessionBox.confId) {
						CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID);
					} else {
						CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED);
					}
				} else {
					Window.alert(cbText.applicationError());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Error.handleError("ConfessionController", "onFailure", caught);
			}
		});
		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);
		
		eventBus.addHandler(UpdateHPEvent.TYPE, new UpdateHPEventHandler() {
					
					@Override
					public void updateHPContact(UpdateHPEvent event) {
						hPPresenter.updateHumanPoints(event.getUpdatedCount());
					}
				});
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();

		if (token != null) {
			Presenter presenter = null;
			if (token.equals(Constants.HISTORY_ITEM_REGISTER_CONFESSION)) {
				presenter = new RegisterConfessionPresenter(eventBus, confessionService, facebookService, new RegisterConfessionView(cbText), accessToken);
			} else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FEED)) {
				presenter = new ConfessionFeedPresenter(eventBus, confessionService, new ConfessionFeedView(confessionService, facebookService, accessToken, cbText));
			} else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID)) {
				presenter = new ConfessionFeedPresenter(eventBus, confessionService, new ConfessionFeedView(confessionService, facebookService, accessToken, cbText), ConfessionBox.confId);
			} else if(token.equals(Constants.HISTORY_ITEM_MY_CONFESSION_FEED)) {
				presenter = new MyConfessionFeedPresenter(eventBus, confessionService, new ConfessionFeedView(confessionService, facebookService, accessToken, cbText));
			} else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED)) {
				presenter = new ConfessionForMeFeedPresenter(eventBus, confessionService, new ConfessionFeedView(confessionService, facebookService, accessToken, cbText));
			}
			if (presenter != null) {
				presenter.go(container);
			}
		}
	}

	@Override
	public void go(HasWidgets container) {
		this.container = container;
	}
}