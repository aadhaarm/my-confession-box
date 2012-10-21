package com.l3.CB.server.manager;

import java.util.List;

import com.l3.CB.server.DAO.ConfessionBasicDAO;
import com.l3.CB.server.DAO.ConfessionOtherDAO;
import com.l3.CB.server.utils.ServerUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.Filters;
import com.l3.CB.shared.TO.UserInfo;

public class ConfessionManager {
	/**
	 * @param confession
	 * @return
	 */
	public static Confession registerConfession(Confession confession, String ipAddress) {
		confession.setUserIp(ipAddress);
		confession = ConfessionBasicDAO.registerConfession(confession);
		
		//Register confession Shared
		if(confession.getConfessedTo() != null) {
			for (ConfessionShare confessionShare : confession.getConfessedTo()) {

				UserInfo userSharedTo = new UserInfo();
				userSharedTo.setId(confessionShare.getFbId());
				userSharedTo = UserManager.registerUser(userSharedTo);
				
				confessionShare.setUserId(userSharedTo.getUserId());
				ConfessionBasicDAO.registerConfessionShare(confessionShare, confession.getConfId());
			
				MailManager.sendConfessionEmail(confessionShare.getUsername()+"@facebook.com", confession.getConfId(), confession.getUserFullName(), confessionShare.getUserFullName());
			}
		}
		return confession;
	}

	/**
	 * @param page
	 * @param filter 
	 * @param locale 
	 * @return
	 */
	public static List<Confession> getConfessionsByFilter(int page, Filters filter, String locale) {
		List<Confession> confessions = ConfessionBasicDAO.getConfessions(page, Constants.FEED_PAGE_SIZE, filter, locale);
		if(confessions != null && !confessions.isEmpty()) {
			for (Confession confession : confessions) {
				if(!confession.isShareAsAnyn()) {
					String userDetailsJSON = ServerUtils.fetchURL(FacebookUtil.getUserByIdUrl(confession.getFbId()));
					confession.setUserDetailsJSON(userDetailsJSON);
				} else {
					// MOST IMPORTANT CODE!!
					confession.setFbId(null);
				}
				
				confession.setConfessedTo(ConfessionBasicDAO.getConfessionShare(confession.getConfId(), false));
			}
		}
		return confessions;
	}
	
	/**
	 * @param page
	 * @param userId
	 * @return
	 */
	public static List<Confession> getConfessionsByUser(int page, Long userId) {
		List<Confession> confessions = ConfessionBasicDAO.getConfessions(userId, page, Constants.FEED_PAGE_SIZE);
		if(confessions != null && !confessions.isEmpty()) {
			for (Confession confession : confessions) {
				if(!confession.isShareAsAnyn()) {
					String userDetailsJSON = ServerUtils.fetchURL(FacebookUtil.getUserByIdUrl(confession.getFbId()));
					confession.setUserDetailsJSON(userDetailsJSON);
				} else {
					// MOST IMPORTANT CODE!!
					confession.setFbId(null);
				}
				confession.setConfessedTo(ConfessionBasicDAO.getConfessionShare(confession.getConfId(), false));
			}
		}
		return confessions;
	}
	
	/**
	 * @param confId
	 * @return
	 */
	public static Confession getOneConfession(Long confId) {
		Confession confession = ConfessionBasicDAO.getConfession(confId);
		confession.setConfessedTo(ConfessionBasicDAO.getConfessionShare(confession.getConfId(), false));
		if(!confession.isShareAsAnyn()) {
			String userDetailsJSON = ServerUtils.fetchURL(FacebookUtil.getUserByIdUrl(confession.getFbId()));
			confession.setUserDetailsJSON(userDetailsJSON);
		} else {
			// MOST IMPORTANT CODE!!
			confession.setFbId(null);
		}
		return confession;
	}

	/**
	 * @param page
	 * @param userId
	 * @return
	 */
	public static List<Confession> getConfessionsConfessedToMe(int page, Long userId) {
		//TODO: Validate if user is correct
		List<Confession> confessions = ConfessionBasicDAO.getConfessionsForMe(userId, page, Constants.FEED_PAGE_SIZE);
		if(confessions != null && !confessions.isEmpty()) {
			for (Confession confession : confessions) {
				String userDetailsJSON = ServerUtils.fetchURL(FacebookUtil.getUserByIdUrl(confession.getFbId()));
				confession.setUserDetailsJSON(userDetailsJSON);
				confession.setConfessedTo(ConfessionBasicDAO.getConfessionShare(confession.getConfId(), true));
			}
		}
		return confessions;
	}

	public static boolean changeIdentityVisibility(Long userId, String fbId, Long confId, boolean shareAnyn) {
		//TODO: validate user
		boolean result = ConfessionOtherDAO.changeVisibility(userId, confId, shareAnyn);
		if(result) {
			PardonManager.changeVisibility(confId, shareAnyn);
		}
		return result; 
	}

	public static boolean changeConfessionVisibility(Long userId, String fbId, Long confId, boolean isVisible) {
		// TODO Validate user
		return ConfessionBasicDAO.updateConfessionVisibility(confId, userId, isVisible);
	}
}
