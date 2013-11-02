package com.l3.CB.server.DAO;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
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

    static {
	ObjectifyService.register(ConfessionDO.class);
	ObjectifyService.register(ConfessionShareDO.class);
	ObjectifyService.register(UserDO.class);
	ObjectifyService.register(PardonConditionDO.class);
    }

    /**
     * Persists confession
     * 
     * @param confession
     * @return confession Id - {@link Long}
     */
    public static Confession registerConfession(Confession confession) {
	ConfessionDO confessionDO = new ConfessionDO(confession);
	ofy().save().entities(confessionDO).now();
	confession.setConfId(confessionDO.getConfId());
	return confession;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	long confessionId = 0;
	//	try {
	//	    pm.makePersistent(confessionDO);
	//	    confessionId = confessionDO.getConfId();
	//	    confession.setConfId(confessionId);
	//	    logger.log(Level.INFO, "Confession logged, ID:" + confessionId);
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while registering confession:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
	//	return confession;
    }

    public static ConfessionShare registerConfessionShare(ConfessionShare confessedTo, Long confId) {
	confessedTo.setConfId(confId);
	ConfessionShareDO confessionShareDO = new ConfessionShareDO(confessedTo);
	ofy().save().entities(confessionShareDO).now();
	confessedTo.setShareId(confessionShareDO.getShareId());
	return confessedTo;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    ConfessionShareDO confessionShareDO = getConfessedTo(confessedTo);
	//	    confessionShareDO.setConfId(confId);
	//	    pm.makePersistent(confessionShareDO);
	//	    confessedTo.setShareId(confessionShareDO.getShareId());
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while registering confession share:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    //    private static ConfessionShareDO getConfessedTo(ConfessionShare confessedTo) {
    //	ConfessionShareDO confessionShareDO = null;
    //	if(confessedTo != null) {
    //	    confessionShareDO = new ConfessionShareDO();
    //	    confessionShareDO.setUserId(confessedTo.getUserId());
    //	    confessionShareDO.setTimeStamp(confessedTo.getTimeStamp());
    //	    if(confessedTo.getRelation() != null) {
    //		confessionShareDO.setRelation(confessedTo.getRelation().name());
    //	    }
    //	}
    //	return confessionShareDO;
    //    }

    //    private static ConfessionDO getConfessionDO(Confession confession) {
    //	if (confession != null) {
    //	    ConfessionDO confessionDO = new ConfessionDO();
    //	    confessionDO.setUserId(confession.getUserId());
    //	    confessionDO.setShareAsAnyn(confession.isShareAsAnyn());
    //	    confessionDO.setConfessionTitle(confession.getConfessionTitle());
    //	    confessionDO.setConfession(new Text(confession.getConfession()));
    //	    confessionDO.setTimeStamp(confession.getTimeStamp());
    //	    confessionDO.setLastUpdateTimeStamp(confession.getTimeStamp());
    //	    confessionDO.setUserIp(confession.getUserIp());
    //	    confessionDO.setLocale(confession.getLocale());
    //	    return confessionDO;
    //	}
    //	return null;
    //    }

    //    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
    //        int x = Random.nextInt(clazz.getEnumConstants().length);
    //        return clazz.getEnumConstants()[x];
    //    }    

    @SuppressWarnings("unchecked")
    public static List<Confession> getConfessions(int page, int pageSize, Filters filter, String locale) {
	List<Confession> confessions = null;

	com.googlecode.objectify.cmd.Query<ConfessionDO> queryOfy = ofy().load().type(ConfessionDO.class).filter("isVisibleOnPublicWall", Boolean.TRUE);

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(ConfessionDO.class);
	//	    query.setRange((page*pageSize), ((page*pageSize)+pageSize));
	//	    query.setOrdering("lastUpdateTimeStamp desc");

	//	    if(filter.equals(Filters.RANDOM)) {
	//		filter = randomEnum(Filters.class);
	//	    }

	switch (filter) {
	case LOCALE_SPECIFIC:
	    queryOfy = queryOfy.filter("locale", locale);

	    //		query.setFilter("isVisibleOnPublicWall == status && locale == lang");
	    //		query.declareParameters("String status" + ", "  + "String lang");
	    //		List<ConfessionDO> resultSet1 = null;
	    //		resultSet1 = (List<ConfessionDO>) query.execute(true, locale);
	    //		if (resultSet1 != null && !resultSet1.isEmpty()) {
	    //		    confessions = new ArrayList<Confession>();
	    //		    Iterator<ConfessionDO> it = resultSet1.iterator();
	    //		    while (it.hasNext()) {
	    //			ConfessionDO confessionDO = it.next();
	    //			Confession confession = confessionDO.toConfessionTO();
	    //			confessions.add(confession);
	    //		    }
	    //		}
	    break;
	case OPEN:
	    queryOfy = queryOfy.filter("shareAsAnyn", false);

	    //		query.setFilter("isVisibleOnPublicWall == status && shareAsAnyn == closed");
	    //		query.declareParameters("String status" + ", "  + "String closed");
	    //		List<ConfessionDO> resultSet2 = null;
	    //		resultSet2 = (List<ConfessionDO>) query.execute(true, false);
	    //		if (resultSet2 != null && !resultSet2.isEmpty()) {
	    //		    confessions = new ArrayList<Confession>();
	    //		    Iterator<ConfessionDO> it = resultSet2.iterator();
	    //		    while (it.hasNext()) {
	    //			ConfessionDO confessionDO = it.next();
	    //			Confession confession = confessionDO.toConfessionTO();
	    //			confessions.add(confession);
	    //		    }
	    //		}
	    break;
	case CLOSED:
	    queryOfy = queryOfy.filter("shareAsAnyn", true);

	    //		query.setFilter("isVisibleOnPublicWall == status && shareAsAnyn == closed");
	    //		query.declareParameters("String status" + ", "  + "String closed");
	    //		List<ConfessionDO> resultSet3 = null;
	    //		resultSet3 = (List<ConfessionDO>) query.execute(true, true);
	    //		if (resultSet3 != null && !resultSet3.isEmpty()) {
	    //		    confessions = new ArrayList<Confession>();
	    //		    Iterator<ConfessionDO> it = resultSet3.iterator();
	    //		    while (it.hasNext()) {
	    //			ConfessionDO confessionDO = it.next();
	    //			Confession confession = confessionDO.toConfessionTO();
	    //			confessions.add(confession);
	    //		    }
	    //		}
	    break;
	case MOST_SAME_BOATS:
	    queryOfy = queryOfy.order("-numOfSameBoatVote");

	    //		query.setFilter("isVisibleOnPublicWall == status");
	    //		query.declareParameters("String status");
	    //		query.setOrdering("numOfSameBoatVote desc");					
	    //		List<ConfessionDO> resultSet4 = null;
	    //		resultSet4 = (List<ConfessionDO>) query.execute(true);
	    //		if (resultSet4 != null && !resultSet4.isEmpty()) {
	    //		    confessions = new ArrayList<Confession>();
	    //		    Iterator<ConfessionDO> it = resultSet4.iterator();
	    //		    while (it.hasNext()) {
	    //			ConfessionDO confessionDO = it.next();
	    //			Confession confession = confessionDO.toConfessionTO();
	    //			confessions.add(confession);
	    //		    }
	    //		}
	    break;
	case MOST_LAME:
	    queryOfy = queryOfy.order("-numOfLameVote");

	    //		query.setFilter("isVisibleOnPublicWall == status");
	    //		query.declareParameters("String status");
	    //		query.setOrdering("numOfLameVote desc");					
	    //		List<ConfessionDO> resultSet5 = null;
	    //		resultSet5 = (List<ConfessionDO>) query.execute(true);
	    //		if (resultSet5 != null && !resultSet5.isEmpty()) {
	    //		    confessions = new ArrayList<Confession>();
	    //		    Iterator<ConfessionDO> it = resultSet5.iterator();
	    //		    while (it.hasNext()) {
	    //			ConfessionDO confessionDO = it.next();
	    //			Confession confession = confessionDO.toConfessionTO();
	    //			confessions.add(confession);
	    //		    }
	    //		}
	    break;
	case MOST_SYMPATHY:
	    queryOfy = queryOfy.order("-numOfSympathyVote");

	    //		query.setFilter("isVisibleOnPublicWall == status");
	    //		query.declareParameters("String status");
	    //		query.setOrdering("numOfSympathyVote desc");					
	    //		List<ConfessionDO> resultSet6 = null;
	    //		resultSet6 = (List<ConfessionDO>) query.execute(true);
	    //		if (resultSet6 != null && !resultSet6.isEmpty()) {
	    //		    confessions = new ArrayList<Confession>();
	    //		    Iterator<ConfessionDO> it = resultSet6.iterator();
	    //		    while (it.hasNext()) {
	    //			ConfessionDO confessionDO = it.next();
	    //			Confession confession = confessionDO.toConfessionTO();
	    //			confessions.add(confession);
	    //		    }
	    //		}
	    break;
	case MOST_SHOULD_BE_PARDONED:
	    queryOfy = queryOfy.order("-numOfShouldBePardonedVote");

	    //		query.setFilter("isVisibleOnPublicWall == status");
	    //		query.declareParameters("String status");
	    //		query.setOrdering("numOfShouldBePardonedVote desc");					
	    //		List<ConfessionDO> resultSet7 = null;
	    //		resultSet7 = (List<ConfessionDO>) query.execute(true);
	    //		if (resultSet7 != null && !resultSet7.isEmpty()) {
	    //		    confessions = new ArrayList<Confession>();
	    //		    Iterator<ConfessionDO> it = resultSet7.iterator();
	    //		    while (it.hasNext()) {
	    //			ConfessionDO confessionDO = it.next();
	    //			Confession confession = confessionDO.toConfessionTO();
	    //			confessions.add(confession);
	    //		    }
	    //		}
	    break;

	case MOST_SHOULD_NOT_BE_PARDONED:
	    queryOfy = queryOfy.order("-numOfShouldNotBePardonedVote");

	    //		query.setFilter("isVisibleOnPublicWall == status");
	    //		query.declareParameters("String status");
	    //		query.setOrdering("numOfShouldNotBePardonedVote desc");					
	    //		List<ConfessionDO> resultSet8 = null;
	    //		resultSet8 = (List<ConfessionDO>) query.execute(true);
	    //		if (resultSet8 != null && !resultSet8.isEmpty()) {
	    //		    confessions = new ArrayList<Confession>();
	    //		    Iterator<ConfessionDO> it = resultSet8.iterator();
	    //		    while (it.hasNext()) {
	    //			ConfessionDO confessionDO = it.next();
	    //			Confession confession = confessionDO.toConfessionTO();
	    //			confessions.add(confession);
	    //		    }
	    //		}
	    break;
	case ALL:
	    queryOfy = queryOfy.order("-lastUpdateTimeStamp");
	    //		List<ConfessionDO> resultSet9 = null;
	    //		query.setFilter("isVisibleOnPublicWall == status");
	    //		query.declareParameters("String status");
	    //		resultSet9 = (List<ConfessionDO>) query.execute(true);
	    //		if (resultSet9 != null && !resultSet9.isEmpty()) {
	    //		    confessions = new ArrayList<Confession>();
	    //		    Iterator<ConfessionDO> it = resultSet9.iterator();
	    //		    while (it.hasNext()) {
	    //			ConfessionDO confessionDO = it.next();
	    //			Confession confession = confessionDO.toConfessionTO();
	    //			confessions.add(confession);
	    //		    }
	    //		}
	    break;
	case MOST_VOTED:
	    queryOfy = queryOfy.order("-numOfTotalVote");

	    //		query.setFilter("isVisibleOnPublicWall == status");
	    //		query.declareParameters("String status");
	    //		query.setOrdering("numOfTotalVote desc");					
	    //		List<ConfessionDO> resultSet10 = null;
	    //		resultSet10 = (List<ConfessionDO>) query.execute(true);
	    //		if (resultSet10 != null && !resultSet10.isEmpty()) {
	    //		    confessions = new ArrayList<Confession>();
	    //		    Iterator<ConfessionDO> it = resultSet10.iterator();
	    //		    while (it.hasNext()) {
	    //			ConfessionDO confessionDO = it.next();
	    //			Confession confession = confessionDO.toConfessionTO();
	    //			confessions.add(confession);
	    //		    }
	    //		}
	    break;
	default:
	    queryOfy = queryOfy.order("-lastUpdateTimeStamp");

	    //		List<ConfessionDO> resultSet11 = null;
	    //		query.setFilter("isVisibleOnPublicWall == status");
	    //		query.declareParameters("String status");
	    //		resultSet11 = (List<ConfessionDO>) query.execute(true);
	    //		if (resultSet11 != null && !resultSet11.isEmpty()) {
	    //		    confessions = new ArrayList<Confession>();
	    //		    Iterator<ConfessionDO> it = resultSet11.iterator();
	    //		    while (it.hasNext()) {
	    //			ConfessionDO confessionDO = it.next();
	    //			Confession confession = confessionDO.toConfessionTO();
	    //			confessions.add(confession);
	    //		    }
	    //		}
	    break;
	}
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting confessions for DB:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}

	confessions = new ArrayList<Confession>();
	queryOfy = queryOfy.offset(page * pageSize).limit(pageSize);	
	for (ConfessionDO confessionDO : queryOfy) {
	    confessions.add(confessionDO.toConfessionTO());
	}

	return confessions;
    }

    public static List<Confession> getConfessions(Long userId, int page, int pageSize) {
	List<Confession> confessions = new ArrayList<Confession>();
	com.googlecode.objectify.cmd.Query<ConfessionDO> queryOfy = ofy()
		.load().type(ConfessionDO.class).filter("userId", userId)
		.offset(page * pageSize).limit(pageSize)
		.order("-lastUpdateTimeStamp");	
	for (ConfessionDO confessionDO : queryOfy) {
	    confessions.add(confessionDO.toConfessionTO());
	}

	return confessions;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(ConfessionDO.class);
	//	    query.setFilter("userId == id");
	//	    query.declareParameters("String id");
	//	    query.setRange((page*pageSize), ((page*pageSize)+pageSize));
	//	    query.setOrdering("lastUpdateTimeStamp desc");
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionDO> result = (List<ConfessionDO>) query.execute(userId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		confessions = new ArrayList<Confession>();
	//		Iterator<ConfessionDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionDO confessionDO = it.next();
	//		    Confession confession = confessionDO.toConfessionTO();
	//		    confessions.add(confession);
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting confessions for DB:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    //    private static Confession getConfession(ConfessionDO confessionDO) {
    //	Confession confession = null;
    //	if (confessionDO != null) {
    //	    confession = new Confession(confessionDO.getConfId(), confessionDO.getConfessionTitle(),
    //		    confessionDO.getConfession().getValue(), confessionDO.getTimeStamp(),
    //		    confessionDO.getUserId(), confessionDO.isShareAsAnyn());
    //	    confession.setVisibleOnPublicWall(confessionDO.isVisibleOnPublicWall());
    //	    confession.setNumOfAbuseVote(confessionDO.getNumOfAbuseVote());
    //	    confession.setNumOfLameVote(confessionDO.getNumOfLameVote());
    //	    confession.setNumOfSameBoatVote(confessionDO.getNumOfSameBoatVote());
    //	    confession.setNumOfShouldBePardonedVote(confessionDO.getNumOfShouldBePardonedVote());
    //	    confession.setNumOfShouldNotBePardonedVote(confessionDO.getNumOfShouldNotBePardonedVote());
    //	    confession.setNumOfSympathyVote(confessionDO.getNumOfSympathyVote());
    //	    confession.setUpdateTimeStamp(confessionDO.getLastUpdateTimeStamp());
    //
    //	    UserDO userDO = UserDAO.getUserByUserId(new UserInfo(confessionDO.getUserId()));
    //	    if(userDO != null) {
    //		confession.setFbId(userDO.getFbId());
    //		confession.setGender(userDO.getGender());
    //	    }
    //	}
    //	return confession;
    //    }

    public static Confession getConfession(Long confId) {
	ConfessionDO confessionDO = ofy().load().type(ConfessionDO.class).id(confId).get();
	if(confessionDO != null) {
	    return confessionDO.toConfessionTO();
	}
	return null;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(ConfessionDO.class);
	//	    query.setFilter("confId == id && isVisibleOnPublicWall == status");
	//	    query.declareParameters("String id" + ", "  + "String status");
	//
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionDO> result = (List<ConfessionDO>) query.execute(confId, true);
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<ConfessionDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionDO confessionDO = it.next();
	//		    confession = confessionDO.toConfessionTO();
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting confession for DB:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
	//	return confession;
    }


    public static List<Confession> getConfessionsForMe(Long userId, int page, int pageSize) {

	List<Confession> confessions = new ArrayList<Confession>();

	com.googlecode.objectify.cmd.Query<ConfessionShareDO> queryOfy = ofy()
		.load().type(ConfessionShareDO.class).filter("userId", userId)
		.offset(page * pageSize).limit(pageSize)
		.order("-timeStamp");

	for (ConfessionShareDO confessionShareDO : queryOfy) {
	    Confession confession = getConfession(confessionShareDO.getConfId());
	    if(confession != null) {
		confessions.add(confession);
	    }
	}

	return confessions;

	//	List<Confession> confessions = null;
	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query queryGetconfId = pm.newQuery(ConfessionShareDO.class);
	//	    queryGetconfId.setFilter("userId == id");
	//	    queryGetconfId.declareParameters("String id");
	//	    queryGetconfId.setRange((page*pageSize), ((page*pageSize)+pageSize));
	//	    queryGetconfId.setOrdering("timeStamp desc");
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionShareDO> result = (List<ConfessionShareDO>) queryGetconfId.execute(userId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		confessions = new ArrayList<Confession>();
	//		Iterator<ConfessionShareDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionShareDO confessionShareDO = it.next();
	//		    Confession confession = getConfession(confessionShareDO.getConfId());
	//		    confessions.add(confession);
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting confessions for DB:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
    }

    /**
     * Get confession shares
     * @param confId
     * @param allDetails
     * @return List of {@link ConfessionShare} objects
     */
    public static List<ConfessionShare> getConfessionShare(Long confId, boolean allDetails) {
	List<ConfessionShare> confessionShares = new ArrayList<ConfessionShare>();

	com.googlecode.objectify.cmd.Query<ConfessionShareDO> queryOfy = ofy().load().type(ConfessionShareDO.class).filter("confId", confId);
	for (ConfessionShareDO confessionShareDO : queryOfy) {
	    confessionShares.add(getConfessedShareTO(confessionShareDO, allDetails));
	}	

	return confessionShares;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(ConfessionShareDO.class);
	//	    query.setFilter("confId == id");
	//	    query.declareParameters("String id");
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionShareDO> result = (List<ConfessionShareDO>) query.execute(confId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		confessionShares = new ArrayList<ConfessionShare>();
	//		Iterator<ConfessionShareDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionShareDO confessionShareDO = it.next();
	//		    confessionShares.add(getConfessedShareTO(confessionShareDO, allDetails));
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting confession share:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
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

	boolean isChange = false;

	com.googlecode.objectify.cmd.Query<ConfessionShareDO> queryOfy = ofy()
		.load().type(ConfessionShareDO.class).filter("userId", userId)
		.filter("confId", confId);

	for (ConfessionShareDO confessionShareDO : queryOfy) {
	    if(!confessionShareDO.getPardonStatus().equals(pardonedStatus.name())){
		confessionShareDO.setPardonStatus(pardonedStatus.name());
		ofy().save().entities(confessionShareDO).now();
		isChange = true;
	    }
	}

	return isChange;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(ConfessionShareDO.class);
	//	    query.setFilter("userId == user && confId == conf");
	//	    query.declareParameters("String user" + ", "  + "String conf");
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionShareDO> result = (List<ConfessionShareDO>) query.execute(userId, confId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<ConfessionShareDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionShareDO confessionShareDO = it.next();
	//		    if(!confessionShareDO.getPardonStatus().equals(pardonedStatus.name())){
	//			confessionShareDO.setPardonStatus(pardonedStatus.name());
	//			pm.makePersistent(confessionShareDO);
	//			isChange = true;
	//		    }
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting user from DB:" + e.getMessage());           
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static boolean addPardonCondition(Long userId, Long confId, List<PardonCondition> pardonConditions) {

	com.googlecode.objectify.cmd.Query<ConfessionShareDO> queryOfy = ofy()
		.load().type(ConfessionShareDO.class).filter("", userId)
		.filter("", confId);

	for (ConfessionShareDO confessionShareDO : queryOfy) {
	    confessionShareDO.setPardonConditionDOs(getPardonConditionsDO(userId, confId, pardonConditions));
	    ofy().save().entities(confessionShareDO).now();
	}

	return true;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(ConfessionShareDO.class);
	//	    query.setFilter("userId == user && confId == conf");
	//	    query.declareParameters("String user" + ", "  + "String conf");
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionShareDO> result = (List<ConfessionShareDO>) query.execute(userId, confId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<ConfessionShareDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionShareDO confessionShare = it.next();
	//		    confessionShare.setPardonConditionDOs(getPardonConditionsDO(userId, confId, pardonConditions));
	//		    pm.makePersistent(confessionShare);
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting user from DB:" + e.getMessage());           
	//	    return false;
	//	} finally {
	//	    pm.close();
	//	}
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

	List<PardonCondition> pardonConditions = new ArrayList<PardonCondition>();

	com.googlecode.objectify.cmd.Query<PardonConditionDO> queryOfy = ofy().load().type(PardonConditionDO.class).filter("confId", confId);
	for (PardonConditionDO pardonConditionDO : queryOfy) {
	    PardonCondition pardonCondition = new PardonCondition(pardonConditionDO.getCondition(), pardonConditionDO.getCount());
	    pardonCondition.setUserId(pardonConditionDO.getUserId());
	    pardonCondition.setFulfil(pardonConditionDO.isFulfil());
	    pardonConditions.add(pardonCondition);
	}

	return pardonConditions;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(PardonConditionDO.class);
	//	    query.setFilter("confId == conf");
	//	    query.declareParameters("String conf");
	//	    @SuppressWarnings("unchecked")
	//	    List<PardonConditionDO> result = (List<PardonConditionDO>) query.execute(confId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<PardonConditionDO> it = result.iterator();
	//		pardonConditions = new ArrayList<PardonCondition>();
	//		while (it.hasNext()) {
	//		    PardonConditionDO pardonConditionDO = it.next();
	//		    PardonCondition pardonCondition = new PardonCondition(pardonConditionDO.getCondition(), pardonConditionDO.getCount());
	//		    pardonCondition.setUserId(pardonConditionDO.getUserId());
	//		    pardonCondition.setFulfil(pardonConditionDO.isFulfil());
	//		    pardonConditions.add(pardonCondition);
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while registering confession condition:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
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

	com.googlecode.objectify.cmd.Query<PardonConditionDO> queryOfy = ofy()
		.load().type(PardonConditionDO.class).filter("condition", condition)
		.filter("confId", confId);

	for (PardonConditionDO pardonConditionDO : queryOfy) {
	    pardonConditionDO.setFulfil(isFulfill);
	    ofy().save().entities(pardonConditionDO).now();
	}

	return true;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(PardonConditionDO.class);
	//	    query.setFilter("condition == cond && confId == conf");
	//	    query.declareParameters("String cond" + ", "  + "String conf");
	//	    @SuppressWarnings("unchecked")
	//	    List<PardonConditionDO> result = (List<PardonConditionDO>) query.execute(condition, confId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<PardonConditionDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    PardonConditionDO pardonConditionDO = it.next();
	//		    pardonConditionDO.setFulfil(isFulfill);
	//		    pm.makePersistent(pardonConditionDO);
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while updating confession condition:" + e.getMessage());
	//	    return false;
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static boolean updateConfessionVisibility(Long confId, Long userId, boolean isVisible) {

	ConfessionDO confessionDO = ofy().load().type(ConfessionDO.class).id(confId).get();
	if(confessionDO != null) {
	    confessionDO.setVisibleOnPublicWall(isVisible);
	    ofy().save().entities(confessionDO).now();
	}

	return true;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(ConfessionDO.class);
	//	    query.setFilter("userId == user && confId == conf");
	//	    query.declareParameters("String user" + ", "  + "String conf");
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionDO> result = (List<ConfessionDO>) query.execute(userId, confId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<ConfessionDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionDO confessionDO = it.next();
	//		    confessionDO.setVisibleOnPublicWall(isVisible);
	//		    pm.makePersistent(confessionDO);
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while updating confession condition:" + e.getMessage());
	//	    return false;
	//	} finally {
	//	    pm.close();
	//	}
    }

    public static long getConfessionCount(Long userId) {
	return ofy().load().type(ConfessionDO.class).filter("userId", userId).count();

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(ConfessionDO.class);
	//	    query.setFilter("userId == id");
	//	    query.declareParameters("String id");
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionDO> result = (List<ConfessionDO>) query.execute(userId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		return result.size();
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting confessions count from DB:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
	//	return 0;
    }


    public static long getConfessionsForMeCount(Long userId) {

	return ofy().load().type(ConfessionShareDO.class).filter("userId", userId).count();

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query queryGetconfId = pm.newQuery(ConfessionShareDO.class);
	//	    queryGetconfId.setFilter("userId == id");
	//	    queryGetconfId.declareParameters("String id");
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionShareDO> result = (List<ConfessionShareDO>) queryGetconfId.execute(userId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		return result.size();
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting confessions for DB:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
	//
	//	return 0;
    }

    public static Confession updateConfession(Confession confession) {
	ConfessionDO confessionDO = ofy().load().type(ConfessionDO.class).id(confession.getConfId()).get();
	if(confessionDO != null) {
	    confessionDO.setConfession(new Text(confession.getConfession()));
	    confessionDO.setUserIp(confession.getUserIp());
	    confessionDO.setLastUpdateTimeStamp(confession.getUpdateTimeStamp());
	    ofy().save().entities(confessionDO).now();
	    confession = confessionDO.toConfessionTO();
	}
	return confession;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    Query query = pm.newQuery(ConfessionDO.class);
	//	    query.setFilter("confId == id");
	//	    query.declareParameters("String id");
	//
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionDO> result = (List<ConfessionDO>) query.execute(confession.getConfId());
	//	    if (result != null && !result.isEmpty()) {
	//		Iterator<ConfessionDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionDO confessionDO = it.next();
	//		    confessionDO.setConfession(new Text(confession.getConfession()));
	//		    confessionDO.setUserIp(confession.getUserIp());
	//		    confessionDO.setLastUpdateTimeStamp(confession.getUpdateTimeStamp());
	//		    pm.makePersistent(confessionDO);
	//		}
	//	    }
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE,
	//		    "Error while getting confession for DB:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
	//	return confession;
    }

    public static void updateConfessionTimeStamp(Long confId, Date updateTimeStamp) {

	ConfessionDO confessionDO = ofy().load().type(ConfessionDO.class).id(confId).get();
	if(confessionDO != null) {
	    confessionDO.setLastUpdateTimeStamp(updateTimeStamp);
	    ofy().save().entities(confessionDO).now();
	}

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
	//		    confessionDO.setLastUpdateTimeStamp(updateTimeStamp);
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

    public static Ref<ConfessionDO> getConfessionRef(long confId) {
	if(confId != 0) {
	    return ofy().load().type(ConfessionDO.class).id(confId);
	}
	return null;
    }
}