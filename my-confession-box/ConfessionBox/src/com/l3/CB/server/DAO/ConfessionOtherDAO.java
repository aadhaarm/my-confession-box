package com.l3.CB.server.DAO;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.ObjectifyService;
import com.l3.CB.server.DO.ConfessionDO;
import com.l3.CB.server.DO.ConfessionDraftDO;
import com.l3.CB.server.DO.ConfessionShareDO;
import com.l3.CB.server.DO.PardonConditionDO;
import com.l3.CB.server.DO.SubscribtionDO;
import com.l3.CB.server.DO.UserActivityDO;
import com.l3.CB.server.DO.UserDO;
import com.l3.CB.server.manager.UserManager;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionOtherDAO {

    static Logger logger = Logger.getLogger("CBLogger");

    static {
	ObjectifyService.register(ConfessionDO.class);
	ObjectifyService.register(ConfessionShareDO.class);
	ObjectifyService.register(UserDO.class);
	ObjectifyService.register(PardonConditionDO.class);

	ObjectifyService.register(UserActivityDO.class);
	ObjectifyService.register(SubscribtionDO.class);
	ObjectifyService.register(ConfessionDraftDO.class);
    }

    public static Map<String, Long> getUserActivity(Long userId, Long confId) {

	Map<String, Long> activityMap = new HashMap<String, Long>();

	com.googlecode.objectify.cmd.Query<UserActivityDO> queryOfy = ofy()
		.load().type(UserActivityDO.class).filter("userId", userId)
		.filter("confId", confId);

	for (UserActivityDO userActivityDO : queryOfy) {
	    activityMap.put(userActivityDO.getActivityType(), (long) 0);					
	}

	return activityMap;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(UserActivityDO.class);
	//	    query.setFilter("userId == user && confId == conf");
	//	    query.declareParameters("String user" + ", "  + "String conf");
	//	    @SuppressWarnings("unchecked")
	//	    List<UserActivityDO> result = (List<UserActivityDO>) query.execute(userId, confId);
	//
	//	    if (!result.isEmpty()) {
	//		activityMap = new HashMap<String, Long>();
	//		Iterator<UserActivityDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    UserActivityDO userActivityDO = it.next();
	//		    activityMap.put(userActivityDO.getActivityType(), (long) 0);					
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting user from DB:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static Long getActivityCount(Long confId, Activity activity) {
	long count = 0;

	ConfessionDO confessionDO = ofy().load().type(ConfessionDO.class).id(confId).get();

	if(confessionDO != null) {
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
	}

	return count;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(ConfessionDO.class);
	//	    query.setFilter("confId == id");
	//	    query.declareParameters("String id");
	//
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionDO> result = (List<ConfessionDO>) query.execute(confId);
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<ConfessionDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionDO confessionDO = it.next();
	//		    switch (activity) {
	//		    case ABUSE:
	//			count = confessionDO.getNumOfAbuseVote();
	//			break;
	//		    case LAME:
	//			count = confessionDO.getNumOfLameVote();
	//			break;
	//		    case SAME_BOAT:
	//			count = confessionDO.getNumOfSameBoatVote();
	//			break;
	//		    case SHOULD_BE_PARDONED:
	//			count = confessionDO.getNumOfShouldBePardonedVote();
	//			break;
	//		    case SHOULD_NOT_BE_PARDONED:
	//			count = confessionDO.getNumOfShouldNotBePardonedVote();
	//			break;
	//		    case SYMPATHY:
	//			count = confessionDO.getNumOfSympathyVote();
	//			break;
	//		    }
	//		    pm.makePersistent(confessionDO);
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting confession for DB:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static Long registerUserActivity(Long userId, Long confId, Activity activity, Date timeStamp) {
	long count = 0;
	UserActivityDO userActivityDO = new UserActivityDO(userId, confId, activity.name(), true, timeStamp);
	ofy().save().entities(userActivityDO).now();
	count = getActivityCount(confId, activity);
	return count;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    UserActivityDO userActivityDO = new UserActivityDO(userId, confId, activity.name(), true, timeStamp);
	//	    pm.makePersistent(userActivityDO);
	//	    count = getActivityCount(confId, activity);
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE, "Error while registering user activity:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
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

	ConfessionDO confessionDO = ofy().load().type(ConfessionDO.class).id(confId).get();
	if(confessionDO != null) {
	    confessionDO.setShareAsAnyn(shareAnyn);
	    ofy().save().entities(confessionDO).now();

	    Confession confession = new Confession(confId, shareAnyn); 
	    confession.setUserId(confessionDO.getUserId());
	    return confession;
	}
	return null;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	Confession confession = null;
	//	try {
	//	    Query query = pm.newQuery(ConfessionDO.class);
	//	    query.setFilter("userId == user && confId == conf");
	//	    query.declareParameters("String user" + ", "  + "String conf");
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionDO> result = (List<ConfessionDO>) query.execute(userId, confId);
	//
	//	    if (!result.isEmpty()) {
	//		Iterator<ConfessionDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionDO confessionDO = it.next();
	//		    confessionDO.setShareAsAnyn(shareAnyn);
	//		    pm.makePersistent(confessionDO);
	//		    confession = new Confession(confId, shareAnyn); 
	//		    confession.setUserId(confessionDO.getUserId());
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while Changing confession visibility:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static long updateActivityCountInConfession(Long confId, Activity activity) {

	long count = 0;

	ConfessionDO confessionDO = ofy().load().type(ConfessionDO.class).id(confId).get();

	if(confessionDO != null) {
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
	    ofy().save().entities(confessionDO).now();

	}

	return count;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	long count = 0;
	//	try {
	//	    Query query = pm.newQuery(ConfessionDO.class);
	//	    query.setFilter("confId == id");
	//	    query.declareParameters("String id");
	//
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionDO> result = (List<ConfessionDO>) query.execute(confId);
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<ConfessionDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionDO confessionDO = it.next();
	//		    switch (activity) {
	//		    case ABUSE:
	//			count = confessionDO.incrementAbuseVote();
	//			break;
	//		    case LAME:
	//			count = confessionDO.incrementLameVote();
	//			break;
	//		    case SAME_BOAT:
	//			count = confessionDO.incrementSameBoatVote();
	//			break;
	//		    case SHOULD_BE_PARDONED:
	//			count = confessionDO.incrementShouldBePardonedVote();
	//			break;
	//		    case SHOULD_NOT_BE_PARDONED:
	//			count = confessionDO.incrementShouldNotBePardonedVote();
	//			break;
	//		    case SYMPATHY:
	//			count = confessionDO.incrementSympathyVote();
	//			break;
	//		    }
	//		    pm.makePersistent(confessionDO);
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting confession for DB:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static List<Long> getUserActivity(Long userId, int page, int pageSize) {
	List<Long> confIds = null;

	com.googlecode.objectify.cmd.Query<UserActivityDO> queryOfy = ofy()
		.load().type(UserActivityDO.class)
		.filter("userId", userId)
		.offset(page * pageSize).limit(pageSize)
		.order("-timeStamp");	
	Map<Long, Long> confessionIDs = new HashMap<Long, Long>();

	for (UserActivityDO userActivityDO : queryOfy) {
	    confessionIDs.put(userActivityDO.getConfId(), userActivityDO.getConfId());
	}
	confIds = new ArrayList<Long>(confessionIDs.values());

	return confIds;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(UserActivityDO.class);
	//	    query.setFilter("userId == id");
	//	    query.declareParameters("String id");
	//	    query.setRange((page*pageSize), ((page*pageSize)+pageSize));
	//	    query.setOrdering("timeStamp desc");
	//	    query.setUnique(false);
	//
	//	    @SuppressWarnings("unchecked")
	//	    List<UserActivityDO> result = (List<UserActivityDO>) query.execute(userId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		Map<Long, Long> confessionIDs = new HashMap<Long, Long>();
	//		for (UserActivityDO userActivityDO : result) {
	//		    confessionIDs.put(userActivityDO.getConfId(), userActivityDO.getConfId());
	//		}
	//		confIds = new ArrayList<Long>(confessionIDs.values());
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting confession for DB:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static boolean isSubscribed(Long confId, Long userId, boolean changeSubscribtionStatus, Date timeStamp) {

	boolean isSubscribed = false;
	boolean subscriptionExisted = false;

	com.googlecode.objectify.cmd.Query<SubscribtionDO> queryOfy = ofy()
		.load().type(SubscribtionDO.class)
		.filter("userId", userId)
		.filter("confId", confId);

	for (SubscribtionDO subscribtionDO : queryOfy) {
	    subscriptionExisted = true;
	    if(subscribtionDO != null) {
		if(changeSubscribtionStatus) {
		    subscribtionDO.setSubscribed(!subscribtionDO.isSubscribed());
		    subscribtionDO.setTimeStamp(timeStamp);
		    ofy().save().entities(subscribtionDO).now();
		}
		isSubscribed = subscribtionDO.isSubscribed();
	    }
	}

	if(!subscriptionExisted && changeSubscribtionStatus) {
	    SubscribtionDO subscribtionDO = new SubscribtionDO(userId, confId, true);
	    subscribtionDO.setTimeStamp(timeStamp);
	    ofy().save().entities(subscribtionDO).now();
	}

	return isSubscribed;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	boolean isSubscribed = false;
	//	try {
	//	    Query query = pm.newQuery(SubscribtionDO.class);
	//	    query.setFilter("userId == user && confId == conf");
	//	    query.declareParameters("String user" + ", "  + "String conf");
	//	    @SuppressWarnings("unchecked")
	//	    List<SubscribtionDO> result = (List<SubscribtionDO>) query.execute(userId, confId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<SubscribtionDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    SubscribtionDO subscribtionDO = it.next();
	//		    if(subscribtionDO != null) {
	//			if(changeSubscribtionStatus) {
	//			    subscribtionDO.setSubscribed(!subscribtionDO.isSubscribed());
	//			    subscribtionDO.setTimeStamp(timeStamp);
	//			    pm.makePersistent(subscribtionDO);
	//			}
	//			isSubscribed = subscribtionDO.isSubscribed();
	//		    }
	//		}
	//	    } else if(changeSubscribtionStatus) {
	//		SubscribtionDO subscribtionDO = new SubscribtionDO(userId, confId, true);
	//		subscribtionDO.setTimeStamp(timeStamp);
	//		pm.makePersistent(subscribtionDO);
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while checking if user is subscribed:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static List<UserInfo> getUsersSubscribedToConf(Long confId) {

	List<UserInfo> subscribedUsers = new ArrayList<UserInfo>();

	com.googlecode.objectify.cmd.Query<SubscribtionDO> queryOfy = ofy()
		.load().type(SubscribtionDO.class).filter("confId", confId);

	for (SubscribtionDO subscribtionDO : queryOfy) {
	    if(subscribtionDO != null && subscribtionDO.isSubscribed()) {
		subscribedUsers.add(UserManager.getUserByUserId(subscribtionDO.getUserId()));
	    }
	}

	return subscribedUsers;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	List<UserInfo> subscribedUsers = null;
	//	try {
	//	    Query query = pm.newQuery(SubscribtionDO.class);
	//	    query.setFilter("confId == conf");
	//	    query.declareParameters("String conf");
	//	    @SuppressWarnings("unchecked")
	//	    List<SubscribtionDO> result = (List<SubscribtionDO>) query.execute(confId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		subscribedUsers = new ArrayList<UserInfo>();
	//		Iterator<SubscribtionDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    SubscribtionDO subscribtionDO = it.next();
	//		    if(subscribtionDO != null && subscribtionDO.isSubscribed()) {
	//			subscribedUsers.add(UserManager.getUserByUserId(subscribtionDO.getUserId()));
	//		    }
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while checking if user is subscribed:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static List<Long> getUserSubscribedConfessionIDs(Long userId, int pageSize, int page) {


	com.googlecode.objectify.cmd.Query<SubscribtionDO> queryOfy = ofy()
		.load().type(SubscribtionDO.class).filter("", userId)
		.offset(page * pageSize).limit(pageSize).order("-page*pageSize");

	Map<Long, Long> confessionIDs = new HashMap<Long, Long>();

	for (SubscribtionDO subscribtionDO : queryOfy) {
	    if(subscribtionDO != null && subscribtionDO.isSubscribed()) {
		confessionIDs.put(subscribtionDO.getConfId(), subscribtionDO.getConfId());
	    }
	}
	List<Long> subscribedConfessions = new ArrayList<Long>(confessionIDs.values());

	return subscribedConfessions;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	List<Long> subscribedConfessions = null;
	//	try {
	//	    Query query = pm.newQuery(SubscribtionDO.class);
	//
	//	    query.setFilter("userId == user");
	//	    query.declareParameters("String user");
	//
	//	    query.setRange((page*pageSize), ((page*pageSize)+pageSize));
	//	    query.setOrdering("timeStamp desc");
	//
	//	    @SuppressWarnings("unchecked")
	//	    List<SubscribtionDO> result = (List<SubscribtionDO>) query.execute(userId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		Map<Long, Long> confessionIDs = new HashMap<Long, Long>();
	//		Iterator<SubscribtionDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    SubscribtionDO subscribtionDO = it.next();
	//		    if(subscribtionDO != null && subscribtionDO.isSubscribed()) {
	//			confessionIDs.put(subscribtionDO.getConfId(), subscribtionDO.getConfId());
	//		    }
	//		}
	//		subscribedConfessions = new ArrayList<Long>(confessionIDs.values());
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while checking if user is subscribed:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static void saveConfessionDraft(Confession confession) {

	boolean savedDraftedExist = false;

	com.googlecode.objectify.cmd.Query<ConfessionDraftDO> queryOfy = ofy()
		.load().type(ConfessionDraftDO.class)
		.filter("userId", confession.getUserId());

	for (ConfessionDraftDO confessionDraftDO : queryOfy) {
	    savedDraftedExist = true;
	    confessionDraftDO.setConfessionTitle(confession.getConfessionTitle());
	    confessionDraftDO.setConfession(new Text(confession.getConfession()));
	    confessionDraftDO.setShareAsAnyn(confession.isShareAsAnyn());
	    confessionDraftDO.setShareToUserIDForSave(confession.getShareToUserIDForSave());
	    confessionDraftDO.setShareToRelationForSave(confession.getShareToRelationForSave());
	    ofy().save().entities(confessionDraftDO).now();
	}

	if(!savedDraftedExist) {
	    ConfessionDraftDO confDraftDO = new ConfessionDraftDO();
	    confDraftDO.setUserId(confession.getUserId());
	    confDraftDO.setConfessionTitle(confession.getConfessionTitle());
	    confDraftDO.setConfession(new Text(confession.getConfession()));
	    confDraftDO.setShareAsAnyn(confession.isShareAsAnyn());
	    confDraftDO.setShareToUserIDForSave(confession.getShareToUserIDForSave());
	    confDraftDO.setShareToRelationForSave(confession.getShareToRelationForSave());
	    ofy().save().entities(confDraftDO).now();
	}

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(ConfessionDraftDO.class);
	//	    query.setFilter("userId == user");
	//	    query.declareParameters("String user");
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionDraftDO> result = (List<ConfessionDraftDO>) query.execute(confession.getUserId());
	//	    
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<ConfessionDraftDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionDraftDO confessionDraftDO = it.next();
	//		    confessionDraftDO.setConfessionTitle(confession.getConfessionTitle());
	//		    confessionDraftDO.setConfession(new Text(confession.getConfession()));
	//		    confessionDraftDO.setShareAsAnyn(confession.isShareAsAnyn());
	//		    confessionDraftDO.setShareToUserIDForSave(confession.getShareToUserIDForSave());
	//		    confessionDraftDO.setShareToRelationForSave(confession.getShareToRelationForSave());
	//		    pm.makePersistent(confessionDraftDO);
	//		}
	//	    } else {
	//		ConfessionDraftDO confDraftDO = new ConfessionDraftDO();
	//		confDraftDO.setUserId(confession.getUserId());
	//		confDraftDO.setConfessionTitle(confession.getConfessionTitle());
	//		confDraftDO.setConfession(new Text(confession.getConfession()));
	//		confDraftDO.setShareAsAnyn(confession.isShareAsAnyn());
	//		confDraftDO.setShareToUserIDForSave(confession.getShareToUserIDForSave());
	//		confDraftDO.setShareToRelationForSave(confession.getShareToRelationForSave());
	//		pm.makePersistent(confDraftDO);
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while registering confession DRAFT:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static Confession getSavedConfessionDraft(Long userId) {

	Confession confessionDraft = null;

	com.googlecode.objectify.cmd.Query<ConfessionDraftDO> queryOfy = ofy()
		.load().type(ConfessionDraftDO.class)
		.filter("userId", userId);

	for (ConfessionDraftDO confessionDraftDO : queryOfy) {
	    confessionDraft = new Confession();
	    confessionDraft.setConfessionTitle(confessionDraftDO.getConfessionTitle());
	    confessionDraft.setConfession(confessionDraftDO.getConfession().getValue());
	    confessionDraft.setShareAsAnyn(confessionDraftDO.isShareAsAnyn());
	    confessionDraft.setShareToUserIDForSave(confessionDraftDO.getShareToUserIDForSave());
	    confessionDraft.setShareToRelationForSave(confessionDraftDO.getShareToRelationForSave());
	}

	return confessionDraft;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	Confession confessionDraft = null;
	//	try {
	//	    Query query = pm.newQuery(ConfessionDraftDO.class);
	//	    query.setFilter("userId == user");
	//	    query.declareParameters("String user");
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionDraftDO> result = (List<ConfessionDraftDO>) query.execute(userId);
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<ConfessionDraftDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionDraftDO confessionDraftDO = it.next();
	//		    confessionDraft = new Confession();
	//		    confessionDraft.setConfessionTitle(confessionDraftDO.getConfessionTitle());
	//		    confessionDraft.setConfession(confessionDraftDO.getConfession().getValue());
	//		    confessionDraft.setShareAsAnyn(confessionDraftDO.isShareAsAnyn());
	//		    confessionDraft.setShareToUserIDForSave(confessionDraftDO.getShareToUserIDForSave());
	//		    confessionDraft.setShareToRelationForSave(confessionDraftDO.getShareToRelationForSave());
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while geting confession DRAFT:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static void clearConfessionDraft(Long userId) {

	com.googlecode.objectify.cmd.Query<ConfessionDraftDO> queryOfy = ofy()
		.load().type(ConfessionDraftDO.class)
		.filter("userId", userId);

	for (ConfessionDraftDO confessionDraftDO : queryOfy) {
	    ofy().delete().entities(confessionDraftDO).now();
	}

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(ConfessionDraftDO.class);
	//	    query.setFilter("userId == user");
	//	    query.declareParameters("String user");
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionDraftDO> result = (List<ConfessionDraftDO>) query.execute(userId);
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<ConfessionDraftDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionDraftDO confessionDraftDO = it.next();
	//		    pm.deletePersistent(confessionDraftDO);
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while geting confession DRAFT:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }
}