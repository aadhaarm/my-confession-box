package com.l3.CB.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface FacebookServiceAsync {

	void login(String authToken, AsyncCallback<String> callback);

	void getUserDetails(String accessToken, AsyncCallback<String> callback);

	void getAuthUrl(AsyncCallback<String> callback);

	void getFriends(String accessToken, AsyncCallback<String> callback);
}
