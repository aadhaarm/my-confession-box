/**
 * L3 Confession Box
 * 
 * Nov 5, 2012, 8:40:21 PM
 */
package com.l3.CB.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.view.confession.ConfessionsContainer;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.Filters;

public class ConfessionFeedPresenter implements Presenter {

    public interface Display {
	Widget asWidget();

	public void setConfessionPagesLoaded(int confessionPagesLoaded);
	public void setConfessions(List<Confession> confessions, boolean isAnyn, boolean showUserControls, Filters filter, boolean showExtendedDetails, boolean showPardonHelpText);
	public void addLoaderImage();
	public void incrementConfessionPagesLoaded();
	public void removeLoaderImage();
	public void showConfessionFilters();
	public void clearConfessions();
	public void setConfessions(Confession confession);
	public void showEmptyScreen();

	public int getConfessionPagesLoaded();
	public boolean isMoreConfessions();
	public Image getLoaderImage();
	public HTMLPanel getVpnlConfessionList();
	public HasChangeHandlers getConfessionFilterListBox();
	public HasFocusHandlers getConfessionFilterListBoxForHelp();
	public HasClickHandlers getRefreshButton();
	public void setFilterInfo(String filterInfoText);

	public void resetConfessionsThisView();	
	public void setMoreConfessions(boolean moreConfessions);
    }

    private final Display display;
    boolean showUserControls = ConfessionBox.isAdmin ? true : false;
    boolean showExtendedDetails = false;
    boolean showPardonHelpText = false;

    Filters filter = Filters.ALL;

    public ConfessionFeedPresenter(Display display) {
	super();
	this.display = display;
	display.showConfessionFilters();
	setConfessions(false);
	bind();
    }

    public ConfessionFeedPresenter(Display display, String confId) {
	super();
	this.display = display;
	try {
	    setConfessions(Long.parseLong(confId));
	} catch (Exception e) {
	    setConfessions(false);
	}
	bind();
    }

    public ConfessionFeedPresenter(ConfessionsContainer confessionsContainer, Filters filter) {
	super();
	this.display = confessionsContainer;
	this.filter = filter;
	setConfessions(false);
	bind();
    }

    private void setConfessions(Long confId) {
	// Get confessions - Non secure info only
	ConfessionBox.confessionService.getConfession(confId, null, null, false, new AsyncCallback<Confession>() {
	    @Override
	    public void onSuccess(Confession result) {
		if(result != null) {
		    ArrayList<Confession> confessionList = new ArrayList<Confession>();
		    confessionList.add(result);
		    display.setConfessions(confessionList, true, showUserControls, filter, showExtendedDetails, showPardonHelpText);
		}
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("ConfessionFeedPresenter", "onFailure", caught);
	    }
	});
    }

    private void setConfessions(final boolean clean) {
	if(clean) {
	    display.setMoreConfessions(true);
	    display.clearConfessions();
	    display.resetConfessionsThisView();
	}
	this.display.setConfessionPagesLoaded(0);
	ConfessionBox.confessionService.getConfessions(0, filter, ConfessionBox.getLoggedInUserInfo().getLocale(), ConfessionBox.getLoggedInUserInfo().getUserId(), new AsyncCallback<List<Confession>>() {
	    @Override
	    public void onSuccess(List<Confession> result) {
		display.setConfessions(result, true, showUserControls, filter, showExtendedDetails, showPardonHelpText);
	    }
	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("ConfessionFeedPresenter", "onFailure", caught);
	    }
	});
    }

    /**
     * Bind events
     */
    private void bind() {

	// ON WINDOW SCROLL
	Window.addWindowScrollHandler(new Window.ScrollHandler() {
	    boolean inEvent = false;
	    @Override
	    public void onWindowScroll(Window.ScrollEvent event) {
		if(display.isMoreConfessions() && !inEvent && ((Window.getScrollTop() + Window.getClientHeight()) >= (Document.get().getBody().getAbsoluteBottom()))) {
		    display.addLoaderImage();
		    inEvent = true;
		    display.incrementConfessionPagesLoaded();

		    ConfessionBox.confessionService.getConfessions(
			    display.getConfessionPagesLoaded(), filter,
			    ConfessionBox.getLoggedInUserInfo().getLocale(),
			    ConfessionBox.getLoggedInUserInfo().getUserId(),
			    new AsyncCallback<List<Confession>>() {

			@Override
			public void onSuccess(List<Confession> result) {
			    display.setConfessions(result, true, showUserControls, filter, showExtendedDetails, showPardonHelpText);
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
    }

    @Override
    public void go(HasWidgets container) {
	RootPanel.get(Constants.DIV_MAIN_CONTENT).clear();
	RootPanel.get(Constants.DIV_MAIN_CONTENT).add(display.asWidget());	
    }
}