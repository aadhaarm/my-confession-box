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
		    confessions = getConfessions(confIDs, page, Constants.FEED_PAGE_SIZE, filter, locale);
		    getUserDetails(confessions);
		} 
	    } else if(filter.equals(Filters.SUBSCRIBED)) {
		List<Long> confIDs = ConfessionOtherDAO.getUserSubscribedConfessionIDs(userId, Constants.FEED_PAGE_SIZE, page);
		if(confIDs != null && !confIDs.isEmpty()) {
		    confessions = getConfessions(confIDs, page, Constants.FEED_PAGE_SIZE, filter, locale);
		    getUserDetails(confessions);
		} 
	    } else {
		String cacheKey = Integer.toString(page) + filter.name() + locale;
		confessions = CacheManager.getCachedConfessionList(cacheKey);
		if(confessions == null) {
		    confessions = ConfessionBasicDAO.getConfessions(page, Constants.FEED_PAGE_SIZE, filter, locale);
		    getUserDetails(confessions);
		    CacheManager.cacheConfessionList(cacheKey, confessions);
		}
	    }

	}
	return confessions;
    }

    public static List<Confession> getConfessions(List<Long> confIDs, int page,	int feedPageSize, Filters filter, String locale) {
	List<Confession> confesions = new ArrayList<Confession>();
	for (Long confId : confIDs) {
	    confesions.add(ConfessionManager.getOneConfession(confId, false));
	}
	return confesions;
    }

    /**
     * @param confessions
     */
    private static void getUserDetails(List<Confession> confessions) {
	if(confessions != null && !confessions.isEmpty()) {
	    for (Confession confession : confessions) {
		if(!confession.isShareAsAnyn()) {
		    String userDetailsJSON = ServerUtils.fetchURL(FacebookUtil.getUserByIdUrl(confession.getFbId()));
		    confession.setUserDetailsJSON(userDetailsJSON);
		} else {
		    // MOST IMPORTANT CODE!!
		    confession.setFbId(null);
		}

		confession.setConfessedTo(ConfessionBasicDAO.getConfessionShare(confession.getConfId(), false));
	    }
	}
    }

    /**
     * @param page
     * @param userId
     * @return
     */
    public static List<Confession> getConfessionsByUser(int page, Long userId) {
	List<Confession> confessions = ConfessionBasicDAO.getConfessions(userId, page, Constants.FEED_PAGE_SIZE);
	getUserDetails(confessions);
	return confessions;
    }

    /**
     * @param confId
     * @return
     */
    public static Confession getOneConfession(Long confId, boolean secure) {
	String cacheKey = confId.toString() + secure;
	Confession confession = CacheManager.getCachedConfession(cacheKey);
	if(confession == null) {
	    confession = ConfessionBasicDAO.getConfession(confId);
	    if(confession != null) {
		confession.setConfessedTo(ConfessionBasicDAO.getConfessionShare(confession.getConfId(), false));
		if(!confession.isShareAsAnyn() || secure) {
		    String userDetailsJSON = ServerUtils.fetchURL(FacebookUtil.getUserByIdUrl(confession.getFbId()));
		    confession.setUserDetailsJSON(userDetailsJSON);
		} else {
		    // MOST IMPORTANT CODE!!
		    confession.setFbId(null);
		}
		CacheManager.cacheConfession(cacheKey, confession);
	    }
	}
	return confession;
    }

    /**
     * @param page
     * @param userId
     * @return
     */
    public static List<Confession> getConfessionsConfessedToMe(int page, Long userId) {
	//TODO: Validate if user is correct
	List<Confession> confessions = ConfessionBasicDAO.getConfessionsForMe(userId, page, Constants.FEED_PAGE_SIZE);
	if(confessions != null && !confessions.isEmpty()) {
	    for (Confession confession : confessions) {
		String userDetailsJSON = ServerUtils.fetchURL(FacebookUtil.getUserByIdUrl(confession.getFbId()));
		confession.setUserDetailsJSON(userDetailsJSON);
		confession.setConfessedTo(ConfessionBasicDAO.getConfessionShare(confession.getConfId(), true));
	    }
	}
	return confessions;
    }

    public static boolean changeIdentityVisibility(Long userId, String fbId, Long confId, boolean shareAnyn) {
	if(UserManager.validateUser(userId, fbId)) {
	    boolean result = ConfessionOtherDAO.changeVisibility(userId, confId, shareAnyn);
	    if(result) {
		PardonManager.validateIfConditionsMet(confId);
	    }
	    return result; 
	}
	return false;
    }

    public static boolean changeConfessionVisibility(Long userId, String fbId, Long confId, boolean isVisible) {
	if(UserManager.validateUser(userId, fbId)) {
	    return ConfessionBasicDAO.updateConfessionVisibility(confId, userId, isVisible);
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
	// VALIDATE USER
	return ConfessionOtherDAO.getSavedConfessionDraft(userId);
    }
}