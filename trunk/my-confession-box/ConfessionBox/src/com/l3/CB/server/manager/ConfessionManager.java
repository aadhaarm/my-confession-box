package com.l3.CB.server.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.l3.CB.server.DAO.ConfessionBasicDAO;
import com.l3.CB.server.DAO.ConfessionOtherDAO;
import com.l3.CB.server.utils.ServerUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.Filters;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionManager {

    private static void setUserActivity(Long loggedInUserId, List<Confession> confessions) {
	if(confessions != null && !confessions.isEmpty()) {
	    for (Confession confession : confessions) {
		setUserActivity(loggedInUserId, confession);
	    }
	}
    }

    private static void setUserActivity(Long loggedInUserId, Confession confession) {
	if(confession != null) {
	    confession.setActivityCount(ActivityManager.getUserActivity(loggedInUserId, confession.getConfId()));
	}
    }


    /**
     * @param confession
     * @return
     */
    public static Confession registerConfession(Confession confession, String ipAddress) {
	if(confession.getConfId() == null) {

	    confession.setUserIp(ipAddress);

	    // PERSIST CONFESSION
	    confession = ConfessionBasicDAO.registerConfession(confession);

	    //Register confession Shared
	    if(confession.getConfessedTo() != null) {
		// Get the confession by user details
		UserInfo confessionByUser = UserManager.getUserByUserId(confession.getUserId());

		for (ConfessionShare confessionShare : confession.getConfessedTo()) {
		    addConfessedToUser(confession.getConfId(), confessionByUser, confessionShare);
		}
	    }
	} else {
	    /*
	     * Update flow
	     */
	    // Set updated IP address
	    confession.setUserIp(ipAddress);

	    // PERSIST CONFESSION
	    confession = ConfessionBasicDAO.updateConfession(confession);
	}
	return confession;
    }

    /**
     * Add CONFESSED TO user to a confession
     * 
     * @param confession
     * @param confessionByUser
     * @param confessionShare
     */
    public static void addConfessedToUser(Long confId, UserInfo confessionByUser, ConfessionShare confessionShare) {
	// Get User Share to details, register if not already registered
	UserInfo userSharedTo = new UserInfo();
	userSharedTo.setId(confessionShare.getFbId());
	userSharedTo.setName(confessionShare.getUserFullName());
	// REGISTER USER if not registered
	userSharedTo = UserManager.registerUser(userSharedTo);

	// REGISTER confession share
	confessionShare.setUserId(userSharedTo.getUserId());
	ConfessionBasicDAO.registerConfessionShare(confessionShare, confId);

	if(userSharedTo.getEmail() != null && "".equals(userSharedTo.getEmail())) {
	    MailManager.sendConfessionEmail(userSharedTo, confessionByUser, confId);
	}
    }

    /**
     * @param page
     * @param filter 
     * @param locale 
     * @param userId 
     * @return
     */
    public static List<Confession> getConfessionsByFilter(int page, Filters filter, String locale, Long userId) {
	List<Confession> confessions = null;

	if(confessions == null) {
	    if(filter.equals(Filters.USER_ACTIVITY)) {
		List<Long> confIDs = ConfessionOtherDAO.getUserActivity(userId, page, Constants.FEED_PAGE_SIZE);
		if(confIDs != null) {
		    confessions = getConfessions(confIDs, page, Constants.FEED_PAGE_SIZE, filter, locale, userId);
		    getUserDetails(confessions, false);
		} 
	    } else if(filter.equals(Filters.SUBSCRIBED)) {
		List<Long> confIDs = ConfessionOtherDAO.getUserSubscribedConfessionIDs(userId, Constants.FEED_PAGE_SIZE, page);
		if(confIDs != null && !confIDs.isEmpty()) {
		    confessions = getConfessions(confIDs, page, Constants.FEED_PAGE_SIZE, filter, locale, userId);
		    getUserDetails(confessions, false);
		} 
	    } else {
		String cacheKey = Integer.toString(page) + filter.name() + locale;
		confessions = CacheManager.getCachedConfessionList(cacheKey);
		if(confessions == null) {
		    confessions = ConfessionBasicDAO.getConfessions(page, Constants.FEED_PAGE_SIZE, filter, locale);
		    getUserDetails(confessions, false);
		    CacheManager.cacheConfessionList(cacheKey, confessions);
		}
	    }
	    setUserActivity(userId, confessions);
	}
	return confessions;
    }

    public static List<Confession> getConfessions(List<Long> confIDs, int page,	int feedPageSize, Filters filter, String locale, Long userId) {
	List<Confession> confessions = new ArrayList<Confession>();
	for (Long confId : confIDs) {
	    Confession confession = CacheManager.getCachedConfession(confId);
	    if(confession == null) {
		confessions.add(ConfessionManager.getOneConfession(confId, false, userId));
		CacheManager.cacheConfession(confession);
	    }
	}
	return confessions;
    }

    /**
     * @param confessions
     * @param getSharedToAllDetails 
     */
    private static void getUserDetails(List<Confession> confessions, boolean getSharedToAllDetails) {
	if(confessions != null && !confessions.isEmpty()) {
	    for (Confession confession : confessions) {
		if(confession != null) {
		    if(!confession.isShareAsAnyn()) {
			String userDetailsJSON = ServerUtils.fetchURL(FacebookUtil.getUserByIdUrl(confession.getFbId()));
			confession.setUserDetailsJSON(userDetailsJSON);
		    } else {
			// MOST IMPORTANT CODE!!
			confession.setFbId(null);
		    }
		    confession.setConfessedTo(ConfessionBasicDAO.getConfessionShare(confession.getConfId(), getSharedToAllDetails));
		}
	    }
	}
    }

    /**
     * @param page
     * @param userId
     * @param getSharedToAllDetails 
     * @return
     */
    public static List<Confession> getConfessionsByUser(int page, Long userId, boolean getSharedToAllDetails) {
	List<Confession> confessions = ConfessionBasicDAO.getConfessions(userId, page, Constants.FEED_PAGE_SIZE);
	if(confessions != null && !confessions.isEmpty())
	getUserDetails(confessions, getSharedToAllDetails);
	setUserActivity(userId, confessions);
	return confessions;
    }

    /**
     * @param confId
     * @param userId 
     * @return
     */
    public static Confession getOneConfession(Long confId, boolean secure, Long userId) {
	Confession confession = ConfessionBasicDAO.getConfession(confId);
	if(confession != null) {
	    confession.setConfessedTo(ConfessionBasicDAO.getConfessionShare(confession.getConfId(), false));
	    if(!confession.isShareAsAnyn() || secure) {
		String userDetailsJSON = ServerUtils.fetchURL(FacebookUtil.getUserByIdUrl(confession.getFbId()));
		confession.setUserDetailsJSON(userDetailsJSON);
	    } else {
		// MOST IMPORTANT CODE!!
		confession.setFbId(null);
	    }
	}
	if(userId != null) {
	    setUserActivity(userId, confession);
	}
	return confession;
    }

    /**
     * @param page
     * @param userId
     * @return
     */
    public static List<Confession> getConfessionsConfessedToMe(int page, Long userId) {
	List<Confession> confessions = ConfessionBasicDAO.getConfessionsForMe(userId, page, Constants.FEED_PAGE_SIZE);
	if(confessions != null && !confessions.isEmpty()) {
	    for (Confession confession : confessions) {
		if(confession != null) {
		    String userDetailsJSON = ServerUtils.fetchURL(FacebookUtil.getUserByIdUrl(confession.getFbId()));
		    confession.setUserDetailsJSON(userDetailsJSON);
		    confession.setConfessedTo(ConfessionBasicDAO.getConfessionShare(confession.getConfId(), true));
		}
	    }
	    setUserActivity(userId, confessions);
	}
	return confessions;
    }

    public static void changeIdentityVisibility(Long userId, String fbId, Long confId, boolean shareAnyn, Date updateTimeStamp) {
	if(UserManager.validateUser(userId, fbId)) {
	    Confession confession = ConfessionOtherDAO.changeVisibility(userId, confId, shareAnyn);
	    PardonManager.validateIfAllConditionsMet(confId, updateTimeStamp, confession, null);
	    CacheManager.flushConfession(confId);
	}
    }

    public static boolean changeConfessionVisibility(Long userId, String fbId, Long confId, boolean isVisible, Date updateTimeStamp) {
	if(UserManager.validateUser(userId, fbId)) {
	    boolean returnVal = ConfessionBasicDAO.updateConfessionVisibility(confId, userId, isVisible);
	    CacheManager.flushConfession(confId);
	    updateConfessionTimeStamp(confId, updateTimeStamp);
	    return returnVal;
	}
	return false;
    }

    public static long getConfessionsByUserCount(Long userId) {
	return ConfessionBasicDAO.getConfessionCount(userId);
    }

    public static long getConfessionsForMeCount(Long userId) {
	return ConfessionBasicDAO.getConfessionsForMeCount(userId);
    }

    public static boolean changeSubscribtionStatus(Long confId, Long userId, Date timeStamp) {
	return ConfessionOtherDAO.isSubscribed(confId, userId, true, timeStamp);
    }

    public static boolean isSubscribed(Long confId, Long userId) {
	return ConfessionOtherDAO.isSubscribed(confId, userId, false, null);
    }

    public static List<UserInfo> getUserSubscribed(Long confId) {
	return ConfessionOtherDAO.getUsersSubscribedToConf(confId);
    }

    public static void registerConfessionDraft(Confession confession) {
	ConfessionOtherDAO.saveConfessionDraft(confession);
    }

    public static Confession getConfessionDraft(Long userId, String fbId) {
	return ConfessionOtherDAO.getSavedConfessionDraft(userId);
    }

    public static void clearConfessionDraft(Long userId, String fbId) {
	ConfessionOtherDAO.clearConfessionDraft(userId);
    }

    public static void updateConfessionTimeStamp(Long confId, Date updateTimeStamp) {
	ConfessionBasicDAO.updateConfessionTimeStamp(confId, updateTimeStamp);
    }
}