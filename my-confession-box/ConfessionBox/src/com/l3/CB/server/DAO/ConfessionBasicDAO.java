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
    }

    public static ConfessionShare registerConfessionShare(ConfessionShare confessedTo, Long confId) {
	confessedTo.setConfId(confId);
	ConfessionShareDO confessionShareDO = new ConfessionShareDO(confessedTo);
	ofy().save().entities(confessionShareDO).now();
	confessedTo.setShareId(confessionShareDO.getShareId());
	return confessedTo;
    }

    public static List<Confession> getConfessions(int page, int pageSize, Filters filter, String locale) {
	List<Confession> confessions = null;

//	if(page == 0 && filter.equals(Filters.ALL)) {
//	    // Add 5 selected confessions
//	    com.googlecode.objectify.cmd.Query<ConfessionDO> queryOfy = ofy().load().type(ConfessionDO.class).filter("isVisibleOnPublicWall", Boolean.TRUE);
//	    queryOfy = queryOfy.filter("isSelected", true);
//	    queryOfy = queryOfy.offset(page * pageSize).limit(50);	
//	    confessions = new ArrayList<Confession>();
//	    for (ConfessionDO confessionDO : queryOfy) {
//		confessions.add(confessionDO.toConfessionTO());
//	    }
//	    
//	    Collections.shuffle(confessions);
//	    confessions = confessions.subList(0, confessions.size() >= 5? 5:(confessions.size()-1));
//	} else {
	    com.googlecode.objectify.cmd.Query<ConfessionDO> queryOfy = ofy().load().type(ConfessionDO.class).filter("isVisibleOnPublicWall", Boolean.TRUE);
	    switch (filter) {
	    case LOCALE_SPECIFIC:
		queryOfy = queryOfy.filter("locale", locale);
		break;
	    case OPEN:
		queryOfy = queryOfy.filter("shareAsAnyn", false);
		break;
	    case CLOSED:
		queryOfy = queryOfy.filter("shareAsAnyn", true);
		break;
	    case MOST_SAME_BOATS:
		queryOfy = queryOfy.order("-numOfSameBoatVote");
		break;
	    case MOST_LAME:
		queryOfy = queryOfy.order("-numOfLameVote");
		break;
	    case MOST_SYMPATHY:
		queryOfy = queryOfy.order("-numOfSympathyVote");
		break;
	    case MOST_SHOULD_BE_PARDONED:
		queryOfy = queryOfy.order("-numOfShouldBePardonedVote");
		break;
	    case MOST_SHOULD_NOT_BE_PARDONED:
		queryOfy = queryOfy.order("-numOfShouldNotBePardonedVote");
		break;
	    case ALL:
		queryOfy = queryOfy.order("-lastUpdateTimeStamp");
		break;
	    case MOST_VOTED:
		queryOfy = queryOfy.order("-numOfTotalVote");
		break;
	    default:
		queryOfy = queryOfy.order("-lastUpdateTimeStamp");
		break;
	    }

	    confessions = new ArrayList<Confession>();

	    queryOfy = queryOfy.offset(page * pageSize).limit(pageSize);	
	    for (ConfessionDO confessionDO : queryOfy) {
		confessions.add(confessionDO.toConfessionTO());
	    }
//	}

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
    }

    public static Confession getConfession(Long confId) {
	ConfessionDO confessionDO = ofy().load().type(ConfessionDO.class).id(confId).get();
	if(confessionDO != null) {
	    return confessionDO.toConfessionTO();
	}
	return null;
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
    }

    public static boolean updateConfessionCondition(Long confId, String condition, boolean isFulfill) {

	com.googlecode.objectify.cmd.Query<PardonConditionDO> queryOfy = ofy()
		.load().type(PardonConditionDO.class).filter("condition", condition)
		.filter("confId", confId);

	for (PardonConditionDO pardonConditionDO : queryOfy) {
	    pardonConditionDO.setFulfil(isFulfill);
	    ofy().save().entities(pardonConditionDO).now();
	}

	return true;
    }

    public static boolean updateConfessionVisibility(Long confId, Long userId, boolean isVisible) {

	ConfessionDO confessionDO = ofy().load().type(ConfessionDO.class).id(confId).get();
	if(confessionDO != null) {
	    confessionDO.setVisibleOnPublicWall(isVisible);
	    ofy().save().entities(confessionDO).now();
	}

	return true;
    }

    public static long getConfessionCount(Long userId) {
	return ofy().load().type(ConfessionDO.class).filter("userId", userId).count();
    }


    public static long getConfessionsForMeCount(Long userId) {
	return ofy().load().type(ConfessionShareDO.class).filter("userId", userId).count();
    }

    public static Confession updateConfession(Confession confession) {
	ConfessionDO confessionDO = ofy().load().type(ConfessionDO.class).id(confession.getConfId()).get();
	if(confessionDO != null) {
	    confessionDO.setConfession(new Text(confession.getConfession()));
	    confessionDO.setUserIp(confession.getUserIp());
	    confessionDO.setLastUpdateTimeStamp(confession.getUpdateTimeStamp());
	    confessionDO.setSelected(confession.isSelected());
	    ofy().save().entities(confessionDO).now();

	    confession = confessionDO.toConfessionTO();
	}
	return confession;
    }

    public static void updateConfessionTimeStamp(Long confId, Date updateTimeStamp) {

	ConfessionDO confessionDO = ofy().load().type(ConfessionDO.class).id(confId).get();
	if(confessionDO != null) {
	    confessionDO.setLastUpdateTimeStamp(updateTimeStamp);
	    ofy().save().entities(confessionDO).now();
	}
    }

    public static Ref<ConfessionDO> getConfessionRef(long confId) {
	if(confId != 0) {
	    return ofy().load().type(ConfessionDO.class).id(confId);
	}
	return null;
    }
}