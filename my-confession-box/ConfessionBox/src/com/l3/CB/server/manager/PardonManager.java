package com.l3.CB.server.manager;

import java.util.List;

import com.l3.CB.server.DAO.ConfessionBasicDAO;
import com.l3.CB.server.DAO.ConfessionOtherDAO;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.PardonStatus;
import com.l3.CB.shared.TO.UserInfo;

public class PardonManager {

    /**
     * @param pandonByUser
     * @param confId
     * @param pardonedToUser
     * @param pardonConditions
     * @param pardonStatus 
     */
    public static void pardonWithConditions(UserInfo pandonByUser, Long confId, UserInfo pardonedToUser, List<PardonCondition> pardonConditions, PardonStatus pardonStatus) {
	switch (pardonStatus) {
	case PARDONED:
	    processPardonStatus(pandonByUser, pardonedToUser, confId, pardonConditions, pardonStatus);
	    break;
	case PARDONED_WITH_CONDITION:
	    ConfessionBasicDAO.addPardonCondition(pandonByUser.getUserId(), confId, pardonConditions);
	    validateIfConditionsMet(confId);
	    break;
	}
    }

    public static void validateIfConditionsMet(Long confId) {

	int totalConditions = 0;
	int conditionsMet = 0;

	// Fetch pardon conditions
	List<PardonCondition> pardonConditions = ConfessionBasicDAO.getConfessionCondition(confId);
	// FETCH confession
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
			// Identity OPEN
			if(!confession.isShareAsAnyn()) {
			    // Set to condition FULFILL
			    ConfessionBasicDAO.updateConfessionCondition(confId, pardonCondition.getCondition(), true);
			    conditionsMet++;
			} else {
			    // Identity hidden
			    // Set to condition not met yet
			    ConfessionBasicDAO.updateConfessionCondition(confId, pardonCondition.getCondition(), false);
			}
		    } else {
			long activityCount = ConfessionOtherDAO.getActivityCount(confId, Activity.SHOULD_BE_PARDONED.name());
			// activity votes > = condition count
			if(activityCount >= pardonCondition.getCount()) {
			    // Set to condition FULFILL
			    ConfessionBasicDAO.updateConfessionCondition(confId, pardonCondition.getCondition(), true);
			    conditionsMet++;
			}
		    }
		}
	    }

	    UserInfo pardonByUser = UserManager.getUserByUserId(pardonByUserId);
	    UserInfo pardonedToUser = UserManager.getUserByUserId(confession.getUserId());

	    if(totalConditions == conditionsMet) {
		// All conditions met
		processPardonStatus(pardonByUser, pardonedToUser, confId, pardonConditions, PardonStatus.PARDONED);
	    } else {
		// Some conditions left
		processPardonStatus(pardonByUser, pardonedToUser, confId, pardonConditions, PardonStatus.PARDONED_WITH_CONDITION);
	    }
	}		
    }


    private static boolean processPardonStatus(UserInfo pandonByUser, UserInfo pardonedToUser, Long confId, List<PardonCondition> pardonConditions, PardonStatus pardonedStatus) {
	ConfessionBasicDAO.pardonConfession(pandonByUser.getUserId(), confId, pardonConditions, pardonedStatus);

	// Mail users if confession is pardoned
	switch (pardonedStatus) {
	case PARDONED:
	    MailManager.sendPardonMail(pandonByUser, confId, pardonedToUser);
	    checkSubscription(confId);
	    break;
	}
	return true;
    }

    private static void checkSubscription(Long confId) {
	List<UserInfo>  subscribedUsers = ConfessionManager.getUserSubscribed(confId);
	if(subscribedUsers != null && !subscribedUsers.isEmpty()) {
	    for (UserInfo subscriberUser : subscribedUsers) {
		if(subscriberUser != null && subscriberUser.getEmail() != null && !subscriberUser.getEmail().isEmpty()) {
		    MailManager.sendSubscriptionMail(subscriberUser, confId);
		}
	    }
	}
    }
}