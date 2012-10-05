package com.l3.CB.server.DAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.l3.CB.server.DO.ConfessionDO;
import com.l3.CB.server.DO.UserDO;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionBasicDAO {

	static Logger logger = Logger.getLogger("CBLogger");

	private static UserDO getUser(long userId) {
		UserDO userDO = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(UserDO.class);
			query.setFilter("userId == id");
			query.declareParameters("String id");
			@SuppressWarnings("unchecked")
			List<UserDO> result = (List<UserDO>) query
					.execute(userId);

			if (!result.isEmpty()) {
				Iterator<UserDO> it = result.iterator();
				while (it.hasNext()) {
					userDO = it.next();
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
			Query query = pm.newQuery(UserDO.class);
			query.setFilter("fbId == id");
			query.declareParameters("String id");
			@SuppressWarnings("unchecked")
			List<UserDO> result = (List<UserDO>) query
					.execute(userInfo.getId());

			if (!result.isEmpty()) {
				Iterator<UserDO> it = result.iterator();
				while (it.hasNext()) {
					UserDO userDO = it.next();
					userInfo.setUserId(userDO.getUserId());
				}
			} else {
				UserDO userDO = getUserDO(userInfo);
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

	public static Confession registerConfession(Confession confession) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		long confessionId = 0;
		try {
			ConfessionDO confessionDO = getConfessionDO(confession);
			pm.makePersistent(confessionDO);
			confessionId = confessionDO.getConfId();
			confession.setConfId(confessionId);
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while registering confession:" + e.getMessage());
		} finally {
			pm.close();
		}
		return confession;
	}

	private static ConfessionDO getConfessionDO(Confession confession) {
		if (confession != null) {
			ConfessionDO confessionDO = new ConfessionDO();
			confessionDO.setUserId(confession.getUserId());
			confessionDO.setShareAsAnyn(confession.isShareAsAnyn());
			confessionDO.setConfession(confession.getConfession());
			confessionDO.setTimeStamp(confession.getTimeStamp());
			return confessionDO;
		}
		return null;
	}

	private static UserDO getUserDO(UserInfo userInfo) {
		if (userInfo != null) {
			return new UserDO(userInfo.getId(), userInfo.getGender());
		}
		return null;
	}

	public static List<Confession> getConfessions() {
		List<Confession> confessions = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(ConfessionDO.class);
			query.setRange(0, 4);
			query.setOrdering("timeStamp desc");
			@SuppressWarnings("unchecked")
			List<ConfessionDO> result = (List<ConfessionDO>) query.execute();

			if (!result.isEmpty()) {
				confessions = new ArrayList<Confession>();
				Iterator<ConfessionDO> it = result.iterator();
				while (it.hasNext()) {
					ConfessionDO confessionDO = it.next();
					Confession confession = getConfession(confessionDO);
					confession.setActivityCount(ConfessionOtherDAO.getUserActivity(confession.getConfId()));
					confessions.add(confession);
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while getting confessions for DB:" + e.getMessage());
		} finally {
			pm.close();
		}
		return confessions;
	}

	private static Confession getConfession(ConfessionDO confessionDO) {
		Confession confession = null;
		if (confessionDO != null) {
			confession = new Confession(confessionDO.getConfId(),
			confessionDO.getConfession(), confessionDO.getTimeStamp(),
			confessionDO.getUserId(), confessionDO.isShareAsAnyn());
	
			UserDO userDO = getUser(confessionDO.getUserId());
			if(userDO != null) {
				confession.setFbId(userDO.getFbId());
				confession.setGender(userDO.getGender());
			}
		}
		return confession;
	}
}
