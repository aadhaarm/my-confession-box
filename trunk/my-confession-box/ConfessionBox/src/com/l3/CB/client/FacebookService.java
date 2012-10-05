package com.l3.CB.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.l3.CB.shared.TO.UserInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("facebook")
public interface FacebookService extends RemoteService {
    public String login(String authToken);
    
    public String getUserDetails(String accessToken);
    
    public String getAuthUrl();
}