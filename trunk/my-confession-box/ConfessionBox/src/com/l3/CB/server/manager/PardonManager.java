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
     * @param pardonByUser
     * @param confId
     * @param pardonedToUser
     * @param pardonConditions
     * @param pardonStatus 
     */
    public static void pardonConfession(UserInfo pardonByUser, Long confId, UserInfo pardonedToUser, List<PardonCondition> pardonConditions, PardonStatus pardonStatus) {
	switch (pardonStatus) {
	case PARDONED:
	    // Update pardon share with new status
	    ConfessionBasicDAO.updateConfessionSharePardonCondition(pardonByUser.getUserId(), confId, pardonConditions, pardonStatus);
	    // Float required emails
	    informPardonEvent(pardonByUser, pardonedToUser, confId);
	    break;
	case PARDONED_WITH_CONDITION:
	    // Add conditions in DB
	    ConfessionBasicDAO.addPardonCondition(pardonByUser.getUserId(), confId, pardonConditions);
	    // Update pardon share with new status
	    ConfessionBasicDAO.updateConfessionSharePardonCondition(pardonByUser.getUserId(), confId, pardonConditions, pardonStatus);
	    // Validate if any condition already met and update share status
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
	Confession confession = ConfessionManager.getOneConfession(confId, false);
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
		// Update pardon share with new status
		ConfessionBasicDAO.updateConfessionSharePardonCondition(pardonByUser.getUserId(), confId, pardonConditions, PardonStatus.PARDONED);
		// Float required emails
		informPardonEvent(pardonByUser, pardonedToUser, confId);
	    } else {
		// Update pardon share with new status
		ConfessionBasicDAO.updateConfessionSharePardonCondition(pardonByUser.getUserId(), confId, pardonConditions, PardonStatus.PARDONED_WITH_CONDITION);
	    }
	}		
    }

    private static boolean informPardonEvent(UserInfo pardonByUser, UserInfo pardonedToUser, Long confId) {
	if(pardonedToUser != null && pardonedToUser.getEmail() == null) {
	    pardonedToUser = UserManager.getUserByFbId(pardonedToUser);
	}
	MailManager.sendPardonMail(pardonByUser, confId, pardonedToUser);
	checkSubscription(confId);
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