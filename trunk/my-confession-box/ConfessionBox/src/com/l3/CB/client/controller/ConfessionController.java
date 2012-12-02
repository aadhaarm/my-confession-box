/**
 * L3 Confession Box
 * 
 * Nov 5, 2012, 7:39:30 PM
 */
package com.l3.CB.client.controller;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.EditConfessionEvent;
import com.l3.CB.client.event.EditConfessionEventHandler;
import com.l3.CB.client.event.UpdateHPEvent;
import com.l3.CB.client.event.UpdateHPEventHandler;
import com.l3.CB.client.event.UpdateMenuEvent;
import com.l3.CB.client.event.UpdateMenuEventHandler;
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.client.presenter.ConfessionForMeFeedPresenter;
import com.l3.CB.client.presenter.FooterPresenter;
import com.l3.CB.client.presenter.HumanPointPresenter;
import com.l3.CB.client.presenter.MenuPresenter;
import com.l3.CB.client.presenter.MyConfessionFeedPresenter;
import com.l3.CB.client.presenter.Presenter;
import com.l3.CB.client.presenter.RegisterConfessionPresenter;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.view.ConfessionFeedView;
import com.l3.CB.client.view.FooterView;
import com.l3.CB.client.view.HumanPointView;
import com.l3.CB.client.view.MenuView;
import com.l3.CB.client.view.RegisterConfessionView;
import com.l3.CB.shared.Constants;

public class ConfessionController implements Presenter, ValueChangeHandler<String> {

    /**
     * Application variables
     */
    private HasWidgets container;
    private HumanPointPresenter humanPointPresenter = null;
    private MenuPresenter menuPresenter = null;

    /**
     * Constructors
     * 
     * @param confId
     */
    public ConfessionController(String confId) {
	super();
	/**
	 * Call server with logged in user info
	 * Register if new, update info or just bring the user UserID with the user info
	 */
	if(ConfessionBox.isLoggedIn) {

	    /*
	     * Register user and get User ID
	     */
	    ConfessionBox.confessionService.registerUser(ConfessionBox.getLoggedInUserInfo(), new AsyncCallback<Long>() {
		@Override
		public void onSuccess(Long result) {
		    if(result != null) {
			ConfessionBox.getLoggedInUserInfo().setUserId(result);
			initializeApp();
		    } else {
			Window.alert(ConfessionBox.cbText.applicationError());
		    }
		}

		@Override
		public void onFailure(Throwable caught) {
		    Error.handleError("ConfessionController", "onFailure", caught);
		}
	    });
	}
	// Bind events
	bind();
    }

    /**
     * Initialize Application for first view
     */
    private void initializeApp() {
	// Initialize MENU
	menuPresenter = new MenuPresenter(new MenuView());
	menuPresenter.go(container);

	// Initialize Human points
	if(ConfessionBox.isLoggedIn) {
	    humanPointPresenter = new HumanPointPresenter(new HumanPointView());
	    humanPointPresenter.go(container);
	}

	// Initialize Footer
	FooterPresenter footerPresenter = new FooterPresenter(new FooterView());
	footerPresenter.go(container);

	String loadHash = Window.Location.getParameter(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED);
	if(loadHash != null) {
	    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED);
	} else if(null != ConfessionBox.confId) {
	    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID);
	} else {
	    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED);
	}
    }

    /**
     * Bind various events
     */
    private void bind() {

	ConfessionBox.logo.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		Location.reload();
	    }
	});

	// Listen all history new item addition
	History.addValueChangeHandler(this);

	ConfessionBox.confEventBus.addHandler(UpdateHPEvent.TYPE, new UpdateHPEventHandler() {
	    @Override
	    public void updateHPContact(UpdateHPEvent event) {
		humanPointPresenter.updateHumanPoints(event.getUpdatedCount());
	    }
	});

	ConfessionBox.confEventBus.addHandler(UpdateMenuEvent.TYPE, new UpdateMenuEventHandler() {
	    @Override
	    public void updateMenuCount(UpdateMenuEvent event) {
		if(ConfessionBox.isLoggedIn) {
		    menuPresenter.initializeMenuCounts();
		}
	    }
	});

	ConfessionBox.confEventBus.addHandler(EditConfessionEvent.TYPE, new EditConfessionEventHandler() {

	    @Override
	    public void editConfession(EditConfessionEvent event) {
		Presenter presenter = new RegisterConfessionPresenter(new RegisterConfessionView(), event.getConfessionToBeEdited());
		presenter.go(container);
	    }
	});
    }

    /**
     * On value change - When history event change item
     */
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
	String token = event.getValue();

	if (token != null) {
	    Presenter presenter = null;

	    if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FEED)) {
		presenter = new ConfessionFeedPresenter(new ConfessionFeedView());
		MenuView.selectMenuItem(1);
	    } else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID)) {
		presenter = new ConfessionFeedPresenter(new ConfessionFeedView(), ConfessionBox.confId);
		MenuView.selectMenuItem(1);
	    } else if(ConfessionBox.isLoggedIn) {
		if (token.equals(Constants.HISTORY_ITEM_REGISTER_CONFESSION)) {
		    presenter = new RegisterConfessionPresenter(new RegisterConfessionView());
		    MenuView.selectMenuItem(2);
		} else if(token.equals(Constants.HISTORY_ITEM_MY_CONFESSION_FEED)) {
		    MenuView.selectMenuItem(3);
		    presenter = new MyConfessionFeedPresenter(new ConfessionFeedView());
		} else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED)) {
		    MenuView.selectMenuItem(4);
		    presenter = new ConfessionForMeFeedPresenter(new ConfessionFeedView());
		}
	    } 

	    if(presenter != null) {
		presenter.go(container);
	    }
	}
    }

    /**
     * Presenter GO - initiate Controller
     */
    @Override
    public void go(HasWidgets container) {
	// If not logged-in
	if(!ConfessionBox.isLoggedIn) {
	    initializeApp();
	}
	this.container = container;
	// Remove loading Animation
	ConfessionBox.removeApplicationLoad();
    }
}