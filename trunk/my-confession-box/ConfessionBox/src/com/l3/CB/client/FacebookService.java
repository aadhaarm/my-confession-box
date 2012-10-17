package com.l3.CB.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("facebook")
public interface FacebookService extends RemoteService {
    
	public String getAccessToken(String authToken);
    
    public String getUserDetails(String accessToken);

    public String getUserDetails(String fbId, String accessToken);
    
    public String getAuthUrl();

    public String getFriends(String accessToken);
}