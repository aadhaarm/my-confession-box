package com.l3.CB.client.presenter;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.shared.Constants;

public class FooterPresenter implements Presenter {

    public interface Display {
	public Anchor getAncAbout();
	public Anchor getAncPrivacy();
	Widget asWidget();
    }

    private final Display display;

    public FooterPresenter(Display display) {
	super();
	this.display = display;
	bind();
    }

    private void bind() {
	
    }

    @Override
    public void go(HasWidgets container) {
	RootPanel.get(Constants.DIV_FOOTER).clear();
	RootPanel.get(Constants.DIV_FOOTER).add(display.asWidget());		
    }
}