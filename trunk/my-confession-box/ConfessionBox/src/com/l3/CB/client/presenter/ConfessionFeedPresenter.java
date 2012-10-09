package com.l3.CB.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionFeedPresenter implements Presenter {

	public interface Display {
		Widget asWidget();
		public void setConfessionPagesLoaded(int confessionPagesLoaded);
		public void setConfessions(List<Confession> confessions);
	}

	private final HandlerManager eventBus;
	private final ConfessionServiceAsync rpcService; 
	private UserInfo userInfo;
	private final Display display;


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
				display.setConfessions(confessionList);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void setConfessions() {
		this.display.setConfessionPagesLoaded(0);
		rpcService.getConfessions(0, new AsyncCallback<List<Confession>>() {
			
			@Override
			public void onSuccess(List<Confession> result) {
				if(result != null) {
					display.setConfessions(result);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void bind() {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void go(HasWidgets container) {
		RootPanel.get(Constants.DIV_MAIN_CONTENT).clear();
		RootPanel.get(Constants.DIV_MAIN_CONTENT).add(display.asWidget());	
	}

}
