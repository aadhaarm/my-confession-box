package com.l3.CB.server;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.l3.CB.client.ConfessionService;
import com.l3.CB.server.DAO.ConfessionBasicDAO;
import com.l3.CB.server.DAO.ConfessionOtherDAO;
import com.l3.CB.server.utils.ServerUtils;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;
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
		return ConfessionBasicDAO.registerConfession(confession);
	}

	@Override
	public List<Confession> getConfessions(int page) {
		List<Confession> confessions = ConfessionBasicDAO.getConfessions();
		for (Confession confession : confessions) {
			String userDetailsJSON = ServerUtils.fetchURL(FacebookUtil.getUserByIdUrl(confession.getFbId()));
			confession.setUserDetailsJSON(userDetailsJSON);
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
}
