package com.l3.CB.client.util;

import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ui.widgets.ActivityButton;
import com.l3.CB.client.ui.widgets.FBCommentWidget;
import com.l3.CB.client.ui.widgets.FBLikeWidget;
import com.l3.CB.client.ui.widgets.PardonPopupPanel;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.UserInfo;

public class FeedViewUtils {

    public static FlowPanel getPardonWidget(final Confession confession, boolean anynView, final UserInfo confessionByUser) {
	FlowPanel fPnlPardon = new FlowPanel();
	if(confession.getConfessedTo() != null && !confession.getConfessedTo().isEmpty()) {
	    List<ConfessionShare> confessionShares = confession.getConfessedTo();
	    for (ConfessionShare confessionShare : confessionShares) {
		if(confessionShare.getPardonConditions() != null && !confessionShare.getPardonConditions().isEmpty()) {
		    fPnlPardon.setStyleName("condition");
		    Label lblConfession = new Label("Pardoned with condition: This Confession has been pardoned with the following conditions.");
		    lblConfession.setStyleName("pardonCondition");
		    fPnlPardon.add(lblConfession);

		    Anchor ancConditionHelpInfo = new Anchor("[?]");
		    fPnlPardon.add(ancConditionHelpInfo);
		    // Add conditions for pardon 
		    addPardonConditionStatus(confessionShare.getPardonConditions(), fPnlPardon);
		} else if(!anynView) {
		    final Button btnPardon = new Button("Pardon");
		    if(confessionShare.getPardonStatus() != null) {
			switch (confessionShare.getPardonStatus()) {
			case PARDONED:
			    btnPardon.setEnabled(false);
			    break;
			default:
			    final PardonPopupPanel pardonPopupPanel = new PardonPopupPanel(confession, confessionByUser, btnPardon);
			    pardonPopupPanel.setAnimationEnabled(true);
			    pardonPopupPanel.setGlassEnabled(true);
			    pardonPopupPanel.addStyleName(Constants.STYLE_CLASS_PARDON_MODAL);

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

    public static void addPardonConditionStatus(List<PardonCondition> pardonConditions, FlowPanel fPnlPardon) {
	int countCondition = 1;
	for (PardonCondition pardonCondition : pardonConditions) {
	    if(pardonCondition != null) {
		FlowPanel fPnlCondition = new FlowPanel();
		if(Constants.pardonConditionUnhide.equalsIgnoreCase((pardonCondition.getCondition()))) {
		    Label lblCondition = new Label(countCondition + ". " + ConfessionBox.cbText.pardonPopupOpenIdentityConditionView() + ".");
		    lblCondition.setStyleName("pardonCondition");
		    Anchor ancConditionHelpInfo = new Anchor("[?]");
		    fPnlCondition.add(lblCondition);
		    fPnlCondition.add(ancConditionHelpInfo);
		    if(pardonCondition.isFulfil()) {
			HTML tick = new HTML("&#10003;");
			tick.setStyleName("conditionTick");
			fPnlCondition.add(tick);
		    }
		    fPnlCondition.setStyleName("pardonConditionList");
		    fPnlPardon.add(fPnlCondition);
		} else if(Constants.pardonConditionSPVotes.equalsIgnoreCase((pardonCondition.getCondition()))) {
		    Label lblCondition = new Label(countCondition + ". " + ConfessionBox.cbText.pardonPopupPardonActivityConditionPartOne() + pardonCondition.getCount() + ConfessionBox.cbText.pardonPopupPardonActivityConditionPartTwo());
		    lblCondition.setStyleName("pardonCondition");
		    Anchor ancConditionHelpInfo = new Anchor("[?]");
		    fPnlCondition.add(lblCondition);
		    fPnlCondition.add(ancConditionHelpInfo);
		    if(pardonCondition.isFulfil()) {
			HTML tick = new HTML("&#10003;");
			tick.setStyleName("conditionTick");
			fPnlCondition.add(tick);
		    }
		    fPnlCondition.setStyleName("pardonConditionList");
		    fPnlPardon.add(fPnlCondition);
		}
	    }
	    countCondition++;
	}
    }

    public static FlowPanel getUserActivityButtons(final Confession confession) {
	final FlowPanel fPlaneUserActivity = new FlowPanel();
	fPlaneUserActivity.setStyleName("buttons");
	final ActivityButton btnSB = (ActivityButton) getSBButton(confession);
	final ActivityButton btnSY = (ActivityButton) getSYButton(confession);
	final ActivityButton btnLM = (ActivityButton) getLMButton(confession);
	final ActivityButton btnSP = (ActivityButton) getSPButton(confession);
	final ActivityButton btnSNP = (ActivityButton) getSNPButton(confession);
	final ActivityButton btnAB = (ActivityButton) getABButton(confession);

	ConfessionBox.confessionService.getUserActivity(ConfessionBox.loggedInUserInfo.getUserId(),
		confession.getConfId(), new AsyncCallback<Map<String, Long>>() {

	    @Override
	    public void onSuccess(Map<String, Long> result) {

		if (result != null) {
		    if (result.containsKey(Activity.ABUSE.name())) {
			btnAB.disableBtn();
		    }
		    if (result.containsKey(Activity.LAME.name())) {
			btnLM.disableBtn();
		    }
		    if (result.containsKey(Activity.SAME_BOAT.name())) {
			btnSB.disableBtn();
		    }
		    if (result.containsKey(Activity.SHOULD_BE_PARDONED
			    .name())) {
			btnSP.disableBtn();
		    }
		    if (result
			    .containsKey(Activity.SHOULD_NOT_BE_PARDONED
				    .name())) {
			btnSNP.disableBtn();
		    }
		    if (result.containsKey(Activity.SYMPATHY.name())) {
			btnSY.disableBtn();
		    }
		}

		fPlaneUserActivity.add(btnSB);
		fPlaneUserActivity.add(btnSY);
		fPlaneUserActivity.add(btnLM);
		fPlaneUserActivity.add(btnSP);
		fPlaneUserActivity.add(btnSNP);
		fPlaneUserActivity.add(btnAB);
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("ConfessionFeedView", "onFailure", caught);
	    }
	});

	return fPlaneUserActivity;
    }

    public static Widget getABButton(final Confession confession) {
	return new ActivityButton(Activity.ABUSE, confession, ConfessionBox.cbText.buttonTitleReportAbuse(),
		Constants.STYLE_CLASS_ABUSE_ACTIVITY, new Image("/images/blank.png",0,0,37,40));
    }

    public static Widget getSNPButton(final Confession confession) {
	return new ActivityButton(Activity.SHOULD_NOT_BE_PARDONED, confession, ConfessionBox.cbText.buttonTitleShouldNotBePardoned(),
		Constants.STYLE_CLASS_SNP_ACTIVITY, new Image("/images/blank.png",0,0,37,40));
    }

    public static Widget getSPButton(final Confession confession) {
	return new ActivityButton(Activity.SHOULD_BE_PARDONED, confession, ConfessionBox.cbText.buttonTitleShouldBePardoned(),
		Constants.STYLE_CLASS_SP_ACTIVITY, new Image("/images/shouldBepardoned.png",0,0,37,40));
    }

    public static Widget getLMButton(final Confession confession) {
	return new ActivityButton(Activity.LAME, confession, ConfessionBox.cbText.buttonTitleLameConfession(),
		Constants.STYLE_CLASS_LAME_ACTIVITY, new Image("/images/blank.png",0,0,37,40));
    }

    public static Widget getSYButton(final Confession confession) {
	return new ActivityButton(Activity.SYMPATHY, confession, ConfessionBox.cbText.buttonTitleSympathy(),
		Constants.STYLE_CLASS_SYM_ACTIVITY, new Image("/images/sympathies.png",0,0,37,40));
    }

    public static Widget getSBButton(final Confession confession) {
	return new ActivityButton(Activity.SAME_BOAT, confession, ConfessionBox.cbText.buttonTitleSameBoat(),
		Constants.STYLE_CLASS_SB_ACTIVITY, new Image("/images/sameBoat.png",0,0,37,40));
    }

    public static Widget getLikeButton(Long confId) {
	return new FBLikeWidget(confId).getFbLikeHtml();
    }

    public static Widget getCommentSection(Long confId) {
	return new FBCommentWidget(confId).getFbCommentHtml();
    }
}
