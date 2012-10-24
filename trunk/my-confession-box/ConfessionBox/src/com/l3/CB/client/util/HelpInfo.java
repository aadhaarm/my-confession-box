package com.l3.CB.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.l3.CB.shared.CBText;
import com.l3.CB.shared.Constants;

public class HelpInfo {
	
	static CBText cbText = GWT.create(CBText.class);
	private static ScrollPanel sPnlToolTip = new ScrollPanel();

	public static enum type {
		CONFESSION_FILTER, SAME_BOAT_BUTTON, SYMPATHY_BUTTON, SHOULD_BE_PARDONED_BUTTON, SHOULD_NOT_BE_PARDONED_BUTTON, LAME_BUTTON, ABUSE_BUTTON;
	}
	
	/**
	 * @param confessionFilter 
	 * 
	 */
	public static void showHelpInfo(type confessionFilter) {
		RootPanel.get(Constants.DIV_LEFT_TOOL_TIP).remove(sPnlToolTip);
		sPnlToolTip.clear();
		switch (confessionFilter) {
		case CONFESSION_FILTER:
			sPnlToolTip.add(new Label(cbText.feedPageFilterToolTip()));
			break;
		case ABUSE_BUTTON:
			sPnlToolTip.add(new Label(cbText.abuseButtonToolTip()));
			break;
		case LAME_BUTTON:
			sPnlToolTip.add(new Label(cbText.lameButtonToolTip()));
			break;
		case SAME_BOAT_BUTTON:
			sPnlToolTip.add(new Label(cbText.sameBoatButtonToolTip()));
			break;
		case SHOULD_BE_PARDONED_BUTTON:
			sPnlToolTip.add(new Label(cbText.shouleBePardonedButtonToolTip()));
			break;
		case SHOULD_NOT_BE_PARDONED_BUTTON:
			sPnlToolTip.add(new Label(cbText.shouldNotBePardonedButtonToolTip()));
			break;
		case SYMPATHY_BUTTON:
			sPnlToolTip.add(new Label(cbText.sympathyButtonToolTip()));
			break;
		default:
			break;
		}
		
		RootPanel.get(Constants.DIV_LEFT_TOOL_TIP).add(sPnlToolTip);
	}
}
