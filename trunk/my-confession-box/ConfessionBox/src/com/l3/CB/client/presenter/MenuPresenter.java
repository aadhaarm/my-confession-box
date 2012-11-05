package com.l3.CB.client.presenter;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ui.widgets.MenuButton;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;

public class MenuPresenter implements Presenter {

    public interface Display {
	public PushButton setFeedItemSelected();
	public PushButton getFeedItem();
	public PushButton getConfessItem();
	public PushButton getMyConItem();
	public PushButton getConToMeItem();
	public MenuButton getBtnMenuItemMyConf();
	public MenuButton getBtnMenuItemConfToMe();

	Widget asWidget();
    }

    private final Display display;

    public MenuPresenter(Display display) {
	super();
	this.display = display;
	this.display.setFeedItemSelected();
	bind();
    }

    private void bind() {
	Timer refreshTimer = new Timer() {
	    @Override
	    public void run() {
		initializeMenuCounts();
		schedule(1000*60*15);
	    }
	};
	refreshTimer.schedule(100);
    }

    /**
     * Set menu counts
     */
    public void initializeMenuCounts() {
	ConfessionBox.confessionService.getMyConfessionNumber(ConfessionBox.loggedInUserInfo.getUserId(), new AsyncCallback<Long>() {

	    @Override
	    public void onSuccess(Long result) {
		display.getBtnMenuItemMyConf().getBtnCount().setText(Long.toString(result));
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("MenuPresenter", "onFailure", caught);
	    }
	});

	ConfessionBox.confessionService.getNumberOfConfessionForMe(ConfessionBox.loggedInUserInfo.getUserId(), new AsyncCallback<Long>() {

	    @Override
	    public void onSuccess(Long result) {
		display.getBtnMenuItemConfToMe().getBtnCount().setText(Long.toString(result));
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("MenuPresenter", "onFailure", caught);
	    }
	});
    }

    @Override
    public void go(HasWidgets container) {
	RootPanel.get(Constants.DIV_LEFT_MENU).clear();
	RootPanel.get(Constants.DIV_LEFT_MENU).add(display.asWidget());		
    }
}