/**
 * L3 Confession Box
 * 
 * Oct 13, 2012, 10:14:08 AM
 */
package com.l3.CB.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("confession-box")
public interface ConfessionService extends RemoteService {

	UserInfo registerUser(UserInfo userInfo);
	
	Confession registerConfession(Confession confession);
	
	List<Confession> getConfessions(int page);
	
	List<Confession> getConfessions(int page, Long userId);
	
	List<Confession> getConfessionsForMe(int page, Long userId);
	
	Confession getConfession(Long confId);
	
	Long userActivity(Long userId, Long confId, Activity activity);
	
	Map<String, Long> getUserActivity(Long userId, Long confId);
	
	void pardonConfession(UserInfo pandonByUser, Long confId, UserInfo pardonedToUser);
}
