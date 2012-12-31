package com.l3.CB.client.ui.widgets;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
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
    private String defaultValue = null;

    public CBTextArea(int numOfChars, boolean big, String defaultText) {
	super();
	this.maxCharsAllowed = numOfChars;
	
	
	cbTextArea = new RichTextArea();
	cbTextArea.setStyleName("confessionTextEditor");
	
	// Default value
	defaultValue = defaultText;
	// Set default text
	setUpDefaultText();
	
	if(big) {
	    cbTextArea.setSize(Integer.toString(getCommentWidth()) + "px", "200px");
	} else {
	    cbTextArea.setSize(Integer.toString(getCommentWidth()) + "px", "70px");
	}
	this.add(cbTextArea);
	
	lblRemainChar = new Label(numOfChars + ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	lblRemainChar.setStyleName("ch_remaining");
	this.add(lblRemainChar);

	lblErrorMsg = new Label(ConfessionBox.cbText.confessionTextBoxErrorMessage());
	bind();
    }
    
    /**
     * @return
     */
    private int getCommentWidth() {
	if(ConfessionBox.isMobile) {
	    int width = Window.getClientWidth() - 40;
	    if(width <= 500) {
		return width;
	    } else {
		return 565;
	    }
	} else {
	    return 565;
	}
    }


    /**
     * 
     */
    private void setUpDefaultText() {
	cbTextArea.setText(defaultValue);
	cbTextArea.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if(defaultValue.equals(cbTextArea.getText())) {
		    cbTextArea.setText("");
		}
	    }
	});

	cbTextArea.addBlurHandler(new BlurHandler() {
	    @Override
	    public void onBlur(BlurEvent event) {
		if("".equals(cbTextArea.getText())) {
		    cbTextArea.setText(defaultValue);
		}
	    }
	});
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
	if(!cbTextArea.getText().equals(defaultValue)) {
	    lblRemainChar.setText(maxCharsAllowed - cbTextArea.getText().length() + ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	} else {
	    lblRemainChar.setText(maxCharsAllowed + ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	}
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
	if(!isValid || defaultValue.equals(cbTextArea.getText())) {
	    this.add(lblErrorMsg);
	    cbTextArea.setStylePrimaryName(Constants.STYLE_CLASS_FIELD_ERROR);
	    isValid = false;
	} else {
	    this.remove(lblErrorMsg);
	    cbTextArea.removeStyleName(Constants.STYLE_CLASS_FIELD_ERROR);
	}
	return isValid;
    }

    private String removeHtmlTags(String html) {
	html = html.trim();
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

    public void disable() {
	cbTextArea.setEnabled(false);
    }
    
    public void enable() {
	cbTextArea.setEnabled(true);
    }

}