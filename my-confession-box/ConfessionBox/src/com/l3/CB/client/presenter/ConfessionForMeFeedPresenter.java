package com.l3.CB.client.presenter;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateFeedToMeEvent;
import com.l3.CB.client.event.UpdateFeedToMeEventHandler;
import com.l3.CB.client.event.UpdateIdentityVisibilityEvent;
import com.l3.CB.client.event.UpdateIdentityVisibilityEventHandler;
import com.l3.CB.client.presenter.ConfessionFeedPresenter.Display;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.Filters;

public class ConfessionForMeFeedPresenter implements Presenter {

    private final Display display;
    private final boolean showUserControls;
    private boolean showExtendedDetails = true;
    private boolean showPardonHelpText = true;

    public ConfessionForMeFeedPresenter(Display display) {
	super();
	this.display = display;
	showUserControls = false;
	setConfessions(true);
	bind();
    }

    private void setConfessions(boolean clean) {
	CommonUtils.showApplicationLoad();
	if(clean) {
	    this.display.clearConfessions();
	    display.setMoreConfessions(true);
	}
	this.display.setConfessionPagesLoaded(0);
	ConfessionBox.confessionService.getConfessionsTOME(0, ConfessionBox.getLoggedInUserInfo().getUserId(), ConfessionBox.getLoggedInUserInfo().getId(), new AsyncCallback<List<Confession>>() {
	    @Override
	    public void onSuccess(List<Confession> result) {
		display.setConfessions(result, false, showUserControls, Filters.ALL, showExtendedDetails, showPardonHelpText);
	    }
	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("ConfessionForMeFeedPresenter", "onFailure", caught);
	    }
	});
    }

    private void bind() {
	ConfessionBox.eventBus.addHandler(UpdateIdentityVisibilityEvent.TYPE, new UpdateIdentityVisibilityEventHandler() {
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
		    ConfessionBox.confessionService.getConfessionsTOME(display.getConfessionPagesLoaded(), ConfessionBox.getLoggedInUserInfo().getUserId(), ConfessionBox.getLoggedInUserInfo().getId(), new AsyncCallback<List<Confession>>() {
			@Override
			public void onSuccess(List<Confession> result) {
			    display.setConfessions(result, false, showUserControls, Filters.ALL, showExtendedDetails, showPardonHelpText);
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
//	display.getRefreshButton().addClickHandler(new ClickHandler() {
//	    @Override
//	    public void onClick(ClickEvent event) {
//		setConfessions(true);
//	    }
//	});

	ConfessionBox.eventBus.addHandler(UpdateFeedToMeEvent.TYPE, new UpdateFeedToMeEventHandler() {
	    @Override
	    public void updateFeedToMeConfessions(UpdateFeedToMeEvent event) {
		Confession confessionToBepdated = event.getConfession();
		if(confessionToBepdated != null) {
		    ConfessionBox.confessionService.getConfession(confessionToBepdated.getConfId(), ConfessionBox.getLoggedInUserInfo().getUserId(), ConfessionBox.getLoggedInUserInfo().getId(), true, new AsyncCallback<Confession>() {
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