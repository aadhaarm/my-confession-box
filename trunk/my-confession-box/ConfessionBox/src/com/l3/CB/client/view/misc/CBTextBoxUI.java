package com.l3.CB.client.view.misc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FieldVerifier;

public class CBTextBoxUI extends Composite {

    private static CBTextBoxUIUiBinder uiBinder = GWT
	    .create(CBTextBoxUIUiBinder.class);

    interface CBTextBoxUIUiBinder extends UiBinder<Widget, CBTextBoxUI> {
    }

    private final String defaultValue;
    
    private int numOfChars = 70;

    @UiField
    TextBox txtTitle;

    @UiField
    SpanElement lblRemainChar;

    @UiField
    SpanElement spanErrorMessage;

    public CBTextBoxUI() {
	initWidget(uiBinder.createAndBindUi(this));
	defaultValue = "Enter Confession Title here..";

	txtTitle.setText(defaultValue);

    }

    public CBTextBoxUI(String firstName) {
	initWidget(uiBinder.createAndBindUi(this));
	defaultValue = "Enter Confession Title here..";
    }

    public boolean validate() {
	updateCharsLeft();
	String text = txtTitle.getText();
	text = text.trim();
	text = SimpleHtmlSanitizer.sanitizeHtml(text).asString();
	txtTitle.setText(text);
	boolean isValid = FieldVerifier.isValidTitle(txtTitle.getText());
	if(!isValid || defaultValue.equals(txtTitle.getText())) {
	    showErrorMessage();
	    isValid = false;
	} else {
	    hideErrorMessage();
	}
	return isValid;
    }

    private void hideErrorMessage() {
	spanErrorMessage.setInnerText("");
    }

    private void showErrorMessage() {
	spanErrorMessage.setInnerText(ConfessionBox.cbText.confessionTextBoxErrorMessage());
    }

    
    public String getValue() {
	return txtTitle.getText();
    }

    private void updateCharsLeft() {
	if(!txtTitle.getText().equals(defaultValue)){
	    lblRemainChar.setInnerText(numOfChars - txtTitle.getText().length() + ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	} else {
	    lblRemainChar.setInnerText(numOfChars + ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	}
    }

    public void disable() {
	txtTitle.setEnabled(false);
    }

    public void enable() {
	txtTitle.setEnabled(true);
    }

    @UiHandler("txtTitle")
    void handleOnFocus(FocusEvent event) {
	if(defaultValue.equals(txtTitle.getText())) {
	    txtTitle.setText("");
	}
    }

    @UiHandler("txtTitle")
    void handleKeyPress(KeyPressEvent event) {
	updateCharsLeft();
    }

    @UiHandler("txtTitle")
    void handleBlurEvent(BlurEvent event) {
	if("".equals(txtTitle.getText())) {
	    txtTitle.setText(defaultValue);
	}
    }
}