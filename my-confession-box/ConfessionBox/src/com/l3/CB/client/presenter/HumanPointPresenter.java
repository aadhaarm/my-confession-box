package com.l3.CB.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;

public class HumanPointPresenter implements Presenter {

    public interface Display {
	Widget asWidget();
	public void setPoint(int points);
    }

    private final Display display;

    public HumanPointPresenter(Display display) {
	super();
	this.display = display;

	bind();
    }

    private void bind() {
	update();
    }

    @Override
    public void go(HasWidgets container) {
	if(!ConfessionBox.isMobile) {
	    RootPanel.get(Constants.DIV_LEFT_HUMAN_POINT).clear();
	    RootPanel.get(Constants.DIV_LEFT_HUMAN_POINT).add(display.asWidget());		
	}
    }

    public void update() {
	ConfessionBox.confessionService.getHumanPoints(ConfessionBox.getLoggedInUserInfo().getUserId(), new AsyncCallback<Integer>() {

	    @Override
	    public void onSuccess(Integer result) {
		display.setPoint(result);
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("HumanPointPresenter", "onFailure", caught);
	    }
	});
    }

    /**
     * UPDATE HUMAN POINTS and REFRESH HUMAN POINTS BAR
     * @param activity
     */
    public void updateHumanPoints(final int points) {
	ConfessionBox.confessionService.updateHumanPoints(ConfessionBox.getLoggedInUserInfo().getUserId(), points, new AsyncCallback<Integer>() {

	    @Override
	    public void onSuccess(Integer result) {
		display.setPoint(result);
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("ActivityButton", "getActivityTimer", caught);
	    }
	});
    }
}