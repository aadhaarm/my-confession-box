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
    public String getUserLoggedInDetails(String accessToken) {
	final String url = FacebookUtil.getUserUrl(ServerUtils.encode(accessToken));
	return makeSecure(ServerUtils.fetchURL(url));
    }


    @Override
    public String getUserDetails(String fbId, String accessToken) {
	final String url = FacebookUtil.getUserUrl(fbId, ServerUtils.encode(accessToken));
	return makeSecure(ServerUtils.fetchURL(url));
    }

    @Override
    public String getFriends(String accessToken) {
	return makeSecure(ServerUtils.fetchURL(FacebookUtil.getFriendsListUrl(accessToken)));
    }

    private String makeSecure(String fetchURL) {
	if(fetchURL != null) {
	    return "/*" + fetchURL + "*/";
	}
	return null;
    }
}
