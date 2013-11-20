package com.l3.CB.client.presenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateHPEvent;
import com.l3.CB.client.event.UpdateHPEventHandler;

public class HeaderPresenter implements Presenter {

    public interface Display {
	public void initializeMenuCounts(Integer count);
	public HasClickHandlers getApplnTitle();
	public ListBox getFilterListBox();
	Widget asWidget();
    }

    private final Display display;

    public HeaderPresenter(Display display) {
	super();
	this.display = display;

	bind();
    }

    private void bind() {
	// Handle update human points event
	ConfessionBox.eventBus.addHandler(UpdateHPEvent.TYPE, new UpdateHPEventHandler() {
	    @Override
	    public void updateHPContact(UpdateHPEvent event) {
		display.initializeMenuCounts(event.getUpdatedCount());
	    }
	});
    }

    @Override
    public void go(HasWidgets container) {
	display.initializeMenuCounts(null);
	RootPanel.get("header").clear();
	RootPanel.get("header").add(display.asWidget());		
    }
}