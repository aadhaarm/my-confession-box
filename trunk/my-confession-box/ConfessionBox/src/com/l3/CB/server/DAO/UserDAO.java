package com.l3.CB.server.DAO;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.l3.CB.server.DO.UserDO;
import com.l3.CB.shared.TO.UserInfo;

public class UserDAO {
	
	static Logger logger = Logger.getLogger("CBLogger");

	public static UserDO getUserByUserId(UserInfo userInfo) {
		UserDO userDO = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(UserDO.class);
			query.setFilter("userId == id");
			query.declareParameters("String id");
			@SuppressWarnings("unchecked")
			List<UserDO> result = (List<UserDO>) query
					.execute(userInfo.getUserId());

			if (result != null &&  !result.isEmpty()) {
				Iterator<UserDO> it = result.iterator();
				while (it.hasNext()) {
					userDO = it.next();
				}
				if(userDO != null && userDO.getGender() == null && userInfo.getGender() != null) {
					userDO.setGender(userInfo.getGender());
					userDO.setName(userInfo.getName());
					userDO.setUserName(userInfo.getUsername());
					pm.makePersistent(userDO);
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while getting user from DB:" + e.getMessage());
		} finally {
			pm.close();
		}
		return userDO;
	}

	
	public static UserInfo registerUser(UserInfo userInfo) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		long userId = 0;
		try {
			UserDO userDO = getUserByFBId(userInfo);
			if (userDO != null) {
				userInfo.setUserId(userDO.getUserId());
			} else {
				userDO = getUserDO(userInfo);
				pm.makePersistent(userDO);
				userId = userDO.getUserId();
				userInfo.setUserId(userId);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while registering user:" + e.getMessage());
		} finally {
			pm.close();
		}
		return userInfo;
	}

	/**
	 * 
	 * @param userInfo
	 * @return
	 */
	public static UserDO getUserByFBId(UserInfo userInfo) {
		UserDO userDO = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(UserDO.class);
			query.setFilter("fbId == id");
			query.declareParameters("String id");
			@SuppressWarnings("unchecked")
			List<UserDO> result = (List<UserDO>) query
					.execute(userInfo.getId());

			if (result != null && !result.isEmpty()) {
				Iterator<UserDO> it = result.iterator();
				while (it.hasNext()) {
					userDO = it.next();
				}
				if(userDO != null && (userDO.getGender() == null || "".equals(userDO.getGender())) && userInfo.getGender() != null) {
					userDO.setGender(userInfo.getGender());
					userDO.setName(userInfo.getName());
					userDO.setUserName(userInfo.getUsername());
					userDO.setEmail(userInfo.getEmail());
					pm.makePersistent(userDO);
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while getting user from DB:" + e.getMessage());
		} finally {
			pm.close();
		}
		return userDO;
	}
	
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
		PersistenceManager pm = PMF.get().getPersistenceManager();
		long userId = 0;
		try {
			Query query = pm.newQuery(UserDO.class);
			query.setFilter("fbId == id");
			query.declareParameters("String id");
			@SuppressWarnings("unchecked")
			List<UserDO> result = (List<UserDO>) query.execute(fbId);

			if (result != null && !result.isEmpty()) {
				Iterator<UserDO> it = result.iterator();
				while (it.hasNext()) {
					UserDO userDO = it.next();
					userId = userDO.getUserId();
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while registering user:" + e.getMessage());
		} finally {
			pm.close();
		}
		return userId;
	}
}
