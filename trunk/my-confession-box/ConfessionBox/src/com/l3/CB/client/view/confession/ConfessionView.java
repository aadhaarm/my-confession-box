package com.l3.CB.client.view.confession;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ui.widgets.comment.AddCommentTemplate;
import com.l3.CB.client.ui.widgets.comment.CommentListTemplate;
import com.l3.CB.client.ui.widgets.comment.CommentListWidget;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionView extends Composite {

    private static ConfessionViewUiBinder uiBinder = GWT
	    .create(ConfessionViewUiBinder.class);

    interface ConfessionViewUiBinder extends UiBinder<Widget, ConfessionView> {
    }

    private Confession confession;
    private boolean anonymousView;
    private UserInfo confessedByUserInfo;
    private boolean showUserControls;
    private boolean showExtendedDetails;
    private boolean showPardonHelpText;

    @UiField
    HTMLPanel mainDiv;

    @UiField
    Anchor ancConfessionTitle;

    @UiField
    Anchor ancUserName;

    @UiField
    SpanElement spanDateTile;

    @UiField
    HTML htmlConfessionText;

    @UiField
    Anchor ancMore;

    @UiField
    Image imgProfileImage;

    @UiField
    AddCommentTemplate addCommentWidget;

    @UiField
    CommentListTemplate commentList;

    @UiField
    Button btnSameBoat;
    @UiField
    Button btnSympathy;
    @UiField
    Button btnLame;
    @UiField
    Button btnShudBePar;
    @UiField
    Button btnShudNtBePar;
    @UiField
    Button btnRepAbuse;

    @UiField
    SpanElement spanSameBoatNum;
    @UiField
    SpanElement spanSympathyNum;
    @UiField
    SpanElement spanLameNum;
    @UiField
    SpanElement spanShudBeParNum;
    @UiField
    SpanElement spanShudNtBeParNum;
    @UiField
    SpanElement spanRepAbuseNum;

    @UiField
    ButtonElement badgePardonStatus;

    public ConfessionView(Confession confession, boolean showUserControls, boolean isAnyn, boolean showExtendedDetails, boolean showPardonHelpText) {
	initWidget(uiBinder.createAndBindUi(this));

	if(confession != null) {
	    this.confession = confession;
	    this.anonymousView = isAnyn;
	    this.showUserControls = showUserControls;
	    this.showExtendedDetails = showExtendedDetails;
	    this.showPardonHelpText = showPardonHelpText;

	    /*
	     *  Confession ID
	     */
	    mainDiv.ensureDebugId("confession-id-" + confession.getConfId());

	    /*
	     *  Confession Title
	     */
	    ancConfessionTitle.setText(confession.getConfessionTitle());

	    // Get confession by user info
	    confessedByUserInfo = FacebookUtil.getUserInfo(confession.getUserDetailsJSON());
	    if(confessedByUserInfo != null) {
		confession.setFbId(confessedByUserInfo.getId());
		this.confession = confession;
	    }

	    /*
	     * User Name
	     */
	    if (!confession.isShareAsAnyn() || !isAnyn) {
		if (confessedByUserInfo != null) {
		    ancUserName.setText(confessedByUserInfo.getName());
		    ancUserName.setHref(confessedByUserInfo.getLink());
		    ancUserName.setTarget("_BLANK");
		}
	    } else {
		// Anonymous user
		ancUserName.setText(ConfessionBox.cbText.confessedByAnynName());
		ancUserName.setTitle(ConfessionBox.cbText.profileNameAnonymousTileText());
	    }

	    /*
	     * Date time
	     */
	    spanDateTile.setInnerText(CommonUtils.getDateInAGOFormat(confession.getTimeStamp()));

	    /*
	     * Confession Text
	     */
	    htmlConfessionText.setHTML(CommonUtils.trunkate(confession.getConfession(), 200));
	    if(confession.getConfession() != null && confession.getConfession().length() < 200) {
		ancMore.setVisible(false);
	    }

	    /*
	     * Profile Image
	     */
	    if (confession != null && !confession.isShareAsAnyn() || !isAnyn) {
		imgProfileImage.setUrl(FacebookUtil.getUserImageUrl(confession.getFbId()));
	    } else {
		imgProfileImage.setUrl(FacebookUtil.getFaceIconImage(confession.getGender()));
	    }

	    /*
	     * Comment
	     */
	    addCommentWidget.initialize(confession.getConfId());
	    commentList.initializeWidget(confession.getConfId());

	    /*
	     * Activity Vote Count
	     */
	    spanLameNum.setInnerText(getCountToDisplay(Long.toString(getCount(confession, Activity.LAME))));
	    spanRepAbuseNum.setInnerText(getCountToDisplay(Long.toString(getCount(confession, Activity.ABUSE))));
	    spanSameBoatNum.setInnerText(getCountToDisplay(Long.toString(getCount(confession, Activity.SAME_BOAT))));
	    spanShudBeParNum.setInnerText(getCountToDisplay(Long.toString(getCount(confession, Activity.SHOULD_BE_PARDONED))));
	    spanShudNtBeParNum.setInnerText(getCountToDisplay(Long.toString(getCount(confession, Activity.SHOULD_NOT_BE_PARDONED))));
	    spanSympathyNum.setInnerText(getCountToDisplay(Long.toString(getCount(confession, Activity.SYMPATHY))));

	    /*
	     * Button disable status
	     */
	    if (confession.getActivityCount() != null) {
		if (confession.getActivityCount().containsKey(Activity.ABUSE.name())) {
		    disableBtn(btnRepAbuse);
		}
		if (confession.getActivityCount().containsKey(Activity.LAME.name())) {
		    disableBtn(btnLame);
		}
		if (confession.getActivityCount().containsKey(Activity.SAME_BOAT.name())) {
		    disableBtn(btnSameBoat);
		}
		if (confession.getActivityCount().containsKey(Activity.SHOULD_BE_PARDONED.name())) {
		    disableBtn(btnShudBePar);
		}
		if (confession.getActivityCount().containsKey(Activity.SHOULD_NOT_BE_PARDONED.name())) {
		    disableBtn(btnShudNtBePar);
		}
		if (confession.getActivityCount().containsKey(Activity.SYMPATHY.name())) {
		    disableBtn(btnSympathy);
		}
	    }

	    /*
	     * Pardon Status
	     */
	    if(confession != null 
		    && !confession.isOnlyDedicate() 
		    && confession.getConfessedTo() != null 
		    && !confession.getConfessedTo().isEmpty()) {

		for (ConfessionShare confessionShare : confession.getConfessedTo()) {
		    if(confessionShare != null && confessionShare.getPardonStatus() != null) {
			switch (confessionShare.getPardonStatus()) {
			case PARDONED:
			    // Set Badge Text with time stamp
			    badgePardonStatus.setInnerText(ConfessionBox.cbText.pardonStatusLabel()
				    + " "
				    + ConfessionBox.cbText.dateTimeStampPrefix()
				    + " "
				    + CommonUtils.getDateInAGOFormat(confessionShare.getTimeStamp()));
			    // Set tool-tip
			    badgePardonStatus.setTitle("Confessor has been pardoned by " 
				    + CommonUtils.getPronoun(confession.getGender()) 
				    + " " 
				    + CommonUtils.checkForNullConfessedTo(confessionShare.getRelation()) 
				    + ".");
			    // Set badge style
			    badgePardonStatus.addClassName("uk-badge-success");
			    break;
			default:
			    badgePardonStatus.setInnerText(ConfessionBox.cbText.awaitingPardonStatusLabel()
				    + " " 
				    + ConfessionBox.cbText.dateTimeStampPrefix() 
				    + " " 
				    + CommonUtils.getDateInAGOFormat(confessionShare.getTimeStamp()));
			    // Set tool-tip
			    badgePardonStatus.setTitle("Confessor is awaiting pardon from " 
				    + CommonUtils.getPronoun(confession.getGender()) 
				    + " " 
				    + CommonUtils.checkForNullConfessedTo(confessionShare.getRelation()) 
				    + ".");
			    // Set badge style
			    badgePardonStatus.addClassName("uk-badge-danger");
			    break;
			}
		    }
		}
	    } else {
		badgePardonStatus.removeFromParent();
	    }


	}
    }

    public ConfessionView(String firstName) {
	initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("ancMore")
    void showMore(ClickEvent event) {
	htmlConfessionText.setHTML(confession.getConfession());
	ancMore.setVisible(false);
    }

    @UiHandler("ancConfessionTitle")
    void conConfTitleClick(ClickEvent event) {
	ConfessionBox.confId = confession.getConfId().toString();
	CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID);
    }

    /**
     * Get count of votes
     * 
     * @param confession
     * @param activity
     * @return
     */
    private long getCount(Confession confession, Activity activity) {
	long count = 0;
	if(confession != null && activity != null) {
	    switch (activity) {
	    case ABUSE:
		count = confession.getNumOfAbuseVote();
		break;
	    case LAME:
		count = confession.getNumOfLameVote();
		break;
	    case SAME_BOAT:
		count = confession.getNumOfSameBoatVote();
		break;
	    case SHOULD_BE_PARDONED:
		count = confession.getNumOfShouldBePardonedVote();
		break;
	    case SHOULD_NOT_BE_PARDONED:
		count = confession.getNumOfShouldNotBePardonedVote();
		break;
	    case SYMPATHY:
		count = confession.getNumOfSympathyVote();
		break;
	    }
	}
	return count;
    }

    /**
     * Get counts in ago format
     * @param count
     * @return
     */
    private String getCountToDisplay(String count) {
	if(count == null) {
	    return "";
	}
	if(count != null && count.length() == 4) {
	    count = count.substring(0, 0) + "k";
	} else if(count != null && count.length() == 5) {
	    count = count.substring(0, 1) + "k";
	} else if(count != null && count.length() > 5) {
	    count = "⬆" + count.substring(0, 2) + "k";
	}
	return count;
    }

    /**
     * Disable activity button
     * Enable the Share button
     */
    public void disableBtn(Button btn) {
	btn.setEnabled(false);
	btn.addStyleName("uk-button-primary");
    }

    @UiHandler("btnSameBoat")
    void onSameBoatVote(ClickEvent event) {
	registerVote(Activity.SAME_BOAT, spanSameBoatNum, btnSameBoat);
    }
    @UiHandler("btnLame")
    void onLameVote(ClickEvent event) {
	registerVote(Activity.LAME, spanLameNum, btnLame);
    }
    @UiHandler("btnRepAbuse")
    void onRepAbuseVote(ClickEvent event) {
	registerVote(Activity.ABUSE, spanRepAbuseNum, btnRepAbuse);
    }
    @UiHandler("btnShudBePar")
    void onShudBeParVote(ClickEvent event) {
	registerVote(Activity.SHOULD_BE_PARDONED, spanShudBeParNum, btnShudBePar);
    }
    @UiHandler("btnShudNtBePar")
    void onShudNtBeParVote(ClickEvent event) {
	registerVote(Activity.SHOULD_NOT_BE_PARDONED, spanShudNtBeParNum, btnShudNtBePar);
    }
    @UiHandler("btnSympathy")
    void onSympathyVote(ClickEvent event) {
	registerVote(Activity.SYMPATHY, spanSympathyNum, btnSympathy);
    }

    /**
     * @param activity
     * @param btn
     */
    private void registerVote(Activity activity, final SpanElement spanElement, final Button btn) {
	if(ConfessionBox.isLoggedIn) {
	    ConfessionBox.confessionService.registerUserActivity(ConfessionBox.getLoggedInUserInfo().getUserId(), confession.getConfId(), activity , new Date(), new AsyncCallback<Long>() {
		@Override
		public void onSuccess(Long result) {
		    spanElement.setInnerText(getCountToDisplay(result.toString()));
		    disableBtn(btn);
		}

		@Override
		public void onFailure(Throwable caught) {
		    Error.handleError("ActivityButton", "onFailure", caught);
		}
	    });
	} else {
	    CommonUtils.login(0);
	}
    }
}