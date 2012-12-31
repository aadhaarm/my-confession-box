package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.presenter.LoginStatusPresenter;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.FacebookUtil;

public class LoginStatusView extends Composite implements LoginStatusPresenter.Display {

    public LoginStatusView() {

	final FlowPanel fPnlLoginStatus = new FlowPanel();

	if(ConfessionBox.isLoggedIn && ConfessionBox.loggedInUserInfo != null) {
	    final FlowPanel pnlLoginStatus = new FlowPanel();
	    pnlLoginStatus.setStyleName("userLoginStatus");
	    Image loginUserImage = new Image(FacebookUtil.getUserImageUrl(ConfessionBox.loggedInUserInfo.getId()));
	    loginUserImage.setStyleName("userLoginStatusImage");
	    pnlLoginStatus.add(loginUserImage);
	    Label userFullName = new Label("You are loggedin as '" + ConfessionBox.loggedInUserInfo.getName() + "'");
	    userFullName.setStyleName("userLoginStatusName");
	    pnlLoginStatus.add(userFullName);

	    /*
	     * Logout link
	     */
	    final Anchor ancLogout = new Anchor(ConfessionBox.cbText.logoutLinkLabelText());
	    if(ConfessionBox.isTouchEnabled) {
//		ancLogout.setStyleName(Constants.STYLE_CLASS_SUBSCRIBE_LINK_TO_BTN_U);
	    }

	    if(ConfessionBox.isLoggedIn) {
		ancLogout.addClickHandler(new ClickHandler() {
		    @Override
		    public void onClick(ClickEvent event) {
			CommonUtils.logout(ConfessionBox.cbText.logoutInfoMessage());
			fPnlLoginStatus.remove(pnlLoginStatus);
		    }
		});
	    }
	    pnlLoginStatus.add(ancLogout);

	    fPnlLoginStatus.add(pnlLoginStatus);
	}


	initWidget(fPnlLoginStatus);
    }

    @Override
    public Widget asWidget() {
	return this;
    }
}