package com.l3.CB.client.view.misc;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

public class CBButton extends Button {

    boolean value;

    private CBButton() {
	super();

	this.setStyleName("uk-button uk-button-large");
	
	bind();
    }

    private void bind() {
	
	this.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		value = !value;
		setState();
	    }

	});
    }

    /**
     * 
     */
    private void setState() {
	if(value) {
	    addStyleName("uk-button-primary");
	} else {
	    removeStyleName("uk-button-primary");
	}
    }

    public boolean getValue() {
	return value;
    }

    public void setValue(boolean value) {
	this.value = value;
	setState();
    }
}