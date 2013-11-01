package com.l3.CB.client.view.misc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.presenter.TextPresenter;

public class PopupPanelUI extends Composite implements TextPresenter.Display {

    private static PopupPanelUIUiBinder uiBinder = GWT
	    .create(PopupPanelUIUiBinder.class);

    interface PopupPanelUIUiBinder extends UiBinder<Widget, PopupPanelUI> {
    }
    
    @UiField
    SpanElement spanHeading;
    
    @UiField
    HTML spanText;
    
    public PopupPanelUI() {
	initWidget(uiBinder.createAndBindUi(this));
    }

    public PopupPanelUI(String heading, String text) {
	initWidget(uiBinder.createAndBindUi(this));
	setText(heading, text);
    }

    
    /**
     * @param heading
     * @param text
     */
    private void setText(String heading, String text) {
	spanHeading.setInnerHTML(heading);
	spanText.setHTML(text);
    }
}