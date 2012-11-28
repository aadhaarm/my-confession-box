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
	CONFESSION_FILTER, SAME_BOAT_BUTTON, SYMPATHY_BUTTON, SHOULD_BE_PARDONED_BUTTON, SHOULD_NOT_BE_PARDONED_BUTTON, LAME_BUTTON, 
	ABUSE_BUTTON, REGISTER_CONF_HIDE_ID_CHECKBOX, REGISTER_CONF_SHARE_WITH_CHECKBOX, UNDO_VOTE, SHARE_VOTE, PARDON_CONDITION_HELP_TEXT, 
	PARDON_CONDITION_UNDHIDE_HELP_TEXT, PARDON_CONDITION_SPVOTE_HELP_TEXT, REGISTER_CONFESSION_TITLE_HELP, REGISTER_CONFESION_OPTIONS;
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
	    sPnlToolTip.add(new Label(cbText.shouldBePardonedButtonToolTip()));
	    break;
	case SHOULD_NOT_BE_PARDONED_BUTTON:
	    sPnlToolTip
	    .add(new Label(cbText.shouldNotBePardonedButtonToolTip()));
	    break;
	case SYMPATHY_BUTTON:
	    sPnlToolTip.add(new Label(cbText.sympathyButtonToolTip()));
	    break;
	case REGISTER_CONF_HIDE_ID_CHECKBOX:
	    sPnlToolTip.add(new Label(cbText
		    .registerConfHideIdCheckBoxToolTip()));
	    break;
	case REGISTER_CONF_SHARE_WITH_CHECKBOX:
	    sPnlToolTip.add(new Label(cbText
		    .registerConfShareWithCheckBoxToolTip()));
	    break;
	case UNDO_VOTE:
	    sPnlToolTip.add(new Label(cbText.undoVoteToolTip()));
	    break;
	case SHARE_VOTE:
	    sPnlToolTip.add(new Label(cbText.shareVoteToolTip()));
	    break;
	case PARDON_CONDITION_HELP_TEXT:
	    sPnlToolTip.add(new Label(cbText.pardonConditionHelpInfoToolTip()));
	    break;
	case PARDON_CONDITION_UNDHIDE_HELP_TEXT:
	    sPnlToolTip.add(new Label(cbText
		    .pardonConditionUnHideHelpInfoToolTip()));
	    break;
	case PARDON_CONDITION_SPVOTE_HELP_TEXT:
	    sPnlToolTip.add(new Label(cbText
		    .pardonConditionSPVoteHelpInfoToolTip()));
	    break;
	case REGISTER_CONFESSION_TITLE_HELP:
	    sPnlToolTip.add(new Label(cbText.registerConfessionTitleHelpInfoToolTip()));
	    break;
	case REGISTER_CONFESION_OPTIONS:
	    sPnlToolTip.add(new Label(cbText.registerConfessionOptionsHelpInfoToolTip()));
	default:
	    break;
	}

	sPnlToolTip.setStyleName(Constants.STYLE_CLASS_HELP_INFO);
	RootPanel.get(Constants.DIV_LEFT_TOOL_TIP).add(sPnlToolTip);
    }
    
    public static void cleanToolTip() {
	sPnlToolTip.clear();
    }
}
