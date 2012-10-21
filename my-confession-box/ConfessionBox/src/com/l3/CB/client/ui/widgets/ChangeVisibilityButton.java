package com.l3.CB.client.ui.widgets;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class ChangeVisibilityButton extends PushButton {

	Logger logger = Logger.getLogger("CBLogger");

	public ChangeVisibilityButton(final Confession confession, final UserInfo userInfo,
			final ConfessionServiceAsync confessionService, String titleText,
			Image buttonImage, final boolean shareAnyn) {
		super();
		this.addStyleName("userControlButtonContainer");
		// TODO: I18N
		this.setTitle(titleText);
		// final Label btnCount = new Label(getCountToDisplay(confession.getActivityCount().get(activity.name()).toString()));

		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				confessionService.changeIdentityVisibility(userInfo.getUserId(), userInfo.getId(), confession.getConfId(), !shareAnyn, new AsyncCallback<Boolean>() {
					
					@Override
					public void onSuccess(Boolean result) {
						if(result) {
							
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						logger.log(Constants.LOG_LEVEL,
								"Exception in UserControlButton.onFailure()"
										+ caught.getCause());
					}
				});
			}
		});
	}
}
