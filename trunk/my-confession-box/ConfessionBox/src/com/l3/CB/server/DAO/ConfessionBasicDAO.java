package com.l3.CB.server.DAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.l3.CB.server.DO.ConfessionDO;
import com.l3.CB.server.DO.ConfessionShareDO;
import com.l3.CB.server.DO.UserDO;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionBasicDAO {

	static Logger logger = Logger.getLogger("CBLogger");

	private static UserDO getUserByFBId(UserInfo userInfo) {
		UserDO userDO = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
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
					userDO = it.next();
				}
				if(userDO != null && userDO.getGender() == null && userInfo.getGender() != null) {
					userDO.setGender(userInfo.getGender());
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

	private static UserDO getUserByUserId(UserInfo userInfo) {
		UserDO userDO = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(UserDO.class);
			query.setFilter("userId == id");
			query.declareParameters("String id");
			@SuppressWarnings("unchecked")
			List<UserDO> result = (List<UserDO>) query
					.execute(userInfo.getUserId());

			if (!result.isEmpty()) {
				Iterator<UserDO> it = result.iterator();
				while (it.hasNext()) {
					userDO = it.next();
				}
				if(userDO != null && userDO.getGender() == null && userInfo.getGender() != null) {
					userDO.setGender(userInfo.getGender());
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

	public static ConfessionShare registerConfessionShare(ConfessionShare confessedTo, Long confId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			ConfessionShareDO confessionShareDO = getConfessedTo(confessedTo);
			confessionShareDO.setConfId(confId);
			pm.makePersistent(confessionShareDO);
			confessedTo.setShareId(confessionShareDO.getShareId());
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while registering confession share:" + e.getMessage());
		} finally {
			pm.close();
		}
		return confessedTo;
	}

	private static ConfessionShareDO getConfessedTo(ConfessionShare confessedTo) {
		ConfessionShareDO confessionShareDO = null;
		if(confessedTo != null) {
			confessionShareDO = new ConfessionShareDO();
			confessionShareDO.setUserId(confessedTo.getUserId());
		}
		return confessionShareDO;
	}

	private static ConfessionDO getConfessionDO(Confession confession) {
		if (confession != null) {
			ConfessionDO confessionDO = new ConfessionDO();
			confessionDO.setUserId(confession.getUserId());
			confessionDO.setShareAsAnyn(confession.isShareAsAnyn());
			confessionDO.setConfessionTitle(confession.getConfessionTitle());
			confessionDO.setConfession(confession.getConfession());
			confessionDO.setTimeStamp(confession.getTimeStamp());
			confessionDO.setUserIp(confession.getUserIp());
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

	public static List<Confession> getConfessions(int page, int pageSize) {
		List<Confession> confessions = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(ConfessionDO.class);
			query.setRange((page*pageSize), ((page*pageSize)+pageSize));
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

	public static List<Confession> getConfessions(Long userId, int page, int pageSize) {
		List<Confession> confessions = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(ConfessionDO.class);
			query.setFilter("userId == id");
			query.declareParameters("String id");
			query.setRange((page*pageSize), ((page*pageSize)+pageSize));
			query.setOrdering("timeStamp desc");
			@SuppressWarnings("unchecked")
			List<ConfessionDO> result = (List<ConfessionDO>) query.execute(userId);

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

			UserDO userDO = getUserByUserId(new UserInfo(confessionDO.getUserId()));
			if(userDO != null) {
				if(!confession.isShareAsAnyn()) {
					confession.setFbId(userDO.getFbId());
				}
				confession.setGender(userDO.getGender());
			}
		}
		return confession;
	}

	public static Confession getConfession(Long confId) {
		Confession confession = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(ConfessionDO.class);
			query.setFilter("confId == id");
			query.declareParameters("String id");

			@SuppressWarnings("unchecked")
			List<ConfessionDO> result = (List<ConfessionDO>) query.execute(confId);
			if (!result.isEmpty()) {
				Iterator<ConfessionDO> it = result.iterator();
				while (it.hasNext()) {
					ConfessionDO confessionDO = it.next();
					confession = getConfession(confessionDO);
					confession.setActivityCount(ConfessionOtherDAO.getUserActivity(confession.getConfId()));
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while getting confession for DB:" + e.getMessage());
		} finally {
			pm.close();
		}
		return confession;
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

			if (!result.isEmpty()) {
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
