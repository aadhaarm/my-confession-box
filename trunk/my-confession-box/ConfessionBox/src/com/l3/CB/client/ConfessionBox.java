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
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
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

    /*
     * CB text for I18N
     */
    public static CBText cbText = GWT.create(CBText.class);

    public static boolean applicationLoaded = false;

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
    public static HandlerManager eventBus;
    public static FacebookServiceAsync facebookService = null;
    public static ConfessionServiceAsync confessionService = null;
    public static Image imgLogo;
    public static boolean isMobile;
    public static boolean isTouchEnabled;

    /*
     * Pop-up panel showing LOADING
     */
    static PopupPanel pnlApplicationLoad;

    /**
     * ON-LOAD This is the entry point method.
     */
    @Override
    public void onModuleLoad() {

	//	Window.alert("User Agent:" + Navigator.getUserAgent()
	//		+ " App code name:" + Navigator.getAppCodeName() + " Platform:"
	//		+ Navigator.getPlatform() + " App name:"
	//		+ Navigator.getAppName() + " Java enabled:"
	//		+ Boolean.toString(Navigator.isJavaEnabled()) + " Screen dim:"
	//		+ Window.getClientWidth() + "*" + Window.getClientHeight());

	// Decide whether to consider device screen to be small or large?
	CommonUtils.setupScreen();
	
	if(!isMobile) {
	    // Introduce App
	    PopupPanel pPnlIntro = CommonUtils.introductionCB();
	    pPnlIntro.center();
	}
	
	// Application title/LOGO image
	imgLogo = new Image(Constants.IMAGE_APPLICATION_LOGO_PATH);
	
	// Initialize services
	confessionService = (ConfessionServiceAsync)GWT.create(ConfessionService.class);
	facebookService = (FacebookServiceAsync)GWT.create(FacebookService.class);
	
	// Get application event bus
	eventBus = new HandlerManager(null);
	
	// Get application auth-code that facebook might have sent
	String authCode = Location.getParameter("code");
	
	// FB might have also sent an error message
	String error = Location.getParameter("error_reason");
	
	// Get confession ID from request if any
	confId = Location.getParameter(Constants.REQ_PARAM_CONF_ID);
	if (null != error && error.equals("user_denied")) {
	    // Proceed to app if user did not give permissions or an error has occurred
	    proceedToApp(confessionService, facebookService, eventBus);
	} else if (authCode != null) {
	    if(CommonUtils.isNullOrEmpty(confId)) {
		confId = Location.getParameter("state");
	    }
	    // Login user and initialize application
	    loginAndInitializeApplication(authCode);
	} else {
//	    proceedToApp(confessionService, facebookService, eventBus);
    	    initializeUserInfo(true);
	}
    }

    /**
     * Login user and initialize application
     * 
     * @param authCode
     */
    private void loginAndInitializeApplication(String authCode) {
	facebookService.login(authCode, new AsyncCallback<String>() {
	    @Override
	    public void onSuccess(String resultAccessToken) {
		if(resultAccessToken != null) {
		    accessToken = CommonUtils.processAccessToken(resultAccessToken);
		    if(CommonUtils.isNotNullAndNotEmpty(accessToken)) {
			ConfessionBox.facebookService.getUserDetails(accessToken, new AsyncCallback<String>() {
			    @Override
			    public void onSuccess(String userDetailsJSON) {
				if(CommonUtils.isNotNullAndNotEmpty(userDetailsJSON)){
				    // parse the response text into JSON
				    ConfessionBox.loggedInUserInfo = CommonUtils.getUserInfo(userDetailsJSON);
				    if(loggedInUserInfo != null) {
					isLoggedIn = true;
					loginStatus = "login";
				    }
				    proceedToApp(confessionService, facebookService, eventBus);
				}
			    }
			    @Override
			    public void onFailure(Throwable caught) {
				Error.handleError("ConfessionBox", "onFailure",	caught);
			    }
			});
		    } else {
			proceedToApp(confessionService, facebookService, eventBus);
		    }
		}
	    }
	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("ConfessionBox", "onFailure", caught);
	    }
	});
    }

    /**
     * Initialize User info
     * User logged in on FB
     */
    public static void initializeUserInfo(final boolean proceedFirstLoad) {
	if(proceedFirstLoad) {
	    showApplicationLoad();
	}

	if(Navigator.isJavaEnabled()) {
	    CommonUtils.loginInFB(false, "");
	    Timer timer = new Timer() {
		int count = 0;
		@Override
		public void run() {
		    if(loginStatus != null) {
			ConfessionBox.loggedInUserInfo = CommonUtils.getLoggedInUserInfo(1, null);
			if(confessionService != null && facebookService != null) {
			    if(proceedFirstLoad) {
				proceedToApp(confessionService, facebookService, eventBus);
			    }
			    // Cancel timer
			    this.cancel();
			}   
		    } 
		    else 
			if(count < 3){
			    count++;
			    // Timer to reschedule for 1 sec
			    this.schedule(1000);
			} else {
			    if(proceedFirstLoad) {
				proceedToApp(confessionService, facebookService, eventBus);
			    }
			}
		}
	    };
	    // 0.5 second to start first load
	    timer.schedule(500);
	} else {
	    CommonUtils.login(1);
	}
    }

    /**
     * Proceed to app controller after initialization
     *   
     * @param confessionService
     * @param facebookService
     * @param confEventBus
     */
    public static void proceedToApp(final ConfessionServiceAsync confessionService, final FacebookServiceAsync facebookService, final HandlerManager confEventBus) {
	applicationLoaded = true;
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
	vPnlApplnLoad.add(imgLogo);
	vPnlApplnLoad.add(loaderImage);
	pnlApplicationLoad.add(vPnlApplnLoad);
	RootPanel.get(Constants.DIV_MAIN_CONTENT).add(pnlApplicationLoad);

	pnlApplicationLoad.setPopupPositionAndShow(new PositionCallback() {
	    @Override
	    public void setPosition(int offsetWidth, int offsetHeight) {
		pnlApplicationLoad.setPopupPosition((Window.getClientWidth()/2)-(offsetWidth/2), (Window.getClientHeight()/2)-(offsetHeight));
	    }
	});
    }

    /**
     * Remove animation
     */
    public static void removeApplicationLoad() {
	if(pnlApplicationLoad != null) {
	    pnlApplicationLoad.hide();
	    RootPanel.get(Constants.DIV_MAIN_CONTENT).remove(pnlApplicationLoad);
	}
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
    public static void setLoggedInUserInfoAndResetApp(UserInfo loggedInUserInfo, String hashEvent) {
	ConfessionBox.loggedInUserInfo = loggedInUserInfo;
	if(applicationLoaded) {
	    ConfessionController.updateUserInfoAndInitializeAPP();
	}
	if(hashEvent != null && !hashEvent.isEmpty()) {
	    CommonUtils.fireHistoryEvent(hashEvent);
	}
    }
}