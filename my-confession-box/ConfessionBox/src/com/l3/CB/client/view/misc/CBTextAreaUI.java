package com.l3.CB.client.view.misc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FieldVerifier;

public class CBTextAreaUI extends Composite {

    private static CBTextAreaUIUiBinder uiBinder = GWT.create(CBTextAreaUIUiBinder.class);

    interface CBTextAreaUIUiBinder extends UiBinder<Widget, CBTextAreaUI> {
    }

    private String defaultValue = null;
    private int maxCharsAllowed = 0;

    @UiField
    TextArea textArea;

    @UiField
    SpanElement spanMessage;

    @UiField
    SpanElement spanErrorMessage;

    public CBTextAreaUI() {
	initWidget(uiBinder.createAndBindUi(this));
    }

    public void setupWidget(int numOfChars, boolean big, String defaultText) {

	this.maxCharsAllowed = numOfChars;

	// Default value
	this.defaultValue = defaultText;
	textArea.setText(defaultText);
	textArea.getElement().setAttribute("maxlength", Constants.CONF_MAX_CHARS + "");
    }

    private void updateCharsLeft() {
	if((textArea != null && !textArea.getText().equals(defaultValue))  
		|| (textArea != null && !textArea.getText().equals(defaultValue))) {
	    spanMessage.setInnerText(maxCharsAllowed - textArea.getText().length() 
		    + ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	} else {
	    spanMessage.setInnerText(maxCharsAllowed 
		    + ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	}
    }

    /**
     * Validate if text is correct
     * @param min
     * @param max
     * @return {@link Boolean}
     */
    public boolean validate(int min, int max) {
	boolean isValid = false;

	textArea.setText(removeHtmlTags(textArea.getText()));
	updateCharsLeft();

	isValid = FieldVerifier.isValidConfession(textArea.getText(), min, max);

	if(!isValid || defaultValue.equals(textArea.getText())) {
	    showErrorMessage();
	    isValid = false;
	} else {
	    hideErrorMessage();
	}
	
	return isValid;
    }

    private void hideErrorMessage() {
	spanErrorMessage.setInnerText("");
	textArea.removeStyleName(Constants.STYLE_CLASS_DANGER);
    }

    private void showErrorMessage() {
	spanErrorMessage.setInnerText(ConfessionBox.cbText.confessionTextBoxErrorMessage());
	textArea.addStyleName(Constants.STYLE_CLASS_DANGER);
    }

    private String removeHtmlTags(String html) {
	html = html.trim();
	String regex = "\\<(?!br).*?\\>";
	html = html.replaceAll(regex, "");
	return html;
    }

    public String getValue() {
	return textArea.getText();	
    }

    public void disable() {
	textArea.setEnabled(false);
    }

    public void enable() {
	textArea.setEnabled(true);
    }

    @UiHandler("textArea")
    void handleRichOnFocus(FocusEvent event) {
	if(defaultValue.equals(textArea.getText())) {
	    textArea.setText("");
	}
	updateCharsLeft();
    }

    @UiHandler("textArea")
    void handleRichKeyUp(KeyUpEvent event) {
	updateCharsLeft();
    }

    @UiHandler("textArea")
    void handleRichBlurEvent(BlurEvent event) {
	if(textArea.getText().isEmpty()) {
	    textArea.setText(defaultValue);
	}
	updateCharsLeft();
	validate(Constants.CONF_MIN_CHARS, Constants.CONF_MAX_CHARS);
    }

    public void setText(String text) {
	textArea.setText(text);
    }
}