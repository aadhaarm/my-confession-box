package com.l3.CB.client.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.client.ui.widgets.ActivityButton;
import com.l3.CB.client.ui.widgets.FBCommentWidget;
import com.l3.CB.client.ui.widgets.FBLikeWidget;
import com.l3.CB.client.ui.widgets.PardonPopupPanel;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionFeedView extends Composite implements ConfessionFeedPresenter.Display {

    private final DecoratorPanel contentTableDecorator;
    private final VerticalPanel vpnlConfessionList;
    private int confessionPagesLoaded;
    private Image loaderImage;
    private boolean isMoreConfessionsAvailable;
    private final ListBox lstFilterOptions;
    private final Button btnRefresh;
    private final HorizontalPanel hPnlTopBar;
    private List<Confession> confessionsThisView;

    public ConfessionFeedView() {
	super();
	lstFilterOptions = new ListBox();
	CommonUtils.getMeFilterListBox(lstFilterOptions);

	btnRefresh = new Button();
	btnRefresh.setTitle("Refresh");
	btnRefresh.setStyleName(Constants.STYLE_CLASS_REFRESH_BUTTON);

	hPnlTopBar = new HorizontalPanel();
	hPnlTopBar.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
	hPnlTopBar.add(lstFilterOptions);
	hPnlTopBar.add(btnRefresh);

	confessionPagesLoaded = 1;
	getMeLoaderImage();
	isMoreConfessionsAvailable = true;

	vpnlConfessionList = new VerticalPanel();
	vpnlConfessionList.add(hPnlTopBar);

	contentTableDecorator = new DecoratorPanel();
	contentTableDecorator.add(vpnlConfessionList);

	initWidget(contentTableDecorator);
    }

    private void getMeLoaderImage() {
	loaderImage = new Image(Constants.LOADER_IMAGE_PATH);
	loaderImage.addStyleName(Constants.STYLE_CLASS_LOADER_IMAGE);
    }

    @Override
    public void setConfessions(List<Confession> confessions, boolean isAnyn, boolean showUserControls) {
	if(confessionsThisView == null) {
	    confessionsThisView = new ArrayList<Confession>();
	}
	if (confessions != null && !confessions.isEmpty()) {
	    for (final Confession confession : confessions) {

		UserInfo confessedByUserInfo = FacebookUtil.getUserInfo(confession.getUserDetailsJSON());

		final FlowPanel fPnlMainContainer = new FlowPanel();
		fPnlMainContainer.getElement().setId("confession-id-" + confession.getConfId());
		fPnlMainContainer.addStyleName(Constants.STYLE_CLASS_CONFESSION_MAIN_CONTAINER);

		if(confessedByUserInfo != null) {
		    confession.setFbId(confessedByUserInfo.getId());
		}

		// Set USER CONTROLS
		if(showUserControls) {
		    fPnlMainContainer.add(CommonUtils.getUserControls(confession));
		}
		// TOP Container
		FlowPanel fPnlTopContent = new FlowPanel();
		fPnlTopContent.setStyleName("top");
		// Set USER PROFILE PIC or ANYN PIC 
		fPnlTopContent.add(CommonUtils.getProfilePicture(confession, isAnyn));
		// Set USER NAME
		fPnlTopContent.add(CommonUtils.getName(confession, confessedByUserInfo, isAnyn));
		// Set STATUS BAR
		fPnlTopContent.add(CommonUtils.getStatusBar(confession));

		fPnlMainContainer.add(fPnlTopContent);

		// MIDDLE Container
		FlowPanel fPnlMiddleContent = new FlowPanel();
		fPnlMiddleContent.setStyleName("middle");
		// Confession Title
		Label lblConfessionTitle = new Label(confession.getConfessionTitle());
		lblConfessionTitle.setStyleName(Constants.STYLE_CLASS_CONFESSION_TITLE_TEXT);
		fPnlMiddleContent.add(lblConfessionTitle);
		// Confession text
		fPnlMiddleContent.add(CommonUtils.getTextTruncated(confession.getConfession()));

		// Pardon widget
		if(confession.getConfessedTo() != null && !confession.getConfessedTo().isEmpty()) {
		    fPnlMiddleContent.add(getPardonWidget(confession, isAnyn, confessedByUserInfo));
		}
		//		Widget conditionStatusWidget = CommonUtils.getConditionStatus(confession);
		//		if(conditionStatusWidget != null) {
		//		    fPnlMiddleContent.add(conditionStatusWidget);
		//		}

		//		fPnlMiddleContent.add(CommonUtils.getUndoToolTipBar());
		//		fPnlMiddleContent.add(CommonUtils.getShareToolTipBar());

		fPnlMainContainer.add(fPnlMiddleContent);

		FlowPanel fPnlButtonEtc = new FlowPanel();
		// User ACTIVITY Buttons
		fPnlButtonEtc.add(getUserActivityButtons(confession));
		// Show Pardon status
		Label lblPardonStatus = CommonUtils.getPardonStatus(confession);
		if(lblPardonStatus != null) {
		    fPnlButtonEtc.add(lblPardonStatus);
		}

		fPnlMainContainer.add(fPnlButtonEtc);

		// FB WIDGETS
		final FlowPanel fPnlFBWidgets = new FlowPanel();
		fPnlFBWidgets.setStyleName("fb_widgets");
		fPnlFBWidgets.add(getLikeButton(confession.getConfId()));
		fPnlFBWidgets.add(getCommentSection(confession.getConfId()));
		fPnlMainContainer.add(fPnlFBWidgets);

		vpnlConfessionList.add(fPnlMainContainer);
		CommonUtils.parseXFBMLJS(DOM.getElementById("confession-id-" + confession.getConfId()));

		confessionsThisView.add(confession);
	    }
	} else {
	    if(confessionsThisView == null || confessionsThisView.isEmpty()) {
		vpnlConfessionList.clear();
		vpnlConfessionList.add(CommonUtils.getEmptyWidget());
	    }
	    isMoreConfessionsAvailable = false;
	}
    }

    private Widget getPardonWidget(final Confession confession, boolean anynView, final UserInfo confessionByUser) {
	FlowPanel fPnlPardon = new FlowPanel();

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
	return fPnlPardon;
    }

    private void addPardonConditionStatus(List<PardonCondition> pardonConditions, FlowPanel fPnlPardon) {
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

    private FlowPanel getUserActivityButtons(final Confession confession) {
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

    private Widget getABButton(final Confession confession) {
	return new ActivityButton(Activity.ABUSE, confession, ConfessionBox.cbText.buttonTitleReportAbuse(),
		Constants.STYLE_CLASS_ABUSE_ACTIVITY, new Image("/images/blank.png",0,0,37,40));
    }

    private Widget getSNPButton(final Confession confession) {
	return new ActivityButton(Activity.SHOULD_NOT_BE_PARDONED, confession, ConfessionBox.cbText.buttonTitleShouldNotBePardoned(),
		Constants.STYLE_CLASS_SNP_ACTIVITY, new Image("/images/blank.png",0,0,37,40));
    }

    private Widget getSPButton(final Confession confession) {
	return new ActivityButton(Activity.SHOULD_BE_PARDONED, confession, ConfessionBox.cbText.buttonTitleShouldBePardoned(),
		Constants.STYLE_CLASS_SP_ACTIVITY, new Image("/images/shouldBepardoned.png",0,0,37,40));
    }

    private Widget getLMButton(final Confession confession) {
	return new ActivityButton(Activity.LAME, confession, ConfessionBox.cbText.buttonTitleLameConfession(),
		Constants.STYLE_CLASS_LAME_ACTIVITY, new Image("/images/blank.png",0,0,37,40));
    }

    private Widget getSYButton(final Confession confession) {
	return new ActivityButton(Activity.SYMPATHY, confession, ConfessionBox.cbText.buttonTitleSympathy(),
		Constants.STYLE_CLASS_SYM_ACTIVITY, new Image("/images/sympathies.png",0,0,37,40));
    }

    private Widget getSBButton(final Confession confession) {
	return new ActivityButton(Activity.SAME_BOAT, confession, ConfessionBox.cbText.buttonTitleSameBoat(),
		Constants.STYLE_CLASS_SB_ACTIVITY, new Image("/images/sameBoat.png",0,0,37,40));
    }

    private Widget getLikeButton(Long confId) {
	return new FBLikeWidget(confId).getFbLikeHtml();
    }

    private Widget getCommentSection(Long confId) {
	return new FBCommentWidget(confId).getFbCommentHtml();
    }

    @Override
    public Widget asWidget() {
	return this;
    }

    @Override
    public void setConfessionPagesLoaded(int confessionPagesLoaded) {
	this.confessionPagesLoaded = confessionPagesLoaded;
    }

    public void setMoreConfessions(boolean moreConfessions) {
	this.isMoreConfessionsAvailable = moreConfessions;
    }

    @Override
    public int getConfessionPagesLoaded() {
	return confessionPagesLoaded;
    }

    @Override
    public Image getLoaderImage() {
	return loaderImage;
    }

    @Override
    public boolean isMoreConfessions() {
	return isMoreConfessionsAvailable;
    }

    @Override
    public VerticalPanel getVpnlConfessionList() {
	return vpnlConfessionList;
    }

    @Override
    public void addLoaderImage() {
	vpnlConfessionList.add(loaderImage);
    }

    @Override
    public void incrementConfessionPagesLoaded() {
	confessionPagesLoaded++;
    }

    @Override
    public void removeLoaderImage() {
	vpnlConfessionList.remove(loaderImage);
    }

    @Override
    public void showConfessionFilters() {
	lstFilterOptions.setVisible(true);
    }

    @Override
    public HasChangeHandlers getConfessionFilterListBox() {
	return lstFilterOptions;
    }

    @Override
    public HasFocusHandlers getConfessionFilterListBoxForHelp() {
	return lstFilterOptions;
    }

    @Override
    public void clearConfessions() {
	vpnlConfessionList.clear();
	hPnlTopBar.add(lstFilterOptions);
	hPnlTopBar.add(btnRefresh);
	vpnlConfessionList.add(hPnlTopBar);
    }

    @Override
    public HasClickHandlers getRefreshButton() {
	return btnRefresh;
    }

    @Override
    public void showEmptyScreen() {
    }
}
