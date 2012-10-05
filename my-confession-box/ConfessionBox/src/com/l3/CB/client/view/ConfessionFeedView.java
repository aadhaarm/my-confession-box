package com.l3.CB.client.view;

import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionFeedView extends Composite implements ConfessionFeedPresenter.Display {

	private Grid confessionGrid;
	ConfessionServiceAsync confessionService;
	UserInfo userInfo;
	DecoratorPanel contentTableDecorator;

	public ConfessionFeedView(ConfessionServiceAsync rpcService, UserInfo userInfo) {
		super();
		this.confessionService = rpcService;
		this.userInfo = userInfo;

		contentTableDecorator = new DecoratorPanel();
		initWidget(contentTableDecorator);
	}

	public void setConfessions(List<Confession> confessions) {
		if(confessions != null && !confessions.isEmpty()) {

			confessionGrid = new Grid(confessions.size() * 4, 3);
			contentTableDecorator.add(confessionGrid);

			int row = 0;
			for (Confession confession : confessions) {
				confessionGrid.setHTML(row, 0, getProfilePictureTag(confession));
				confessionGrid.setWidget(row, 1, getConfessionWithName(confession));
				row++;
				confessionGrid.setWidget(row, 1, getUserActivityButtons(confession));
				row++;
				confessionGrid.setHTML(row, 1, getLikeButton(confession.getConfId()));
				row++;
				confessionGrid.setHTML(row, 1, getCommentSection(confession.getConfId()));
				row++;
			}
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
			Anchor ancUserName = new Anchor(userInfo.getName(), userInfo.getLink());
			ancUserName.addStyleName("confession");
			pnlConfession.add(ancUserName);
		}

		pnlConfession.add(new Label(confession.getConfession()));

		return pnlConfession;
	}

	private HorizontalPanel getUserActivityButtons(final Confession confession) {
		final HorizontalPanel hPlaneUserActivity = new HorizontalPanel();
		final Button btnSB = (Button) getSBButton(confession);
		final Button btnSY = (Button) getSYButton(confession);
		final Button btnLM = (Button) getLMButton(confession);
		final Button btnSP = (Button) getSPButton(confession);
		final Button btnSNP = (Button) getSNPButton(confession);
		final Button btnAB = (Button) getABButton(confession);

		confessionService.getUserActivity(userInfo.getUserId(), confession.getConfId(), new AsyncCallback<Map<String,Long>>() {

			@Override
			public void onSuccess(Map<String, Long> result) {

				if(result != null) {
					if(confession.getTimeStamp() != null) {
						hPlaneUserActivity.add(new Label(DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT).format(confession.getTimeStamp())));
					}
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

				hPlaneUserActivity.add(btnSB);
				hPlaneUserActivity.add(btnSY);
				hPlaneUserActivity.add(btnLM);
				hPlaneUserActivity.add(btnSP);
				hPlaneUserActivity.add(btnSNP);
				hPlaneUserActivity.add(btnAB);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});

		return hPlaneUserActivity;
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
		final Button btnRepostAbuse = new Button("AB");
		btnRepostAbuse.addStyleName("activityButton");
		btnRepostAbuse.setTitle(confession.getActivityCount().get(Activity.ABUSE.name()).toString());
		btnRepostAbuse.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnRepostAbuse.setEnabled(false);
				confessionService.userActivity(userInfo.getUserId(), confession.getConfId(), Activity.ABUSE, new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						btnRepostAbuse.setTitle(result.toString());
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
		final Button btnSNP = new Button("SNP");
		btnSNP.addStyleName("activityButton");
		btnSNP.setTitle(confession.getActivityCount().get(Activity.SHOULD_NOT_BE_PARDONED.name()).toString());
		btnSNP.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnSNP.setEnabled(false);
				confessionService.userActivity(userInfo.getUserId(), confession.getConfId(), Activity.SHOULD_NOT_BE_PARDONED, new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						btnSNP.setTitle(result.toString());
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
		final Button btnSP = new Button("SP");
		btnSP.addStyleName("activityButton");
		btnSP.setTitle(confession.getActivityCount().get(Activity.SHOULD_BE_PARDONED.name()).toString());
		btnSP.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				btnSP.setEnabled(false);
				confessionService.userActivity(userInfo.getUserId(), confession.getConfId(), Activity.SHOULD_BE_PARDONED, new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						btnSP.setTitle(result.toString().toString());
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
		final Button btnLM = new Button("Lame");
		btnLM.addStyleName("activityButton");
		btnLM.setTitle(confession.getActivityCount().get(Activity.LAME.name()).toString());
		btnLM.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnLM.setEnabled(false);
				confessionService.userActivity(userInfo.getUserId(), confession.getConfId(), Activity.LAME, new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						btnLM.setTitle(result.toString());
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
		final Button btnSY = new Button("Sym");
		btnSY.addStyleName("activityButton");
		btnSY.setTitle(confession.getActivityCount().get(Activity.SYMPATHY.name()).toString());
		btnSY.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				btnSY.setEnabled(false);
				confessionService.userActivity(userInfo.getUserId(), confession.getConfId(), Activity.SYMPATHY, new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						btnSY.setTitle(result.toString());
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
		final Button btnSB = new Button("SB");
		btnSB.addStyleName("activityButton");
		btnSB.setTitle(confession.getActivityCount().get(Activity.SAME_BOAT.name()).toString());
		
		btnSB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnSB.setEnabled(false);
				confessionService.userActivity(userInfo.getUserId(), confession.getConfId(), Activity.SAME_BOAT, new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						btnSB.setTitle(result.toString());
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
		sb.append(FacebookUtil.REDIRECT_URL).append("#").append(confId).append("\"");
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
}
