package com.l3.CB.client.view.misc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RichTextArea;
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
    RichTextArea richTextArea;

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
	textArea.setText(defaultValue);
	richTextArea.setText(defaultText);

	if(ConfessionBox.isMobile) {
	    richTextArea.removeFromParent();
	} else {
	    textArea.removeFromParent();	
	}

    }

    public CBTextAreaUI(String firstName) {
	initWidget(uiBinder.createAndBindUi(this));
    }

    private void updateCharsLeft() {
	if(ConfessionBox.isMobile) {
	    if((textArea != null && !textArea.getText().equals(defaultValue))  
		    || (textArea != null && !textArea.getText().equals(defaultValue))) {
		spanMessage.setInnerText(maxCharsAllowed - textArea.getText().length() 
			+ ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	    } else {
		spanMessage.setInnerText(maxCharsAllowed 
			+ ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	    }
	} else {
	    if((richTextArea != null && !richTextArea.getText().equals(defaultValue))  
		    || (richTextArea != null && !richTextArea.getText().equals(defaultValue))) {
		spanMessage.setInnerText(maxCharsAllowed - richTextArea.getText().length() 
			+ ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	    } else {
		spanMessage.setInnerText(maxCharsAllowed 
			+ ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	    }
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

	if(!ConfessionBox.isMobile) {
	    richTextArea.setHTML(removeHtmlTags(richTextArea.getHTML()));
	    updateCharsLeft();

	    isValid = FieldVerifier.isValidConfession(richTextArea.getText(), min, max);

	    if(!isValid || defaultValue.equals(richTextArea.getText())) {
		showErrorMessage();
		isValid = false;
	    } else {
		hideErrorMessage();
	    }
	} else {
	    textArea.setText(removeHtmlTags(textArea.getText()));
	    updateCharsLeft();

	    isValid = FieldVerifier.isValidConfession(textArea.getText(), min, max);
	    if(!isValid || defaultValue.equals(textArea.getText())) {
		showErrorMessage();
		isValid = false;
	    } else {
		hideErrorMessage();
	    }
	}
	return isValid;
    }

    private void hideErrorMessage() {
	spanErrorMessage.setInnerText("");
	textArea.removeStyleName(Constants.STYLE_CLASS_DANGER);
	richTextArea.removeStyleName(Constants.STYLE_CLASS_DANGER);
    }

    private void showErrorMessage() {
	spanErrorMessage.setInnerText(ConfessionBox.cbText.confessionTextBoxErrorMessage());
	textArea.addStyleName(Constants.STYLE_CLASS_DANGER);
	richTextArea.addStyleName(Constants.STYLE_CLASS_DANGER);
    }

    private String removeHtmlTags(String html) {
	html = html.trim();
	String regex = "\\<(?!br).*?\\>";
	html = html.replaceAll(regex, "");
	return html;
    }

    public String getValue() {
	if(ConfessionBox.isMobile) {
	    return textArea.getText();
	} else {
	    return richTextArea.getText();	
	}
    }

    public void disable() {
	if(ConfessionBox.isMobile) {
	    textArea.setEnabled(false);
	} else {
	    richTextArea.setEnabled(false);
	}
    }

    public void enable() {
	if(ConfessionBox.isMobile) {
	    textArea.setEnabled(true);
	} else {
	    richTextArea.setEnabled(true);
	}
    }

    @UiHandler("textArea")
    void handleOnFocus(FocusEvent event) {
	if(defaultValue.equals(textArea.getText())) {
	    textArea.setText("");
	}
    }

    @UiHandler("textArea")
    void handleKeyPress(KeyPressEvent event) {
	updateCharsLeft();
    }

    @UiHandler("textArea")
    void handleBlurEvent(BlurEvent event) {
	if("".equals(textArea.getText())) {
	    textArea.setText(defaultValue);
	}
    }

    @UiHandler("richTextArea")
    void handleRichOnFocus(FocusEvent event) {
	if(defaultValue.equals(richTextArea.getText())) {
	    richTextArea.setText("");
	}
    }

    @UiHandler("richTextArea")
    void handleRichKeyPress(KeyPressEvent event) {
	updateCharsLeft();
    }

    @UiHandler("richTextArea")
    void handleRichBlurEvent(BlurEvent event) {
	if("".equals(richTextArea.getText())) {
	    richTextArea.setText(defaultValue);
	}
    }

    public void setText(String text) {
	if(ConfessionBox.isMobile) {
	    textArea.setText(text);
	} else {
	    richTextArea.setText(text);
	}
    }
}