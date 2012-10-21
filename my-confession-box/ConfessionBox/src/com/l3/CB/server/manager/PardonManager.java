package com.l3.CB.server.manager;

import java.util.List;

import com.l3.CB.server.DAO.ConfessionBasicDAO;
import com.l3.CB.shared.Constants;
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
	public static void pardonWithConditions(UserInfo pandonByUser, Long confId,
			UserInfo pardonedToUser, List<PardonCondition> pardonConditions) {
		ConfessionBasicDAO.pardonConfession(pandonByUser.getUserId(), confId, pardonConditions);
		MailManager.sendPardonMail(pandonByUser, confId, pardonedToUser);
	}

	public static void changeVisibility(Long confId, boolean shareAnyn) {
		int totalConditions = 0;
		int conditionsMet = 0;
		PardonCondition unhidePardonCondition = null;
		List<PardonCondition> pardonConditions = ConfessionBasicDAO.getConfessionCondition(confId);

		if(pardonConditions != null && !pardonConditions.isEmpty()) {
			// Set total number of conditions
			totalConditions = pardonConditions.size();
			for (PardonCondition pardonCondition : pardonConditions) {
				if(pardonCondition.isFulfil() && !Constants.pardonConditionUnhide.equals(pardonCondition.getCondition())) {
					conditionsMet++;
				} else {
					if(Constants.pardonConditionUnhide.equals(pardonCondition.getCondition())) {
						unhidePardonCondition = pardonCondition;
						ConfessionBasicDAO.updateConfessionCondition(confId, Constants.pardonConditionUnhide, !shareAnyn);
						if(!shareAnyn) {
							conditionsMet++;
							unhidePardonCondition.setFulfil(true);
						}
					}
				}
			}

			if(totalConditions == conditionsMet) {
				Confession confession = ConfessionManager.getOneConfession(confId);
				UserInfo pardonByUser = UserManager.getUserByUserId(unhidePardonCondition.getUserId());
				UserInfo pardonedToUser = UserManager.getUserByUserId(confession.getUserId());

				PardonManager.pardonWithConditions(pardonByUser, confId, pardonedToUser, pardonConditions);
			}
		}
	}

	/**
	 * @param confId
	 * @param updatedActivityCount
	 */
	public static void activityCountChanged(Long confId,
			long updatedActivityCount) {
		int totalConditions = 0;
		int conditionsMet = 0;
		PardonCondition spVotesPardonCondition = null;


		List<PardonCondition> pardonConditions = ConfessionBasicDAO.getConfessionCondition(confId);

		if(pardonConditions != null && !pardonConditions.isEmpty()) {
			// Set total number of conditions
			totalConditions = pardonConditions.size();

			for (PardonCondition pardonCondition : pardonConditions) {
				if(pardonCondition.isFulfil() && Constants.pardonConditionUnhide.equals(pardonCondition.getCondition())) {
					conditionsMet++;
				} else {
					if(Constants.pardonConditionSPVotes.equals(pardonCondition.getCondition())) {
						spVotesPardonCondition = pardonCondition;
						if(updatedActivityCount >= pardonCondition.getCount()) {
							conditionsMet++;
							ConfessionBasicDAO.updateConfessionCondition(confId, Constants.pardonConditionSPVotes, true);
							spVotesPardonCondition.setFulfil(true);
						}
					}
				}
			}
			if(totalConditions ==  conditionsMet) {
				Confession confession = ConfessionManager.getOneConfession(confId);
				UserInfo pardonByUser = UserManager.getUserByUserId(spVotesPardonCondition.getUserId());
				UserInfo pardonedToUser = UserManager.getUserByUserId(confession.getUserId());

				PardonManager.pardonWithConditions(pardonByUser, confId, pardonedToUser, pardonConditions);
			}
		}
	}

}
