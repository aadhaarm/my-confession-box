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

import com.google.appengine.api.datastore.Text;
import com.l3.CB.server.DO.ConfessionDO;
import com.l3.CB.server.DO.ConfessionDraftDO;
import com.l3.CB.server.DO.SubscribtionDO;
import com.l3.CB.server.DO.UserActivityDO;
import com.l3.CB.server.manager.UserManager;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

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

    public static Long getActivityCount(Long confId, Activity activity) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	long count = 0;
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
			count = confessionDO.getNumOfAbuseVote();
			break;
		    case LAME:
			count = confessionDO.getNumOfLameVote();
			break;
		    case SAME_BOAT:
			count = confessionDO.getNumOfSameBoatVote();
			break;
		    case SHOULD_BE_PARDONED:
			count = confessionDO.getNumOfShouldBePardonedVote();
			break;
		    case SHOULD_NOT_BE_PARDONED:
			count = confessionDO.getNumOfShouldNotBePardonedVote();
			break;
		    case SYMPATHY:
			count = confessionDO.getNumOfSympathyVote();
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
	return count;
    }

    public static Long registerUserActivity(Long userId, Long confId, Activity activity, Date timeStamp) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	long count = 0;
	try {
	    UserActivityDO userActivityDO = new UserActivityDO(userId, confId, activity.name(), true, timeStamp);
	    pm.makePersistent(userActivityDO);
	    count = getActivityCount(confId, activity);
	} catch (Exception e) {
	    logger.log(Level.SEVERE, "Error while registering user activity:" + e.getMessage());
	} finally {
	    pm.close();
	}

	return count;
    }

    public static Map<String, Long> getUserActivity(Long confId) {
	Map<String, Long> activityCount = new HashMap<String, Long>();
	activityCount.put(Activity.ABUSE.name(), getActivityCount(confId, Activity.ABUSE));		
	activityCount.put(Activity.LAME.name(), getActivityCount(confId, Activity.LAME));		
	activityCount.put(Activity.SAME_BOAT.name(), getActivityCount(confId, Activity.SAME_BOAT));		
	activityCount.put(Activity.SHOULD_BE_PARDONED.name(), getActivityCount(confId, Activity.SHOULD_BE_PARDONED));		
	activityCount.put(Activity.SHOULD_NOT_BE_PARDONED.name(), getActivityCount(confId, Activity.SHOULD_NOT_BE_PARDONED));		
	activityCount.put(Activity.SYMPATHY.name(), getActivityCount(confId, Activity.SYMPATHY));		
	return activityCount;
    }

    public static Confession changeVisibility(Long userId, Long confId, boolean shareAnyn) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	Confession confession = null;
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
		    confession = new Confession(confId, shareAnyn); 
		    confession.setUserId(confessionDO.getUserId());
		}
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while Changing confession visibility:" + e.getMessage());
	} finally {
	    pm.close();
	}
	return confession;
    }

    public static long updateActivityCountInConfession(Long confId, Activity activity) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	long count = 0;
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
			count = confessionDO.incrementAbuseVote();
			break;
		    case LAME:
			count = confessionDO.incrementLameVote();
			break;
		    case SAME_BOAT:
			count = confessionDO.incrementSameBoatVote();
			break;
		    case SHOULD_BE_PARDONED:
			count = confessionDO.incrementShouldBePardonedVote();
			break;
		    case SHOULD_NOT_BE_PARDONED:
			count = confessionDO.incrementShouldNotBePardonedVote();
			break;
		    case SYMPATHY:
			count = confessionDO.incrementSympathyVote();
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
	return count;
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

    public static boolean isSubscribed(Long confId, Long userId, boolean changeSubscribtionStatus, Date timeStamp) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	boolean isSubscribed = false;
	try {
	    Query query = pm.newQuery(SubscribtionDO.class);
	    query.setFilter("userId == user && confId == conf");
	    query.declareParameters("String user" + ", "  + "String conf");
	    @SuppressWarnings("unchecked")
	    List<SubscribtionDO> result = (List<SubscribtionDO>) query.execute(userId, confId);

	    if (result != null && !result.isEmpty()) {
		Iterator<SubscribtionDO> it = result.iterator();
		while (it.hasNext()) {
		    SubscribtionDO subscribtionDO = it.next();
		    if(subscribtionDO != null) {
			if(changeSubscribtionStatus) {
			    subscribtionDO.setSubscribed(!subscribtionDO.isSubscribed());
			    subscribtionDO.setTimeStamp(timeStamp);
			    pm.makePersistent(subscribtionDO);
			}
			isSubscribed = subscribtionDO.isSubscribed();
		    }
		}
	    } else if(changeSubscribtionStatus) {
		SubscribtionDO subscribtionDO = new SubscribtionDO(userId, confId, true);
		subscribtionDO.setTimeStamp(timeStamp);
		pm.makePersistent(subscribtionDO);
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while checking if user is subscribed:" + e.getMessage());
	} finally {
	    pm.close();
	}
	return isSubscribed;
    }

    public static List<UserInfo> getUsersSubscribedToConf(Long confId) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	List<UserInfo> subscribedUsers = null;
	try {
	    Query query = pm.newQuery(SubscribtionDO.class);
	    query.setFilter("confId == conf");
	    query.declareParameters("String conf");
	    @SuppressWarnings("unchecked")
	    List<SubscribtionDO> result = (List<SubscribtionDO>) query.execute(confId);

	    if (result != null && !result.isEmpty()) {
		subscribedUsers = new ArrayList<UserInfo>();
		Iterator<SubscribtionDO> it = result.iterator();
		while (it.hasNext()) {
		    SubscribtionDO subscribtionDO = it.next();
		    if(subscribtionDO != null && subscribtionDO.isSubscribed()) {
			subscribedUsers.add(UserManager.getUserByUserId(subscribtionDO.getUserId()));
		    }
		}
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while checking if user is subscribed:" + e.getMessage());
	} finally {
	    pm.close();
	}
	return subscribedUsers;
    }

    public static List<Long> getUserSubscribedConfessionIDs(Long userId, int pageSize, int page) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	List<Long> subscribedConfessions = null;
	try {
	    Query query = pm.newQuery(SubscribtionDO.class);

	    query.setFilter("userId == user");
	    query.declareParameters("String user");
	    
	    query.setRange((page*pageSize), ((page*pageSize)+pageSize));
	    query.setOrdering("timeStamp desc");
	    
	    @SuppressWarnings("unchecked")
	    List<SubscribtionDO> result = (List<SubscribtionDO>) query.execute(userId);

	    if (result != null && !result.isEmpty()) {
		subscribedConfessions = new ArrayList<Long>();
		Iterator<SubscribtionDO> it = result.iterator();
		while (it.hasNext()) {
		    SubscribtionDO subscribtionDO = it.next();
		    if(subscribtionDO != null && subscribtionDO.isSubscribed()) {
			subscribedConfessions.add(subscribtionDO.getConfId());
		    }
		}
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while checking if user is subscribed:" + e.getMessage());
	} finally {
	    pm.close();
	}
	return subscribedConfessions;

    }
    
    public static void saveConfessionDraft(Confession confession) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    Query query = pm.newQuery(ConfessionDraftDO.class);
	    query.setFilter("userId == user");
	    query.declareParameters("String user");
	    @SuppressWarnings("unchecked")
	    List<ConfessionDraftDO> result = (List<ConfessionDraftDO>) query.execute(confession.getUserId());
	    if (result != null && !result.isEmpty()) {
		Iterator<ConfessionDraftDO> it = result.iterator();
		while (it.hasNext()) {
		    ConfessionDraftDO confessionDraftDO = it.next();
		    confessionDraftDO.setConfessionTitle(confession.getConfessionTitle());
		    confessionDraftDO.setConfession(new Text(confession.getConfession()));
		    pm.makePersistent(confessionDraftDO);
		}
	    } else {
		ConfessionDraftDO confDraftDO = new ConfessionDraftDO();
		confDraftDO.setUserId(confession.getUserId());
		confDraftDO.setConfessionTitle(confession.getConfessionTitle());
		confDraftDO.setConfession(new Text(confession.getConfession()));
		pm.makePersistent(confDraftDO);
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while registering confession DRAFT:" + e.getMessage());
	} finally {
	    pm.close();
	}
    }

    public static Confession getSavedConfessionDraft(Long userId) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	Confession confessionDraft = null;
	try {
	    Query query = pm.newQuery(ConfessionDraftDO.class);
	    query.setFilter("userId == user");
	    query.declareParameters("String user");
	    @SuppressWarnings("unchecked")
	    List<ConfessionDraftDO> result = (List<ConfessionDraftDO>) query.execute(userId);
	    if (result != null && !result.isEmpty()) {
		Iterator<ConfessionDraftDO> it = result.iterator();
		while (it.hasNext()) {
		    ConfessionDraftDO confessionDraftDO = it.next();
		    confessionDraft = new Confession();
		    confessionDraft.setConfessionTitle(confessionDraftDO.getConfessionTitle());
		    confessionDraft.setConfession(confessionDraftDO.getConfession().getValue());
		}
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while geting confession DRAFT:" + e.getMessage());
	} finally {
	    pm.close();
	}
	return confessionDraft;
    }

    public static void clearConfessionDraft(Long userId) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    Query query = pm.newQuery(ConfessionDraftDO.class);
	    query.setFilter("userId == user");
	    query.declareParameters("String user");
	    @SuppressWarnings("unchecked")
	    List<ConfessionDraftDO> result = (List<ConfessionDraftDO>) query.execute(userId);
	    if (result != null && !result.isEmpty()) {
		Iterator<ConfessionDraftDO> it = result.iterator();
		while (it.hasNext()) {
		    ConfessionDraftDO confessionDraftDO = it.next();
		    pm.deletePersistent(confessionDraftDO);
		}
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while geting confession DRAFT:" + e.getMessage());
	} finally {
	    pm.close();
	}
    }
}
