package com.l3.CB.client.ui.widgets;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FieldVerifier;

public class CBTextArea extends FlowPanel {

    private RichTextArea cbTextArea = null;
    private final Label lblRemainChar;
    private int maxCharsAllowed = 0;
    private final Label lblErrorMsg;

    public CBTextArea(int numOfChars, boolean big) {
	super();
	this.maxCharsAllowed = numOfChars;

	cbTextArea = new RichTextArea();
	if(big) {
	    cbTextArea.setSize("526px", "200px");
	} else {
	    cbTextArea.setSize("526px", "70px");
	}
	this.add(cbTextArea);

	lblRemainChar = new Label(numOfChars + ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	lblRemainChar.setStyleName("ch_remaining");
	this.add(lblRemainChar);

	lblErrorMsg = new Label(ConfessionBox.cbText.confessionTextBoxErrorMessage());
	bind();
    }

    private void bind() {
	cbTextArea.addKeyPressHandler(new KeyPressHandler() {
	    @Override
	    public void onKeyPress(KeyPressEvent event) {
		updateCharsLeft();
	    }
	});
    }

    private void updateCharsLeft() {
	lblRemainChar.setText(maxCharsAllowed - cbTextArea.getText().length() + ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
    }

    /**
     * Validate if text is correct
     * @param min
     * @param max
     * @return {@link Boolean}
     */
    public boolean validate(int min, int max) {
	cbTextArea.setHTML(removeHtmlTags(cbTextArea.getHTML()));
	updateCharsLeft();

	boolean isValid = FieldVerifier.isValidConfession(cbTextArea.getText(), min, max);
	if(!isValid) {
	    this.add(lblErrorMsg);
	    cbTextArea.setStylePrimaryName(Constants.STYLE_CLASS_FIELD_ERROR);
	} else {
	    this.remove(lblErrorMsg);
	    cbTextArea.removeStyleName(Constants.STYLE_CLASS_FIELD_ERROR);
	}
	return isValid;
    }

    private String removeHtmlTags(String html) {
	String regex = "\\<(?!br).*?\\>";
	html = html.replaceAll(regex, "");
	return html;
    }

    public RichTextArea getCbTextArea() {
	return cbTextArea;
    }

    public void setCbTextArea(RichTextArea cbTextArea) {
	this.cbTextArea = cbTextArea;
    }
}