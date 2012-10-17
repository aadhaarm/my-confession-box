package com.l3.CB.client.view;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import sun.security.krb5.internal.PAData;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.FacebookServiceAsync;
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.client.ui.widgets.CBButton;
import com.l3.CB.client.ui.widgets.PardonPopupPanel;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.CBText;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.UserInfo;
import com.sun.xml.internal.xsom.impl.Const;

public class ConfessionFeedView extends Composite implements
ConfessionFeedPresenter.Display {

	Logger logger = Logger.getLogger("CBLogger");

	private VerticalPanel vpnlConfessionList;
	private ConfessionServiceAsync confessionService;
	private FacebookServiceAsync facebookService;
	private UserInfo loggedInUserInfo;
	private String accessToken;
	
	private DecoratorPanel contentTableDecorator;
	private ScrollPanel contentScrollPanel;
	private int confessionPagesLoaded;
	private Image loaderImage;
	private boolean moreConfessions;

	CBText cbText;
	
	public ConfessionFeedView(ConfessionServiceAsync confessionService,
			UserInfo userInfo, FacebookServiceAsync facebookService, String accessToken, CBText cbText) {
		super();
		this.confessionService = confessionService;
		this.facebookService = facebookService;
		this.loggedInUserInfo = userInfo;
		this.accessToken = accessToken;
		this.cbText = cbText;
		
		confessionPagesLoaded = 1;
		getMeLoaderImage();
		moreConfessions = true;

		contentScrollPanel = new ScrollPanel();
		contentScrollPanel.addStyleName("confessionScrollPanel");
		//		contentScrollPanel.setAlwaysShowScrollBars(true);

		vpnlConfessionList = new VerticalPanel();
		contentScrollPanel.add(vpnlConfessionList);

		contentTableDecorator = new DecoratorPanel();
		contentTableDecorator.add(contentScrollPanel);

		initWidget(contentTableDecorator);
	}

	private void getMeLoaderImage() {
		loaderImage = new Image(Constants.LOADER_IMAGE_PATH);
		loaderImage.addStyleName("loaderImage");
	}

	public void setConfessions(List<Confession> confessions, boolean isAnyn) {

		if (confessions != null && !confessions.isEmpty()) {
			for (Confession confession : confessions) {
				UserInfo confessedByUserInfo = FacebookUtil.getUserInfo(confession.getUserDetailsJSON());

				Grid grid = new Grid(5, 2);
				grid.getElement().setId("confession-id-" + confession.getConfId());
				grid.addStyleName(Constants.STYLE_CLASS_CONFESSION_GRID);
				int row = 0;

				if(confessedByUserInfo != null) {
					confession.setFbId(confessedByUserInfo.getId());
				}
				grid.setHTML(row, 0, CommonUtils.getProfilePictureAndName(confession, isAnyn));
				grid.setWidget(row, 1, CommonUtils.getConfessionWithName(confession, confessedByUserInfo, isAnyn, cbText));
				
				if(confession.getConfessedTo() != null) {
					row++;
					grid.setWidget(row, 0, getPardonWidget(confession, isAnyn, confessedByUserInfo));
				}
				row++;
				grid.setWidget(row, 1, getUserActivityButtons(confession));
				row++;
				grid.setHTML(row, 1, getLikeButton(confession.getConfId()));
				row++;
				grid.setHTML(row, 1, getCommentSection(confession.getConfId()));

				vpnlConfessionList.add(grid);
				CommonUtils.parseXFBMLJS(DOM.getElementById("confession-id-" + confession.getConfId()));
			}
		} else {
			moreConfessions = false;
		}
	}

	private Widget getPardonWidget(final Confession confession, boolean anynView, final UserInfo confessionByUser) {
		HorizontalPanel pardonPanel = new HorizontalPanel();
		List<ConfessionShare> confessionShares = confession.getConfessedTo();
		for (ConfessionShare confessionShare : confessionShares) {
			if(anynView) {
				Label confStatus = new Label();
				if(confessionShare.isPardon()) {
					confStatus.setText("Pardoned");
				} else {
					confStatus.setText("Yet to be pardoned");
				}
				pardonPanel.add(confStatus);
			} else {
				final Button btnPardon = new Button("Pardon");
				if(confessionShare.isPardon()) {
					btnPardon.setEnabled(false);
				}

				final PardonPopupPanel pardonPopupPanel = new PardonPopupPanel(confession, loggedInUserInfo, confessionByUser, cbText);
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
				
				if(pardonPopupPanel != null) {
					pardonPopupPanel.getBtnCancel().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							pardonPopupPanel.hide();
						}
					});
					pardonPopupPanel.getBtnPardon().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							facebookService.getUserDetails(confession.getFbId(), accessToken, new AsyncCallback<String>() {
								
								@Override
								public void onSuccess(String result) {
									UserInfo confessedByUser = CommonUtils.getUserInfo(result);
									if(confessedByUser != null) {
										confessionService.pardonConfession(loggedInUserInfo, confession.getConfId(), confessedByUser, new AsyncCallback<Void>() {
											
											@Override
											public void onSuccess(Void result) {
												btnPardon.setEnabled(false);
												pardonPopupPanel.hide();
											}
											
											@Override
											public void onFailure(Throwable caught) {
												btnPardon.setEnabled(true);
												logger.log(Constants.LOG_LEVEL,
														"Exception in ConfessionFeedView.onFailure()"
																+ caught.getCause());
											}
										});
										
									}
								}
								
								@Override
								public void onFailure(Throwable caught) {
									logger.log(Constants.LOG_LEVEL,
											"Exception in ConfessionFeedView.onFailure()"
													+ caught.getCause());
								}
							});
						}
					});
				}
			}
		}
		return pardonPanel;
	}

	private FlowPanel getUserActivityButtons(final Confession confession) {
		final FlowPanel fPlaneUserActivity = new FlowPanel();
		final CBButton btnSB = (CBButton) getSBButton(confession);
		final CBButton btnSY = (CBButton) getSYButton(confession);
		final CBButton btnLM = (CBButton) getLMButton(confession);
		final CBButton btnSP = (CBButton) getSPButton(confession);
		final CBButton btnSNP = (CBButton) getSNPButton(confession);
		final CBButton btnAB = (CBButton) getABButton(confession);

		if (confession.getTimeStamp() != null) {
			fPlaneUserActivity.add(new Label(DateTimeFormat.getFormat(
					PredefinedFormat.DATE_TIME_SHORT).format(
							confession.getTimeStamp())));
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
				logger.log(Constants.LOG_LEVEL,
						"Exception in ConfessionFeedView.onFailure()"
								+ caught.getCause());					}
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
		return new CBButton(Activity.ABUSE, confession, loggedInUserInfo,
				confessionService, cbText.buttonTitleReportAbuse(),
				Constants.STYLE_CLASS_ABUSE_ACTIVITY, new Image("/images/blank.png",0,0,37,40));
	}

	private Widget getSNPButton(final Confession confession) {
		return new CBButton(Activity.SHOULD_NOT_BE_PARDONED, confession,
				loggedInUserInfo, confessionService, cbText.buttonTitleShouldNotBePardoned(),
				Constants.STYLE_CLASS_SNP_ACTIVITY, new Image("/images/blank.png",0,0,37,40));
	}

	private Widget getSPButton(final Confession confession) {
		return new CBButton(Activity.SHOULD_BE_PARDONED, confession, loggedInUserInfo,
				confessionService, cbText.buttonTitleShouldBePardoned(),
				Constants.STYLE_CLASS_SP_ACTIVITY, new Image("/images/shouldBepardoned.png",0,0,37,40));
	}

	private Widget getLMButton(final Confession confession) {
		return new CBButton(Activity.LAME, confession, loggedInUserInfo,
				confessionService, cbText.buttonTitleLameConfession(),
				Constants.STYLE_CLASS_LAME_ACTIVITY, new Image("/images/blank.png",0,0,37,40));
	}

	private Widget getSYButton(final Confession confession) {
		return new CBButton(Activity.SYMPATHY, confession, loggedInUserInfo,
				confessionService, cbText.buttonTitleSympathy(),
				Constants.STYLE_CLASS_SYM_ACTIVITY, new Image("/images/sympathies.png",0,0,37,40));
	}

	private Widget getSBButton(final Confession confession) {
		return new CBButton(Activity.SAME_BOAT, confession, loggedInUserInfo,
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

	public ScrollPanel getContentScrollPanel() {
		return contentScrollPanel;
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
	public int getMaximumVerticalScrollPosition() {
		return contentScrollPanel.getMaximumVerticalScrollPosition();
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
	public int getVerticalScrollPosition() {
		return contentScrollPanel.getVerticalScrollPosition();
	}
}
