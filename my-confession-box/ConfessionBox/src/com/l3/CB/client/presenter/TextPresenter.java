/**
 * L3 Confession Box
 * 
 * Nov 5, 2012, 8:40:21 PM
 */
package com.l3.CB.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.shared.Constants;

public class TextPresenter implements Presenter {

    public interface Display {
	Widget asWidget();
    }

    private final Display display;

    public TextPresenter(Display display) {
	super();
	this.display = display;
    }

    @Override
    public void go(HasWidgets container) {
	RootPanel.get(Constants.DIV_MAIN_CONTENT).clear();
	RootPanel.get(Constants.DIV_MAIN_CONTENT).add(display.asWidget());	
    }
}