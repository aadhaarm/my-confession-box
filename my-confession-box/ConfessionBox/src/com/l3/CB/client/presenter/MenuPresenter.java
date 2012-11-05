package com.l3.CB.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.ui.widgets.MenuButton;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.UserInfo;

public class MenuPresenter implements Presenter {

	public interface Display {
		public PushButton setFeedItemSelected();
		public PushButton getFeedItem();
		public PushButton getConfessItem();
		public PushButton getMyConItem();
		public PushButton getConToMeItem();
		public MenuButton getBtnMenuItemMyConf();
		public MenuButton getBtnMenuItemConfToMe();
		
		Widget asWidget();
	}
	
	@SuppressWarnings("unused")
	private final HandlerManager eventBus;
	private final ConfessionServiceAsync rpcService; 
	private UserInfo userInfo;
	private final Display display;

	public MenuPresenter(HandlerManager eventBus,
			ConfessionServiceAsync rpcService, UserInfo userInfo,
			Display display) {
		super();
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		this.userInfo = userInfo;
		this.display = display;

		this.display.setFeedItemSelected();

		bind();
	}

	private void bind() {
		Timer t = new Timer() {
			public void run() {
				rpcService.getMyConfessionCount(userInfo.getUserId(), new AsyncCallback<Long>() {
					
					@Override
					public void onSuccess(Long result) {
						display.getBtnMenuItemMyConf().getBtnCount().setText(Long.toString(result));
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Error.handleError("MenuPresenter", "onFailure", caught);
					}
				});

				rpcService.getConfessionForMeCount(userInfo.getUserId(), new AsyncCallback<Long>() {
					
					@Override
					public void onSuccess(Long result) {
						display.getBtnMenuItemConfToMe().getBtnCount().setText(Long.toString(result));
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Error.handleError("MenuPresenter", "onFailure", caught);
					}
				});

				
//				this.schedule(300000);
			}
		};
		t.schedule(10);
	}

	@Override
	public void go(HasWidgets container) {
		RootPanel.get(Constants.DIV_LEFT_MENU).clear();
		RootPanel.get(Constants.DIV_LEFT_MENU).add(display.asWidget());		
	}
}