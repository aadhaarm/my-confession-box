package com.l3.CB.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>FacebookService</code>.
 */
public interface FacebookServiceAsync {

	void getUserLoggedInDetails(String accessToken, AsyncCallback<String> callback);

	void getFriends(String accessToken, AsyncCallback<String> callback);

	void getUserDetails(String fbId, String accessToken, AsyncCallback<String> callback);
}
