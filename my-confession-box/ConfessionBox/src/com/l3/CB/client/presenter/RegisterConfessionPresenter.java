package com.l3.CB.client.presenter;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class RegisterConfessionPresenter implements Presenter {
	
	Logger logger = Logger.getLogger("CBLogger");
	
	public interface Display {
		HasClickHandlers getSubmitBtn();
		String getConfession();
		boolean isAnynShare();
		Widget asWidget();
	}
	
	private final HandlerManager eventBus;
	private final ConfessionServiceAsync rpcService; 
	private UserInfo userInfo;
	private final Display display;

	public RegisterConfessionPresenter(HandlerManager eventBus,
			ConfessionServiceAsync rpcService, UserInfo userInfo,
			Display display) {
		super();
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		this.userInfo = userInfo;
		this.display = display;
		
		bind();
	}

	private void bind() {
		this.display.getSubmitBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final Confession confession = new Confession(display.getConfession(), display.isAnynShare());
				confession.setUserId(userInfo.getUserId());
				confession.setTimeStamp(new Date());
				rpcService.registerConfession(confession, new AsyncCallback<Confession>() {
					
					@Override
					public void onSuccess(Confession result) {
						History.newItem(Constants.HISTORY_ITEM_CONFESSION_FEED);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, "Exception when registering confession:" + caught.getMessage());
					}
				});
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		RootPanel.get(Constants.DIV_MAIN_CONTENT).clear();
		RootPanel.get(Constants.DIV_MAIN_CONTENT).add(display.asWidget());		
	}

}
