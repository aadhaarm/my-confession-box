package com.l3.CB.server;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;
import com.l3.CB.client.ConfessionService;
import com.l3.CB.server.manager.ActivityManager;
import com.l3.CB.server.manager.CacheManager;
import com.l3.CB.server.manager.ConfessionManager;
import com.l3.CB.server.manager.PardonManager;
import com.l3.CB.server.manager.UserManager;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.Filters;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.PardonStatus;
import com.l3.CB.shared.TO.UserInfo;
/**
 * The server side implementation of the RPC service.
 */
public class ConfessionServiceImpl extends XsrfProtectedServiceServlet implements
ConfessionService {

    /**
     * Default serial version ID
     */
    private static final long serialVersionUID = 1L;
    
    @SuppressWarnings("unused")
    private static final CacheManager cacheManager = new CacheManager();

    @Override
    public Long registerUser(UserInfo userInfo) {
	return UserManager.registerUser(userInfo).getUserId();
    }

    /**
     * Register confession.
     * @param confession - {@link Confession}
     */
    @Override
    public void registerConfession(Confession confession) {
	ConfessionManager.registerConfession(confession, getThreadLocalRequest().getRemoteHost().toString());
	clearConfessionDraft(confession.getUserId(), confession.getFbId());
    }

    /**
     * Get confessions.
     * @param page - {@link Integer}
     */
    @Override
    public List<Confession> getConfessions(int page, Filters filter, String locale, Long userId) {
	return ConfessionManager.getConfessionsByFilter(page, filter, locale, userId);
    }

    /**
     * Get confessions
     * @param oage - {@link Integer}
     * @param userId - {@link Long}
     */
    @Override
    public List<Confession> getConfessionsIDID(int page, Long userId, String fbId) {
	List<Confession> confessions = null;
	if(UserManager.validateUser(userId, fbId)) {
	    confessions = ConfessionManager.getConfessionsByUser(page, userId, true);
	}
	return confessions;
    }

    @Override
    public Long registerUserActivity(Long userId, Long confId, Activity activity, Date updateTimeStamp) {
	return ActivityManager.registerUserActivity(userId, confId, activity, updateTimeStamp);
    }
    
    @Override
    public Map<String, Long> getUserActivity(Long userId, Long confId) {
	return ActivityManager.getUserActivity(userId, confId);
    }

    /**
     * Get confession
     */
    @Override
    public Confession getConfession(Long confId, Long userId, String fbId, boolean secure) {
	Confession confession = null;
	if(secure) {
	    if(UserManager.validateUser(userId, fbId)) {
		confession = ConfessionManager.getOneConfession(confId, secure, userId);
	    }
	} else {
	    confession = ConfessionManager.getOneConfession(confId, secure, userId);
	}
	return confession;
    }

    @Override
    public List<Confession> getConfessionsTOME(int page, Long userId, String fbId) {
	List<Confession> confList = null;
	if(UserManager.validateUser(userId, fbId)) {
	    confList = ConfessionManager.getConfessionsConfessedToMe(page, userId);
	}
	return confList; 
    }

    @Override
    public void pardonConfession(UserInfo pardonByUser, Long confId, UserInfo pardonedToUser, List<PardonCondition> pardonConditions, PardonStatus pardonStatus, Date updateTimeStamp) {
	if(UserManager.validateUser(pardonByUser.getUserId(), pardonByUser.getId())) {
	    PardonManager.pardonConfession(pardonByUser, confId, pardonedToUser, pardonConditions, pardonStatus, updateTimeStamp);
	}
    }

    @Override
    public boolean changeIdentityVisibility(Long userId, String fbId, Long confId, boolean shareAnyn, Date updateTimeStamp) {
	ConfessionManager.changeIdentityVisibility(userId, fbId, confId, shareAnyn, updateTimeStamp);
	return true;
    }

    @Override
    public boolean changeConfessionVisibility(Long userId, String fbId, Long confId, boolean isVisible, Date updateTimeStamp) {
	return ConfessionManager.changeConfessionVisibility(userId, fbId, confId, isVisible, updateTimeStamp);
    }

    @Override
    public long getMyConfessionNumber(Long userId) {
	return ConfessionManager.getConfessionsByUserCount(userId);
    }

    @Override
    public long getNumberOfConfessionForMe(Long userId) {
	return ConfessionManager.getConfessionsForMeCount(userId);
    }

    @Override
    public boolean subscribe(Long confId, Long userId, Date timeStamp) {
	return ConfessionManager.changeSubscribtionStatus(confId, userId, timeStamp);
    }

    @Override
    public boolean isSubscribed(Long confId, Long userId) {
	return ConfessionManager.isSubscribed(confId, userId);
    }

    @Override
    public int updateHumanPoints(Long userId, int points) {
	return UserManager.updateHumanPoints(userId, points);
    }

    @Override
    public int getHumanPoints(Long userId) {
	return UserManager.getHumanPoints(userId);
    }

    @Override
    public UserInfo isUserRegistered(String fbId) {
	return UserManager.isUserRegistered(fbId);
    }

    @Override
    public void createConfessedToUser(Long confId, Long userId, String fbId, ConfessionShare confessionShare, Date updateTimeStamp) {
	if(confessionShare != null && UserManager.validateUser(userId, fbId)) {
	    // Get the confession by user details
	    UserInfo confessionByUser = UserManager.getUserByUserId(userId);
	    ConfessionManager.addConfessedToUser(confId, confessionByUser, confessionShare);
	    ConfessionManager.updateConfessionTimeStamp(confId, updateTimeStamp);
	}
    }

    @Override
    public void registerConfessionDraft(Confession confession) {
	ConfessionManager.registerConfessionDraft(confession);
    }

    @Override
    public Confession getConfessionDraft(Long userId, String fbId) {
	return ConfessionManager.getConfessionDraft(userId, fbId);
    }

    @Override
    public void clearConfessionDraft(Long userId, String fbId) {
	ConfessionManager.clearConfessionDraft(userId, fbId);
    }
}