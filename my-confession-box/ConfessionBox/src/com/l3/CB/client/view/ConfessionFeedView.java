package com.l3.CB.client.view;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.client.ui.widgets.CBButton;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionFeedView extends Composite implements
		ConfessionFeedPresenter.Display {

	Logger logger = Logger.getLogger("CBLogger");

	private VerticalPanel vpnlConfessionList;
	private ConfessionServiceAsync confessionService;
	private UserInfo userInfo;
	private DecoratorPanel contentTableDecorator;
	private ScrollPanel contentScrollPanel;
	private int confessionPagesLoaded;
	private Image loaderImage;
	private boolean moreConfessions;

	public ConfessionFeedView(ConfessionServiceAsync rpcService,
			UserInfo userInfo) {
		super();
		this.confessionService = rpcService;
		this.userInfo = userInfo;
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

	public void setConfessions(List<Confession> confessions) {

		if (confessions != null && !confessions.isEmpty()) {
			for (Confession confession : confessions) {
				Grid grid = new Grid(4, 2);
				grid.getElement().setId(
						"confession-id-" + confession.getConfId());
				grid.setHTML(0, 0, getProfilePictureTag(confession));
				grid.setWidget(0, 1, getConfessionWithName(confession));
				grid.setWidget(1, 1, getUserActivityButtons(confession));
				grid.setHTML(2, 1, getLikeButton(confession.getConfId()));
				grid.setHTML(3, 1, getCommentSection(confession.getConfId()));
				vpnlConfessionList.add(grid);
				CommonUtils.parseXFBMLJS(DOM.getElementById("confession-id-"
						+ confession.getConfId()));
			}
		} else {
			moreConfessions = false;
		}
	}

	private Widget getConfessionWithName(Confession confession) {
		VerticalPanel pnlConfession = new VerticalPanel();

		if (confession.isShareAsAnyn()) {
			Label lblConf = new Label();
			lblConf.addStyleName("confession");
			lblConf.setText("Anonymous");
			pnlConfession.add(lblConf);
		} else {
			UserInfo userInfo = FacebookUtil.getUserInfo(confession
					.getUserDetailsJSON());
			if (userInfo != null) {
				Anchor ancUserName = new Anchor(userInfo.getName(),
						userInfo.getLink());
				ancUserName.addStyleName("confession");
				pnlConfession.add(ancUserName);
			}
		}

		pnlConfession.add(new Label(confession.getConfession()));

		return pnlConfession;
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
		confessionService.getUserActivity(userInfo.getUserId(),
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
		// TODO: Button text
		// TODO: I18N
		return new CBButton(Activity.ABUSE, confession, userInfo,
				confessionService, "I Report Abuse!",
				Constants.STYLE_CLASS_ABUSE_ACTIVITY, "AB ", new Image("/images/blank.png",0,0,37,40));
	}

	private Widget getSNPButton(final Confession confession) {
		// TODO: Button text
		// TODO: I18N
		return new CBButton(Activity.SHOULD_NOT_BE_PARDONED, confession,
				userInfo, confessionService, "You should not be pardoned.",
				Constants.STYLE_CLASS_SNP_ACTIVITY, "SNP ", new Image("/images/blank.png",0,0,37,40));
	}

	private Widget getSPButton(final Confession confession) {
		// TODO: Button text
		// TODO: I18N
		return new CBButton(Activity.SHOULD_BE_PARDONED, confession, userInfo,
				confessionService, "You should be pordoned.",
				Constants.STYLE_CLASS_SP_ACTIVITY, "SP ", new Image("/images/shouldBepardoned.png",0,0,37,40));
	}

	private Widget getLMButton(final Confession confession) {
		// TODO: Button text
		// TODO: I18N
		return new CBButton(Activity.LAME, confession, userInfo,
				confessionService, "Such a Lame confession..",
				Constants.STYLE_CLASS_LAME_ACTIVITY, "Lame ", new Image("/images/blank.png",0,0,37,40));
	}

	private Widget getSYButton(final Confession confession) {
		// TODO: Button text
		// TODO: I18N
		return new CBButton(Activity.SYMPATHY, confession, userInfo,
				confessionService, "I have sympathy for you.",
				Constants.STYLE_CLASS_SYM_ACTIVITY, "Sym ", new Image("/images/sympathies.png",0,0,37,40));
	}

	private Widget getSBButton(final Confession confession) {
		// TODO: Button text
		// TODO: I18N
		return new CBButton(
				Activity.SAME_BOAT,
				confession,
				userInfo,
				confessionService,
				"Same Boat: Click if the above confessed ever happened with you or you were in the same situation as the above confessee. " +
				"You can anounymously register your 'Same Boat' vote.",
				Constants.STYLE_CLASS_SB_ACTIVITY, "SB ", new Image("/images/sameboat.png",0,0,37,40));
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

	// <fb:profile-pic uid="12345" width="32" height="32" linked="true"
	// \><fb:profile-pic>
	private String getProfilePictureTag(Confession confession) {
		final StringBuilder sb = new StringBuilder();

		if (confession.isShareAsAnyn()) {
			sb.append("<img src=");
			sb.append("'")
					.append(FacebookUtil.getFaceIconImage(confession
							.getGender())).append("'");
			sb.append("/>");
		} else {
			sb.append("<fb:profile-pic uid=\"")
					.append(confession.getFbId())
					.append("\" width=\"50\" height=\"50\" linked=\"true\"></fb:profile-pic>");
		}
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
