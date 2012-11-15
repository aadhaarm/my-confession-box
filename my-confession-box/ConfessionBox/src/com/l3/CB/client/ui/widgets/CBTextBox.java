package com.l3.CB.client.ui.widgets;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FieldVerifier;

public class CBTextBox extends FlowPanel {

    private final TextBox txtTitle;
//    private final Label lblErrorMsg;
    private final String defaultValue;

    public CBTextBox() {
	super();
	defaultValue = "Enter Confession Title here..";
//	lblErrorMsg = new Label(ConfessionBox.cbText.confessionTitleErrorMessage());
	this.setStyleName("reg_confession_title");

	txtTitle = new TextBox();
	txtTitle.setMaxLength(70);
	txtTitle.setWidth("526px");
	txtTitle.removeStyleName("gwt-TextBox");
	txtTitle.setText(defaultValue);
	this.add(txtTitle);

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

    public boolean validate() {
	boolean isValid = FieldVerifier.isValidTitle(txtTitle.getText());
	if(!isValid || defaultValue.equals(txtTitle.getText())) {
//	    this.add(lblErrorMsg);
	    txtTitle.setStyleName(Constants.STYLE_CLASS_FIELD_ERROR);
	} else {
//	    this.remove(lblErrorMsg);
	    txtTitle.removeStyleName(Constants.STYLE_CLASS_FIELD_ERROR);
	}
	return isValid;
    }

    public TextBox getTxtTitle() {
	return txtTitle;
    }
}