package com.l3.CB.client.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionFeedPresenter implements Presenter {

	public interface Display {
		Widget asWidget();
		public void setConfessionPagesLoaded(int confessionPagesLoaded);
		public void setConfessions(List<Confession> confessions, boolean isAnyn);
		public ScrollPanel getContentScrollPanel();
		public int getConfessionPagesLoaded();
		public Image getLoaderImage();
		public boolean isMoreConfessions();
		public VerticalPanel getVpnlConfessionList();
		public int getMaximumVerticalScrollPosition();
		public void addLoaderImage();
		public void incrementConfessionPagesLoaded();
		public void removeLoaderImage();
		int getVerticalScrollPosition();
	}

	@SuppressWarnings("unused")
	private final HandlerManager eventBus;
	private final ConfessionServiceAsync rpcService; 
	@SuppressWarnings("unused")
	private UserInfo userInfo;
	private final Display display;
	Logger logger = Logger.getLogger("CBLogger");


	public ConfessionFeedPresenter(HandlerManager eventBus,
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

	public ConfessionFeedPresenter(HandlerManager eventBus,
			ConfessionServiceAsync rpcService, UserInfo userInfo,
			Display display, String confId) {
		super();
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		this.userInfo = userInfo;
		this.display = display;
		
		try {
			setConfessions(Long.parseLong(confId));
		} catch (Exception e) {
			setConfessions();
		}
		
		bind();
	}
	
	private void setConfessions(Long confId) {
		rpcService.getConfession(confId, new AsyncCallback<Confession>() {
			
			@Override
			public void onSuccess(Confession result) {
				ArrayList<Confession> confessionList = new ArrayList<Confession>();
				confessionList.add(result);
				display.setConfessions(confessionList, true);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Constants.LOG_LEVEL,
						"Exception in ConfessionFeedPresenter.onFailure()"
								+ caught.getCause());
			}
		});
	}

	private void setConfessions() {
		this.display.setConfessionPagesLoaded(0);
		rpcService.getConfessions(0, new AsyncCallback<List<Confession>>() {
			
			@Override
			public void onSuccess(List<Confession> result) {
				if(result != null) {
					display.setConfessions(result, true);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Constants.LOG_LEVEL,
						"Exception in ConfessionFeedPresenter.onFailure()"
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
					rpcService.getConfessions(display.getConfessionPagesLoaded(),
							new AsyncCallback<List<Confession>>() {

								@Override
								public void onSuccess(List<Confession> result) {
									display.setConfessions(result, true);
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
