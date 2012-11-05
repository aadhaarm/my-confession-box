package com.l3.CB.server.manager;

import java.util.List;

import com.l3.CB.server.DAO.ConfessionBasicDAO;
import com.l3.CB.server.DAO.ConfessionOtherDAO;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.UserInfo;

public class PardonManager {

    /**
     * @param pandonByUser
     * @param confId
     * @param pardonedToUser
     * @param pardonConditions
     */
    public static void pardonWithConditions(UserInfo pandonByUser, Long confId, UserInfo pardonedToUser, List<PardonCondition> pardonConditions) {
	boolean noPardonedConditions = checkIfNoPardonConditions(confId, pardonConditions);
	if(noPardonedConditions) {
	    justPardon(pandonByUser, pardonedToUser, confId, pardonConditions, true);
	} else {
	    ConfessionBasicDAO.addPardonCondition(pandonByUser.getUserId(), confId, pardonConditions);
	    validateIfConditionsMet(confId);
	}
    }

    public static void validateIfConditionsMet(Long confId) {
	int totalConditions = 0;
	int conditionsMet = 0;

	List<PardonCondition> pardonConditions = ConfessionBasicDAO.getConfessionCondition(confId);
	Confession confession = ConfessionManager.getOneConfession(confId);
	long pardonByUserId = 0;

	if(pardonConditions != null && !pardonConditions.isEmpty()) {
	    // Set total number of conditions
	    totalConditions = pardonConditions.size();
	    for (PardonCondition pardonCondition : pardonConditions) {
		pardonByUserId = pardonCondition.getUserId();
		if(pardonCondition.isFulfil() && !Constants.pardonConditionUnhide.equals(pardonCondition.getCondition())) {
		    conditionsMet++;
		} else {
		    if(Constants.pardonConditionUnhide.equals(pardonCondition.getCondition())) {
			if(!confession.isShareAsAnyn()) {
			    ConfessionBasicDAO.updateConfessionCondition(confId, pardonCondition.getCondition(), true);
			    conditionsMet++;
			} else {
			    ConfessionBasicDAO.updateConfessionCondition(confId, pardonCondition.getCondition(), false);
			}
		    } else {
			long activityCount = ConfessionOtherDAO.getActivityCount(confId, Activity.SHOULD_BE_PARDONED.name());
			if(pardonCondition.getCount() <= activityCount) {
			    ConfessionBasicDAO.updateConfessionCondition(confId, pardonCondition.getCondition(), true);
			    conditionsMet++;
			}
		    }
		}
	    }

	    UserInfo pardonByUser = UserManager.getUserByUserId(pardonByUserId);
	    UserInfo pardonedToUser = UserManager.getUserByUserId(confession.getUserId());
	    if(totalConditions == conditionsMet) {
		justPardon(pardonByUser, pardonedToUser, confId, pardonConditions, true);
	    } else {
		justPardon(pardonByUser, pardonedToUser, confId, pardonConditions, false);
	    }
	}		
    }

    private static boolean checkIfNoPardonConditions(Long confId, List<PardonCondition> pardonConditions) {
	boolean noPardonedConditions = true;
	if(pardonConditions != null && !pardonConditions.isEmpty()) {
	    noPardonedConditions = false;
	}
	return noPardonedConditions;
    }

    private static boolean justPardon(UserInfo pandonByUser, UserInfo pardonedToUser, Long confId, List<PardonCondition> pardonConditions, boolean isPardoned) {
	ConfessionBasicDAO.pardonConfession(pandonByUser.getUserId(), confId, pardonConditions, isPardoned);
	if(isPardoned) {
	    MailManager.sendPardonMail(pandonByUser, confId, pardonedToUser);
	}
	return true;
    }
}