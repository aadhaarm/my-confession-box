package com.l3.CB.client.presenter;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateFeedToMeEvent;
import com.l3.CB.client.event.UpdateFeedToMeEventHandler;
import com.l3.CB.client.presenter.ConfessionFeedPresenter.Display;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;

public class ConfessionForMeFeedPresenter implements Presenter {

    private final Display display;
    private final boolean showUserControls;

    public ConfessionForMeFeedPresenter(Display display) {
	super();
	this.display = display;
	showUserControls = false;
	setConfessions(true);
	bind();
    }

    private void setConfessions(boolean clean) {
	if(clean) {
	    this.display.clearConfessions();
	}
	this.display.setConfessionPagesLoaded(0);
	ConfessionBox.confessionService.getConfessionsTOME(0, ConfessionBox.loggedInUserInfo.getUserId(), new AsyncCallback<List<Confession>>() {
	    @Override
	    public void onSuccess(List<Confession> result) {
		display.setConfessions(result, false, showUserControls);
		if(result == null || result.isEmpty()) {
		    display.showEmptyScreen();
		}
	    }
	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("ConfessionForMeFeedPresenter", "onFailure", caught);
	    }
	});
    }

    private void bind() {
	/*
	 * LOAD MORE CONFESSIONS on scroll
	 */
	Window.addWindowScrollHandler(new Window.ScrollHandler() {
	    boolean inEvent = false;
	    @Override
	    public void onWindowScroll(com.google.gwt.user.client.Window.ScrollEvent event) {
		if(display.isMoreConfessions() && !inEvent && ((Window.getScrollTop() + Window.getClientHeight()) >= (Document.get().getBody().getAbsoluteBottom()))) {
		    display.addLoaderImage();
		    inEvent = true;
		    display.incrementConfessionPagesLoaded();
		    ConfessionBox.confessionService.getConfessionsTOME(display.getConfessionPagesLoaded(), ConfessionBox.loggedInUserInfo.getUserId(),	new AsyncCallback<List<Confession>>() {
			@Override
			public void onSuccess(List<Confession> result) {
			    display.setConfessions(result, false, showUserControls);
			    inEvent = false;
			    display.removeLoaderImage();
			}
			@Override
			public void onFailure(Throwable caught) {
			    Error.handleError("ConfessionForMeFeedPresenter", "onFailure", caught);
			}
		    });
		}
	    }
	});

	/*
	 * Refresh when REFRESH button is pressed
	 */
	display.getRefreshButton().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		setConfessions(true);
	    }
	});

	ConfessionBox.confEventBus.addHandler(UpdateFeedToMeEvent.TYPE, new UpdateFeedToMeEventHandler() {
	    @Override
	    public void updateFeedToMeConfessions(UpdateFeedToMeEvent event) {
		Confession confessionToBepdated = event.getConfession();
		if(confessionToBepdated != null) {
		    ConfessionBox.confessionService.getConfession(confessionToBepdated.getConfId(), true, new AsyncCallback<Confession>() {
			@Override
			public void onSuccess(Confession result) {
			    if(result != null) {
				display.setConfessions(result);
			    }
			}

			@Override
			public void onFailure(Throwable caught) {
			    Error.handleError("ConfessionForMeFeedPresenter : on UpdateFeedToMeEvent - confessionService.getConfession", "onFailure", caught);
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