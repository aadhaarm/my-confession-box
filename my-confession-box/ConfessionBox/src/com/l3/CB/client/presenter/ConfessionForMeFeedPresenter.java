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

public class ConfessionForMeFeedPresenter implements Presenter {

	@SuppressWarnings("unused")
	private final HandlerManager eventBus;
	private final ConfessionServiceAsync rpcService; 
	private UserInfo userInfo;
	private final Display display;
	private final boolean showUserControls;

	public ConfessionForMeFeedPresenter(HandlerManager eventBus,
			ConfessionServiceAsync rpcService, UserInfo userInfo,
			Display display) {
		super();
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		this.userInfo = userInfo;
		this.display = display;
		
		showUserControls = false;
		setConfessions();
		bind();
	}

	private void setConfessions() {
		this.display.setConfessionPagesLoaded(0);
		rpcService.getConfessionsTOME(0, userInfo.getUserId(), new AsyncCallback<List<Confession>>() {
			@Override
			public void onSuccess(List<Confession> result) {
				if(result != null) {
					display.setConfessions(result, false, showUserControls);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				Error.handleError("ConfessionForMeFeedPresenter", "onFailure", caught);
			}
		});
	}

	private void bind() {
		Window.addWindowScrollHandler(new Window.ScrollHandler() {
			boolean inEvent = false;
			@Override
			public void onWindowScroll(com.google.gwt.user.client.Window.ScrollEvent event) {
				if(display.isMoreConfessions() && !inEvent && ((Window.getScrollTop() + Window.getClientHeight()) >= (Document.get().getBody().getAbsoluteBottom()))) {
					display.addLoaderImage();
					inEvent = true;
					display.incrementConfessionPagesLoaded();
					rpcService.getConfessionsTOME(display.getConfessionPagesLoaded(), userInfo.getUserId(),	new AsyncCallback<List<Confession>>() {
						@Override
						public void onSuccess(List<Confession> result) {
							if(result != null) {
								display.setConfessions(result, false, showUserControls);
								inEvent = false;
								display.removeLoaderImage();
							}
						}
						@Override
						public void onFailure(Throwable caught) {
							Error.handleError("ConfessionForMeFeedPresenter", "onFailure", caught);
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