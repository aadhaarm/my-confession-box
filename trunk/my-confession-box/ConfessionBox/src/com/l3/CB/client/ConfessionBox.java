/**
 * L3 Confession Box
 * 
 * Oct 13, 2012, 9:55:07 AM
 */
package com.l3.CB.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.l3.CB.client.controller.ConfessionController;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.CBText;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.UserInfo;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ConfessionBox implements EntryPoint {

    /*
     * CB text for I18N
     */
    public static CBText cbText = GWT.create(CBText.class);

    /*
     *  Login status and variables
     */
    public static String loginStatus;
    public static boolean isLoggedIn = false;
    public static UserInfo loggedInUserInfo;
    public static String confId;
    public static String accessToken;

    /*
     * Application variables 
     */
    public static HandlerManager confEventBus;
    public static FacebookServiceAsync facebookService = null;
    public static ConfessionServiceAsync confessionService = null;
    public static Image logo;

    /*
     * Pop-up panel showing LOADING
     */
    static PopupPanel pnlApplicationLoad;

    /**
     * ONLOAD This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
	
	// Application title/LOGO image
	logo = new Image(Constants.APPLICATION_LOGO_IMAGE);

	// Get XSRF setup
//	String cookie = Cookies.getCookie("JSESSIONID");
//	if(cookie == null) {
//	    Cookies.setCookie("JSESSIONID", Double.toString(Random.nextDouble()));
//	}

	// Exchange token with server
//	XsrfTokenServiceAsync xsrf = (XsrfTokenServiceAsync)GWT.create(XsrfTokenService.class);
//	((ServiceDefTarget)xsrf).setServiceEntryPoint(GWT.getModuleBaseURL() + "xsrf");
//	
//	xsrf.getNewXsrfToken(new AsyncCallback<XsrfToken>() {
//	    @Override
//	    public void onFailure(Throwable caught) {
//		Window.Location.assign(Window.Location.createUrlBuilder().setProtocol("https").buildString());
//		Error.handleError("ConfessionBox", "onFailure", caught);
//	    }
//	    @Override
//	    public void onSuccess(XsrfToken result) {
		confessionService = (ConfessionServiceAsync)GWT.create(ConfessionService.class);
//		((HasRpcToken) confessionService).setRpcToken(result);

		facebookService = (FacebookServiceAsync)GWT.create(FacebookService.class);
//		((HasRpcToken) facebookService).setRpcToken(result);
//	    }
//	});

	// Get Facebook access token and other information
	confEventBus = new HandlerManager(null);
	
	initializeUserInfo(true);
    }

    /**
     * Initialize User info
     * User logged in on FB
     */
    public static void initializeUserInfo(final boolean proceedFirstLoad) {
	if(proceedFirstLoad) {
	    showApplicationLoad();
	}
	CommonUtils.loginInFB(false);
	Timer timer = new Timer() {
	    @Override
	    public void run() {
		if(loginStatus != null) {
		    ConfessionBox.loggedInUserInfo = CommonUtils.getLoggedInUserInfo();
		    
		    if(confessionService != null && facebookService != null) {
			
			if(proceedFirstLoad) {
			    proceedToApp(confessionService, facebookService, confEventBus);
			}
			
			// Cancel timer
			this.cancel();
		    }   
		} else {
		    // Timer to reschedule for 1 sec
		    this.schedule(1000);
		}
	    }
	};
	// 0.5 second to start first load
	timer.schedule(500);
    }

    /**
     * Proceed to app controller after initialization
     *   
     * @param confessionService
     * @param facebookService
     * @param confEventBus
     */
    private static void proceedToApp(final ConfessionServiceAsync confessionService, final FacebookServiceAsync facebookService, final HandlerManager confEventBus) {
	if(!isLoggedIn) {
	    // If user chose to deny giving permission to CB or user isn't logged-in Facebook
	    loggedInUserInfo = new UserInfo();
	    loggedInUserInfo.setUserId(new Long(0));
	    loggedInUserInfo.setLocale(LocaleInfo.getCurrentLocale().getLocaleName());
	}
	confId = Location.getParameter(Constants.REQ_PARAM_CONF_ID);
	ConfessionController confessionController = new ConfessionController(confId);
	confessionController.go(RootPanel.get());
    }

    /**
     * APPLICATION LOAD animation
     */
    private static void showApplicationLoad() {
	Image loaderImage = new Image(Constants.LOAD_APPLICATION_IMAGE_PATH);
	pnlApplicationLoad = new PopupPanel(false);
	pnlApplicationLoad.setGlassEnabled(true);
	pnlApplicationLoad.setAnimationEnabled(true);
	loaderImage.addStyleName(Constants.STYLE_CLASS_LOADER_IMAGE);
	VerticalPanel vPnlApplnLoad = new VerticalPanel();
	vPnlApplnLoad.add(logo);
	vPnlApplnLoad.add(loaderImage);
	pnlApplicationLoad.add(vPnlApplnLoad);
	RootPanel.get(Constants.DIV_MAIN_CONTENT).add(pnlApplicationLoad);

	pnlApplicationLoad.setPopupPositionAndShow(new PositionCallback() {
	    @Override
	    public void setPosition(int offsetWidth, int offsetHeight) {
		pnlApplicationLoad.setPopupPosition((Window.getClientWidth()/2)-(offsetWidth/2), (Window.getClientHeight()/2)-(offsetHeight));
	    }
	});
	
//	pnlApplicationLoad.center();
    }

    /**
     * Remove animation
     */
    public static void removeApplicationLoad() {
	pnlApplicationLoad.hide();
	RootPanel.get(Constants.DIV_MAIN_CONTENT).remove(pnlApplicationLoad);
	RootPanel.get(Constants.DIV_APPLN_TITLE).add(logo);
    }

    /**
     * Get logged-in user info
     * 
     * @return {@link UserInfo}
     */
    public static UserInfo getLoggedInUserInfo() {
	return loggedInUserInfo;
    }

    /**
     * Set logged-in user info
     * 
     * @param loggedInUserInfo
     */
    public static void setLoggedInUserInfo(UserInfo loggedInUserInfo) {
	ConfessionBox.loggedInUserInfo = loggedInUserInfo;
    }
}