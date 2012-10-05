package com.l3.CB.client.ui.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

public class Like {

	public Like() {
	    Element likeElement = DOM.createElement("fb:like");
	  }

	  public Like(final String href) {
	    this();
	  }
}
