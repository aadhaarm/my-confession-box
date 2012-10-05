package com.l3.CB.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ConfessionServiceAsync {

	void registerUser(UserInfo userInfo, AsyncCallback<UserInfo> callback);

	void registerConfession(Confession confession,
			AsyncCallback<Confession> callback);

	void getConfessions(int page, AsyncCallback<List<Confession>> callback);

	void userActivity(Long userId, Long confId, Activity activity,
			AsyncCallback<Long> callback);

	void getUserActivity(Long userId, Long confId,
			AsyncCallback<Map<String, Long>> callback);
}
