package com.l3.CB.server;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;
import com.l3.CB.client.ConfessionService;
import com.l3.CB.server.DAO.UserDAO;
import com.l3.CB.server.manager.ActivityManager;
import com.l3.CB.server.manager.ConfessionManager;
import com.l3.CB.server.manager.PardonManager;
import com.l3.CB.server.manager.UserManager;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.Filters;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.UserInfo;
/**
 * The server side implementation of the RPC service.
 */
public class ConfessionServiceImpl extends XsrfProtectedServiceServlet implements
		ConfessionService {

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public UserInfo registerUser(UserInfo userInfo) {
		return UserManager.registerUser(userInfo);
	}

	/**
	 * Register confession.
	 * @param confession - {@link Confession}
	 */
	@Override
	public Confession registerConfession(Confession confession) {
      	return ConfessionManager.registerConfession(confession, getThreadLocalRequest().getRemoteHost().toString());
	}

	/**
	 * Get confessions.
	 * @param page - {@link Integer}
	 */
	@Override
	public List<Confession> getConfessions(int page, Filters filter, String locale, Long userId) {
		return ConfessionManager.getConfessionsByFilter(page, filter, locale, userId);
	}

	/**
	 * Get confessions
	 * @param oage - {@link Integer}
	 * @param userId - {@link Long}
	 */
	@Override
	public List<Confession> getConfessionsIDID(int page, Long userId) {
		List<Confession> confessions = ConfessionManager.getConfessionsByUser(page, userId);
		return confessions;
	}

	@Override
	public Long registerUserActivity(Long userId, Long confId, Activity activity) {
		return ActivityManager.registerUserActivity(userId, confId, activity);
	}

	@Override
	public Map<String, Long> getUserActivity(Long userId, Long confId) {
		return ActivityManager.getUserActivity(userId, confId);
	}

	/**
	 * Get confessions
	 * @param confId - {@link Long}
	 */
	@Override
	public Confession getConfession(Long confId) {
		return ConfessionManager.getOneConfession(confId);
	}

	public List<Confession> getConfessionsTOME(int page, Long userId) {
		return ConfessionManager.getConfessionsConfessedToMe(page, userId);
	}

	@Override
	public void pardonConfession(UserInfo pandonByUser, Long confId, UserInfo pardonedToUser, List<PardonCondition> pardonConditions) {
		PardonManager.pardonWithConditions(pandonByUser, confId, pardonedToUser, pardonConditions);
	}

	public boolean changeIdentityVisibility(Long userId, String fbId, Long confId, boolean shareAnyn) {
		return ConfessionManager.changeIdentityVisibility(userId, fbId, confId, shareAnyn);
	}

	@Override
	public boolean changeConfessionVisibility(Long userId, String fbId,
			Long confId, boolean isVisible) {
		// TODO Validate user
		return ConfessionManager.changeConfessionVisibility(userId, fbId, confId, isVisible);
	}

	@Override
	public long getMyConfessionNumber(Long userId) {
		return ConfessionManager.getConfessionsByUserCount(userId);
	}

	@Override
	public long getNumberOfConfessionForMe(Long userId) {
		return ConfessionManager.getConfessionsForMeCount(userId);
	}

	@Override
	public boolean subscribe(Long confId, Long userId) {
		return ConfessionManager.changeSubscribtionStatus(confId, userId);
	}

	@Override
	public boolean isSubscribed(Long confId, Long userId) {
		return ConfessionManager.isSubscribed(confId, userId);
	}

	@Override
	public int updateHumanPoints(Long userId, int points) {
		return UserManager.updateHumanPoints(userId, points);
	}

	@Override
	public int getHumanPoints(Long userId) {
		return UserManager.getHumanPoints(userId);
	}
}
