package com.l3.CB.client.view;

import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
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
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;
import com.google.gwt.user.client.DOM;

public class ConfessionFeedView extends Composite implements ConfessionFeedPresenter.Display {

	private VerticalPanel vpnlConfessionList;
	private ConfessionServiceAsync confessionService;
	private UserInfo userInfo;
	private DecoratorPanel contentTableDecorator;
	private ScrollPanel contentPanel;
	private int confessionPagesLoaded;
	private Image loaderImage;
	private boolean moreConfessions;
	
	public ConfessionFeedView(ConfessionServiceAsync rpcService, UserInfo userInfo) {
		super();
		this.confessionService = rpcService;
		this.userInfo = userInfo;
		confessionPagesLoaded = 1;
		loaderImage = new Image("/images/ajax-loader_1.gif");
		loaderImage.addStyleName("loaderImage");
		moreConfessions = true;
		
		contentPanel = new ScrollPanel();
		contentPanel.addStyleName("confessionScrollPanel");
//		contentPanel.setAlwaysShowScrollBars(true);
		contentPanel.addScrollHandler(new ScrollHandler() {
			boolean inEvent = false;
			@Override
			public void onScroll(ScrollEvent event) {
				if(moreConfessions && !inEvent && contentPanel.getMaximumVerticalScrollPosition() <= contentPanel.getVerticalScrollPosition()) {
					vpnlConfessionList.add(loaderImage);
					inEvent = true;
					confessionPagesLoaded++;
					confessionService.getConfessions(confessionPagesLoaded, new AsyncCallback<List<Confession>>() {
						
						@Override
						public void onSuccess(List<Confession> result) {
							setConfessions(result);
							inEvent = false;
							vpnlConfessionList.remove(loaderImage);
						}
						
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			}
		});
		
		vpnlConfessionList = new VerticalPanel();
		contentPanel.add(vpnlConfessionList);

		contentTableDecorator = new DecoratorPanel();
		contentTableDecorator.add(contentPanel);
		
		initWidget(contentTableDecorator);
	}

	public void setConfessions(List<Confession> confessions) {

		if(confessions != null && !confessions.isEmpty()) {
			for (Confession confession : confessions) {
				Grid grid = new Grid(4, 2);
				grid.getElement().setId("confession-id-" + confession.getConfId());
				grid.setHTML(0, 0, getProfilePictureTag(confession));
				grid.setWidget(0, 1, getConfessionWithName(confession));
				grid.setWidget(1, 1, getUserActivityButtons(confession));
				grid.setHTML(2, 1, getLikeButton(confession.getConfId()));
				grid.setHTML(3, 1, getCommentSection(confession.getConfId()));
				vpnlConfessionList.add(grid);
				CommonUtils.parseXFBMLJS(DOM.getElementById("confession-id-" + confession.getConfId()));
			}
		} else {
			moreConfessions = false;
		}
	}

	private Widget getConfessionWithName(Confession confession) {
		VerticalPanel pnlConfession = new VerticalPanel();

		if(confession.isShareAsAnyn()) {
			Label lblConf = new Label();
			lblConf.addStyleName("confession");
			lblConf.setText("Anonymous");
			pnlConfession.add(lblConf);
		} else {
			UserInfo userInfo = FacebookUtil.getUserInfo(confession.getUserDetailsJSON());
			if(userInfo != null) {
				Anchor ancUserName = new Anchor(userInfo.getName(), userInfo.getLink());
				ancUserName.addStyleName("confession");
				pnlConfession.add(ancUserName);
			}
		}

		pnlConfession.add(new Label(confession.getConfession()));

		return pnlConfession;
	}

	private FlowPanel getUserActivityButtons(final Confession confession) {
		final FlowPanel fPlaneUserActivity = new FlowPanel();
		final Button btnSB = (Button) getSBButton(confession);
		final Button btnSY = (Button) getSYButton(confession);
		final Button btnLM = (Button) getLMButton(confession);
		final Button btnSP = (Button) getSPButton(confession);
		final Button btnSNP = (Button) getSNPButton(confession);
		final Button btnAB = (Button) getABButton(confession);

		if(confession.getTimeStamp() != null) {
			fPlaneUserActivity.add(new Label(DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT).format(confession.getTimeStamp())));
		}
		confessionService.getUserActivity(userInfo.getUserId(), confession.getConfId(), new AsyncCallback<Map<String,Long>>() {

			@Override
			public void onSuccess(Map<String, Long> result) {

				if(result != null) {
					if(result.containsKey(Activity.ABUSE.name())) {
						btnAB.setEnabled(false);
					}
					if(result.containsKey(Activity.LAME.name())) {
						btnLM.setEnabled(false);
					}
					if(result.containsKey(Activity.SAME_BOAT.name())) {
						btnSB.setEnabled(false);
					}
					if(result.containsKey(Activity.SHOULD_BE_PARDONED.name())) {
						btnSP.setEnabled(false);
					}
					if(result.containsKey(Activity.SHOULD_NOT_BE_PARDONED.name())) {
						btnSNP.setEnabled(false);
					}
					if(result.containsKey(Activity.SYMPATHY.name())) {
						btnSY.setEnabled(false);
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
				// TODO Auto-generated method stub

			}
		});

		return fPlaneUserActivity;
	}

	private String getCommentSection(Long confId) {
		//<fb:comments href="http://example.com" num_posts="2" width="470"></fb:comments>
		final StringBuilder sb = new StringBuilder();
		sb.append("<fb:comments href=\"");
		sb.append(FacebookUtil.REDIRECT_URL).append("?conf=").append(confId).append("\"");
		sb.append(" num_posts=\"2\" width=\"470\"></fb:comments>");
		return sb.toString();
	}

	private Widget getABButton(final Confession confession) {
		final Button btnRepostAbuse = new Button("AB " + confession.getActivityCount().get(Activity.ABUSE.name()).toString());
		btnRepostAbuse.addStyleName("activityButton");
		btnRepostAbuse.setTitle("I Report Abuse!");
		btnRepostAbuse.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnRepostAbuse.setEnabled(false);
				confessionService.userActivity(userInfo.getUserId(), confession.getConfId(), Activity.ABUSE, new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						btnRepostAbuse.setText("AB " + result.toString());
					}

					@Override
					public void onFailure(Throwable caught) {
						btnRepostAbuse.setEnabled(true);
					}
				});
			}
		});
		return btnRepostAbuse;
	}

	private Widget getSNPButton(final Confession confession) {
		final Button btnSNP = new Button("SNP "+ confession.getActivityCount().get(Activity.SHOULD_NOT_BE_PARDONED.name()).toString());
		btnSNP.addStyleName("activityButton");
		btnSNP.setTitle("You should not be pordoned.");
		btnSNP.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnSNP.setEnabled(false);
				confessionService.userActivity(userInfo.getUserId(), confession.getConfId(), Activity.SHOULD_NOT_BE_PARDONED, new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						btnSNP.setText("SNP " + result.toString());
					}

					@Override
					public void onFailure(Throwable caught) {
						btnSNP.setEnabled(true);
					}
				});
			}
		});
		return btnSNP;
	}

	private Widget getSPButton(final Confession confession) {
		final Button btnSP = new Button("SP " + confession.getActivityCount().get(Activity.SHOULD_BE_PARDONED.name()).toString());
		btnSP.addStyleName("activityButton");
		btnSP.setTitle("You should be pordoned.");
		btnSP.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				btnSP.setEnabled(false);
				confessionService.userActivity(userInfo.getUserId(), confession.getConfId(), Activity.SHOULD_BE_PARDONED, new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						btnSP.setText("SP " + result.toString().toString());
					}

					@Override
					public void onFailure(Throwable caught) {
						btnSP.setEnabled(true);
					}
				});
			}
		});
		return btnSP;
	}

	private Widget getLMButton(final Confession confession) {
		final Button btnLM = new Button("Lame " + confession.getActivityCount().get(Activity.LAME.name()).toString());
		btnLM.addStyleName("activityButton");
		btnLM.setTitle("Such a Lame confession..");
		btnLM.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnLM.setEnabled(false);
				confessionService.userActivity(userInfo.getUserId(), confession.getConfId(), Activity.LAME, new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						btnLM.setText("Lame " + result.toString());
					}

					@Override
					public void onFailure(Throwable caught) {
						btnLM.setEnabled(true);
					}
				});
			}
		});
		return btnLM;
	}

	private Widget getSYButton(final Confession confession) {
		final Button btnSY = new Button("Sym " + confession.getActivityCount().get(Activity.SYMPATHY.name()).toString());
		btnSY.addStyleName("activityButton");
		btnSY.setTitle("I have sympathy for you.");
		btnSY.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				btnSY.setEnabled(false);
				confessionService.userActivity(userInfo.getUserId(), confession.getConfId(), Activity.SYMPATHY, new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						btnSY.setText("Sym " + result.toString());
					}

					@Override
					public void onFailure(Throwable caught) {
						btnSY.setEnabled(true);
					}
				});
			}
		});
		return btnSY;
	}

	private Button getSBButton(final Confession confession) {
		final Button btnSB = new Button("SB " + confession.getActivityCount().get(Activity.SAME_BOAT.name()).toString());
		btnSB.addStyleName("activityButton");
		btnSB.setTitle("I am in sort of same boat.");
		
		btnSB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnSB.setEnabled(false);
				confessionService.userActivity(userInfo.getUserId(), confession.getConfId(), Activity.SAME_BOAT, new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						btnSB.setText("SB " + result.toString());
					}

					@Override
					public void onFailure(Throwable caught) {
						btnSB.setEnabled(true);
					}
				});
			}
		});
		return btnSB;
	}

	private String getLikeButton(Long confId) {
		//<fb:like href="http://apps.facebook.com.fbconfessalfa" send="true" width="450" show_faces="false" font="lucida grande"></fb:like>
		final StringBuilder sb = new StringBuilder();
		sb.append("<fb:like href=\"");
		sb.append(FacebookUtil.REDIRECT_URL).append("?conf=").append(confId).append("\"");
		sb.append(" send=\"true\" width=\"450\" show_faces=\"false\" font=\"lucida grande\"></fb:like>");
		return sb.toString();
	}

	private String getProfilePictureTag(Confession confession) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<img src=");
		if(confession.isShareAsAnyn()) {
			UserInfo userInfo = FacebookUtil.getUserInfo(confession.getUserDetailsJSON());
			sb.append("'").append(FacebookUtil.getFaceIconImage(userInfo.getGender())).append("'");
		} else {
			sb.append("'").append(FacebookUtil.getUserImageUrl(confession.getFbId())).append("'");
		}
		sb.append("/>");
		return sb.toString();
	}

	@Override
	public Widget asWidget() {
		return this;
	}


	public void setConfessionPagesLoaded(int confessionPagesLoaded) {
		this.confessionPagesLoaded = confessionPagesLoaded;
	}
}
