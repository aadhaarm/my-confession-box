package com.l3.CB.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.l3.CB.shared.CBText;

public class Error {
	
	private static CBText cbText = GWT.create(CBText.class);
	
	public static void handleError(String string, String string2, Throwable caught) {
		Window.alert(cbText.applicationError());
	}
}
