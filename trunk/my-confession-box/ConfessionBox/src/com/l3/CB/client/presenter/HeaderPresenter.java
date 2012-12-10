package com.l3.CB.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;

public class HeaderPresenter implements Presenter {

    public interface Display {
	public void initializeMenuCounts();
	public HasClickHandlers getApplnTitle();
	Widget asWidget();
    }

    private final Display display;

    public HeaderPresenter(Display display) {
	super();
	this.display = display;

	bind();
    }

    private void bind() {
	display.getApplnTitle().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		Window.Location.reload();
	    }
	});
    }

    @Override
    public void go(HasWidgets container) {
	if(ConfessionBox.isMobile) {
	    display.initializeMenuCounts();
	}
	RootPanel.get("header").clear();
	RootPanel.get("header").add(display.asWidget());		
    }
}