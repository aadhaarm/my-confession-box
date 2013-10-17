package com.l3.CB.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.l3.CB.shared.CommentFilter;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Comment;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.ConfessionUpdate;
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
    void registerConfession(Confession confession, AsyncCallback<Void> callback);    
    void registerConfessionUpdate(ConfessionUpdate confessionUpdate, AsyncCallback<Void> callback);
    
    void getConfessions(int page, Filters filter, String locale, Long userId, AsyncCallback<List<Confession>> callback);
    void getConfessionsIDID(int page, Long userId, String fbId, AsyncCallback<List<Confession>> callback);
    void getConfessionsTOME(int page, Long userId, String fbId, AsyncCallback<List<Confession>> callback);
    void getConfession(Long confId, Long userId, String fbId, boolean secure, AsyncCallback<Confession> callback);

    void pardonConfession(UserInfo pandonByUser, Long confId, UserInfo pardonedToUser, List<PardonCondition> pardonConditions,
	    PardonStatus pardonStatus, Date updateTimeStamp, AsyncCallback<Void> callback);
    
    void registerUserActivity(Long userId, Long confId, Activity activity, Date updateTimeStamp, AsyncCallback<Long> callback);
    void getUserActivity(Long userId, Long confId, AsyncCallback<Map<String, Long>> callback);

    void changeIdentityVisibility(Long userId, String fbId, Long confId, boolean shareAnyn, Date updateTimeStamp, AsyncCallback<Boolean> callback);
    void changeConfessionVisibility(Long userId, String fbId, Long confId, boolean isVisible, Date updateTimeStamp, AsyncCallback<Boolean> callback);
    void createConfessedToUser(Long confId, Long userId, String fbId, ConfessionShare confessionShare, Date updateTimeStamp, AsyncCallback<Void> callback);

    // Counts
    void getMyConfessionNumber(Long userId, AsyncCallback<Long> callback);
    void getNumberOfConfessionForMe(Long userId, AsyncCallback<Long> callback);

    void subscribe(Long confId, Long userId, Date timeStamp, AsyncCallback<Boolean> callback);
    void isSubscribed(Long confId, Long userId, AsyncCallback<Boolean> callback);

    // Human points
    void updateHumanPoints(Long userId, int points, AsyncCallback<Integer> callback);
    void getHumanPoints(Long userId, AsyncCallback<Integer> callback);

    // Confession Drafts
    void registerConfessionDraft(Confession confession, AsyncCallback<Void> callback);
    void getConfessionDraft(Long userId, String fbId, AsyncCallback<Confession> callback);
    void clearConfessionDraft(Long userId, String fbId, AsyncCallback<Void> callback);
    void getConfessionUpdates(Long confId, AsyncCallback<List<ConfessionUpdate>> callback);
    
    // COMMENT
    void saveComment(Comment comment, AsyncCallback<Void> callback);
    void getComments(CommentFilter filter, AsyncCallback<CommentFilter> callback);
    void voteOnComment(CommentFilter filter, AsyncCallback<Void> callback);
}