package com.l3.CB.server.DAO;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.logging.Logger;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.l3.CB.server.DO.UserDO;
import com.l3.CB.shared.TO.UserInfo;

public class UserDAO {

    static Logger logger = Logger.getLogger("CBLogger");

    static {
	ObjectifyService.register(UserDO.class);
    }

    /**
     * Get User if registered. Update info if old info is not complete
     * @param userInfo
     * @return {@link UserDO}
     */
    public static UserDO getUserByUserId(UserInfo userInfo) {
	return ofy().load().type(UserDO.class).id(userInfo.getUserId()).get();

	//	if(userInfo != null) {
	//	    PersistenceManager pm = PMF.get().getPersistenceManager();
	//	    try {
	//		Query query = pm.newQuery(UserDO.class);
	//		query.setFilter("userId == id");
	//		query.declareParameters("String id");
	//		@SuppressWarnings("unchecked")
	//		List<UserDO> result = (List<UserDO>) query.execute(userInfo.getUserId());
	//
	//		if (result != null &&  !result.isEmpty()) {
	//		    Iterator<UserDO> it = result.iterator();
	//		    while (it.hasNext()) {
	//			userDO = it.next();
	//			// We assume if gender has come, rest of the details also must have come with user info object
	//			if(userDO != null && userInfo.getGender() != null) {
	//			    logger.log(Level.INFO, "Updating info, User id:" + userDO.getUserId());
	//			    userDO.setGender(userInfo.getGender());
	//			    userDO.setName(userInfo.getName());
	//			    userDO.setUserName(userInfo.getUsername());
	//			    userDO.setEmail(userInfo.getEmail());
	//			    pm.makePersistent(userDO);
	//			}
	//		    }
	//		}
	//	    } catch (Exception e) {
	//		logger.log(Level.SEVERE,
	//			"Error while getting user from DB or updating Info:" + e.getMessage());
	//	    } finally {
	//		pm.close();
	//	    }
	//	}
    }

    /**
     * Register user and persist its user details
     * @param userInfo
     * @return {@link UserInfo}
     */
    public static UserInfo registerUser(UserInfo userInfo) {
	if(userInfo != null) {
	    //	    long userId = 0;
	    //Get user details if already exist, update details if we have latest of them
	    UserDO userDO = getUserByFBId(userInfo);

	    if (userDO != null) {
		userInfo.setUserId(userDO.getUserId());
	    } else {
		// Register a new user
		userDO = getUserDO(userInfo);
		ofy().save().entities(userDO).now();
		userInfo.setUserId(userDO.getUserId());
		//		PersistenceManager pm = PMF.get().getPersistenceManager();
		//		try {
		//		    userDO = getUserDO(userInfo);
		//		    pm.makePersistent(userDO);
		//		    userId = userDO.getUserId();
		//		    userInfo.setUserId(userId);
		//		} catch (Exception e) {
		//		    logger.log(Level.SEVERE, "Error while registering user:" + e.getMessage());
		//		} finally {
		//		    pm.close();
		//		}
	    }
	}
	return userInfo;
    }

    /**
     * Get User by FBID
     * @param userInfo
     * @return
     */
    public static UserDO getUserByFBId(UserInfo userInfo) {
	UserDO userDO = ofy().load().type(UserDO.class).filter("fbId", userInfo.getId()).first().get();

	if(userDO != null && (userDO.getGender() == null || "".equals(userDO.getGender())) && userInfo.getGender() != null) {
	    userDO.setGender(userInfo.getGender());
	    userDO.setName(userInfo.getName());
	    userDO.setUserName(userInfo.getUsername());
	    userDO.setEmail(userInfo.getEmail());
	    ofy().save().entities(userDO).now();
	}

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(UserDO.class);
	//	    query.setFilter("fbId == id");
	//	    query.declareParameters("String id");
	//	    @SuppressWarnings("unchecked")
	//	    List<UserDO> result = (List<UserDO>) query.execute(userInfo.getId());
	//
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<UserDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    userDO = it.next();
	//		}
	//		if(userDO != null && (userDO.getGender() == null || "".equals(userDO.getGender())) && userInfo.getGender() != null) {
	//		    userDO.setGender(userInfo.getGender());
	//		    userDO.setName(userInfo.getName());
	//		    userDO.setUserName(userInfo.getUsername());
	//		    userDO.setEmail(userInfo.getEmail());
	//		    pm.makePersistent(userDO);
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting user from DB:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
	return userDO;
    }

    /**
     * 
     * @param userInfo
     * @return
     */
    public static boolean validateUser(Long userId, String fbId) {
	boolean isValid = false;
	if(userId != null && userId != 0) {

	    int count = ofy().load().type(UserDO.class).filter("userId", userId).filter("fbId", fbId).count();
	    if(count > 0) {
		isValid = true;
	    }
	}
	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(UserDO.class);
	//	    query.setFilter("userId == user && fbId == id");
	//	    query.declareParameters("String user" + ", "  + "String id");
	//	    @SuppressWarnings("unchecked")
	//	    List<UserDO> result = (List<UserDO>) query.execute(userId, fbId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		isValid = true;
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE, "Error while validating user from DB:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
	return isValid;
    }


    /**
     * Convert user TO to DO
     * @param userInfo
     * @return {@link UserDO}
     */
    private static UserDO getUserDO(UserInfo userInfo) {
	UserDO userDO = null;
	if (userInfo != null) {
	    userDO = new UserDO(userInfo.getId(), userInfo.getGender());
	    userDO.setName(userInfo.getName());
	    userDO.setUserName(userInfo.getUsername());
	    userDO.setEmail(userInfo.getEmail());
	}
	return userDO;
    }

    public static Long getUserId(String fbId) {
	long userId = 0;
	UserDO userDO = ofy().load().type(UserDO.class).filter("fbId", fbId).first().get();
	if(userDO != null) {
	    userId = userDO.getUserId();
	}
	return userId;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(UserDO.class);
	//	    query.setFilter("fbId == id");
	//	    query.declareParameters("String id");
	//	    @SuppressWarnings("unchecked")
	//	    List<UserDO> result = (List<UserDO>) query.execute(fbId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<UserDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    UserDO userDO = it.next();
	//		    userId = userDO.getUserId();
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while registering user:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static int updateHumanPoints(Long userId, int points, boolean updatePoints) {
	int updatedHumanPoints = 0;
	if(userId != null && userId != 0) {

	    UserDO userDO = ofy().load().type(UserDO.class).id(userId).get();
	    if(userDO != null) {
		if(updatePoints) {
		    userDO.setHumanPoints(points + userDO.getHumanPoints());
		    ofy().save().entities(userDO).now();
		}
		updatedHumanPoints = userDO.getHumanPoints();
	    }
	}
	return updatedHumanPoints;

	//	if(userId != null && userId != 0) {
	//	    int updatedHumanPoints = 0;
	//	    PersistenceManager pm = PMF.get().getPersistenceManager();
	//	    try {
	//		Query query = pm.newQuery(UserDO.class);
	//		query.setFilter("userId == id");
	//		query.declareParameters("String id");
	//		@SuppressWarnings("unchecked")
	//		List<UserDO> result = (List<UserDO>) query.execute(userId);
	//
	//		if (result != null && !result.isEmpty()) {
	//		    Iterator<UserDO> it = result.iterator();
	//		    while (it.hasNext()) {
	//			UserDO userDO = it.next();
	//			if(userDO != null) {
	//			    if(updatePoints) {
	//				userDO.setHumanPoints(points + userDO.getHumanPoints());
	//				pm.makePersistent(userDO);
	//			    }
	//			    updatedHumanPoints = userDO.getHumanPoints();
	//			}
	//		    }
	//		}
	//	    } catch (Exception e) {
	//		logger.log(Level.SEVERE,
	//			"Error while getting user human points from DB:" + e.getMessage());
	//	    } finally {
	//		pm.close();
	//	    }
	//	    return updatedHumanPoints;
	//	}
	//	return 0;
    }

    public static String getFBId(Long userId) {
	String fbId = null;
	UserDO userDO = ofy().load().type(UserDO.class).id(userId).get();
	if(userDO != null) {
	    fbId = userDO.getFbId();
	}
	return fbId;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(UserDO.class);
	//	    query.setFilter("userId == id");
	//	    query.declareParameters("String id");
	//	    @SuppressWarnings("unchecked")
	//	    List<UserDO> result = (List<UserDO>) query.execute(userId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<UserDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    UserDO userDO = it.next();
	//		    if(userDO != null) {
	//			fbId = userDO.getFbId();
	//		    }
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting user Id:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static Ref<UserDO> getUserRef(long userId) {
	if(userId != 0) {
	    return ofy().load().type(UserDO.class).id(userId);
	}
	return null;
    }
}
