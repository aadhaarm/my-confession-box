/**
 * L3 Confession Box
 * 
 * Oct 13, 2012, 10:14:08 AM
 */
package com.l3.CB.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.Filters;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.PardonStatus;
import com.l3.CB.shared.TO.UserInfo;

/**
 * The client side stub for the RPC service.
 */
@XsrfProtect
@RemoteServiceRelativePath("confession-box")
public interface ConfessionService extends XsrfProtectedService {

    // REGISTER USER
    UserInfo registerUser(UserInfo userInfo);
    UserInfo isUserRegistered(String fbId);
    
    // HUMAN POINTS methods
    int updateHumanPoints(Long userId, int points);
    int getHumanPoints(Long userId);
    
    // REGISTER CONFESSION
    Confession registerConfession(Confession confession);
    
    // GET Confession
    List<Confession> getConfessions(int page, Filters filter, String locale, Long userId);
    List<Confession> getConfessionsIDID(int page, Long userId);
    List<Confession> getConfessionsTOME(int page, Long userId);
    Confession getConfession(Long confId);
    long getNumberOfConfessionForMe(Long userId);

    // USER ACTIVITY
    Long registerUserActivity(Long userId, Long confId, Activity activity);
    Map<String, Long> getUserActivity(Long userId, Long confId);

    // User control and confession changes
    void pardonConfession(UserInfo pandonByUser, Long confId, UserInfo pardonedToUser, List<PardonCondition> pardonConditions, PardonStatus pardonStatus);
    boolean changeIdentityVisibility(Long userId, String fbId, Long confId, boolean shareAnyn);
    boolean changeConfessionVisibility(Long userId, String fbId, Long confId, boolean isVisible);
    void createConfessedToUser(Long confId, Long userId, String fbId, ConfessionShare confessionShare);
    
    long getMyConfessionNumber(Long userId);

    // SUBSCRIBE
    boolean subscribe(Long confId, Long userId);
    boolean isSubscribed(Long confId, Long userId);
}