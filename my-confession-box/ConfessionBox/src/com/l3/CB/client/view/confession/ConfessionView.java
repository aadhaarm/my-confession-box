package com.l3.CB.client.view.confession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateFeedToMeEvent;
import com.l3.CB.client.event.UpdateHPEvent;
import com.l3.CB.client.ui.widgets.PardonPopupPanel;
import com.l3.CB.client.ui.widgets.comment.AddCommentTemplate;
import com.l3.CB.client.ui.widgets.comment.CommentListTemplate;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionPackage;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.PardonStatus;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionView extends Composite {

    private static ConfessionViewUiBinder uiBinder = GWT
	    .create(ConfessionViewUiBinder.class);

    interface ConfessionViewUiBinder extends UiBinder<Widget, ConfessionView> {
    }

    private Confession confession;
    private boolean isAnyn;
    private UserInfo confessedByUserInfo;
    private boolean showUserControls;
    private boolean showExtendedDetails;
    private boolean showPardonHelpText;
    private PardonPopupPanel pardonPopupPanel;
    private boolean isSubscribed;

    @UiField
    HTMLPanel mainDiv;

    @UiField
    Anchor ancConfessionTitle;

    @UiField
    Anchor ancUserName;

    @UiField
    SpanElement spanDateTile;

    @UiField
    SpanElement htmlConfessionText;

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
    Button btnHideIdentity;
    @UiField
    Button btnHideConfession;
    @UiField
    Button btnPreview;
    @UiField
    Button btnPardon;

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
    Anchor ancConfessedTo;
    @UiField
    SpanElement spanConfessedTo;

    @UiField
    ButtonElement badgePardonStatus;

    @UiField
    ParagraphElement userCtrlBtnBlock;

    @UiField
    ParagraphElement adminCtrlBtnBlock;
    @UiField
    Button btnSelectConfession;
    @UiField
    Button btnConfession_BringToTop;

    @UiField
    ParagraphElement pardonBtnBlock;

    /*
     * Like, Share, Subscribe
     */

    //    @UiField(provided = true)
    //    ShareAnchor ancShare;

    @UiField
    Button btnSubscribe;

    @UiField
    DivElement divFBLike;

    public ConfessionView(Confession confession, boolean showUserControls, boolean isAnyn, 
	    boolean showExtendedDetails, boolean showPardonHelpText) {
	this.confession = confession;
	this.isAnyn = isAnyn;
	this.showUserControls = showUserControls;
	this.showExtendedDetails = showExtendedDetails;
	this.showPardonHelpText = showPardonHelpText;

	// Get confession by user info
	confessedByUserInfo = FacebookUtil.getUserInfo(confession.getUserDetailsJSON());
	if(confessedByUserInfo != null) {
	    confession.setFbId(confessedByUserInfo.getId());
	}

	//	ancShare = new ShareAnchor(confession, confessedByUserInfo);

	/*
	 * Init Widget
	 */
	initWidget(uiBinder.createAndBindUi(this));

	btnHideConfession.getElement().setAttribute("data-uk-tooltip", "");
	btnHideIdentity.getElement().setAttribute("data-uk-tooltip", "");
	btnLame.getElement().setAttribute("data-uk-tooltip", "");
	btnPardon.getElement().setAttribute("data-uk-tooltip", "");
	btnPreview.getElement().setAttribute("data-uk-tooltip", "");
	btnRepAbuse.getElement().setAttribute("data-uk-tooltip", "");
	btnSameBoat.getElement().setAttribute("data-uk-tooltip", "");
	btnShudBePar.getElement().setAttribute("data-uk-tooltip", "");
	btnShudNtBePar.getElement().setAttribute("data-uk-tooltip", "");
	btnSympathy.getElement().setAttribute("data-uk-tooltip", "");
	btnSubscribe.getElement().setAttribute("data-uk-tooltip", "");

	getConfessionWidgetsSetup(confession, isAnyn);
    }

    /**
     * @param confession
     * @param isAnyn
     */
    public void getConfessionWidgetsSetup(Confession confession, boolean isAnyn) {
	this.confession = confession;
	this.isAnyn = isAnyn;

	if(confession != null) {

	    /*
	     *  Confession ID
	     */
	    mainDiv.ensureDebugId("confession-id-" + confession.getConfId());

	    /*
	     *  Confession Title
	     */
	    ancConfessionTitle.setText(confession.getConfessionTitle());

	    /*
	     * User Name
	     */
	    setupUserNameDetails();

	    /*
	     * Date time
	     */
	    spanDateTile.setInnerText(CommonUtils.getDateInAGOFormat(confession.getTimeStamp()));

	    /*
	     * Confessed to
	     */
	    setupConfessedTo();

	    /*
	     * Confession Text
	     */
	    String text = CommonUtils.trunkate(confession.getConfession(), 200);
	    text = text.replace("\n", "<br/>");
	    htmlConfessionText.setInnerHTML(text);
	    if(confession.getConfession() != null && confession.getConfession().length() < 200) {
		ancMore.setVisible(false);
	    }

	    /*
	     * Profile Image
	     */
	    setupProfilePictureDetails();

	    /*
	     * Comment
	     */
	    addCommentWidget.initialize(confession.getConfId());
	    commentList.initializeWidget(confession.getConfId());

	    /*
	     * Activity Vote Count
	     */
	    setupActivityButtonStatus();
	    //Button disable status
	    disableActivityButtons();

	    // Remove User control block
	    setupUserCtrlButtons();

	    /*
	     * Pardon Status
	     */
	    setupPardonStatusBadge();

	    /*
	     * Enable Pardon Button
	     */
	    enablePardonButton();

	    /*
	     * FB LIKE
	     */
	    divFBLike.setAttribute("data-href",  FacebookUtil.getActivityUrl(confession.getConfId()));
	    CommonUtils.parseXFBMLJS(mainDiv.getElement());

	    /*
	     * Subscribe
	     */
	    if(ConfessionBox.isLoggedIn) {
		ConfessionBox.confessionService.isSubscribed(confession.getConfId(), ConfessionBox.getLoggedInUserInfo().getUserId(), new AsyncCallback<Boolean>() {
		    @Override
		    public void onSuccess(Boolean result) {
			isSubscribed = result;
			setSubscribeBtnState();
		    }
		    @Override
		    public void onFailure(Throwable caught) {
			Error.handleError("SubscribeAnchor", "onFailure", caught);
		    }
		});
	    } else {
		setSubscribeBtnState();
	    }


	}
    }

    private void setSubscribeBtnState() {
	if(isSubscribed) {
	    btnSubscribe.addStyleName("uk-button-primary");
	    btnSubscribe.setTitle("Subscribed!");
	    btnSubscribe.setText("Subscribed");
	} else {
	    btnSubscribe.removeStyleName("uk-button-primary");
	    btnSubscribe.setTitle("Subscribe to recieve confession updates");
	    btnSubscribe.setText("Subscribe");
	}
    }

    @UiHandler("btnSubscribe")
    void onSubscribeButtonClick(ClickEvent event) {
	if(ConfessionBox.isLoggedIn) {
	    registerSubscription(confession.getConfId());
	} else {
	    CommonUtils.login(0);
	}
    }

    /**
     * @param confId
     */
    private void registerSubscription(final Long confId) {
	ConfessionBox.confessionService.subscribe(confId, ConfessionBox.getLoggedInUserInfo().getUserId(), new Date(), new AsyncCallback<Boolean>() {
	    @Override
	    public void onSuccess(Boolean result) {
		isSubscribed = !isSubscribed;						
		setSubscribeBtnState();
	    }
	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("SubscribeAnchor", "onFailure", caught);
	    }
	});
    }

    private void enablePardonButton() {
	pardonBtnBlock.removeClassName("show");
	pardonBtnBlock.addClassName("hide");

	if(!confession.isOnlyDedicate() && confession.getConfessedTo() != null && !confession.getConfessedTo().isEmpty()) {

	    List<ConfessionShare> confessionShares = confession.getConfessedTo();

	    for (ConfessionShare confessionShare : confessionShares) {

		if(confessionShare.getPardonConditions() != null && !confessionShare.getPardonConditions().isEmpty()) {
		    //		    fPnlPardon.setStyleName(Constants.STYLE_CLASS_PARDON_CONDITION_PANEL);
		    //
		    //		    HTML pardonConditionInfoText = new HTML(Templates.TEMPLATES.pardonConditionInfoText(ConfessionBox.cbText.pardonConditionInfoText()));
		    //		    pardonConditionInfoText.setStyleName(Constants.STYLE_CLASS_PARDON_CONDITION);
		    //
		    //		    pardonConditionInfoText.addClickHandler(new ClickHandler() {
		    //			@Override
		    //			public void onClick(ClickEvent event) {
		    //			    HelpInfo.showHelpInfo(HelpInfo.type.PARDON_CONDITION_HELP_TEXT);
		    //			}
		    //		    });
		    //
		    //		    fPnlPardon.add(pardonConditionInfoText);
		    //
		    //		    // Add conditions for pardon 
		    //		    addPardonConditionStatus(confessionShare.getPardonConditions(), fPnlPardon);
		    //
		} else if(!isAnyn) {
		    //		    fPnlPardon.setStyleName(Constants.STYLE_CLASS_PARDON_BUTTON_PANEL);
		    //		    final Button btnPardon = new Button(ConfessionBox.cbText.pardonButtonLabelText());
		    //		    btnPardon.addMouseOverHandler(new MouseOverHandler() {
		    //
		    //			@Override
		    //			public void onMouseOver(MouseOverEvent event) {
		    //			    if(!ConfessionBox.isMobile) {
		    //				HelpInfo.showHelpInfo(HelpInfo.type.PARDON_BUTTON);
		    //			    }
		    //			}
		    //		    });
		    //		    btnPardon.setStyleName(Constants.STYLE_CLASS_PARDON_BUTTON);
		    pardonBtnBlock.removeClassName("hide");
		    pardonBtnBlock.addClassName("show");
		    if(confessionShare.getPardonStatus() != null) {
			switch (confessionShare.getPardonStatus()) {
			case PARDONED:
			    btnPardon.setEnabled(false);
			    btnPardon.addStyleName("uk-badge-success");
			    break;
			    //			default:
			    //			    pardonPopupPanel = new PardonPopupPanel(confession, confessedByUserInfo, btnPardon);
			    //			    pardonPopupPanel.setAnimationEnabled(true);
			    //			    pardonPopupPanel.setGlassEnabled(true);
			    //			    pardonPopupPanel.setStyleName(Constants.STYLE_CLASS_PARDON_MODAL);
			    //			    break;
			}
		    } 
		}
	    }
	}
    }

    /**
     * @param confession
     */
    private void setupUserCtrlButtons() {
	if(this.showUserControls) {

	    if(confession.isShareAsAnyn()) {
		btnHideIdentity.addStyleName("uk-button-success");
		btnHideIdentity.setText("Hide Identity: ON");
		//		btnHideIdentity.setTitle(ConfessionBox.cbText.unHideIdentityButtonTitleUserControl());
	    } else {
		btnHideIdentity.removeStyleName("uk-button-success");
		btnHideIdentity.setText("Hide Identity: OFF");
		//		btnHideIdentity.setTitle(ConfessionBox.cbText.hideIdentityButtonTitleUserControl());
	    }

	    if(confession.isVisibleOnPublicWall()) {
		btnHideConfession.setText("Hide Confession: OFF");
		//		btnHideConfession.setTitle(ConfessionBox.cbText.hideConfessionButtonTitleUserControl());
		btnHideConfession.removeStyleName("uk-button-success");
	    } else {
		btnHideConfession.setText("Hide Confession: ON");
		//		btnHideConfession.setTitle(ConfessionBox.cbText.unhideConfessionButtonTitleUserControl());
		btnHideConfession.addStyleName("uk-button-success");
	    }
	} else {
	    btnPreview.addStyleName("hide");
	    userCtrlBtnBlock.addClassName("hide");
	}

	if(ConfessionBox.isAdmin) {
	    if(confession.isSelected()) {
		btnSelectConfession.setText("Admin: Select Confession : ON");
		btnSelectConfession.addStyleName("uk-button-success");
	    } else {
		btnSelectConfession.setText("Admin: Select Confession : OFF");
		btnSelectConfession.removeStyleName("uk-button-success");
	    }
	} else {
	    btnConfession_BringToTop.removeFromParent();
	    btnSelectConfession.removeFromParent();
	    
	    adminCtrlBtnBlock.addClassName("hide");
	    adminCtrlBtnBlock.removeFromParent();
	}
    }

    /**
     * @param confession
     */
    private void setupPardonStatusBadge() {

	badgePardonStatus.removeClassName("show");
	badgePardonStatus.addClassName("hide");

	if(confession != null 
		&& !confession.isOnlyDedicate() 
		&& confession.getConfessedTo() != null 
		&& !confession.getConfessedTo().isEmpty()) {

	    for (ConfessionShare confessionShare : confession.getConfessedTo()) {
		if(confessionShare != null && confessionShare.getPardonStatus() != null) {
		    badgePardonStatus.removeClassName("hide");
		    badgePardonStatus.addClassName("show");

		    /*
		     * Pardon Status Badge
		     */
		    switch (confessionShare.getPardonStatus()) {
		    case PARDONED:
			// Set Badge Text with time stamp
			badgePardonStatus.setInnerText(ConfessionBox.cbText.pardonStatusLabel());
			//				+ " "
			//				+ ConfessionBox.cbText.dateTimeStampPrefix()
			//				+ " "
			//				+ CommonUtils.getDateInAGOFormat(confessionShare.getTimeStamp()))
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
	}
    }


    /**
     * @param confession
     */
    private void setupConfessedTo() {
	if(confession != null 
		//		&& !confession.isOnlyDedicate() 
		&& confession.getConfessedTo() != null 
		&& !confession.getConfessedTo().isEmpty()) {

	    for (ConfessionShare confessionShare : confession.getConfessedTo()) {
		if(confessionShare != null && confessionShare.getPardonStatus() != null) {
		    /*
		     * Confessed to
		     */
		    if(showUserControls) {
			ancConfessedTo.setTarget("_BLANK");
			ancConfessedTo.setHref(FacebookUtil.getProfileFBLink(confessionShare.getFbId()).asString());
			ancConfessedTo.setText(confessionShare.getUserFullName());
			spanConfessedTo.setInnerText(
				" (as " + CommonUtils.checkForNullConfessedTo(confessionShare.getRelation()) + " on public wall)");
		    } else {
			ancConfessedTo.addStyleName("hide");
			spanConfessedTo.setInnerText(CommonUtils.getPronoun(confession.getGender()) 
				+ " " + CommonUtils.checkForNullConfessedTo(confessionShare.getRelation()));
		    }
		} 
	    }
	} else {
	    /*
	     * Confessed to
	     */
	    spanConfessedTo.setInnerText("the world");
	}
    }

    /**
     * @param confession
     */
    private void disableActivityButtons() {
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
    }

    /**
     * @param confession
     */
    private void setupActivityButtonStatus() {
	spanLameNum.setInnerText(getCountToDisplay(Long.toString(getCount(confession, Activity.LAME))));
	spanRepAbuseNum.setInnerText(getCountToDisplay(Long.toString(getCount(confession, Activity.ABUSE))));
	spanSameBoatNum.setInnerText(getCountToDisplay(Long.toString(getCount(confession, Activity.SAME_BOAT))));
	spanShudBeParNum.setInnerText(getCountToDisplay(Long.toString(getCount(confession, Activity.SHOULD_BE_PARDONED))));
	spanShudNtBeParNum.setInnerText(getCountToDisplay(Long.toString(getCount(confession, Activity.SHOULD_NOT_BE_PARDONED))));
	spanSympathyNum.setInnerText(getCountToDisplay(Long.toString(getCount(confession, Activity.SYMPATHY))));
    }

    /**
     * @param confession
     * @param isAnyn
     */
    private void setupProfilePictureDetails() {
	if (confession != null && !confession.isShareAsAnyn() || !isAnyn) {
	    imgProfileImage.setUrl(FacebookUtil.getUserImageUrl(confession.getFbId()));
	} else {
	    imgProfileImage.setUrl(FacebookUtil.getFaceIconImage(confession.getGender()));
	}
    }

    /**
     * @param confession
     * @param isAnyn
     */
    private void setupUserNameDetails() {
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
    }

    @UiHandler("ancMore")
    void showMore(ClickEvent event) {
	String text = confession.getConfession();
	text = text.replace("\n", "<br/>");
	htmlConfessionText.setInnerHTML(text);
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
	startVote(Activity.SAME_BOAT, spanSameBoatNum, btnSameBoat);
    }
    @UiHandler("btnLame")
    void onLameVote(ClickEvent event) {
	startVote(Activity.LAME, spanLameNum, btnLame);
    }

    /**
     * @param activity
     * @param sEleVote
     * @param btnVote
     */
    private void startVote(final Activity activity, final SpanElement sEleVote, final Button btnVote) {
	registerVote(activity, sEleVote, btnVote);
    }
    @UiHandler("btnRepAbuse")
    void onRepAbuseVote(ClickEvent event) {
	startVote(Activity.ABUSE, spanRepAbuseNum, btnRepAbuse);
    }
    @UiHandler("btnShudBePar")
    void onShudBeParVote(ClickEvent event) {
	startVote(Activity.SHOULD_BE_PARDONED, spanShudBeParNum, btnShudBePar);
    }
    @UiHandler("btnShudNtBePar")
    void onShudNtBeParVote(ClickEvent event) {
	startVote(Activity.SHOULD_NOT_BE_PARDONED, spanShudNtBeParNum, btnShudNtBePar);
    }
    @UiHandler("btnSympathy")
    void onSympathyVote(ClickEvent event) {
	startVote(Activity.SYMPATHY, spanSympathyNum, btnSympathy);
    }

    @UiHandler("btnHideIdentity") 
    void onHideIdentityClick(ClickEvent event) {

	ConfessionPackage confessionPackagea = new ConfessionPackage();
	confessionPackagea.setUserId(ConfessionBox.getLoggedInUserInfo().getUserId());
	confessionPackagea.setFbId(ConfessionBox.getLoggedInUserInfo().getId());
	confessionPackagea.setConfId(confession.getConfId());
	confessionPackagea.setVisible(!confession.isShareAsAnyn());
	confessionPackagea.setUpdateTimeStamp(new Date());
	confessionPackagea.setAdmin(ConfessionBox.isAdmin);

	ConfessionBox.confessionService.changeIdentityVisibility(confessionPackagea, new AsyncCallback<Boolean>() {
	    @Override
	    public void onSuccess(Boolean result) {
		if(result != null) {
		    if(!confession.isShareAsAnyn()) {
			// Give human points
			ConfessionBox.eventBus.fireEvent(new UpdateHPEvent(Constants.POINTS_ON_UNHIDING_IDENTITY));
			setTitle(ConfessionBox.cbText.unHideIdentityButtonTitleUserControl());
		    } else {
			//Deduct human points
			ConfessionBox.eventBus.fireEvent(new UpdateHPEvent(-1*Constants.POINTS_ON_UNHIDING_IDENTITY));
			setTitle(ConfessionBox.cbText.hideIdentityButtonTitleUserControl());
		    }

		    // Refresh Profile image
		    confession.setShareAsAnyn(!confession.isShareAsAnyn());
		    confession.setFbId(ConfessionBox.loggedInUserInfo.getId());
		    setupProfilePictureDetails();
		    // Refresh Name
		    confessedByUserInfo = ConfessionBox.loggedInUserInfo;
		    setupUserNameDetails();
		    // Refresh User ctrl btns
		    setupUserCtrlButtons();
		}
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("ChangeVisibilityButton", "onFailure", caught);
	    }
	});
    }

    @UiHandler("btnConfession_BringToTop")
    void onAdminBringToTop(ClickEvent clickEvent) {
	if(ConfessionBox.isAdmin) {
	    ConfessionPackage confessionPackage = new ConfessionPackage();
	    confessionPackage.setConfId(confession.getConfId());
	    confessionPackage.setAdmin(ConfessionBox.isAdmin);
	    ConfessionBox.confessionService.markAsUpdatedConfession(confessionPackage, new AsyncCallback<Boolean>() {
	        @Override
	        public void onSuccess(Boolean result) {

	        }
	        @Override
	        public void onFailure(Throwable caught) {
	            Error.handleError("BringToTopButton", "onFailure", caught);
	        }
	    });
	}	
    }
    
    @UiHandler("btnSelectConfession")
    void onSelectConfession(ClickEvent clickEvent) {
	if(ConfessionBox.isAdmin) {

	    ConfessionPackage confessionPackagea = new ConfessionPackage();
	    confessionPackagea.setConfId(confession.getConfId());
	    confessionPackagea.setSelected(!confession.isSelected());
	    confessionPackagea.setAdmin(ConfessionBox.isAdmin);
	    //	    confessionPackagea.setUserId(ConfessionBox.getLoggedInUserInfo().getUserId());
	    //	    confessionPackagea.setFbId(ConfessionBox.getLoggedInUserInfo().getId());
	    //	    confessionPackagea.setVisible(!confession.isVisibleOnPublicWall());
	    //	    confessionPackagea.setUpdateTimeStamp(new Date());

	    ConfessionBox.confessionService.selectConfession(confessionPackagea, new AsyncCallback<Boolean>() {

		@Override
		public void onSuccess(Boolean result) {
		    if(result) {
			confession.setSelected(!confession.isSelected());
			setupUserCtrlButtons();
		    }
		}

		@Override
		public void onFailure(Throwable caught) {
		    Error.handleError("SelectConfessionButton", "onFailure", caught);
		}
	    });
	}
    }

    @UiHandler("btnHideConfession")
    void onHideConfessionClick(ClickEvent event) {

	ConfessionPackage confessionPackagea = new ConfessionPackage();
	confessionPackagea.setUserId(ConfessionBox.getLoggedInUserInfo().getUserId());
	confessionPackagea.setFbId(ConfessionBox.getLoggedInUserInfo().getId());
	confessionPackagea.setConfId(confession.getConfId());
	confessionPackagea.setVisible(!confession.isVisibleOnPublicWall());
	confessionPackagea.setUpdateTimeStamp(new Date());
	confessionPackagea.setAdmin(ConfessionBox.isAdmin);

	ConfessionBox.confessionService.changeConfessionVisibility(confessionPackagea, new AsyncCallback<Boolean>() {
	    @Override
	    public void onSuccess(Boolean result) {
		if(result) {
		    confession.setVisibleOnPublicWall(!confession.isVisibleOnPublicWall());
		    setupUserCtrlButtons();
		}
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("DeleteConfessionButton", "onFailure", caught);
	    }
	});
    }

    @UiHandler("btnPreview")
    void onPreviewClick(ClickEvent event) {
	ConfessionBox.confId = confession.getConfId().toString();
	CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID);	
    }

    @UiHandler("btnPardon")
    void onPardonClick(ClickEvent event) {
	if(Window.confirm("Do you want to pardon for this act?")) {
	    if(confessedByUserInfo != null) {
		List<PardonCondition> pardonConditions = new ArrayList<PardonCondition>();

		/**
		 * Pardon if no conditions
		 * Awaiting pardon if pardoned with conditions		    
		 */
		PardonStatus pardonStatus = PardonStatus.PARDONED;
		//	    if(pardonConditions != null && !pardonConditions.isEmpty()) {
		//		pardonStatus = PardonStatus.PARDONED_WITH_CONDITION;
		//	    }
		ConfessionBox.confessionService.pardonConfession(ConfessionBox.getLoggedInUserInfo(), confession.getConfId(), confessedByUserInfo, pardonConditions, pardonStatus, new Date(), new AsyncCallback<Void>() {
		    @Override
		    public void onSuccess(Void result) {
			btnPardon.setEnabled(false);
			//			    btnPardonHome.setEnabled(false);
			//			    hidePopup();
			ConfessionBox.eventBus.fireEvent(new UpdateFeedToMeEvent(confession));
			ConfessionBox.eventBus.fireEvent(new UpdateHPEvent(Constants.POINTS_ON_PARDONING));
		    }

		    @Override
		    public void onFailure(Throwable caught) {
			btnPardon.setEnabled(true);
			Error.handleError("PardonPopupPanel", "bind", caught);
		    }
		});
	    }
	}
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