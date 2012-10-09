package com.l3.CB.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.l3.CB.client.controller.ConfessionController;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.UserInfo;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ConfessionBox implements EntryPoint {

	Logger logger = Logger.getLogger("CBLogger");

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final ConfessionServiceAsync confessionService = GWT
			.create(ConfessionService.class);
	private final FacebookServiceAsync facebookService = GWT
			.create(FacebookService.class);

	public static UserInfo userInfo;
	public static String confId;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		HandlerManager confEventBus = new HandlerManager(null);
		checkUserLoginStatus(confessionService, facebookService, confEventBus);
	}

	private void checkUserLoginStatus(final ConfessionServiceAsync confessionService,
			FacebookServiceAsync facebookService, final HandlerManager confEventBus) {

		String authToken = Location.getParameter("code");
		logger.log(Level.INFO, "Auth Token:" + authToken);
		String error = Location.getParameter("error_reason");
		logger.log(Level.INFO, "Error on load:" + error);
		confId = Location.getParameter(Constants.REQ_PARAM_CONF_ID);
		
		if (null != error && error.equals("user_denied")) {
			Window.alert("Error:" + error);
			logger.log(Level.INFO, "UserDenied:");
		} else if (authToken == null || "".equals(authToken)) {
			if(null != confId){
				CommonUtils.redirect(FacebookUtil.getAuthorizeUrl(confId));
			} else {
				CommonUtils.redirect(FacebookUtil.getAuthorizeUrl());
			}
		} else {
			confId = Location.getParameter("state");
			facebookService.login(authToken, new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if(result != null) {
						String accessToken = CommonUtils.processAccessToken(result);
						if(accessToken != null && !"".equals(accessToken)) {
							ConfessionBox.this.facebookService.getUserDetails(accessToken, new AsyncCallback<String>() {
								@Override
								public void onSuccess(String result) {
									if(result != null){
										// parse the response text into JSON
										ConfessionBox.userInfo = CommonUtils.getUserInfo(result);
										logger.log(Level.INFO, "User Info:" + userInfo.toString());
										ConfessionController confessionController = new ConfessionController(confEventBus, confessionService, userInfo, confId);
										confessionController.go(RootPanel.get());
									}
								}
								@Override
								public void onFailure(Throwable caught) {
									logger.log(Level.INFO, "Error getUserDetails:" + caught.getStackTrace());
								}
							});
						}
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					logger.log(Level.INFO, "Error onload:" + caught.getMessage());
				}
			});
		}
	}
}
