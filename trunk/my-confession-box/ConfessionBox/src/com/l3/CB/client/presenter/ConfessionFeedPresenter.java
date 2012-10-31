package com.l3.CB.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.util.HelpInfo;
import com.l3.CB.shared.CBText;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.Filters;

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
		public HasFocusHandlers getConfessionFilterListBoxForHelp();
		public void clearConfessions();
		public HasClickHandlers getRegreshButton();
		void showEmptyScreen();
	}

	@SuppressWarnings("unused")
	private final HandlerManager eventBus;
	private final ConfessionServiceAsync rpcService; 
	private final Display display;
	boolean showUserControls = false;
	Filters filter = Filters.ALL;
	CBText cbText = GWT.create(CBText.class);
	
	public ConfessionFeedPresenter(HandlerManager eventBus,
			ConfessionServiceAsync rpcService, Display display) {
		super();
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		this.display = display;

		display.showConfessionFilters();
		setConfessions(false);
		bind();
	}

	public ConfessionFeedPresenter(HandlerManager eventBus,
			ConfessionServiceAsync rpcService,
			Display display, String confId) {
		super();
		this.eventBus = eventBus;
		this.rpcService = rpcService;
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
				if(result != null) {
					ArrayList<Confession> confessionList = new ArrayList<Confession>();
					confessionList.add(result);
					display.setConfessions(confessionList, true, showUserControls);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Error.handleError("ConfessionFeedPresenter", "onFailure", caught);
			}
		});
	}

	private void setConfessions(final boolean clean) {
		this.display.setConfessionPagesLoaded(0);
		rpcService.getConfessions(0, filter, ConfessionBox.loggedInUserInfo.getLocale(), ConfessionBox.loggedInUserInfo.getUserId(), new AsyncCallback<List<Confession>>() {
			@Override
			public void onSuccess(List<Confession> result) {
				if(result != null) {
					if(clean) {
						display.clearConfessions();
					}
					display.setConfessions(result, true, showUserControls);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				Error.handleError("ConfessionFeedPresenter", "onFailure", caught);
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
					
					rpcService.getConfessions(display.getConfessionPagesLoaded(), filter, ConfessionBox.loggedInUserInfo.getLocale(), ConfessionBox.loggedInUserInfo.getUserId(), new AsyncCallback<List<Confession>>() {

						@Override
						public void onSuccess(List<Confession> result) {
							display.setConfessions(result, true, showUserControls);
							inEvent = false;
							display.removeLoaderImage();
						}

						@Override
						public void onFailure(Throwable caught) {
							Error.handleError("ConfessionFeedPresenter", "bind", caught);
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
		
		display.getConfessionFilterListBoxForHelp().addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				HelpInfo.showHelpInfo(HelpInfo.type.CONFESSION_FILTER);
			}
		});
		
		display.getRegreshButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
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