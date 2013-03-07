package com.l3.CB.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.shared.CBText;
import com.l3.CB.shared.Constants;

public class HelpInfo {

    static CBText cbText = GWT.create(CBText.class);
    static FlowPanel tooltipPanel = new FlowPanel();
    private static ScrollPanel sPnlToolTip = new ScrollPanel();

    public static enum type {
	CONFESSION_FILTER, SAME_BOAT_BUTTON, SYMPATHY_BUTTON, SHOULD_BE_PARDONED_BUTTON, SHOULD_NOT_BE_PARDONED_BUTTON, LAME_BUTTON, 
	ABUSE_BUTTON, REGISTER_CONF_HIDE_ID_CHECKBOX, REGISTER_CONF_SHARE_WITH_CHECKBOX, UNDO_VOTE, SHARE_VOTE, PARDON_CONDITION_HELP_TEXT, 
	PARDON_CONDITION_UNDHIDE_HELP_TEXT, PARDON_CONDITION_SPVOTE_HELP_TEXT, REGISTER_CONFESSION_TITLE_HELP, REGISTER_CONFESION_OPTIONS,
	PARDON_BUTTON, SUGGEST_SHARE_VOTE;
    }

    /**
     * @param confessionFilter
     * 
     */
    public static void showHelpInfo(type confessionFilter) {
	sPnlToolTip.setHeight("89px");
	cleanToolTip();
	HTML toolTipTitle = new HTML("Tool Tip");
	toolTipTitle.setStyleName("toolTipTitle");
	tooltipPanel.add(toolTipTitle);

	switch (confessionFilter) {
	case CONFESSION_FILTER:
	    tooltipPanel.add(new Label(cbText.feedPageFilterToolTip()));
	    break;
	case ABUSE_BUTTON:
	    tooltipPanel.add(new Label(cbText.abuseButtonToolTip()));
	    break;
	case LAME_BUTTON:
	    tooltipPanel.add(new Label(cbText.lameButtonToolTip()));
	    break;
	case SAME_BOAT_BUTTON:
	    tooltipPanel.add(new Label(cbText.sameBoatButtonToolTip()));
	    break;
	case SHOULD_BE_PARDONED_BUTTON:
	    tooltipPanel.add(new Label(cbText.shouldBePardonedButtonToolTip()));
	    break;
	case SHOULD_NOT_BE_PARDONED_BUTTON:
	    tooltipPanel
	    .add(new Label(cbText.shouldNotBePardonedButtonToolTip()));
	    break;
	case SYMPATHY_BUTTON:
	    tooltipPanel.add(new Label(cbText.sympathyButtonToolTip()));
	    break;
	case REGISTER_CONF_HIDE_ID_CHECKBOX:
	    tooltipPanel.add(new Label(cbText
		    .registerConfHideIdCheckBoxToolTip()));
	    break;
	case REGISTER_CONF_SHARE_WITH_CHECKBOX:
	    tooltipPanel.add(new Label(cbText
		    .registerConfShareWithCheckBoxToolTip()));
	    break;
	case UNDO_VOTE:
	    tooltipPanel.add(new Label(cbText.undoVoteToolTip()));
	    break;
	case SHARE_VOTE:
	    tooltipPanel.add(new Label(cbText.shareVoteToolTip()));
	    break;
	case PARDON_CONDITION_HELP_TEXT:
	    tooltipPanel.add(new Label(cbText.pardonConditionHelpInfoToolTip()));
	    break;
	case PARDON_CONDITION_UNDHIDE_HELP_TEXT:
	    tooltipPanel.add(new Label(cbText
		    .pardonConditionUnHideHelpInfoToolTip()));
	    break;
	case PARDON_CONDITION_SPVOTE_HELP_TEXT:
	    tooltipPanel.add(new Label(cbText
		    .pardonConditionSPVoteHelpInfoToolTip()));
	    break;
	case REGISTER_CONFESSION_TITLE_HELP:
	    tooltipPanel.add(new Label(cbText.registerConfessionTitleHelpInfoToolTip()));
	    break;
	case REGISTER_CONFESION_OPTIONS:
	    tooltipPanel.add(new Label(cbText.registerConfessionOptionsHelpInfoToolTip()));
	    break;
	case PARDON_BUTTON:
	    tooltipPanel.add(new Label(cbText.pardonButtonHelpInfoToolTip()));
	    break;
	case SUGGEST_SHARE_VOTE:
	    tooltipPanel.add(new Label(cbText.suggestShareVote()));
	    break;
	default:
	    break;
	}
	if(ConfessionBox.isMobile) {
	    final PopupPanel pPnlHelpToolTip = new PopupPanel(true);
	    pPnlHelpToolTip.setGlassEnabled(true);
	    pPnlHelpToolTip.setStyleName("infoModalPopupWindow");
	    pPnlHelpToolTip.add(tooltipPanel);
	    
		PushButton close = new PushButton("Close");
		close.addStyleName("infoModalPopupWindowClose");
		close.addClickHandler(new ClickHandler() {
		    
		    @Override
		    public void onClick(ClickEvent event) {
			pPnlHelpToolTip.hide();
		    }
		});
		tooltipPanel.add(close);

		
	    pPnlHelpToolTip.center();
	} else {
	    sPnlToolTip.add(tooltipPanel);
	    sPnlToolTip.setStyleName(Constants.STYLE_CLASS_HELP_INFO);
	    RootPanel.get(Constants.DIV_LEFT_TOOL_TIP).add(sPnlToolTip);
	}
    }

    public static void cleanToolTip() {
	RootPanel.get(Constants.DIV_LEFT_TOOL_TIP).remove(sPnlToolTip);
	sPnlToolTip.clear();
	tooltipPanel.clear();
    }
}
