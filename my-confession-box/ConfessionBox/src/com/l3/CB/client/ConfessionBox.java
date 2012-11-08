/**
 * L3 Confession Box
 * 
 * Oct 13, 2012, 9:55:07 AM
 */
package com.l3.CB.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.google.gwt.user.client.rpc.XsrfTokenService;
import com.google.gwt.user.client.rpc.XsrfTokenServiceAsync;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.l3.CB.client.controller.ConfessionController;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.CBText;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.UserInfo;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ConfessionBox implements EntryPoint {

    public static CBText cbText = GWT.create(CBText.class);
    public static UserInfo loggedInUserInfo;
    public static String confId;
    public static String accessToken;
    public static HandlerManager confEventBus;
    public static FacebookServiceAsync facebookService = null;
    public static ConfessionServiceAsync confessionService = null;

    PopupPanel pnlApplicationLoad;

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
	// Application title
	RootPanel.get(Constants.DIV_APPLN_TITLE).add(new Label(cbText.applicationTitle()));

	// Get XSRF setup
	String cookie = Cookies.getCookie("JSESSIONID");
	if(cookie == null) {
	    Date expiration = new Date();
	    CalendarUtil.addDaysToDate(expiration, 1);
	    Cookies.setCookie("JSESSIONID", Double.toString(Random.nextDouble()), expiration);
	}

	// Exchange token with server
	XsrfTokenServiceAsync xsrf = (XsrfTokenServiceAsync)GWT.create(XsrfTokenService.class);
	((ServiceDefTarget)xsrf).setServiceEntryPoint(GWT.getModuleBaseURL() + "xsrf");
	xsrf.getNewXsrfToken(new AsyncCallback<XsrfToken>() {
	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("ConfessionBox", "onFailure", caught);
	    }
	    @Override
	    public void onSuccess(XsrfToken result) {
		confessionService = (ConfessionServiceAsync)GWT.create(ConfessionService.class);
		((HasRpcToken) confessionService).setRpcToken(result);

		facebookService = (FacebookServiceAsync)GWT.create(FacebookService.class);
		((HasRpcToken) facebookService).setRpcToken(result);
	    }
	});

	// Get Facebook access token and other information
	confEventBus = new HandlerManager(null);
	showApplicationLoad();
	CommonUtils.login();
	Timer t = new Timer() {
	    @Override
	    public void run() {
		ConfessionBox.loggedInUserInfo = CommonUtils.getLoggedInUserInfo();
		if(ConfessionBox.loggedInUserInfo != null && confessionService != null && facebookService != null) {
		    proceedToApp(confessionService, facebookService, confEventBus);
		    this.cancel();
		} else {
		    this.schedule(1000);
		}
	    }
	};
	t.schedule(500);
    }

    private void proceedToApp(final ConfessionServiceAsync confessionService, final FacebookServiceAsync facebookService, final HandlerManager confEventBus) {
	confId = Location.getParameter(Constants.REQ_PARAM_CONF_ID);
	ConfessionController confessionController = new ConfessionController(confId);
	confessionController.go(RootPanel.get());
	removeApplicationLoad();
    }

    /**
     * APPLICATION LOAD animation
     */
    private void showApplicationLoad() {
	Image loaderImage = new Image(Constants.LOAD_APPLICATION_IMAGE_PATH);
	pnlApplicationLoad = new PopupPanel(false);
	pnlApplicationLoad.setGlassEnabled(true);
	pnlApplicationLoad.setAnimationEnabled(true);
	loaderImage.addStyleName(Constants.STYLE_CLASS_LOADER_IMAGE);
	pnlApplicationLoad.add(loaderImage);
	RootPanel.get(Constants.DIV_MAIN_CONTENT).add(pnlApplicationLoad);
	pnlApplicationLoad.center();
    }

    /**
     * Remove animation
     */
    private void removeApplicationLoad() {
	pnlApplicationLoad.hide();
	RootPanel.get(Constants.DIV_MAIN_CONTENT).remove(pnlApplicationLoad);
    }
}