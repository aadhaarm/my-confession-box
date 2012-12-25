package com.l3.CB.client.util;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.ShowToolTipEvent;
import com.l3.CB.client.ui.widgets.ActivityButton;
import com.l3.CB.client.ui.widgets.FBCommentWidget;
import com.l3.CB.client.ui.widgets.FBLikeWidget;
import com.l3.CB.client.ui.widgets.PardonPopupPanel;
import com.l3.CB.client.ui.widgets.Templates;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.UserInfo;

public class FeedViewUtils {

    /**
     * UNDO tool-tip
     * 
     * @return HTML Undo tool-tip
     */
    public static HTML getUndoToolTipBar() {
	HTML undoTooltip = new HTML(Templates.TEMPLATES.undoToolTip(ConfessionBox.cbText.undoToolTipBar())); 
	undoTooltip.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		HelpInfo.showHelpInfo(HelpInfo.type.UNDO_VOTE);
	    }
	});
	undoTooltip.setStyleName(Constants.STYLE_CLASS_TOOLTIP_LEFT);
	return undoTooltip;
    }

    /**
     * SHARE tool-tip
     * 
     * @return HTML Share tool-tip
     */
    public static HTML getShareToolTipBar() {
	HTML shareTooltip = new HTML(Templates.TEMPLATES.shareToolTip(ConfessionBox.cbText.shareToolTipBar())); 
	shareTooltip.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		HelpInfo.showHelpInfo(HelpInfo.type.SHARE_VOTE);
	    }
	});
	shareTooltip.setStyleName(Constants.STYLE_CLASS_TOOLTIP_RIGHT);
	return shareTooltip;
    }

    /**
     * Confession to me tool-tip
     * 
     * @return HTML Share tool-tip
     */
    public static HTML getConfessedToMeToolTipBar(UserInfo confessee) {
	HTML confessedToMeTooltip = new HTML(Templates.TEMPLATES.confessedToYouWallPardonMessage(confessee.getName(), getHisText(confessee.getGender()), getHimText(confessee.getGender()))); 
	confessedToMeTooltip.setStyleName(Constants.STYLE_CLASS_CONFESS_TO_ME_TOOLTIP);
	return confessedToMeTooltip;
    }


    private static String getHimText(String gender) {
	return gender != null && "male".equals(gender)? "Him" : "her";
    }

    private static String getHisText(String gender) {
	return gender != null && "male".equals(gender)? "His" : "Her";
    }

    /**
     * Show pardon widget
     * 1. Pardon conditions
     * 2. pardon button
     * 
     * @param confession
     * @param anynView
     * @param confessionByUser
     * @return
     */
    public static FlowPanel getPardonWidget(final Confession confession, boolean anynView, final UserInfo confessionByUser) {
	FlowPanel fPnlPardon = new FlowPanel();
	if(confession.getConfessedTo() != null && !confession.getConfessedTo().isEmpty()) {

	    List<ConfessionShare> confessionShares = confession.getConfessedTo();

	    for (ConfessionShare confessionShare : confessionShares) {

		if(confessionShare.getPardonConditions() != null && !confessionShare.getPardonConditions().isEmpty()) {
		    fPnlPardon.setStyleName(Constants.STYLE_CLASS_PARDON_CONDITION_PANEL);

		    HTML pardonConditionInfoText = new HTML(Templates.TEMPLATES.pardonConditionInfoText(ConfessionBox.cbText.pardonConditionInfoText()));
		    pardonConditionInfoText.setStyleName(Constants.STYLE_CLASS_PARDON_CONDITION);

		    pardonConditionInfoText.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			    HelpInfo.showHelpInfo(HelpInfo.type.PARDON_CONDITION_HELP_TEXT);
			}
		    });

		    fPnlPardon.add(pardonConditionInfoText);

		    // Add conditions for pardon 
		    addPardonConditionStatus(confessionShare.getPardonConditions(), fPnlPardon);

		} else if(!anynView) {
		    fPnlPardon.setStyleName(Constants.STYLE_CLASS_PARDON_BUTTON_PANEL);
		    final Button btnPardon = new Button(ConfessionBox.cbText.pardonButtonLabelText());
		    btnPardon.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
			    if(!ConfessionBox.isMobile) {
				HelpInfo.showHelpInfo(HelpInfo.type.PARDON_BUTTON);
			    }
			}
		    });
		    btnPardon.setStyleName(Constants.STYLE_CLASS_PARDON_BUTTON);
		    if(confessionShare.getPardonStatus() != null) {
			switch (confessionShare.getPardonStatus()) {
			case PARDONED:
			    btnPardon.setEnabled(false);
			    break;
			default:
			    final PardonPopupPanel pardonPopupPanel = new PardonPopupPanel(confession, confessionByUser, btnPardon);
			    pardonPopupPanel.setAnimationEnabled(true);
			    pardonPopupPanel.setGlassEnabled(true);
			    pardonPopupPanel.setStyleName(Constants.STYLE_CLASS_PARDON_MODAL);

			    btnPardon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
				    pardonPopupPanel.center();
				    if(pardonPopupPanel.getElement() != null) {
					pardonPopupPanel.getElement().setId(Constants.PARDON_GRID_ID);
					CommonUtils.parseXFBMLJS(DOM.getElementById(Constants.PARDON_GRID_ID));
				    }
				}
			    });
			    break;
			}
		    } 
		    fPnlPardon.add(btnPardon);
		}
	    }
	}
	return fPnlPardon;
    }

    /**
     * Add pardon conditions tp pardon widget 
     * @param pardonConditions
     * @param fPnlPardon
     */
    public static void addPardonConditionStatus(List<PardonCondition> pardonConditions, FlowPanel fPnlPardon) {
	int countCondition = 1;
	if(pardonConditions != null && !pardonConditions.isEmpty()) {
	    FlowPanel fPnlCondition = new FlowPanel();
	    for (PardonCondition pardonCondition : pardonConditions) {
		if(pardonCondition != null) {
		    if(Constants.pardonConditionUnhide.equalsIgnoreCase((pardonCondition.getCondition()))) {

			String statusTick = "";
			if(pardonCondition.isFulfil()) {
			    statusTick = Constants.TICK_MARK;
			}
			HTML pardonConditionInfoText = new HTML(Templates.TEMPLATES.pardonCondition(countCondition + ". "
				+ ConfessionBox.cbText.pardonPopupOpenIdentityConditionView() + ".", statusTick));
			pardonConditionInfoText.setStyleName("pardonCondition");
			pardonConditionInfoText.addClickHandler(new ClickHandler() {
			    @Override
			    public void onClick(ClickEvent event) {
				HelpInfo.showHelpInfo(HelpInfo.type.PARDON_CONDITION_UNDHIDE_HELP_TEXT);
			    }
			});
			fPnlCondition.add(pardonConditionInfoText);
			fPnlCondition.setStyleName("pardonConditionList");
			fPnlPardon.add(fPnlCondition);
		    } else if(Constants.pardonConditionSPVotes.equalsIgnoreCase((pardonCondition.getCondition()))) {
			String statusTick = "";
			if(pardonCondition.isFulfil()) {
			    statusTick = Constants.TICK_MARK;
			}
			HTML pardonConditionInfoText = new HTML(Templates.TEMPLATES.pardonCondition(countCondition + ". " + ConfessionBox.cbText.pardonPopupPardonActivityConditionPartOne()
				+ pardonCondition.getCount() + ConfessionBox.cbText.pardonPopupPardonActivityConditionPartTwo(),
				statusTick));
			pardonConditionInfoText.setStyleName("pardonCondition");
			pardonConditionInfoText.addClickHandler(new ClickHandler() {
			    @Override
			    public void onClick(ClickEvent event) {
				HelpInfo.showHelpInfo(HelpInfo.type.PARDON_CONDITION_SPVOTE_HELP_TEXT);
			    }
			});
			fPnlCondition.add(pardonConditionInfoText);
			fPnlCondition.setStyleName("pardonConditionList");
			fPnlPardon.add(fPnlCondition);
		    }
		}
		countCondition++;
	    }}
    }

    public static FlowPanel getUserActivityButtons(final Confession confession) {
	final FlowPanel fPlaneUserActivity = new FlowPanel();
	fPlaneUserActivity.setStyleName(Constants.STYLE_CLASS_ACTIVITY_BUTTON_DIV);
	final ActivityButton btnSB = (ActivityButton) getSBButton(confession);
	final ActivityButton btnSY = (ActivityButton) getSYButton(confession);
	final ActivityButton btnLM = (ActivityButton) getLMButton(confession);
	final ActivityButton btnSP = (ActivityButton) getSPButton(confession);
	final ActivityButton btnSNP = (ActivityButton) getSNPButton(confession);
	final ActivityButton btnAB = (ActivityButton) getABButton(confession);

	int i = 0;

	if (confession.getActivityCount() != null) {
	    if (confession.getActivityCount().containsKey(Activity.ABUSE.name())) {
		i++;
		btnAB.disableBtn();
	    }
	    if (confession.getActivityCount().containsKey(Activity.LAME.name())) {
		i++;
		btnLM.disableBtn();
	    }
	    if (confession.getActivityCount().containsKey(Activity.SAME_BOAT.name())) {
		i++;
		btnSB.disableBtn();
	    }
	    if (confession.getActivityCount().containsKey(Activity.SHOULD_BE_PARDONED
		    .name())) {
		i++;
		btnSP.disableBtn();
	    }
	    if (confession.getActivityCount()
		    .containsKey(Activity.SHOULD_NOT_BE_PARDONED
			    .name())) {
		i++;
		btnSNP.disableBtn();
	    }
	    if (confession.getActivityCount().containsKey(Activity.SYMPATHY.name())) {
		i++;
		btnSY.disableBtn();
	    }
	    if(i > 0) {
		ConfessionBox.eventBus.fireEvent(new ShowToolTipEvent(confession.getConfId()));
	    }
	}

	fPlaneUserActivity.add(btnSB);
	fPlaneUserActivity.add(btnSY);
	fPlaneUserActivity.add(btnLM);
	fPlaneUserActivity.add(btnSP);
	fPlaneUserActivity.add(btnSNP);
	fPlaneUserActivity.add(btnAB);

	return fPlaneUserActivity;
    }

    public static Widget getABButton(final Confession confession) {
	return new ActivityButton(Activity.ABUSE, confession, ConfessionBox.cbText.buttonTitleReportAbuse(),
		Constants.STYLE_CLASS_ABUSE_ACTIVITY, new Image("/images/flag.png",0,0,32,36));
    }

    public static Widget getSNPButton(final Confession confession) {
	return new ActivityButton(Activity.SHOULD_NOT_BE_PARDONED, confession, ConfessionBox.cbText.buttonTitleShouldNotBePardoned(),
		Constants.STYLE_CLASS_SNP_ACTIVITY, new Image("/images/SNBP.png",0,0,32,36));
    }

    public static Widget getSPButton(final Confession confession) {
	return new ActivityButton(Activity.SHOULD_BE_PARDONED, confession, ConfessionBox.cbText.buttonTitleShouldBePardoned(),
		Constants.STYLE_CLASS_SP_ACTIVITY, new Image("/images/SBP.png",0,0,32,36));
    }

    public static Widget getLMButton(final Confession confession) {
	return new ActivityButton(Activity.LAME, confession, ConfessionBox.cbText.buttonTitleLameConfession(),
		Constants.STYLE_CLASS_LAME_ACTIVITY, new Image("/images/lame.png",0,0,32,36));
    }

    public static Widget getSYButton(final Confession confession) {
	return new ActivityButton(Activity.SYMPATHY, confession, ConfessionBox.cbText.buttonTitleSympathy(),
		Constants.STYLE_CLASS_SYM_ACTIVITY, new Image("/images/sympathies.png",0,0,32,36));
    }

    public static Widget getSBButton(final Confession confession) {
	return new ActivityButton(Activity.SAME_BOAT, confession, ConfessionBox.cbText.buttonTitleSameBoat(),
		Constants.STYLE_CLASS_SB_ACTIVITY, new Image("/images/SameBoat.png",0,0,32,36));
    }

    public static Widget getLikeButton(Long confId) {
	return new FBLikeWidget(confId).getFbLikeHtml();
    }

    public static Widget getCommentSection(Long confId) {
	return new FBCommentWidget(confId).getFbCommentHtml();
    }
}
