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
import com.l3.CB.client.event.UpdateIdentityVisibilityEvent;
import com.l3.CB.client.event.UpdateIdentityVisibilityEventHandler;
import com.l3.CB.client.presenter.ConfessionFeedPresenter.Display;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;

public class MyConfessionFeedPresenter implements Presenter {

    private final Display display;
    private final boolean showUserControls;

    public MyConfessionFeedPresenter(Display display) {
	super();
	this.display = display;
	showUserControls = true;
	setConfessions(true);
	bind();
    }

    private void setConfessions(boolean clean) {
	if(clean) {
	    this.display.clearConfessions();
	}
	this.display.setConfessionPagesLoaded(0);
	ConfessionBox.confessionService.getConfessionsIDID(0, ConfessionBox.getLoggedInUserInfo().getUserId(), ConfessionBox.getLoggedInUserInfo().getId(), new AsyncCallback<List<Confession>>() {
	    @Override
	    public void onSuccess(List<Confession> result) {
		if(result != null) {
		    display.setConfessions(result, true, showUserControls);
		}
	    }
	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("MyConfessionFeedPresenter", "onFailure", caught);
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
		    ConfessionBox.confessionService.getConfessionsIDID(display.getConfessionPagesLoaded(), ConfessionBox.getLoggedInUserInfo().getUserId(), ConfessionBox.getLoggedInUserInfo().getId(), new AsyncCallback<List<Confession>>() {
			@Override
			public void onSuccess(List<Confession> result) {
			    display.setConfessions(result, true, showUserControls);
			    inEvent = false;
			    display.removeLoaderImage();
			}
			@Override
			public void onFailure(Throwable caught) {
			    Error.handleError("MyConfessionFeedPresenter", "onFailure", caught);
			}
		    });
		}
	    }
	});

	display.getRefreshButton().addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		setConfessions(true);
	    }
	});

	ConfessionBox.confEventBus.addHandler(UpdateIdentityVisibilityEvent.TYPE, new UpdateIdentityVisibilityEventHandler() {
	    @Override
	    public void updateIdentityVisibility(UpdateIdentityVisibilityEvent event) {
		Confession confessionToBeUpdated = event.getConfession();
		ConfessionBox.confessionService.getConfession(confessionToBeUpdated.getConfId(), null, null, false, new AsyncCallback<Confession>() {
		    @Override
		    public void onSuccess(Confession result) {
			if(result != null) {
			    result.setFbId(ConfessionBox.getLoggedInUserInfo().getId());
			    display.setConfessions(result);
			}
		    }

		    @Override
		    public void onFailure(Throwable caught) {
			Error.handleError("ConfessionForMeFeedPresenter", "onFailure", caught);
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