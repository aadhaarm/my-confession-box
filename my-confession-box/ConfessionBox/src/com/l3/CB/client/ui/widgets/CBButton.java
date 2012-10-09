package com.l3.CB.client.ui.widgets;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class CBButton extends AbsolutePanel {

	Logger logger = Logger.getLogger("CBLogger");
	final PushButton btn;

	public CBButton(final Activity activity, final Confession confession,
			final UserInfo userInfo,
			final ConfessionServiceAsync confessionService, String titleText,
			String btnStyleName, final String btnLabelText, Image buttonImage) {
		super();
		this.addStyleName("activityButtonContainer");
		
		btn = new PushButton(buttonImage);
		// TODO: Button text
		btn.addStyleName(btnStyleName);
		// TODO: I18N
		this.setTitle(titleText);
		final Label btnCount = new Label(getCountToDisplay(confession.getActivityCount().get(activity.name()).toString()));

		btn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btn.setEnabled(false);
				confessionService.userActivity(userInfo.getUserId(),
						confession.getConfId(), activity,
						new AsyncCallback<Long>() {

							@Override
							public void onSuccess(Long result) {
								btnCount.setText(getCountToDisplay(result.toString()));
							}

							@Override
							public void onFailure(Throwable caught) {
								logger.log(Constants.LOG_LEVEL,
										"Exception in CBButton.onFailure()"
												+ caught.getCause());
								btn.setEnabled(true);
							}
						});
			}
		});
		
		this.add(btn);
		btnCount.setStyleName("btnWrapper");
		this.add(btnCount, btn.getElement());
	}

	
	private String getCountToDisplay(String count) {
		if(count == null) {
			return "";
		}
		if(count != null && count.length() == 4) {
			count = count.substring(0, 0) + "k";
		} else if(count != null && count.length() == 5) {
			count = count.substring(0, 1) + "k";
		} else if(count != null && count.length() > 5) {
			count = "☝" + count.substring(0, 2) + "k";
		}
		return count;
	}

	public PushButton getBtn() {
		return btn;
	}
}
