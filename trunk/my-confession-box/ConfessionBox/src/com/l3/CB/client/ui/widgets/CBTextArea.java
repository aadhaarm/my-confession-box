package com.l3.CB.client.ui.widgets;

import com.google.gwt.user.client.ui.TextArea;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FieldVerifier;

public class CBTextArea extends TextArea {
	public boolean validate() {
		boolean isValid = FieldVerifier.isValidConfession(getText());
		if(!isValid) {
			setStylePrimaryName(Constants.STYLE_CLASS_FIELD_ERROR);
		} else {
			removeStyleName(Constants.STYLE_CLASS_FIELD_ERROR);
		}
		return isValid;
	}
}
