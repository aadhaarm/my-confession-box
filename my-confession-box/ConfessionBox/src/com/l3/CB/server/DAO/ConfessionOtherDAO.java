package com.l3.CB.server.DAO;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.l3.CB.server.DO.UserActivityDO;
import com.l3.CB.shared.TO.Activity;

public class ConfessionOtherDAO {

	static Logger logger = Logger.getLogger("CBLogger");

	public static Map<String, Long> getUserActivity(Long userId, Long confId) {
		Map<String, Long> activityMap = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(UserActivityDO.class);
			query.setFilter("userId == user && confId == conf");
			query.declareParameters("String user" + ", "  + "String conf");
			@SuppressWarnings("unchecked")
			List<UserActivityDO> result = (List<UserActivityDO>) query.execute(userId, confId);

			if (!result.isEmpty()) {
				activityMap = new HashMap<String, Long>();
				Iterator<UserActivityDO> it = result.iterator();
				while (it.hasNext()) {
					UserActivityDO userActivityDO = it.next();
					activityMap.put(userActivityDO.getActivityType(), (long) 0);					
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while getting user from DB:" + e.getMessage());
		} finally {
			pm.close();
		}
		return activityMap;
	}
	
	private static Long getActivityCount(Long confId, String activityType) {
		Long activityCount = new Long(0);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(UserActivityDO.class);
			query.setFilter("confId == conf  && activityType == type");
			query.declareParameters("String conf" + ", "  + "String type");
			List result = (List) query.execute(confId, activityType);
			if (!result.isEmpty()) {
				activityCount = (long) result.size();
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while getting user from DB:" + e.getMessage());
		} finally {
			pm.close();
		}
		return activityCount;
	}

	public static long updateActivityCount(Long userId, Long confId, Activity activity) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		long count = 0;
		try {
			UserActivityDO userActivityDO = new UserActivityDO(userId, confId, activity.name(), true);
			pm.makePersistent(userActivityDO);
			count = getActivityCount(confId, activity.name());
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while registering confession:" + e.getMessage());
		} finally {
			pm.close();
		}
		
		return count;
	}

	public static Map<String, Long> getUserActivity(Long confId) {
		Map<String, Long> activityCount = new HashMap<String, Long>();
		activityCount.put(Activity.ABUSE.name(), getActivityCount(confId, Activity.ABUSE.name()));		
		activityCount.put(Activity.LAME.name(), getActivityCount(confId, Activity.LAME.name()));		
		activityCount.put(Activity.SAME_BOAT.name(), getActivityCount(confId, Activity.SAME_BOAT.name()));		
		activityCount.put(Activity.SHOULD_BE_PARDONED.name(), getActivityCount(confId, Activity.SHOULD_BE_PARDONED.name()));		
		activityCount.put(Activity.SHOULD_NOT_BE_PARDONED.name(), getActivityCount(confId, Activity.SHOULD_NOT_BE_PARDONED.name()));		
		activityCount.put(Activity.SYMPATHY.name(), getActivityCount(confId, Activity.SYMPATHY.name()));		
		return activityCount;
	}
}
