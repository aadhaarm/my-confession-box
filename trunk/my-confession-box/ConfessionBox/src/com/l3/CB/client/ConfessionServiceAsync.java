package com.l3.CB.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.Filters;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.UserInfo;

/**
 * The async counterpart of <code>ConfessionService</code>.
 */
public interface ConfessionServiceAsync {

	void registerUser(UserInfo userInfo, AsyncCallback<UserInfo> callback);

	void registerConfession(Confession confession,
			AsyncCallback<Confession> callback);
	void getConfessions(int page, Filters filter, String locale,
			AsyncCallback<List<Confession>> callback);
	void getConfession(Long confId, AsyncCallback<Confession> callback);
	void getConfessionsIDID(int page, Long userId, AsyncCallback<List<Confession>> callback);
	void getConfessionsTOME(int page, Long userId, AsyncCallback<List<Confession>> callback);
	void pardonConfession(UserInfo pandonByUser, Long confId, UserInfo pardonedToUser, List<PardonCondition> pardonConditions, AsyncCallback<Void> callback);

	void registerUserActivity(Long userId, Long confId, Activity activity, AsyncCallback<Long> callback);
	void getUserActivity(Long userId, Long confId, AsyncCallback<Map<String, Long>> callback);

	void changeIdentityVisibility(Long userId, String fbId, Long confId,
			boolean shareAnyn, AsyncCallback<Boolean> callback);

	void changeConfessionVisibility(Long userId, String fbId, Long confId,
			boolean isVisible, AsyncCallback<Boolean> callback);

	void getCaptchaString(AsyncCallback<String> callback);
}
