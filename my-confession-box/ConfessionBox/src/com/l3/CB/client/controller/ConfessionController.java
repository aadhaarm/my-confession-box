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
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.client.presenter.MenuPresenter;
import com.l3.CB.client.presenter.Presenter;
import com.l3.CB.client.presenter.RegisterConfessionPresenter;
import com.l3.CB.client.view.ConfessionFeedView;
import com.l3.CB.client.view.MenuView;
import com.l3.CB.client.view.RegisterConfessionView;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionController implements Presenter, ValueChangeHandler<String> {
	
	private final HandlerManager eventBus;
	private final ConfessionServiceAsync rpcService; 
	private static UserInfo userInfo;
	private HasWidgets container;


	public ConfessionController(HandlerManager eventBus,
			ConfessionServiceAsync rpcService,
			UserInfo userInfo, String confId) {
		super();
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		ConfessionController.userInfo = userInfo;

		rpcService.registerUser(userInfo, new AsyncCallback<UserInfo>() {
			
			@Override
			public void onSuccess(UserInfo result) {
				ConfessionController.userInfo = result;
				
				if(null != ConfessionBox.confId) {
//					Window.alert("In controller:" + ConfessionController.userInfo.toString());
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
				presenter = new RegisterConfessionPresenter(eventBus, rpcService, userInfo, new RegisterConfessionView());
			} else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FEED)) {
				presenter = new ConfessionFeedPresenter(eventBus, rpcService, userInfo, new ConfessionFeedView(rpcService, userInfo));
			} else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID)) {
				presenter = new ConfessionFeedPresenter(eventBus, rpcService, userInfo, new ConfessionFeedView(rpcService, userInfo), ConfessionBox.confId);
			}
			if (presenter != null) {
				presenter.go(container);
			}
		}
	}

	@Override
	public void go(HasWidgets container) {
		this.container = container;
		Presenter presenter = new MenuPresenter(eventBus, rpcService, userInfo, new MenuView());
		presenter.go(container);
	}
}
