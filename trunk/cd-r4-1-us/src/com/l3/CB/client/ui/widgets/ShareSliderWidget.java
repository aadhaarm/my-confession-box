package com.l3.CB.client.ui.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.l3.CB.shared.Constants;

public class ShareSliderWidget extends FlowPanel implements HasClickHandlers {

    private Anchor btnYes; 
    private Anchor btnNo;

    private boolean selectionStatus = false;

    public ShareSliderWidget(boolean defaultStatus) {

	selectionStatus = defaultStatus;

	this.setStyleName("yesNoBar");

	btnNo = new Anchor("No");
	btnYes = new Anchor("Yes");

	if(selectionStatus) {
	    btnYes.setEnabled(false);
	    btnYes.setStyleName(Constants.STYLE_CLASS_YES_LINK_TO_BTN_S);
	    btnNo.setStyleName(Constants.STYLE_CLASS_NO_LINK_TO_BTN_U);
	} else {
	    btnNo.setEnabled(false);
	    btnYes.setStyleName(Constants.STYLE_CLASS_YES_LINK_TO_BTN_U);
	    btnNo.setStyleName(Constants.STYLE_CLASS_NO_LINK_TO_BTN_S);
	}

//	btnNo.addStyleName(Constants.DIV_OPTIONS_NO_BUTTON);
//	btnYes.addStyleName(Constants.DIV_OPTIONS_YES_BUTTON);

	this.add(btnNo);
	this.add(btnYes);

	btnNo.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		selectionStatus = false;
		btnNo.setEnabled(false);
		btnYes.setEnabled(true);
		btnYes.setStyleName(Constants.STYLE_CLASS_YES_LINK_TO_BTN_U);
		btnNo.setStyleName(Constants.STYLE_CLASS_NO_LINK_TO_BTN_S);
	    }
	});

	btnYes.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		selectionStatus = true;
		btnNo.setEnabled(true);
		btnYes.setEnabled(false);
		btnYes.setStyleName(Constants.STYLE_CLASS_YES_LINK_TO_BTN_S);
		btnNo.setStyleName(Constants.STYLE_CLASS_NO_LINK_TO_BTN_U);
	    }
	});
    }

    public boolean isSelectionStatus() {
	return selectionStatus;
    }

    public void setSelectionStatus(boolean selectionStatus) {
	this.selectionStatus = selectionStatus;
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
	return addDomHandler(handler, ClickEvent.getType());
    }
}