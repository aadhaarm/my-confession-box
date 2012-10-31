/**
 * L3 Confession Box
 * 
 * Oct 13, 2012, 9:55:07 AM
 */
package com.l3.CB.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
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

	private FacebookServiceAsync facebookService = null;
	private ConfessionServiceAsync confessionService = null;

	CBText cbText = GWT.create(CBText.class);

	public static UserInfo loggedInUserInfo;
	public static String loggedInUserEmail;
	public static String confId;
	public static String accessToken;

	PopupPanel pnlApplicationLoad;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Cookies.setCookie("JSESSIONID", Double.toString(Random.nextDouble()));
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
				
		RootPanel.get(Constants.DIV_APPLN_TITLE).add(new Label(cbText.applicationTitle()));
		final HandlerManager confEventBus = new HandlerManager(null);
		showApplicationLoad();
		CommonUtils.login();
		Timer t = new Timer() {
			public void run() {
				if(accessToken != null && !"".equals(accessToken)) {
					checkUserLoginStatus(confessionService, facebookService, confEventBus);
					this.cancel();
				} else {
					this.schedule(1000);
				}
			}
		};
		t.schedule(1000);
	}

	private void showApplicationLoad() {
		pnlApplicationLoad = new PopupPanel(false);
		Image loaderImage = new Image("/images/appln-load-ajax-loader.gif");
		loaderImage.addStyleName(Constants.STYLE_CLASS_LOADER_IMAGE);
		pnlApplicationLoad.add(loaderImage);
		RootPanel.get(Constants.DIV_MAIN_CONTENT).add(pnlApplicationLoad);
		pnlApplicationLoad.setGlassEnabled(true);
		pnlApplicationLoad.setAnimationEnabled(true);
	}
	
	private void removeApplicationLoad() {
		pnlApplicationLoad.hide();
		RootPanel.get(Constants.DIV_MAIN_CONTENT).remove(pnlApplicationLoad);
	}

	private void checkUserLoginStatus(final ConfessionServiceAsync confessionService,
			final FacebookServiceAsync facebookService, final HandlerManager confEventBus) {
		confId = Location.getParameter(Constants.REQ_PARAM_CONF_ID);
		if(accessToken != null && !"".equals(accessToken)) {
			// Get user details of the current logged in user
			ConfessionBox.this.facebookService.getUserLoggedInDetails(accessToken, new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if(result != null){
						// parse the response text into JSON and get user info
						ConfessionBox.loggedInUserInfo = CommonUtils.getUserInfo(result);
						if(loggedInUserInfo.getEmail() != null) {
							loggedInUserEmail = loggedInUserInfo.getEmail();
						}
						if(loggedInUserInfo == null) {
							Window.alert(cbText.applicationError());
						}
						ConfessionController confessionController = new ConfessionController(confEventBus, confessionService, facebookService, confId, accessToken, cbText);
						confessionController.go(RootPanel.get());
						removeApplicationLoad();
					}
				}
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(cbText.applicationError());
				}
			});
		}
	}
}
