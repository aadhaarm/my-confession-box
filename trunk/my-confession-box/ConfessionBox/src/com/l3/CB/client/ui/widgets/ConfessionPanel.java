package com.l3.CB.client.ui.widgets;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.FeedViewUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionPanel extends FlowPanel{

    private Confession confession;
    private final boolean anonymousView;
    private UserInfo confessedByUserInfo;
    private final boolean showUserControls;
    private FlowPanel fPnlMiddleContent;    
    private Image imgProfileImage;
    private HorizontalPanel hPnlUserControls;
    private FlowPanel fPnlName;
    private FlowPanel fPnlStatusBar;
    private Label lblConfessionTitle;
    private FlowPanel fPnlConfessionText;
    private FlowPanel fPnlPardonWidget;
    private FlowPanel fPnlActivityButtons;
    private Label lblPardonStatus;
    private HTML undoToolTip;
    private HTML shareToolTip;

    public ConfessionPanel(Confession confession, boolean showUserControls, boolean isAnyn) {
	super();

	this.confession = confession;
	this.anonymousView = isAnyn;
	this.showUserControls = showUserControls;

	this.getElement().setId("confession-id-" + confession.getConfId());
	this.addStyleName(Constants.STYLE_CLASS_CONFESSION_MAIN_CONTAINER);

	getConfessionWidgetsSetup(confession);
    }

    /**
     * @param confession
     */
    public void getConfessionWidgetsSetup(Confession confession) {

	confessedByUserInfo = FacebookUtil.getUserInfo(confession.getUserDetailsJSON());
	if(confessedByUserInfo != null) {
	    confession.setFbId(confessedByUserInfo.getId());
	    this.confession = confession;
	}

	/*
	 * Instantiate widgets
	 */
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

	// Set USER NAME
	fPnlTopContent.add(fPnlName);

	// Set STATUS BAR
	fPnlTopContent.add(fPnlStatusBar);

	this.add(fPnlTopContent);

	// MIDDLE Container
	fPnlMiddleContent = new FlowPanel();
	fPnlMiddleContent.setStyleName(Constants.DIV_CONFESSION_PANEL_MIDDLE_CONTAINER);

	// Confession Title
	lblConfessionTitle = new Label(confession.getConfessionTitle());
	lblConfessionTitle.setStyleName(Constants.STYLE_CLASS_CONFESSION_TITLE_TEXT);
	fPnlMiddleContent.add(lblConfessionTitle);

	// Confession text
	fPnlMiddleContent.add(fPnlConfessionText);

	// Pardon widget
	fPnlMiddleContent.add(fPnlPardonWidget);

	// UNDO tool tip
	undoToolTip = FeedViewUtils.getUndoToolTipBar();
	undoToolTip.setVisible(false);
	fPnlMiddleContent.add(undoToolTip);
	
	// Share tool tip
	shareToolTip = FeedViewUtils.getShareToolTipBar();
	shareToolTip.setVisible(false);
	fPnlMiddleContent.add(shareToolTip);

	this.add(fPnlMiddleContent);

	// BUTTONS
	FlowPanel fPnlButtonEtc = new FlowPanel();
	// User ACTIVITY Buttons
	fPnlButtonEtc.add(fPnlActivityButtons);
	// Show Pardon status
	if(lblPardonStatus != null) {
	    fPnlButtonEtc.add(lblPardonStatus);
	}
	this.add(fPnlButtonEtc);

	// FB WIDGETS
	final FlowPanel fPnlFBWidgets = new FlowPanel();
	fPnlFBWidgets.setStyleName(Constants.DIV_CONFESSION_PANEL_FBWIDGETS_CONTAINER);
	fPnlFBWidgets.add(FeedViewUtils.getLikeButton(confession.getConfId()));
	fPnlFBWidgets.add(FeedViewUtils.getCommentSection(confession.getConfId()));
	this.add(fPnlFBWidgets);
    }

    public void initializeConfessionWidgets(Confession confession) {
	this.confession = confession;
	//Profile picture
	imgProfileImage = CommonUtils.getProfilePicture(this.confession, anonymousView);
	// User Profile name or ANYN
	fPnlName = CommonUtils.getName(this.confession, confessedByUserInfo, anonymousView);
	// Status Bar (time and subscription status)
	fPnlStatusBar = CommonUtils.getStatusBar(this.confession);
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