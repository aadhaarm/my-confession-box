package com.l3.CB.server.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.l3.CB.server.DAO.ConfessionBasicDAO;
import com.l3.CB.server.DAO.ConfessionOtherDAO;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.ConfessionShare;

public class ActivityManager {

    /**
     * @param userId
     * @param confId
     * @param activity
     * @param updateTimeStamp 
     * @return
     */
    public static Long registerUserActivity(Long userId, Long confId, Activity activity, Date updateTimeStamp) {

	Long updatedActivityCount = ConfessionOtherDAO.updateActivityCountInConfession(confId, activity);
	ConfessionOtherDAO.registerUserActivity(userId, confId, activity, new Date());

	if(activity.equals(Activity.SHOULD_BE_PARDONED)) {
	    List<ConfessionShare> confessionShares = ConfessionBasicDAO.getConfessionShare(confId, false);
	    if(confessionShares != null && !confessionShares.isEmpty())
	    for (ConfessionShare confessionShare : confessionShares) {
		if(confessionShare != null && confessionShare.getPardonStatus() != null) {
		    switch (confessionShare.getPardonStatus()) {
		    case PARDONED_WITH_CONDITION:
			PardonManager.validateIfAllConditionsMet(confId, updateTimeStamp, null, updatedActivityCount);
			break;
		    }
		}
	    }
	}
	
	// Flush cache
	CacheManager.flushActivityCache(userId, confId);
	
	return updatedActivityCount;
    }

    public static Map<String, Long> getUserActivity(Long userId, Long confId) {
	Map<String, Long> activityMap = CacheManager.getActivityMap(userId, confId);
	if(activityMap == null) {
	    activityMap = ConfessionOtherDAO.getUserActivity(userId, confId);
	    CacheManager.cacheActivityMap(userId, confId, activityMap);
	}
	return activityMap;
    }
}