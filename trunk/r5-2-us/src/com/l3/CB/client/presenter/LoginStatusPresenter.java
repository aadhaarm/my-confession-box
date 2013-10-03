package com.l3.CB.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.shared.Constants;

public class LoginStatusPresenter implements Presenter {

    public interface Display {
	Widget asWidget();
    }

    private final Display display;

    public LoginStatusPresenter(Display display) {
	super();
	this.display = display;
	bind();
    }

    private void bind() {
	
    }

    @Override
    public void go(HasWidgets container) {
	RootPanel.get(Constants.DIV_LOGIN_STATUS_BAR).clear();
	RootPanel.get(Constants.DIV_LOGIN_STATUS_BAR).add(display.asWidget());		
    }
}