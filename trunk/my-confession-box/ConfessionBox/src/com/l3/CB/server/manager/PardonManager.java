package com.l3.CB.server.manager;

import java.util.Date;
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
     * @param updateTimeStamp 
     */
    public static void pardonConfession(UserInfo pardonByUser, Long confId, UserInfo pardonedToUser, List<PardonCondition> pardonConditions, PardonStatus pardonStatus, Date updateTimeStamp) {
	switch (pardonStatus) {
	case PARDONED:
	    // Update pardon share with new status
	    ConfessionBasicDAO.updateConfessionSharePardonCondition(pardonByUser.getUserId(), confId, pardonConditions, pardonStatus);
	    // Float required emails
	    ConfessionBasicDAO.updateConfessionTimeStamp(confId, updateTimeStamp);
	    informPardonEvent(pardonByUser, pardonedToUser, confId);
	    CacheManager.flushConfession(confId);
	    break;
	case PARDONED_WITH_CONDITION:
	    // Add conditions in DB
	    ConfessionBasicDAO.addPardonCondition(pardonByUser.getUserId(), confId, pardonConditions);
	    // Update pardon share with new status
	    ConfessionBasicDAO.updateConfessionSharePardonCondition(pardonByUser.getUserId(), confId, pardonConditions, pardonStatus);
	    // Validate if any condition already met and update share status
	    validateIfAllConditionsMet(confId, updateTimeStamp, null, null);
	    ConfessionBasicDAO.updateConfessionTimeStamp(confId, updateTimeStamp);
	    CacheManager.flushConfession(confId);
	    break;
	}
    }

    public static void validateIfAllConditionsMet(Long confId, Date updateTimeStamp, Confession confession, Long updatedActivityCount) {
	int totalConditions = 0;
	int conditionsMet = 0;

	// Fetch pardon conditions
	List<PardonCondition> pardonConditions = ConfessionBasicDAO.getConfessionCondition(confId);
	
	// FETCH confession
	if(confession == null) {
	    confession = ConfessionManager.getOneConfession(confId, false, null);
	}

	long pardonByUserId = 0;

	if(pardonConditions != null && !pardonConditions.isEmpty()) {

	    // Set total number of conditions
	    totalConditions = pardonConditions.size();

	    for (PardonCondition pardonCondition : pardonConditions) {
		if(pardonCondition != null) {
		    // Get pardon by user id from condition
		    pardonByUserId = pardonCondition.getUserId();

		    if(Constants.pardonConditionUnhide.equals(pardonCondition.getCondition())) {
			if(confession != null && !confession.isShareAsAnyn()) {
			    // Set to condition FULFILL
			    ConfessionBasicDAO.updateConfessionCondition(confId, Constants.pardonConditionUnhide, true);
			    conditionsMet++;
			} else {
			    // Set to condition NOT MET yet
			    ConfessionBasicDAO.updateConfessionCondition(confId, Constants.pardonConditionUnhide, false);
			}
		    } else {
			if(pardonCondition.isFulfil()) {
			    conditionsMet++;
			} else {
			    if(updatedActivityCount == null) {
				updatedActivityCount = ConfessionOtherDAO.getActivityCount(confId, Activity.SHOULD_BE_PARDONED);
			    }
			    // activity votes > = condition count
			    if(updatedActivityCount != null && updatedActivityCount >= pardonCondition.getCount()) {
				// Set to condition FULFILL
				ConfessionBasicDAO.updateConfessionCondition(confId, pardonCondition.getCondition(), true);
				conditionsMet++;
			    }
			}
		    }
		}
	    }

	    UserInfo pardonByUser = UserManager.getUserByUserId(pardonByUserId);
	    UserInfo pardonedToUser = UserManager.getUserByUserId(confession.getUserId());

	    if(totalConditions == conditionsMet) {
		// Update pardon share with new status
		boolean isChange = ConfessionBasicDAO.updateConfessionSharePardonCondition(pardonByUser.getUserId(), confId, pardonConditions, PardonStatus.PARDONED);
		if(isChange) {
		    // Float required emails
		    informPardonEvent(pardonByUser, pardonedToUser, confId);
		    ConfessionBasicDAO.updateConfessionTimeStamp(confId, updateTimeStamp);
		}
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