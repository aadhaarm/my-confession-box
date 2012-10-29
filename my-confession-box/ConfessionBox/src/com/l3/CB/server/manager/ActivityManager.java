package com.l3.CB.server.manager;

import java.util.Date;
import java.util.Map;

import com.l3.CB.server.DAO.ConfessionOtherDAO;
import com.l3.CB.shared.TO.Activity;

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
			PardonManager.validateIfConditionsMet(confId);
		}
		return updatedActivityCount;
	}

	public static Map<String, Long> getUserActivity(Long userId, Long confId) {
		return ConfessionOtherDAO.getUserActivity(userId, confId);
	}
}
