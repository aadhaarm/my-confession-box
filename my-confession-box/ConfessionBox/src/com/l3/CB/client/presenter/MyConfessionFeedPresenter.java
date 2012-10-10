package com.l3.CB.client.presenter;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.presenter.ConfessionFeedPresenter.Display;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class MyConfessionFeedPresenter implements Presenter {

	private final HandlerManager eventBus;
	private final ConfessionServiceAsync rpcService; 
	private UserInfo userInfo;
	private final Display display;

	Logger logger = Logger.getLogger("CBLogger");

	public MyConfessionFeedPresenter(HandlerManager eventBus,
			ConfessionServiceAsync rpcService, UserInfo userInfo,
			Display display) {
		super();
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		this.userInfo = userInfo;
		this.display = display;
		
		setConfessions();
		bind();
	}
	
	private void setConfessions() {
		this.display.setConfessionPagesLoaded(0);
		rpcService.getConfessions(0, userInfo.getUserId(), new AsyncCallback<List<Confession>>() {
			
			@Override
			public void onSuccess(List<Confession> result) {
				if(result != null) {
					display.setConfessions(result);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Constants.LOG_LEVEL,
						"Exception in MyConfessionFeedPresenter.onFailure()"
								+ caught.getCause());
			}
		});
	}

	private void bind() {
		display.getContentScrollPanel().addScrollHandler(new ScrollHandler() {
			boolean inEvent = false;

			@Override
			public void onScroll(ScrollEvent event) {
				if (display.isMoreConfessions()
						&& !inEvent
						&& display.getMaximumVerticalScrollPosition() <= display.getVerticalScrollPosition()) {
					display.addLoaderImage();
					inEvent = true;
					display.incrementConfessionPagesLoaded();
					rpcService.getConfessions(display.getConfessionPagesLoaded(), userInfo.getUserId(),
							new AsyncCallback<List<Confession>>() {

								@Override
								public void onSuccess(List<Confession> result) {
									display.setConfessions(result);
									inEvent = false;
									display.removeLoaderImage();
								}

								@Override
								public void onFailure(Throwable caught) {
									logger.log(Constants.LOG_LEVEL,
											"Exception in ConfessionFeedPresenter.onFailure()"
													+ caught.getCause());
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