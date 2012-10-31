package com.l3.CB.client;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("facebook")
public interface FacebookService extends XsrfProtectedService {
    
    public String getUserLoggedInDetails(String accessToken);

    public String getUserDetails(String fbId, String accessToken);
    
    public String getFriends(String accessToken);
}