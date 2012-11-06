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
     * @return
     */
    public static Long registerUserActivity(Long userId, Long confId, Activity activity) {

	long updatedActivityCount = ConfessionOtherDAO.updateActivityCount(userId, confId, activity, new Date());
	ConfessionOtherDAO.updateActivityCountInConfession(confId, activity);

	if(activity.equals(Activity.SHOULD_BE_PARDONED)) {
	    List<ConfessionShare> confessionShares = ConfessionBasicDAO.getConfessionShare(confId, false);
	    for (ConfessionShare confessionShare : confessionShares) {
		if(confessionShare != null && confessionShare.getPardonStatus() != null) {
		    switch (confessionShare.getPardonStatus()) {
		    case PARDONED_WITH_CONDITION:
			PardonManager.validateIfConditionsMet(confId);
			break;
		    }
		}
	    }
	}
	return updatedActivityCount;
    }

    public static Map<String, Long> getUserActivity(Long userId, Long confId) {
	return ConfessionOtherDAO.getUserActivity(userId, confId);
    }
}