/**
 * L3 Confession Box
 * 
 * Oct 13, 2012, 10:14:08 AM
 */
package com.l3.CB.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.ConfessionUpdate;
import com.l3.CB.shared.TO.Filters;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.PardonStatus;
import com.l3.CB.shared.TO.UserInfo;

/**
 * The client side stub for the RPC service.
 */
@XsrfProtect
@RemoteServiceRelativePath("confession-box")
public interface ConfessionService extends RemoteService {

    // REGISTER USER
    Long registerUser(UserInfo userInfo);
    UserInfo isUserRegistered(String fbId);
    
    // HUMAN POINTS methods
    int updateHumanPoints(Long userId, int points);
    int getHumanPoints(Long userId);
    
    // REGISTER CONFESSION
    void registerConfession(Confession confession);
    void registerConfessionUpdate(ConfessionUpdate confessionUpdate);

    // Confession Update
    void registerConfessionDraft(Confession confession);
    List<ConfessionUpdate> getConfessionUpdates(Long confId);
    
    // GET Confession
    List<Confession> getConfessions(int page, Filters filter, String locale, Long userId);
    List<Confession> getConfessionsIDID(int page, Long userId, String fbId);
    List<Confession> getConfessionsTOME(int page, Long userId, String fbId);
    Confession getConfession(Long confId, Long userId, String fbId, boolean secure);
    long getNumberOfConfessionForMe(Long userId);
    Confession getConfessionDraft(Long userId, String fbId);
    void clearConfessionDraft(Long userId, String fbId);
    
    // USER ACTIVITY
    Long registerUserActivity(Long userId, Long confId, Activity activity, Date updateTimeStamp);
    Map<String, Long> getUserActivity(Long userId, Long confId);

    // User control and confession changes
    void pardonConfession(UserInfo pandonByUser, Long confId, UserInfo pardonedToUser, List<PardonCondition> pardonConditions, PardonStatus pardonStatus, Date updateTimeStamp);
    boolean changeIdentityVisibility(Long userId, String fbId, Long confId, boolean shareAnyn, Date updateTimeStamp);
    boolean changeConfessionVisibility(Long userId, String fbId, Long confId, boolean isVisible, Date updateTimeStamp);
    void createConfessedToUser(Long confId, Long userId, String fbId, ConfessionShare confessionShare, Date updateTimeStamp);
    
    long getMyConfessionNumber(Long userId);

    // SUBSCRIBE
    boolean subscribe(Long confId, Long userId, Date timeStamp);
    boolean isSubscribed(Long confId, Long userId);
}