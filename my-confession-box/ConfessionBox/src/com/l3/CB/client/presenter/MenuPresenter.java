package com.l3.CB.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.UserInfo;

public class MenuPresenter implements Presenter {

	public interface Display {
		public MenuItem setFeedItemSelected();
		Widget asWidget();
	}
	
	@SuppressWarnings("unused")
	private final HandlerManager eventBus;
	@SuppressWarnings("unused")
	private final ConfessionServiceAsync rpcService; 
	@SuppressWarnings("unused")
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
		// TODO Auto-generated method stub
	}

	@Override
	public void go(HasWidgets container) {
		RootPanel.get(Constants.DIV_LEFT_MENU).clear();
		RootPanel.get(Constants.DIV_LEFT_MENU).add(display.asWidget());		
	}
}