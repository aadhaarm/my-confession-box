package com.l3.CB.server.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;

import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionUpdate;
import com.l3.CB.shared.TO.UserInfo;

public class CacheManager {

    static Logger logger = Logger.getLogger("CBLogger");
    private static Cache confessionCache;
    private static Cache confessionListCache;
    private static Cache userCache;
    private static Cache jsonCache;
    private static Cache activityCache;
    private static Cache updateCache;
    
    public CacheManager() {
	super();
	try {
	    CacheFactory cacheFactory = net.sf.jsr107cache.CacheManager.getInstance().getCacheFactory();
	    Map<String, Object> confCacheProps = new HashMap<String, Object>();
	    confCacheProps.put(GCacheFactory.EXPIRATION_DELTA, Constants.CONF_CACHE_EXPIRATION_SEC);
	    confessionCache = cacheFactory.createCache(confCacheProps);
	    
	    Map<String, Object> confListCacheProps = new HashMap<String, Object>();
	    confListCacheProps.put(GCacheFactory.EXPIRATION_DELTA, Constants.CONF_LIST_CACHE_EXPIRATION_SEC);
	    confessionListCache = cacheFactory.createCache(confListCacheProps);

	    Map<String, Object> userCacheProps = new HashMap<String, Object>();
	    userCacheProps.put(GCacheFactory.EXPIRATION_DELTA, Constants.USER_CACHE_EXPIRATION_SEC);
	    userCache = cacheFactory.createCache(userCacheProps);

	    Map<String, Object> jsonCacheProps = new HashMap<String, Object>();
	    jsonCacheProps.put(GCacheFactory.EXPIRATION_DELTA, Constants.JSON_CACHE_EXPIRATION_SEC);
	    jsonCache = cacheFactory.createCache(jsonCacheProps);

	    Map<String, Object> activityCacheProps = new HashMap<String, Object>();
	    activityCacheProps.put(GCacheFactory.EXPIRATION_DELTA, Constants.JSON_CACHE_EXPIRATION_SEC);
	    activityCache = cacheFactory.createCache(activityCacheProps);

	    Map<String, Object> updateCacheProps = new HashMap<String, Object>();
	    updateCacheProps.put(GCacheFactory.EXPIRATION_DELTA, Constants.JSON_CACHE_EXPIRATION_SEC);
	    updateCache = cacheFactory.createCache(updateCacheProps);
	    
	} catch (CacheException e) {
	    logger.log(Level.SEVERE, "Error getting cache object:" + e.getMessage());
	}
    }

    public static void cacheConfession(Confession confession) {
	if(confession != null && confession.getConfId() != null) {
	    try {
		String key = getConfessionKey(confession.getConfId());
		confessionCache.put(key, confession);
	    } catch(Exception e) {
		logger.log(Level.SEVERE,"Error while Caching confession:" + e.getMessage());
	    }
	}
    }

    /**
     * @param confession
     * @return
     */
    private static String getConfessionKey(Long confId) {
	return "conf " + confId;
    }

    public static Confession getCachedConfession(Long confId) {
	Object confesssion = confessionCache.get(getConfessionKey(confId));
	if(confesssion != null) {
	    return (Confession)confesssion;
	}
	return null;
    }

    public static void flushConfession(Long confId) {
	confessionCache.remove(getConfessionKey(confId));
    }

    public static void cacheConfessionList(String key, List<Confession> confessionList) {
	if(key != null && confessionList != null && !confessionList.isEmpty()) {
	    try {
		confessionListCache.put(key, confessionList);
	    } catch(Exception e) {
		logger.log(Level.SEVERE,"Error while Caching confession:" + e.getMessage());
	    }
	}
    }

    @SuppressWarnings("unchecked")
    public static List<Confession> getCachedConfessionList(String key) {
	if(key != null) {
	    Object confessions = confessionListCache.get(key);
	    if(confessions != null) {
		return (List<Confession>)confessions;
	    }
	}
	return null;
    }

    public static void cacheUser(UserInfo userInfo) {
	if(userInfo != null && userInfo.getUserId() != null) {
	    try {
		String key = getUserKey(userInfo.getUserId());
		userCache.put(key, userInfo);
	    } catch(Exception e) {
		logger.log(Level.SEVERE,"Error while Caching user:" + e.getMessage());
	    }
	}
    }

    public static UserInfo getCachedUser(Long userId) {
	if(userId != null) {
	    try {
		String key = getUserKey(userId);
		Object userInfo = userCache.get(key);
		if(userInfo != null) {
		    return (UserInfo)userInfo;
		}
	    } catch(Exception e) {
		logger.log(Level.SEVERE,"Error while Caching user:" + e.getMessage());
	    }
	}
	return null;
    }

    /**
     * @param userId
     * @return
     */
    private static String getUserKey(Long userId) {
	return "user" + userId;
    }

    public static void flushUser(Long userId) {
	userCache.remove(getUserKey(userId));
    } 

    public static void cacheValidateUserResult(Long userId, String fbId, boolean isValid) {
	if(userId != null && fbId != null) {
	    userCache.put(getValidateUserKey(userId, fbId), isValid);
	}
    }

    public static Boolean getCachedValidateUserResult(Long userId, String fbId) {
	Object isValidCacheObject = userCache.get(getValidateUserKey(userId, fbId));
	if(isValidCacheObject != null) {
	    return (Boolean)userCache.get(getValidateUserKey(userId, fbId));
	}
	return null;
    }

    private static String getValidateUserKey(Long userId, String fbId) {
	return "valid"+userId+fbId;
    }

    public static void cacheJsonObject(String url, String jsonString) {
	if(jsonString != null) {
	    jsonCache.put(url, jsonString);
	}
    }

    public static String getCachedJsonObject(String url) {
	String jsonString = "";
	if(url != null && !"".equals(url) && jsonCache != null && jsonCache.get(url) != null) {
	    Object jsonObject = jsonCache.get(url);
	    if(jsonObject != null) {
		jsonString = (String)jsonObject;
	    }
	}
	return jsonString;
    }

    public static void cacheActivityMap(Long userId, Long confId, Map<String, Long> activityMap) {
	if(activityMap != null && confId != null) {
	    String key = getActivityKey(userId, confId);
	    activityCache.put(key, activityMap);
	}
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Long> getActivityMap(Long userId, Long confId) {
	Map<String, Long> activityMap = null;
	if(confId != null) {
	    Object activityMapObject = activityCache.get(getActivityKey(userId, confId));
	    if(activityMapObject != null) {
		activityMap = (Map<String, Long>)activityMapObject;
	    }
	}
	return activityMap;
    }

    public static void flushActivityCache(Long userId, Long confId) {
	if(confId != null) {
	    activityCache.remove(getActivityKey(userId, confId));
	}
    }

    /**
     * @param confId
     * @return
     */
    private static String getActivityKey(Long userId, Long confId) {
	StringBuffer sb = new StringBuffer();
	sb.append("u:").append(userId).append("c:").append(confId);
	return sb.toString();
    }

    public static void cacheUpdates(Long confId, List<ConfessionUpdate> confessionUpdates) {
	if(confessionUpdates != null) {
	    updateCache.put(getUpdatesKey(confId), confessionUpdates);
	}
    }
    
    @SuppressWarnings("unchecked")
    public static List<ConfessionUpdate> getUpdates(Long confId) {
	List<ConfessionUpdate> confessionUpdates = null;
	Object o = updateCache.get(getUpdatesKey(confId));
	if(o != null) {
	    confessionUpdates = (List<ConfessionUpdate>)o;
	}
	return confessionUpdates;
    }
    
    public static void flushUpdates(Long confId) {
	updateCache.remove(getUpdatesKey(confId));
    }
    
    private static String getUpdatesKey(Long confId) {
	return "updates" + confId;
    }
}