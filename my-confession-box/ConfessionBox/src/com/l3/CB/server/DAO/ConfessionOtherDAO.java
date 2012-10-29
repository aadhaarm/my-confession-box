package com.l3.CB.server.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.l3.CB.server.DO.ConfessionDO;
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

	public static Long getActivityCount(Long confId, String activityType) {
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

	public static long updateActivityCount(Long userId, Long confId, Activity activity, Date timeStamp) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		long count = 0;
		try {
			UserActivityDO userActivityDO = new UserActivityDO(userId, confId, activity.name(), true, timeStamp);
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

	public static boolean changeVisibility(Long userId, Long confId, boolean shareAnyn) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(ConfessionDO.class);
			query.setFilter("userId == user && confId == conf");
			query.declareParameters("String user" + ", "  + "String conf");
			@SuppressWarnings("unchecked")
			List<ConfessionDO> result = (List<ConfessionDO>) query.execute(userId, confId);

			if (!result.isEmpty()) {
				Iterator<ConfessionDO> it = result.iterator();
				while (it.hasNext()) {
					ConfessionDO confessionDO = it.next();
					confessionDO.setShareAsAnyn(shareAnyn);
					pm.makePersistent(confessionDO);
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while Changing confession visibility:" + e.getMessage());
			return false;
		} finally {
			pm.close();
		}
		return true;
	}

	public static void updateActivityCountInConfession(Long confId, Activity activity) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(ConfessionDO.class);
			query.setFilter("confId == id");
			query.declareParameters("String id");

			@SuppressWarnings("unchecked")
			List<ConfessionDO> result = (List<ConfessionDO>) query.execute(confId);
			if (result != null && !result.isEmpty()) {
				Iterator<ConfessionDO> it = result.iterator();
				while (it.hasNext()) {
					ConfessionDO confessionDO = it.next();
					switch (activity) {
					case ABUSE:
						confessionDO.incrementAbuseVote();
						break;
					case LAME:
						confessionDO.incrementLameVote();
						break;
					case SAME_BOAT:
						confessionDO.incrementSameBoatVote();
						break;
					case SHOULD_BE_PARDONED:
						confessionDO.incrementShouldBePardonedVote();
						break;
					case SHOULD_NOT_BE_PARDONED:
						confessionDO.incrementShouldNotBePardonedVote();
						break;
					case SYMPATHY:
						confessionDO.incrementSympathyVote();
						break;
					}
					pm.makePersistent(confessionDO);
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while getting confession for DB:" + e.getMessage());
		} finally {
			pm.close();
		}
	}

	public static List<Long> getUserActivity(Long userId, int page, int pageSize) {
		List<Long> confIds = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(UserActivityDO.class);
			query.setFilter("userId == id");
			query.declareParameters("String id");
			query.setRange((page*pageSize), ((page*pageSize)+pageSize));
			query.setOrdering("timeStamp desc");

			@SuppressWarnings("unchecked")
			List<UserActivityDO> result = (List<UserActivityDO>) query.execute(userId);
			
			if (result != null && !result.isEmpty()) {
				confIds = new ArrayList<Long>();
				Iterator<UserActivityDO> it = result.iterator();
				for (UserActivityDO userActivityDO : result) {
					confIds.add(userActivityDO.getConfId());
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while getting confession for DB:" + e.getMessage());
		} finally {
			pm.close();
		}

		return confIds;
	}
}