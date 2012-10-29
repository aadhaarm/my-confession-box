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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.FacebookServiceAsync;
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.client.ui.widgets.ActivityButton;
import com.l3.CB.client.ui.widgets.PardonPopupPanel;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.CBText;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionFeedView extends Composite implements ConfessionFeedPresenter.Display {

	private ConfessionServiceAsync confessionService;
	private DecoratorPanel contentTableDecorator;
	private VerticalPanel vpnlConfessionList;
	private UserInfo loggedInUserInfo;
	private int confessionPagesLoaded;
	private Image loaderImage;
	private boolean moreConfessions;
	private final ListBox lstFilterOptions;
	private final Button btnRefresh;
	private final HorizontalPanel hPnlTopBar;
	private List<Confession> confessionsThisView;

	CBText cbText;

	public ConfessionFeedView(ConfessionServiceAsync confessionService,
			UserInfo userInfo, FacebookServiceAsync facebookService, String accessToken, CBText cbText) {
		super();
		this.confessionService = confessionService;
		this.loggedInUserInfo = userInfo;
		this.cbText = cbText;

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
		moreConfessions = true;

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

	public void setConfessions(List<Confession> confessions, boolean isAnyn, boolean showUserControls) {
		if(confessionsThisView == null) {
			confessionsThisView = new ArrayList<Confession>();
		}
		if (confessions != null && !confessions.isEmpty()) {
			for (final Confession confession : confessions) {
				int numRows = 8;
				if(!showUserControls) {
					numRows--;
				}
				if(confession.getConfessedTo() == null || confession.getConfessedTo().isEmpty()) {
					numRows--;
				}
				UserInfo confessedByUserInfo = FacebookUtil.getUserInfo(confession.getUserDetailsJSON());

				final Grid grid = new Grid(numRows, 2);
				grid.getElement().setId("confession-id-" + confession.getConfId());
				grid.addStyleName(Constants.STYLE_CLASS_CONFESSION_GRID);
				int row = 0;

				if(confessedByUserInfo != null) {
					confession.setFbId(confessedByUserInfo.getId());
				}

				// Set USER CONTROLS
				if(showUserControls) {
					grid.setWidget(row, 1,  CommonUtils.getUserControls(confession, loggedInUserInfo, confessionService));
					row++;
				}
				// Set USER PROFILE PIC or ANYN PIC 
				grid.setHTML(row, 0, CommonUtils.getProfilePicture(confession, isAnyn));
				// Set USER NAME
				grid.setWidget(row, 1, CommonUtils.getName(confession, confessedByUserInfo, isAnyn, cbText));
				row++;
				// Confession Title
				Label lblConfessionTitle = new Label(confession.getConfessionTitle());
				lblConfessionTitle.setStyleName(Constants.STYLE_CLASS_CONFESSION_TITLE_TEXT);
				grid.setWidget(row, 1, lblConfessionTitle);
				row++;
				// Confession text
				grid.setWidget(row, 1, CommonUtils.getTextTruncated(confession.getConfession()));
				
				// Pardon widget
				if(confession.getConfessedTo() != null) {
					row++;
					grid.setWidget(row, 1, getPardonWidget(confession, isAnyn, confessedByUserInfo));
				}
				row++;
				// User ACTIVITY Buttons
				grid.setWidget(row, 1, getUserActivityButtons(confession));
				row++;
				// Just OPEN
				final Anchor justOpen = new Anchor(cbText.getMeJustOpenLinkText());
				justOpen.setStyleName(Constants.STYLE_CLASS_JUST_OPEN);
				grid.setWidget(row, 1, justOpen);
				final int rowFinal = row;

				justOpen.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						grid.remove(justOpen);
						grid.setHTML(rowFinal, 1, getLikeButton(confession.getConfId()));
						grid.setHTML(rowFinal+1, 1, getCommentSection(confession.getConfId()));
						CommonUtils.parseXFBMLJS(DOM.getElementById("confession-id-" + confession.getConfId()));
					}
				});

				vpnlConfessionList.add(grid);
				CommonUtils.parseXFBMLJS(DOM.getElementById("confession-id-" + confession.getConfId()));
				
				confessionsThisView.add(confession);
			}
		} else {
			if(confessionsThisView == null || confessionsThisView.isEmpty()) {
				vpnlConfessionList.clear();
				vpnlConfessionList.add(CommonUtils.getEmptyWidget());
			}
			moreConfessions = false;
		}
	}

	private Widget getPardonWidget(final Confession confession, boolean anynView, final UserInfo confessionByUser) {
		HorizontalPanel pardonPanel = new HorizontalPanel();
		pardonPanel.addStyleName(Constants.STYLE_CLASS_PARDON_STATUS_PANEL);
		List<ConfessionShare> confessionShares = confession.getConfessedTo();
		for (ConfessionShare confessionShare : confessionShares) {
			if(anynView) {
				Label confStatus = new Label();

				if(confessionShare.isPardon()) {
					confStatus.setText(cbText.pardonedStatus());
				} else {
					confStatus.setText(cbText.yetToBePardonedStatus());
				}
				pardonPanel.add(confStatus);
			} else {
				final Button btnPardon = new Button("Pardon");
				if(confessionShare.isPardon()) {
					btnPardon.setEnabled(false);
				}

				final PardonPopupPanel pardonPopupPanel = new PardonPopupPanel(confession, loggedInUserInfo, confessionByUser, cbText, confessionService, btnPardon);
				pardonPopupPanel.setAnimationEnabled(true);
				pardonPopupPanel.setGlassEnabled(true);
				pardonPopupPanel.addStyleName(Constants.STYLE_CLASS_PARDON_MODAL);

				pardonPanel.add(btnPardon);
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
			}
			if(confessionShare.getPardonConditions() != null && !confessionShare.getPardonConditions().isEmpty())
				pardonPanel.add(showPardonConditionStatus(confessionShare.getPardonConditions()));
		}
		return pardonPanel;
	}

	private Widget showPardonConditionStatus(List<PardonCondition> pardonConditions) {
		VerticalPanel vPnlPardonCondition = new VerticalPanel();
		for (PardonCondition pardonCondition : pardonConditions) {
			if(pardonCondition != null && Constants.pardonConditionUnhide.equalsIgnoreCase((pardonCondition.getCondition()))) {
				if(pardonCondition.isFulfil()) {
					vPnlPardonCondition.add(new Label(cbText.pardonPopupOpenIdentityConditionFulfilled()));
				} else {
					vPnlPardonCondition.add(new Label(cbText.pardonPopupOpenIdentityConditionYetToBoFulfilled()));
				}
			} else if(pardonCondition != null && Constants.pardonConditionSPVotes.equalsIgnoreCase((pardonCondition.getCondition()))) {
				if(pardonCondition.isFulfil()) {
					vPnlPardonCondition.add(new Label(pardonCondition.getCount() + " " + cbText.pardonPopupPardonActivityConditionFulfilled()));
				} else {
					vPnlPardonCondition.add(new Label(pardonCondition.getCount() + " " + cbText.pardonPopupPardonActivityConditionYetToBoFulfilled()));
				}
			}
		}
		return vPnlPardonCondition;
	}

	private FlowPanel getUserActivityButtons(final Confession confession) {
		final FlowPanel fPlaneUserActivity = new FlowPanel();
		final ActivityButton btnSB = (ActivityButton) getSBButton(confession);
		final ActivityButton btnSY = (ActivityButton) getSYButton(confession);
		final ActivityButton btnLM = (ActivityButton) getLMButton(confession);
		final ActivityButton btnSP = (ActivityButton) getSPButton(confession);
		final ActivityButton btnSNP = (ActivityButton) getSNPButton(confession);
		final ActivityButton btnAB = (ActivityButton) getABButton(confession);

		if (confession.getTimeStamp() != null) {
			Label lblDateTimeStamp = new Label(CommonUtils.getDateInFormat(confession.getTimeStamp()));
			lblDateTimeStamp.addStyleName(Constants.STYLE_CLASS_DATE_TIME_STAMP);
			fPlaneUserActivity.add(lblDateTimeStamp);
		}
		confessionService.getUserActivity(loggedInUserInfo.getUserId(),
				confession.getConfId(), new AsyncCallback<Map<String, Long>>() {

			@Override
			public void onSuccess(Map<String, Long> result) {

				if (result != null) {
					if (result.containsKey(Activity.ABUSE.name())) {
						btnAB.getBtn().setEnabled(false);
					}
					if (result.containsKey(Activity.LAME.name())) {
						btnLM.getBtn().setEnabled(false);
					}
					if (result.containsKey(Activity.SAME_BOAT.name())) {
						btnSB.getBtn().setEnabled(false);
					}
					if (result.containsKey(Activity.SHOULD_BE_PARDONED
							.name())) {
						btnSP.getBtn().setEnabled(false);
					}
					if (result
							.containsKey(Activity.SHOULD_NOT_BE_PARDONED
									.name())) {
						btnSNP.getBtn().setEnabled(false);
					}
					if (result.containsKey(Activity.SYMPATHY.name())) {
						btnSY.getBtn().setEnabled(false);
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

	private String getCommentSection(Long confId) {
		// <fb:comments href="http://example.com" num_posts="2"
		// width="470"></fb:comments>
		final StringBuilder sb = new StringBuilder();
		sb.append("<fb:comments href=\"");
		sb.append(FacebookUtil.REDIRECT_URL).append("?conf=").append(confId)
		.append("\"");
		sb.append(" num_posts=\"2\" width=\"470\"></fb:comments>");
		return sb.toString();
	}

	private Widget getABButton(final Confession confession) {
		return new ActivityButton(Activity.ABUSE, confession, loggedInUserInfo,
				confessionService, cbText.buttonTitleReportAbuse(),
				Constants.STYLE_CLASS_ABUSE_ACTIVITY, new Image("/images/blank.png",0,0,37,40));
	}

	private Widget getSNPButton(final Confession confession) {
		return new ActivityButton(Activity.SHOULD_NOT_BE_PARDONED, confession,
				loggedInUserInfo, confessionService, cbText.buttonTitleShouldNotBePardoned(),
				Constants.STYLE_CLASS_SNP_ACTIVITY, new Image("/images/blank.png",0,0,37,40));
	}

	private Widget getSPButton(final Confession confession) {
		return new ActivityButton(Activity.SHOULD_BE_PARDONED, confession, loggedInUserInfo,
				confessionService, cbText.buttonTitleShouldBePardoned(),
				Constants.STYLE_CLASS_SP_ACTIVITY, new Image("/images/shouldBepardoned.png",0,0,37,40));
	}

	private Widget getLMButton(final Confession confession) {
		return new ActivityButton(Activity.LAME, confession, loggedInUserInfo,
				confessionService, cbText.buttonTitleLameConfession(),
				Constants.STYLE_CLASS_LAME_ACTIVITY, new Image("/images/blank.png",0,0,37,40));
	}

	private Widget getSYButton(final Confession confession) {
		return new ActivityButton(Activity.SYMPATHY, confession, loggedInUserInfo,
				confessionService, cbText.buttonTitleSympathy(),
				Constants.STYLE_CLASS_SYM_ACTIVITY, new Image("/images/sympathies.png",0,0,37,40));
	}

	private Widget getSBButton(final Confession confession) {
		return new ActivityButton(Activity.SAME_BOAT, confession, loggedInUserInfo,
				confessionService, cbText.buttonTitleSameBoat(),
				Constants.STYLE_CLASS_SB_ACTIVITY, new Image("/images/sameBoat.png",0,0,37,40));
	}

	private String getLikeButton(Long confId) {
		// <fb:like href="http://apps.facebook.com.fbconfessalfa" send="true"
		// width="450" show_faces="false" font="lucida grande"></fb:like>
		final StringBuilder sb = new StringBuilder();
		sb.append("<fb:like href=\"");
		sb.append(FacebookUtil.REDIRECT_URL).append("?conf=").append(confId)
		.append("\"");
		sb.append(" send=\"true\" width=\"450\" show_faces=\"false\" font=\"lucida grande\"></fb:like>");
		return sb.toString();
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	public void setConfessionPagesLoaded(int confessionPagesLoaded) {
		this.confessionPagesLoaded = confessionPagesLoaded;
	}

	public void setMoreConfessions(boolean moreConfessions) {
		this.moreConfessions = moreConfessions;
	}

	public int getConfessionPagesLoaded() {
		return confessionPagesLoaded;
	}

	public Image getLoaderImage() {
		return loaderImage;
	}

	public boolean isMoreConfessions() {
		return moreConfessions;
	}

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

	public void clearConfessions() {
		vpnlConfessionList.clear();
		hPnlTopBar.add(lstFilterOptions);
		hPnlTopBar.add(btnRefresh);
		vpnlConfessionList.add(hPnlTopBar);
	}

	public HasClickHandlers getRegreshButton() {
		return btnRefresh;
	}

	@Override
	public void showEmptyScreen() {
		// TODO Auto-generated method stub
		
	}
}