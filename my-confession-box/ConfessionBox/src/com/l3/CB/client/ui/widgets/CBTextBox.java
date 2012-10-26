package com.l3.CB.client.ui.widgets;

import com.google.gwt.user.client.ui.TextBox;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FieldVerifier;

public class CBTextBox extends TextBox {
	public boolean validate() {
		boolean isValid = FieldVerifier.isValidTitle(getText());
		if(!isValid) {
			setStylePrimaryName(Constants.STYLE_CLASS_FIELD_ERROR);
		} else {
			removeStyleName(Constants.STYLE_CLASS_FIELD_ERROR);
		}
		return isValid;
	}
}
