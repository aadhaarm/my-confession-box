package com.l3.CB.client.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.Filters;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionFeedPresenter implements Presenter {

	public interface Display {
		Widget asWidget();
		public void setConfessionPagesLoaded(int confessionPagesLoaded);
		public void setConfessions(List<Confession> confessions, boolean isAnyn, boolean showUserControls);
		public int getConfessionPagesLoaded();
		public Image getLoaderImage();
		public boolean isMoreConfessions();
		public VerticalPanel getVpnlConfessionList();
		public void addLoaderImage();
		public void incrementConfessionPagesLoaded();
		public void removeLoaderImage();
		public void showConfessionFilters();
		public HasChangeHandlers getConfessionFilterListBox();
		public void clearConfessions();
	}

	@SuppressWarnings("unused")
	private final HandlerManager eventBus;
	private final ConfessionServiceAsync rpcService; 
	private UserInfo userInfo;
	private final Display display;
	Logger logger = Logger.getLogger("CBLogger");
	boolean showUserControls = false;
	Filters filter = Filters.ALL;

	public ConfessionFeedPresenter(HandlerManager eventBus,
			ConfessionServiceAsync rpcService, UserInfo userInfo,
			Display display) {
		super();
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		this.userInfo = userInfo;
		this.display = display;

		display.showConfessionFilters();
		
		setConfessions(false);

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
			setConfessions(false);
		}

		bind();
	}

	private void setConfessions(Long confId) {
		rpcService.getConfession(confId, new AsyncCallback<Confession>() {

			@Override
			public void onSuccess(Confession result) {
				ArrayList<Confession> confessionList = new ArrayList<Confession>();
				confessionList.add(result);
				display.setConfessions(confessionList, true, showUserControls);
			}

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Constants.LOG_LEVEL,
						"Exception in ConfessionFeedPresenter.onFailure()"
								+ caught.getCause());
			}
		});
	}

	private void setConfessions(boolean clean) {
		if(clean) {
			this.display.clearConfessions();
		}
		this.display.setConfessionPagesLoaded(0);
		rpcService.getConfessions(0, filter, userInfo.getLocale(), new AsyncCallback<List<Confession>>() {

			@Override
			public void onSuccess(List<Confession> result) {
				if(result != null) {
					display.setConfessions(result, true, showUserControls);
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
		Window.addWindowScrollHandler(new Window.ScrollHandler() {
			boolean inEvent = false;
			@Override
			public void onWindowScroll(
					com.google.gwt.user.client.Window.ScrollEvent event) {
				if(display.isMoreConfessions() && !inEvent && ((Window.getScrollTop() + Window.getClientHeight()) >= (Document.get().getBody().getAbsoluteBottom()))) {
					display.addLoaderImage();
					inEvent = true;
					display.incrementConfessionPagesLoaded();
					
					rpcService.getConfessions(display.getConfessionPagesLoaded(), filter, userInfo.getLocale(), new AsyncCallback<List<Confession>>() {

						@Override
						public void onSuccess(List<Confession> result) {
							display.setConfessions(result, true, showUserControls);
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
		
		display.getConfessionFilterListBox().addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				ListBox lstBoxFilter = (ListBox)event.getSource();
				String selectedFilter = lstBoxFilter.getValue(lstBoxFilter.getSelectedIndex());
				filter = Filters.valueOf(selectedFilter);
				setConfessions(true);
			}
		});
	}


	@Override
	public void go(HasWidgets container) {
		RootPanel.get(Constants.DIV_MAIN_CONTENT).clear();
		RootPanel.get(Constants.DIV_MAIN_CONTENT).add(display.asWidget());	
	}

}
