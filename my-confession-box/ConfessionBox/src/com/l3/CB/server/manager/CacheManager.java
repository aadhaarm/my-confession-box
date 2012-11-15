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

public class CacheManager {

    static Logger logger = Logger.getLogger("CBLogger");
    private static Cache confessionCache;

    public CacheManager() {
	super();
	try {
	    Map<String, Object> props = new HashMap<String, Object>();
	    props.put(GCacheFactory.EXPIRATION_DELTA, Constants.CONF_CACHE_EXPIRATION_SEC);
	    CacheFactory cacheFactory = net.sf.jsr107cache.CacheManager.getInstance().getCacheFactory();
	    confessionCache = cacheFactory.createCache(props);
	} catch (CacheException e) {
	    logger.log(Level.SEVERE, "Error getting cache object:" + e.getMessage());
	}
    }

    public static boolean cacheConfession(String key, Confession confession) {
	try {
	    confessionCache.put(key, confession);
	    return true;
	} catch(Exception e) {
	    logger.log(Level.SEVERE,"Error while Caching confession:" + e.getMessage());
	    return false;
	}
    }

    public static Confession getCachedConfession(String key) {
	return (Confession)confessionCache.get(key);
    }

    public static boolean cacheConfessionList(String key, List<Confession> confessionList) {
	try {
	    confessionCache.put(key, confessionList);
	    return true;
	} catch(Exception e) {
	    logger.log(Level.SEVERE,"Error while Caching confession:" + e.getMessage());
	    return false;
	}
    }

    public static List<Confession> getCachedConfessionList(String key) {
	return (List<Confession>)confessionCache.get(key);
    }
}