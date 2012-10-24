package com.l3.CB.client.presenter;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.presenter.ConfessionFeedPresenter.Display;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class MyConfessionFeedPresenter implements Presenter {

	@SuppressWarnings("unused")
	private final HandlerManager eventBus;
	private final ConfessionServiceAsync rpcService; 
	private UserInfo userInfo;
	private final Display display;
	private final boolean showUserControls;
	
	public MyConfessionFeedPresenter(HandlerManager eventBus,
			ConfessionServiceAsync rpcService, UserInfo userInfo,
			Display display) {
		super();
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		this.userInfo = userInfo;
		this.display = display;
		
		showUserControls = true;
		setConfessions();
		bind();
	}

	private void setConfessions() {
		this.display.setConfessionPagesLoaded(0);
		rpcService.getConfessionsIDID(0, userInfo.getUserId(), new AsyncCallback<List<Confession>>() {
			@Override
			public void onSuccess(List<Confession> result) {
				if(result != null) {
					display.setConfessions(result, true, showUserControls);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				Error.handleError("MyConfessionFeedPresenter", "onFailure", caught);
			}
		});
	}

	private void bind() {
		Window.addWindowScrollHandler(new Window.ScrollHandler() {
			boolean inEvent = false;
			@Override
			public void onWindowScroll(Window.ScrollEvent event) {
				if(display.isMoreConfessions() && !inEvent && ((Window.getScrollTop() + Window.getClientHeight()) >= (Document.get().getBody().getAbsoluteBottom()))) {
					display.addLoaderImage();
					inEvent = true;
					display.incrementConfessionPagesLoaded();
					rpcService.getConfessionsIDID(display.getConfessionPagesLoaded(), userInfo.getUserId(), new AsyncCallback<List<Confession>>() {
						@Override
						public void onSuccess(List<Confession> result) {
							if(result != null) {
								display.setConfessions(result, true, showUserControls);
								inEvent = false;
								display.removeLoaderImage();
							}
						}
						@Override
						public void onFailure(Throwable caught) {
							Error.handleError("MyConfessionFeedPresenter", "onFailure", caught);
						}
					});
				}
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		RootPanel.get(Constants.DIV_MAIN_CONTENT).clear();
		RootPanel.get(Constants.DIV_MAIN_CONTENT).add(display.asWidget());	
	}
}
