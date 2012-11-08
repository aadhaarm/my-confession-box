package com.l3.CB.client.ui.widgets;

import com.google.gwt.user.client.ui.RichTextArea;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FieldVerifier;

public class CBTextArea extends RichTextArea {
    public boolean validate() {
	boolean isValid = FieldVerifier.isValidConfession(getText());
	if(!isValid) {
	    setStylePrimaryName(Constants.STYLE_CLASS_FIELD_ERROR);
	} else {
	    removeStyleName(Constants.STYLE_CLASS_FIELD_ERROR);
	}
	return isValid;
    }
    
    private String removeHtmlTags(String html) {
        String regex = "\\<(?!br).*?\\>";
        html = html.replaceAll(regex, "");
        return html;
    }
    
    public boolean validate(int min, int max) {
	setHTML(removeHtmlTags(getHTML()));
	
	boolean isValid = FieldVerifier.isValidConfession(getText(), min, max);
	if(!isValid) {
	    setStylePrimaryName(Constants.STYLE_CLASS_FIELD_ERROR);
	} else {
	    removeStyleName(Constants.STYLE_CLASS_FIELD_ERROR);
	}
	return isValid;
    }
}
