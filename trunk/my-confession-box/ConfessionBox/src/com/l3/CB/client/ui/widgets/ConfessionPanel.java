package com.l3.CB.client.ui.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ui.widgets.comment.AddCommentWidget;
import com.l3.CB.client.ui.widgets.comment.CommentListWidget;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.FeedViewUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionPanel extends FlowPanel{

    private Confession confession;
    private boolean anonymousView;
    private UserInfo confessedByUserInfo;
    private boolean showUserControls;
    private boolean showExtendedDetails;
    private boolean showPardonHelpText;

    private FlowPanel fPnlMiddleContent;    
    private Image imgProfileImage;
    private Widget hPnlUserControls;
    private FlowPanel fPnlName;
    private FlowPanel fPnlStatusBar;
    private Anchor ancConfessionTitle;
    private FlowPanel fPnlConfessionText;
    private FlowPanel fPnlPardonWidget;
    private FlowPanel fPnlActivityButtons;
    private FlowPanel lblPardonStatus;
    private HTML undoToolTip;
    private HTML shareToolTip;

    public ConfessionPanel(Confession confession, boolean showUserControls, boolean isAnyn, boolean showExtendedDetails, boolean showPardonHelpText) {
	super();
	if(confession != null) {
	    this.confession = confession;
	    this.anonymousView = isAnyn;
	    this.showUserControls = showUserControls;
	    this.showExtendedDetails = showExtendedDetails;
	    this.showPardonHelpText = showPardonHelpText;

	    this.getElement().setId("confession-id-" + confession.getConfId());
	    this.addStyleName(Constants.STYLE_CLASS_CONFESSION_MAIN_CONTAINER);

	    getConfessionWidgetsSetup(confession);
	    bind();
	}
    }

    private void bind() {
	ancConfessionTitle.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		ConfessionBox.confId = confession.getConfId().toString();
		CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID);
		//		Window.open(FacebookUtil.getActivityUrl(confession.getConfId()), confession.getConfessionTitle(), "");
	    }
	});
    }

    /**
     * @param confession
     */
    public void getConfessionWidgetsSetup(final Confession confession) {

	confessedByUserInfo = FacebookUtil.getUserInfo(confession.getUserDetailsJSON());
	if(confessedByUserInfo != null) {
	    confession.setFbId(confessedByUserInfo.getId());
	    this.confession = confession;
	}

	// Set USER CONTROLS
	if(this.showUserControls) {
	    hPnlUserControls = CommonUtils.getUserControls(confession);
	    this.add(hPnlUserControls);
	}

	initializeConfessionWidgets(confession);

	// TOP Container
	FlowPanel fPnlTopContent = new FlowPanel();
	fPnlTopContent.setStyleName(Constants.DIV_CONFESSION_PANEL_TOP_CONTAINER);

	// Set USER PROFILE PIC or ANYN PIC 
	fPnlTopContent.add(imgProfileImage);

	if(ConfessionBox.isMobile) {
	    // Set USER NAME
	    fPnlTopContent.add(fPnlName);
	    // Set STATUS BAR
	    fPnlTopContent.add(fPnlStatusBar);
	} else {
	    // Set STATUS BAR
	    fPnlTopContent.add(fPnlStatusBar);
	    // Set USER NAME
	    fPnlTopContent.add(fPnlName);
	}

	this.add(fPnlTopContent);

	// CONFESSED to me TOOL-TIP bar
	if(showPardonHelpText && confessedByUserInfo != null) {
	    HTML confToMeToolTip = FeedViewUtils.getConfessedToMeToolTipBar(confessedByUserInfo);
	    this.add(confToMeToolTip);
	}

	// MIDDLE Container
	fPnlMiddleContent = new FlowPanel();
	fPnlMiddleContent.setStyleName(Constants.DIV_CONFESSION_PANEL_MIDDLE_CONTAINER);

	// Confession Title
	ancConfessionTitle = new Anchor(confession.getConfessionTitle());

	ancConfessionTitle.setStyleName(Constants.STYLE_CLASS_CONFESSION_TITLE_TEXT);
	fPnlMiddleContent.add(ancConfessionTitle);

	// Confession text
	fPnlMiddleContent.add(fPnlConfessionText);

	// UPDATE panel widget
	final FlowPanel updatePanel = new FlowPanel();
	UpdatePanelWidget updatePanelWidget = new UpdatePanelWidget(confession.getConfId());
	updatePanel.setStyleName(Constants.STYLE_CLASS_UPDATE_CONFESSION_PANEL);
	updatePanel.add(updatePanelWidget);
	fPnlMiddleContent.add(updatePanel);

	if(!anonymousView || this.showUserControls) {
	    final Anchor btnEditConfession = new Anchor(ConfessionBox.cbText.addUpdateLinkText());
	    if(!anonymousView) {
		btnEditConfession.setText(ConfessionBox.cbText.addResponseLinkText());
	    }
	    btnEditConfession.setTitle(ConfessionBox.cbText.editConfessionUserControlButtonTitle());
	    updatePanel.add(btnEditConfession);
	    btnEditConfession.addClickHandler(new ClickHandler() {
		UpdateConfessionWidget updateWidget;
		@Override
		public void onClick(ClickEvent event) {
		    if(updateWidget == null){
			updateWidget = new UpdateConfessionWidget(confession);
			updatePanel.add(updateWidget);
		    } else if(updateWidget.isVisible()) {
			updateWidget.setVisible(false);
		    } else {
			updateWidget.setVisible(true);
		    }
		}
	    });
	}

	// Set USER CONTROLS
	if(this.showUserControls) {
	    if(confession.getConfessedTo() == null || confession.getConfessedTo().isEmpty()) {
		final FlowPanel appealPanel = new FlowPanel();
		appealPanel.setStyleName(Constants.PARDON_APPEAL_PANEL);
		Anchor ancShareConfession = new Anchor(ConfessionBox.cbText.appealForPardonLinkText());
		ancShareConfession.addStyleName(Constants.STYLE_CLASS_PARDON_APPEAL_LINK);
		ancShareConfession.setTitle(ConfessionBox.cbText.shareConfessionUserControlButtonTitle());
		appealPanel.add(ancShareConfession);
		fPnlMiddleContent.add(appealPanel);
		ancShareConfession.addClickHandler(new ClickHandler() {
		    AppealPardonWidget appealForPardonWidget;
		    @Override
		    public void onClick(ClickEvent event) {
			if(appealForPardonWidget == null) {
			    appealForPardonWidget = new AppealPardonWidget(confession);
			    appealPanel.add(appealForPardonWidget);
			} else if(appealForPardonWidget.isVisible()) {
			    appealForPardonWidget.setVisible(false);
			} else {
			    appealForPardonWidget.setVisible(true);
			}
		    }
		});
	    }
	}

	//Middle widget
	this.add(fPnlMiddleContent);

	// Pardon widget
	this.add(fPnlPardonWidget);

	// UNDO tool tip
	undoToolTip = FeedViewUtils.getUndoToolTipBar();
	undoToolTip.setVisible(false);
	fPnlMiddleContent.add(undoToolTip);

	// Share tool tip
	shareToolTip = FeedViewUtils.getShareToolTipBar();
	shareToolTip.setVisible(false);
	fPnlMiddleContent.add(shareToolTip);


	FlowPanel btnActivityAndPardonstatus = new FlowPanel();
	btnActivityAndPardonstatus.setStyleName(Constants.STYLE_CLASS_ACTIVITY_BTN_AND_PARDON_STATUS_DIV);
	// BUTTONS
	FlowPanel fPnlButtonEtc = new FlowPanel();
	fPnlButtonEtc.setStyleName(Constants.STYLE_CLASS_ACTIVITY_BUTTON_PANEL);
	// User ACTIVITY Buttons
	fPnlButtonEtc.add(fPnlActivityButtons);
	btnActivityAndPardonstatus.add(fPnlButtonEtc);

	// Show Pardon status
	if(lblPardonStatus != null) {
	    btnActivityAndPardonstatus.add(lblPardonStatus);
	}
	this.add(btnActivityAndPardonstatus);

	// FB WIDGETS
	final FlowPanel fPnlFBWidgets = new FlowPanel();
	fPnlFBWidgets.setStyleName("comment_frame");

	// Facebook LIKE Button
	fPnlFBWidgets.add(FeedViewUtils.getLikeButton(confession.getConfId()));

	AddCommentWidget addCommentWidget = new AddCommentWidget(confession.getConfId());
	fPnlFBWidgets.add(addCommentWidget);

	// Comment Section
	if(ConfessionBox.isMobile) {
	    // For Mobile - Comment section
	    final Button btnShowFBWidgets = new Button("Comment");
	    btnShowFBWidgets.addTouchEndHandler(new TouchEndHandler() {
		@Override
		public void onTouchEnd(TouchEndEvent event) {
		    btnShowFBWidgets.setVisible(false);
		    CommentListWidget commentListWidget = new CommentListWidget(confession.getConfId());
		    fPnlFBWidgets.add(commentListWidget);
		}
	    });
	    fPnlFBWidgets.add(btnShowFBWidgets);

	    CommonUtils.parseXFBMLJS(fPnlFBWidgets.getElement());
	} else {
	    // For Desktop - Comment Section
	    CommentListWidget commentListWidget = new CommentListWidget(confession.getConfId());
	    fPnlFBWidgets.add(commentListWidget);
	}

	this.add(fPnlFBWidgets);
    }

    public void initializeConfessionWidgets(Confession confession) {
	this.confession = confession;
	//Profile picture
	imgProfileImage = CommonUtils.getProfilePicture(this.confession, anonymousView);
	// User Profile name or ANYN
	fPnlName = CommonUtils.getName(this.confession, confessedByUserInfo, anonymousView, showExtendedDetails);
	// Status Bar (time and subscription status)
	fPnlStatusBar = CommonUtils.getStatusBar(this.confession, confessedByUserInfo);
	// Confession Text
	fPnlConfessionText = CommonUtils.getTextTruncated(this.confession.getConfession());
	//Activity buttons widget
	fPnlActivityButtons = FeedViewUtils.getUserActivityButtons(this.confession);
	// Pardon status
	lblPardonStatus = CommonUtils.getPardonStatus(this.confession);
	// Pardon widget
	fPnlPardonWidget = FeedViewUtils.getPardonWidget(this.confession, anonymousView, confessedByUserInfo);
    }

    public void showUndoTooltip() {
	undoToolTip.setVisible(true);
    }

    public void showShareToolTip() {
	shareToolTip.setVisible(true);
    }

    public void hideUndoToolTip() {
	undoToolTip.setVisible(false);
    }
}