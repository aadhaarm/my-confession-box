package com.l3.CB.server.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Text;
import com.l3.CB.server.DO.ConfessionDO;
import com.l3.CB.server.DO.ConfessionShareDO;
import com.l3.CB.server.DO.PardonConditionDO;
import com.l3.CB.server.DO.UserDO;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.Filters;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.PardonStatus;
import com.l3.CB.shared.TO.Relations;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionBasicDAO {

    static Logger logger = Logger.getLogger("CBLogger");

    /**
     * Persists confession
     * 
     * @param confession
     * @return confession Id - {@link Long}
     */
    public static Confession registerConfession(Confession confession) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	long confessionId = 0;
	try {
	    ConfessionDO confessionDO = getConfessionDO(confession);
	    pm.makePersistent(confessionDO);
	    confessionId = confessionDO.getConfId();
	    confession.setConfId(confessionId);
	    logger.log(Level.INFO, "Confession logged, ID:" + confessionId);
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
	    confessionShareDO.setTimeStamp(confessedTo.getTimeStamp());
	    if(confessedTo.getRelation() != null) {
		confessionShareDO.setRelation(confessedTo.getRelation().name());
	    }
	}
	return confessionShareDO;
    }

    private static ConfessionDO getConfessionDO(Confession confession) {
	if (confession != null) {
	    ConfessionDO confessionDO = new ConfessionDO();
	    confessionDO.setUserId(confession.getUserId());
	    confessionDO.setShareAsAnyn(confession.isShareAsAnyn());
	    confessionDO.setConfessionTitle(confession.getConfessionTitle());
	    confessionDO.setConfession(new Text(confession.getConfession()));
	    confessionDO.setTimeStamp(confession.getTimeStamp());
	    confessionDO.setLastUpdateTimeStamp(confession.getTimeStamp());
	    confessionDO.setUserIp(confession.getUserIp());
	    confessionDO.setLocale(confession.getLocale());
	    return confessionDO;
	}
	return null;
    }


    @SuppressWarnings("unchecked")
    public static List<Confession> getConfessions(int page, int pageSize, Filters filter, String locale) {
	List<Confession> confessions = null;
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    Query query = pm.newQuery(ConfessionDO.class);
	    query.setRange((page*pageSize), ((page*pageSize)+pageSize));
	    query.setOrdering("lastUpdateTimeStamp desc");

	    switch (filter) {
	    case LOCALE_SPECIFIC:
		query.setFilter("isVisibleOnPublicWall == status && locale == lang");
		query.declareParameters("String status" + ", "  + "String lang");
		List<ConfessionDO> resultSet1 = null;
		resultSet1 = (List<ConfessionDO>) query.execute(true, locale);
		if (resultSet1 != null && !resultSet1.isEmpty()) {
		    confessions = new ArrayList<Confession>();
		    Iterator<ConfessionDO> it = resultSet1.iterator();
		    while (it.hasNext()) {
			ConfessionDO confessionDO = it.next();
			Confession confession = getConfession(confessionDO);
			confessions.add(confession);
		    }
		}
		break;
	    case OPEN:
		query.setFilter("isVisibleOnPublicWall == status && shareAsAnyn == closed");
		query.declareParameters("String status" + ", "  + "String closed");
		List<ConfessionDO> resultSet2 = null;
		resultSet2 = (List<ConfessionDO>) query.execute(true, false);
		if (resultSet2 != null && !resultSet2.isEmpty()) {
		    confessions = new ArrayList<Confession>();
		    Iterator<ConfessionDO> it = resultSet2.iterator();
		    while (it.hasNext()) {
			ConfessionDO confessionDO = it.next();
			Confession confession = getConfession(confessionDO);
			confessions.add(confession);
		    }
		}
		break;
	    case CLOSED:
		query.setFilter("isVisibleOnPublicWall == status && shareAsAnyn == closed");
		query.declareParameters("String status" + ", "  + "String closed");
		List<ConfessionDO> resultSet3 = null;
		resultSet3 = (List<ConfessionDO>) query.execute(true, true);
		if (resultSet3 != null && !resultSet3.isEmpty()) {
		    confessions = new ArrayList<Confession>();
		    Iterator<ConfessionDO> it = resultSet3.iterator();
		    while (it.hasNext()) {
			ConfessionDO confessionDO = it.next();
			Confession confession = getConfession(confessionDO);
			confessions.add(confession);
		    }
		}
		break;
	    case MOST_SAME_BOATS:
		query.setFilter("isVisibleOnPublicWall == status");
		query.declareParameters("String status");
		query.setOrdering("numOfSameBoatVote desc");					
		List<ConfessionDO> resultSet4 = null;
		resultSet4 = (List<ConfessionDO>) query.execute(true);
		if (resultSet4 != null && !resultSet4.isEmpty()) {
		    confessions = new ArrayList<Confession>();
		    Iterator<ConfessionDO> it = resultSet4.iterator();
		    while (it.hasNext()) {
			ConfessionDO confessionDO = it.next();
			Confession confession = getConfession(confessionDO);
			confessions.add(confession);
		    }
		}
		break;
	    case MOST_LAME:
		query.setFilter("isVisibleOnPublicWall == status");
		query.declareParameters("String status");
		query.setOrdering("numOfLameVote desc");					
		List<ConfessionDO> resultSet5 = null;
		resultSet5 = (List<ConfessionDO>) query.execute(true);
		if (resultSet5 != null && !resultSet5.isEmpty()) {
		    confessions = new ArrayList<Confession>();
		    Iterator<ConfessionDO> it = resultSet5.iterator();
		    while (it.hasNext()) {
			ConfessionDO confessionDO = it.next();
			Confession confession = getConfession(confessionDO);
			confessions.add(confession);
		    }
		}
		break;
	    case MOST_SYMPATHY:
		query.setFilter("isVisibleOnPublicWall == status");
		query.declareParameters("String status");
		query.setOrdering("numOfSympathyVote desc");					
		List<ConfessionDO> resultSet6 = null;
		resultSet6 = (List<ConfessionDO>) query.execute(true);
		if (resultSet6 != null && !resultSet6.isEmpty()) {
		    confessions = new ArrayList<Confession>();
		    Iterator<ConfessionDO> it = resultSet6.iterator();
		    while (it.hasNext()) {
			ConfessionDO confessionDO = it.next();
			Confession confession = getConfession(confessionDO);
			confessions.add(confession);
		    }
		}
		break;
	    case MOST_SHOULD_BE_PARDONED:
		query.setFilter("isVisibleOnPublicWall == status");
		query.declareParameters("String status");
		query.setOrdering("numOfShouldBePardonedVote desc");					
		List<ConfessionDO> resultSet7 = null;
		resultSet7 = (List<ConfessionDO>) query.execute(true);
		if (resultSet7 != null && !resultSet7.isEmpty()) {
		    confessions = new ArrayList<Confession>();
		    Iterator<ConfessionDO> it = resultSet7.iterator();
		    while (it.hasNext()) {
			ConfessionDO confessionDO = it.next();
			Confession confession = getConfession(confessionDO);
			confessions.add(confession);
		    }
		}
		break;					
	    default:
		List<ConfessionDO> resultSet8 = null;
		query.setFilter("isVisibleOnPublicWall == status");
		query.declareParameters("String status");
		resultSet8 = (List<ConfessionDO>) query.execute(true);
		if (resultSet8 != null && !resultSet8.isEmpty()) {
		    confessions = new ArrayList<Confession>();
		    Iterator<ConfessionDO> it = resultSet8.iterator();
		    while (it.hasNext()) {
			ConfessionDO confessionDO = it.next();
			Confession confession = getConfession(confessionDO);
			confessions.add(confession);
		    }
		}
		break;
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
	    query.setOrdering("lastUpdateTimeStamp desc");
	    @SuppressWarnings("unchecked")
	    List<ConfessionDO> result = (List<ConfessionDO>) query.execute(userId);

	    if (result != null && !result.isEmpty()) {
		confessions = new ArrayList<Confession>();
		Iterator<ConfessionDO> it = result.iterator();
		while (it.hasNext()) {
		    ConfessionDO confessionDO = it.next();
		    Confession confession = getConfession(confessionDO);
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
	    confession = new Confession(confessionDO.getConfId(), confessionDO.getConfessionTitle(),
		    confessionDO.getConfession().getValue(), confessionDO.getTimeStamp(),
		    confessionDO.getUserId(), confessionDO.isShareAsAnyn());
	    confession.setVisibleOnPublicWall(confessionDO.isVisibleOnPublicWall());
	    confession.setNumOfAbuseVote(confessionDO.getNumOfAbuseVote());
	    confession.setNumOfLameVote(confessionDO.getNumOfLameVote());
	    confession.setNumOfSameBoatVote(confessionDO.getNumOfSameBoatVote());
	    confession.setNumOfShouldBePardonedVote(confessionDO.getNumOfShouldBePardonedVote());
	    confession.setNumOfShouldNotBePardonedVote(confessionDO.getNumOfShouldNotBePardonedVote());
	    confession.setNumOfSympathyVote(confessionDO.getNumOfSympathyVote());
	    confession.setUpdateTimeStamp(confessionDO.getLastUpdateTimeStamp());
	    
	    UserDO userDO = UserDAO.getUserByUserId(new UserInfo(confessionDO.getUserId()));
	    if(userDO != null) {
		confession.setFbId(userDO.getFbId());
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
	    if (result != null && !result.isEmpty()) {
		Iterator<ConfessionDO> it = result.iterator();
		while (it.hasNext()) {
		    ConfessionDO confessionDO = it.next();
		    confession = getConfession(confessionDO);
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


    public static List<Confession> getConfessionsForMe(Long userId, int page, int pageSize) {
	List<Confession> confessions = null;
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    Query queryGetconfId = pm.newQuery(ConfessionShareDO.class);
	    queryGetconfId.setFilter("userId == id");
	    queryGetconfId.declareParameters("String id");
	    queryGetconfId.setRange((page*pageSize), ((page*pageSize)+pageSize));
	    queryGetconfId.setOrdering("timeStamp desc");
	    @SuppressWarnings("unchecked")
	    List<ConfessionShareDO> result = (List<ConfessionShareDO>) queryGetconfId.execute(userId);

	    if (result != null && !result.isEmpty()) {
		confessions = new ArrayList<Confession>();
		Iterator<ConfessionShareDO> it = result.iterator();
		while (it.hasNext()) {
		    ConfessionShareDO confessionShareDO = it.next();
		    Confession confession = getConfession(confessionShareDO.getConfId());
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

    /**
     * Get confession shares
     * @param confId
     * @param allDetails
     * @return List of {@link ConfessionShare} objects
     */
    public static List<ConfessionShare> getConfessionShare(Long confId, boolean allDetails) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	List<ConfessionShare> confessionShares = null;
	try {
	    Query query = pm.newQuery(ConfessionShareDO.class);
	    query.setFilter("confId == id");
	    query.declareParameters("String id");
	    @SuppressWarnings("unchecked")
	    List<ConfessionShareDO> result = (List<ConfessionShareDO>) query.execute(confId);

	    if (result != null && !result.isEmpty()) {
		confessionShares = new ArrayList<ConfessionShare>();
		Iterator<ConfessionShareDO> it = result.iterator();
		while (it.hasNext()) {
		    ConfessionShareDO confessionShareDO = it.next();
		    confessionShares.add(getConfessedShareTO(confessionShareDO, allDetails));
		}
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while getting confession share:" + e.getMessage());
	} finally {
	    pm.close();
	}
	return confessionShares;
    }

    private static ConfessionShare getConfessedShareTO(ConfessionShareDO confessionShareDO, boolean allDetails) {
	ConfessionShare confessionShare = null;
	if(confessionShareDO != null) {
	    confessionShare = new ConfessionShare();
	    if(allDetails) {
		confessionShare.setConfId(confessionShareDO.getConfId());
		confessionShare.setShareId(confessionShareDO.getShareId());
		confessionShare.setUserId(confessionShareDO.getUserId());
		UserDO confessedToUser = UserDAO.getUserByUserId(new UserInfo(confessionShareDO.getUserId()));
		if(confessedToUser != null) {
		    confessionShare.setFbId(confessedToUser.getFbId());
		    confessionShare.setUserFullName(confessedToUser.getName());
		}
	    }
	    confessionShare.setTimeStamp(confessionShareDO.getTimeStamp());
	    confessionShare.setPardonStatus(PardonStatus.valueOf(confessionShareDO.getPardonStatus()));
	    confessionShare.setPardonConditions(getPardonConditions(confessionShareDO.getPardonConditionDOs()));
	    if(confessionShareDO.getRelation() != null) {
		confessionShare.setRelation(Relations.valueOf(confessionShareDO.getRelation()));
	    }
	}
	return confessionShare;
    }

    private static List<PardonCondition> getPardonConditions(List<PardonConditionDO> pardonConditionDOs) {
	List<PardonCondition> pardonConditions = null;
	if(pardonConditionDOs != null) {

	    pardonConditions = new ArrayList<PardonCondition>();
	    for (PardonConditionDO pardonConditionDO : pardonConditionDOs) {
		PardonCondition pardonCondition = new PardonCondition();
		pardonCondition.setCondition(pardonConditionDO.getCondition());
		pardonCondition.setCount(pardonConditionDO.getCount());
		pardonCondition.setFulfil(pardonConditionDO.isFulfil());
		pardonConditions.add(pardonCondition);
	    }
	}
	return pardonConditions;
    }

    public static boolean updateConfessionSharePardonCondition(Long userId, Long confId, List<PardonCondition> pardonConditions, PardonStatus pardonedStatus) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	boolean isChange = false;
	try {
	    Query query = pm.newQuery(ConfessionShareDO.class);
	    query.setFilter("userId == user && confId == conf");
	    query.declareParameters("String user" + ", "  + "String conf");
	    @SuppressWarnings("unchecked")
	    List<ConfessionShareDO> result = (List<ConfessionShareDO>) query.execute(userId, confId);

	    if (result != null && !result.isEmpty()) {
		Iterator<ConfessionShareDO> it = result.iterator();
		while (it.hasNext()) {
		    ConfessionShareDO confessionShareDO = it.next();
		    if(!confessionShareDO.getPardonStatus().equals(pardonedStatus.name())){
			confessionShareDO.setPardonStatus(pardonedStatus.name());
			pm.makePersistent(confessionShareDO);
			isChange = true;
		    }
		}
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while getting user from DB:" + e.getMessage());           
	} finally {
	    pm.close();
	}
	return isChange;
    }

    public static boolean addPardonCondition(Long userId, Long confId, List<PardonCondition> pardonConditions) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    Query query = pm.newQuery(ConfessionShareDO.class);
	    query.setFilter("userId == user && confId == conf");
	    query.declareParameters("String user" + ", "  + "String conf");
	    @SuppressWarnings("unchecked")
	    List<ConfessionShareDO> result = (List<ConfessionShareDO>) query.execute(userId, confId);

	    if (result != null && !result.isEmpty()) {
		Iterator<ConfessionShareDO> it = result.iterator();
		while (it.hasNext()) {
		    ConfessionShareDO confessionShare = it.next();
		    confessionShare.setPardonConditionDOs(getPardonConditionsDO(userId, confId, pardonConditions));
		    pm.makePersistent(confessionShare);
		}
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while getting user from DB:" + e.getMessage());           
	    return false;
	} finally {
	    pm.close();
	}
	return true;
    }


    private static List<PardonConditionDO> getPardonConditionsDO(Long userId, Long confId, List<PardonCondition> pardonConditions) {
	List<PardonConditionDO> conditionDOs = null;
	if(pardonConditions != null && !pardonConditions.isEmpty()) {
	    conditionDOs = new ArrayList<PardonConditionDO>();

	    for (PardonCondition pardonCondition : pardonConditions) {
		if(pardonCondition != null) {
		    PardonConditionDO pardonConditionDO = new PardonConditionDO(pardonCondition.getCondition());
		    pardonConditionDO.setUserId(userId);
		    pardonConditionDO.setCount(pardonCondition.getCount());
		    pardonConditionDO.setConfId(confId);
		    conditionDOs.add(pardonConditionDO);
		}
	    }

	}
	return conditionDOs;
    }

    public static List<PardonCondition> getConfessionCondition(Long confId) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	List<PardonCondition> pardonConditions = null;
	try {
	    Query query = pm.newQuery(PardonConditionDO.class);
	    query.setFilter("confId == conf");
	    query.declareParameters("String conf");
	    @SuppressWarnings("unchecked")
	    List<PardonConditionDO> result = (List<PardonConditionDO>) query.execute(confId);

	    if (result != null && !result.isEmpty()) {
		Iterator<PardonConditionDO> it = result.iterator();
		pardonConditions = new ArrayList<PardonCondition>();
		while (it.hasNext()) {
		    PardonConditionDO pardonConditionDO = it.next();
		    PardonCondition pardonCondition = new PardonCondition(pardonConditionDO.getCondition(), pardonConditionDO.getCount());
		    pardonCondition.setUserId(pardonConditionDO.getUserId());
		    pardonCondition.setFulfil(pardonConditionDO.isFulfil());
		    pardonConditions.add(pardonCondition);
		}
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while registering confession condition:" + e.getMessage());
	} finally {
	    pm.close();
	}
	return pardonConditions;
    }

//    public static PardonCondition getConfessionCondition(Long confId, String conditionType) {
//	PersistenceManager pm = PMF.get().getPersistenceManager();
//	PardonCondition pardonCondition = null;
//	try {
//	    Query query = pm.newQuery(PardonConditionDO.class);
//	    query.setFilter("condition == cond && confId == conf");
//	    query.declareParameters("String cond" + ", "  + "String conf");
//	    @SuppressWarnings("unchecked")
//	    List<PardonConditionDO> result = (List<PardonConditionDO>) query.execute(conditionType, confId);
//
//	    if (result != null && !result.isEmpty()) {
//		Iterator<PardonConditionDO> it = result.iterator();
//		while (it.hasNext()) {
//		    PardonConditionDO pardonConditionDO = it.next();
//		    pardonCondition = new PardonCondition(pardonConditionDO.getCondition(), pardonConditionDO.getCount());
//		    pardonCondition.setUserId(pardonConditionDO.getUserId());
//		    pardonCondition.setFulfil(pardonConditionDO.isFulfil());
//		}
//	    }
//	} catch (Exception e) {
//	    logger.log(Level.SEVERE, "Error while getting pardon condition:" + e.getMessage());
//	} finally {
//	    pm.close();
//	}
//	return pardonCondition;
//    }

    
    public static boolean updateConfessionCondition(Long confId, String condition, boolean isFulfill) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    Query query = pm.newQuery(PardonConditionDO.class);
	    query.setFilter("condition == cond && confId == conf");
	    query.declareParameters("String cond" + ", "  + "String conf");
	    @SuppressWarnings("unchecked")
	    List<PardonConditionDO> result = (List<PardonConditionDO>) query.execute(condition, confId);

	    if (result != null && !result.isEmpty()) {
		Iterator<PardonConditionDO> it = result.iterator();
		while (it.hasNext()) {
		    PardonConditionDO pardonConditionDO = it.next();
		    pardonConditionDO.setFulfil(isFulfill);
		    pm.makePersistent(pardonConditionDO);
		}
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while updating confession condition:" + e.getMessage());
	    return false;
	} finally {
	    pm.close();
	}
	return true;
    }

    public static boolean updateConfessionVisibility(Long confId, Long userId, boolean isVisible) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    Query query = pm.newQuery(ConfessionDO.class);
	    query.setFilter("userId == user && confId == conf");
	    query.declareParameters("String user" + ", "  + "String conf");
	    @SuppressWarnings("unchecked")
	    List<ConfessionDO> result = (List<ConfessionDO>) query.execute(userId, confId);

	    if (result != null && !result.isEmpty()) {
		Iterator<ConfessionDO> it = result.iterator();
		while (it.hasNext()) {
		    ConfessionDO confessionDO = it.next();
		    confessionDO.setVisibleOnPublicWall(isVisible);
		    pm.makePersistent(confessionDO);
		}
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while updating confession condition:" + e.getMessage());
	    return false;
	} finally {
	    pm.close();
	}
	return true;
    }

    public static long getConfessionCount(Long userId) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    Query query = pm.newQuery(ConfessionDO.class);
	    query.setFilter("userId == id");
	    query.declareParameters("String id");
	    @SuppressWarnings("unchecked")
	    List<ConfessionDO> result = (List<ConfessionDO>) query.execute(userId);

	    if (result != null && !result.isEmpty()) {
		return result.size();
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while getting confessions count from DB:" + e.getMessage());
	} finally {
	    pm.close();
	}
	return 0;
    }


    public static long getConfessionsForMeCount(Long userId) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    Query queryGetconfId = pm.newQuery(ConfessionShareDO.class);
	    queryGetconfId.setFilter("userId == id");
	    queryGetconfId.declareParameters("String id");
	    @SuppressWarnings("unchecked")
	    List<ConfessionShareDO> result = (List<ConfessionShareDO>) queryGetconfId.execute(userId);

	    if (result != null && !result.isEmpty()) {
		return result.size();
	    }
	} catch (Exception e) {
	    logger.log(Level.SEVERE,
		    "Error while getting confessions for DB:" + e.getMessage());
	} finally {
	    pm.close();
	}

	return 0;
    }

    public static Confession updateConfession(Confession confession) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    Query query = pm.newQuery(ConfessionDO.class);
	    query.setFilter("confId == id");
	    query.declareParameters("String id");

	    @SuppressWarnings("unchecked")
	    List<ConfessionDO> result = (List<ConfessionDO>) query.execute(confession.getConfId());
	    if (result != null && !result.isEmpty()) {
		Iterator<ConfessionDO> it = result.iterator();
		while (it.hasNext()) {
		    ConfessionDO confessionDO = it.next();
		    confessionDO.setConfession(new Text(confession.getConfession()));
		    confessionDO.setUserIp(confession.getUserIp());
		    confessionDO.setLastUpdateTimeStamp(confession.getUpdateTimeStamp());
		    pm.makePersistent(confessionDO);
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

    public static void updateConfessionTimeStamp(Long confId, Date updateTimeStamp) {
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
		    confessionDO.setLastUpdateTimeStamp(updateTimeStamp);
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
}
