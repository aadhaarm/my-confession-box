package com.l3.CB.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.l3.CB.client.FacebookService;
import com.l3.CB.server.utils.ServerUtils;
import com.l3.CB.shared.FacebookUtil;

/**
 * The server side implementation of the RPC service.
 */
public class FacebookServiceImpl extends RemoteServiceServlet implements
		FacebookService {

	/**
	 * Default serial ID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getAccessToken(String authToken) {
		final String url = ServerUtils.getAccessTokenUrl(authToken);
        return ServerUtils.fetchURL(url);
	}

	@Override
	public String getUserDetails(String accessToken) {
		final String url = FacebookUtil.getUserUrl(ServerUtils.encode(accessToken));
        return ServerUtils.fetchURL(url);
	}

	@Override
	public String getUserDetails(String fbId, String accessToken) {
		final String url = FacebookUtil.getUserUrl(fbId, ServerUtils.encode(accessToken));
        return ServerUtils.fetchURL(url);
	}

	@Override
	public String getAuthUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFriends(String accessToken) {
		return ServerUtils.fetchURL(FacebookUtil.getFriendsListUrl(accessToken));
	}

}
