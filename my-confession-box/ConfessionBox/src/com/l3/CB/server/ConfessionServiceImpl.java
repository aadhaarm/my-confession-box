package com.l3.CB.server;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.l3.CB.client.ConfessionService;
import com.l3.CB.server.DAO.ConfessionBasicDAO;
import com.l3.CB.server.DAO.ConfessionOtherDAO;
import com.l3.CB.server.utils.ServerUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.UserInfo;

/**
 * The server side implementation of the RPC service.
 */
public class ConfessionServiceImpl extends RemoteServiceServlet implements
		ConfessionService {

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public UserInfo registerUser(UserInfo userInfo) {
		return ConfessionBasicDAO.registerUser(userInfo);
	}

	@Override
	public Confession registerConfession(Confession confession) {
		confession.setUserIp(getThreadLocalRequest().getRemoteHost());
		confession = ConfessionBasicDAO.registerConfession(confession);
		
		//Register confession Shared
		//TODO: Mail required ppl
		if(confession.getConfessedTo() != null) {
			for (ConfessionShare confessionShare : confession.getConfessedTo()) {

				UserInfo userSharedTo = new UserInfo();
				userSharedTo.setId(confessionShare.getFbId());
				userSharedTo = ConfessionBasicDAO.registerUser(userSharedTo);
				
				confessionShare.setUserId(userSharedTo.getUserId());
				ConfessionBasicDAO.registerConfessionShare(confessionShare, confession.getConfId());
			}
		}
		return confession;
	}

	/**
	 * Get confessions
	 * @param oage - {@link Integer}
	 */
	@Override
	public List<Confession> getConfessions(int page) {
		List<Confession> confessions = ConfessionBasicDAO.getConfessions(page, Constants.FEED_PAGE_SIZE);
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
	 * Get confessions
	 * @param oage - {@link Integer}
	 * @param userId - {@link Long}
	 */
	@Override
	public List<Confession> getConfessions(int page, Long userId) {
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
	
	@Override
	public Long userActivity(Long userId, Long confId, Activity activity) {
		return ConfessionOtherDAO.updateActivityCount(userId, confId, activity);
	}

	@Override
	public Map<String, Long> getUserActivity(Long userId, Long confId) {
		return ConfessionOtherDAO.getUserActivity(userId, confId);
	}

	/**
	 * Get confessions
	 * @param confId - {@link Long}
	 */
	@Override
	public Confession getConfession(Long confId) {
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

	public List<Confession> getConfessionsForMe(int page, Long userId) {
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

	@Override
	public void pardonConfession(Long userId, Long confId) {
		ConfessionBasicDAO.pardonConfession(userId, confId);
		//TODO: Mail required ppl
	}
}
