package com.l3.CB.client.ui.widgets;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FieldVerifier;

public class CBTextBox extends FlowPanel {

    private final TextBox txtTitle;
    private final String defaultValue;
    private final Label lblRemainChar;
    private int numOfChars = 70;
    
    public CBTextBox() {
	super();
	defaultValue = "Enter Confession Title here..";
	this.setStyleName("reg_confession_title");

	txtTitle = new TextBox();
	txtTitle.setMaxLength(70);
	txtTitle.setWidth(Integer.toString(getCommentWidth()) + "px");
	txtTitle.removeStyleName("gwt-TextBox");
	txtTitle.setText(defaultValue);
	this.add(txtTitle);
	
	lblRemainChar = new Label(numOfChars + ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	lblRemainChar.setStyleName("ch_remaining");
	this.add(lblRemainChar);
	txtTitle.addKeyPressHandler(new KeyPressHandler() {
	    @Override
	    public void onKeyPress(KeyPressEvent event) {
		updateCharsLeft();
	    }
	});
	
	txtTitle.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(defaultValue.equals(txtTitle.getText())) {
		    txtTitle.setText("");
		}
	    }
	});

	txtTitle.addBlurHandler(new BlurHandler() {

	    @Override
	    public void onBlur(BlurEvent event) {
		if("".equals(txtTitle.getText())) {
		    txtTitle.setText("Enter Confession Title here..");
		}
	    }
	});
    }

    /**
     * @return
     */
    private int getCommentWidth() {
	if(ConfessionBox.isMobile) {
	    int width = Window.getClientWidth() - 50;
		return width;
//		if(width <= 500) {
//	    } else {
//		return 565;
//	    }
	} else {
	    return 565;
	}
    }

    
    public boolean validate() {
	updateCharsLeft();
	String text = txtTitle.getText();
	text = text.trim();
	text = SimpleHtmlSanitizer.sanitizeHtml(text).asString();
	txtTitle.setText(text);
	boolean isValid = FieldVerifier.isValidTitle(txtTitle.getText());
	if(!isValid || defaultValue.equals(txtTitle.getText())) {
	    txtTitle.setStyleName(Constants.STYLE_CLASS_FIELD_ERROR);
	    isValid = false;
	} else {
	    txtTitle.removeStyleName(Constants.STYLE_CLASS_FIELD_ERROR);
	}
	return isValid;
    }

    public TextBox getTxtTitle() {
	return txtTitle;
    }
    
    private void updateCharsLeft() {
	if(!txtTitle.getText().equals(defaultValue)){
	    lblRemainChar.setText(numOfChars - txtTitle.getText().length() + ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	} else {
	    lblRemainChar.setText(numOfChars + ConfessionBox.cbText.confessionTextBoxRemainingCharactersMessage());
	}
    }

    public void disable() {
	txtTitle.setEnabled(false);
    }
    
    public void enable() {
	txtTitle.setEnabled(true);
    }

}