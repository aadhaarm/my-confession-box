package com.l3.CB.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ui.widgets.MenuButton;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.view.MenuView;
import com.l3.CB.shared.Constants;

public class MenuPresenter implements Presenter {

    public interface Display {
	public HTML setFeedItemSelected();
	public HTML getFeedItem();
	public HTML getConfessItem();
	public HTML getMyConItem();
	public HTML getConToMeItem();
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
	if(ConfessionBox.isLoggedIn) {
	    initializeMenuCounts();
	}
    }

    /**
     * Set menu counts
     */
    public void initializeMenuCounts() {
	ConfessionBox.confessionService.getMyConfessionNumber(ConfessionBox.getLoggedInUserInfo().getUserId(), new AsyncCallback<Long>() {

	    @Override
	    public void onSuccess(Long result) {
		display.getBtnMenuItemMyConf().getBtnCount().setText(Long.toString(result));
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("MenuPresenter", "onFailure", caught);
	    }
	});

	ConfessionBox.confessionService.getNumberOfConfessionForMe(ConfessionBox.getLoggedInUserInfo().getUserId(), new AsyncCallback<Long>() {

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
    
    public void selectMenuItem(int item) {
	MenuView.selectMenuItem(item);
    }
}