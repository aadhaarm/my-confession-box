package com.l3.CB.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.Filters;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.PardonStatus;
import com.l3.CB.shared.TO.UserInfo;

/**
 * The async counterpart of <code>ConfessionService</code>.
 */
public interface ConfessionServiceAsync {

    void registerUser(UserInfo userInfo, AsyncCallback<Long> callback);
    void isUserRegistered(String fbId, AsyncCallback<UserInfo> callback);

    // Register confession
    void registerConfession(Confession confession, AsyncCallback<Void> callback);    void getConfessions(int page, Filters filter, String locale, Long userId, AsyncCallback<List<Confession>> callback);
    void getConfessionsIDID(int page, Long userId, AsyncCallback<List<Confession>> callback);
    void getConfessionsTOME(int page, Long userId, AsyncCallback<List<Confession>> callback);
    void getConfession(Long confId, boolean secure, AsyncCallback<Confession> callback);

    void pardonConfession(UserInfo pandonByUser, Long confId,
	    UserInfo pardonedToUser, List<PardonCondition> pardonConditions,
	    PardonStatus pardonStatus, AsyncCallback<Void> callback);
    
    // User Activity
    void registerUserActivity(Long userId, Long confId, Activity activity, AsyncCallback<Long> callback);
    void getUserActivity(Long userId, Long confId, AsyncCallback<Map<String, Long>> callback);

    // Confession activities
    void changeIdentityVisibility(Long userId, String fbId, Long confId, boolean shareAnyn, AsyncCallback<Boolean> callback);
    void changeConfessionVisibility(Long userId, String fbId, Long confId, boolean isVisible, AsyncCallback<Boolean> callback);
    void createConfessedToUser(Long confId, Long userId, String fbId, ConfessionShare confessionShare, AsyncCallback<Void> callback);

    // Counts
    void getMyConfessionNumber(Long userId, AsyncCallback<Long> callback);
    void getNumberOfConfessionForMe(Long userId, AsyncCallback<Long> callback);

    void subscribe(Long confId, Long userId, Date timeStamp, AsyncCallback<Boolean> callback);
    void isSubscribed(Long confId, Long userId, AsyncCallback<Boolean> callback);

    // Human points
    void updateHumanPoints(Long userId, int points, AsyncCallback<Integer> callback);
    void getHumanPoints(Long userId, AsyncCallback<Integer> callback);
    void registerConfessionDraft(Confession confession,
	    AsyncCallback<Void> callback);
    void getConfessionDraft(Long userId, String fbId,
	    AsyncCallback<Confession> callback);
}