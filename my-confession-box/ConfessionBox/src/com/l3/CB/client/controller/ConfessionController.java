/**
 * L3 Confession Box
 * 
 * Nov 5, 2012, 7:39:30 PM
 */
package com.l3.CB.client.controller;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
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
import com.l3.CB.client.presenter.HumanPointPresenter;
import com.l3.CB.client.presenter.MenuPresenter;
import com.l3.CB.client.presenter.MyConfessionFeedPresenter;
import com.l3.CB.client.presenter.Presenter;
import com.l3.CB.client.presenter.RegisterConfessionPresenter;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.view.ConfessionFeedView;
import com.l3.CB.client.view.HumanPointView;
import com.l3.CB.client.view.MenuView;
import com.l3.CB.client.view.RegisterConfessionView;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionController implements Presenter, ValueChangeHandler<String> {

    private HasWidgets container;
    private HumanPointPresenter humanPointPresenter = null;
    private MenuPresenter menuPresenter = null;

    public ConfessionController(String confId) {
	super();
	/**
	 * Call server with logged in user info
	 * Register if new, update info or just bring the user UserID with the user info
	 */
	ConfessionBox.confessionService.registerUser(ConfessionBox.loggedInUserInfo, new AsyncCallback<UserInfo>() {
	    @Override
	    public void onSuccess(UserInfo result) {
		if(result != null) {
		    ConfessionBox.loggedInUserInfo = result;
		    // Initialize MENU
		    menuPresenter = new MenuPresenter(new MenuView());
		    menuPresenter.go(container);
		    // Initialize Human points
		    humanPointPresenter = new HumanPointPresenter(new HumanPointView());
		    humanPointPresenter.go(container);

		    if(null != ConfessionBox.confId) {
			CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID);
		    } else {
			CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED);
		    }
		} else {
		    Window.alert(ConfessionBox.cbText.applicationError());
		}
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("ConfessionController", "onFailure", caught);
	    }
	});
	bind();
    }

    private void bind() {
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
		menuPresenter.initializeMenuCounts();
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

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
	String token = event.getValue();

	if (token != null) {
	    Presenter presenter = null;
	    if (token.equals(Constants.HISTORY_ITEM_REGISTER_CONFESSION)) {
		presenter = new RegisterConfessionPresenter(new RegisterConfessionView());
	    } else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FEED)) {
		presenter = new ConfessionFeedPresenter(new ConfessionFeedView());
	    } else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID)) {
		presenter = new ConfessionFeedPresenter(new ConfessionFeedView(), ConfessionBox.confId);
	    } else if(token.equals(Constants.HISTORY_ITEM_MY_CONFESSION_FEED)) {
		presenter = new MyConfessionFeedPresenter(new ConfessionFeedView());
	    } else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED)) {
		presenter = new ConfessionForMeFeedPresenter(new ConfessionFeedView());
	    }
	    if (presenter != null) {
		presenter.go(container);
	    }
	}
    }

    @Override
    public void go(HasWidgets container) {
	this.container = container;
    }
}