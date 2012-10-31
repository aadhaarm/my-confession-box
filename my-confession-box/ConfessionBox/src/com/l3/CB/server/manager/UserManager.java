package com.l3.CB.server.manager;

import com.l3.CB.server.DAO.UserDAO;
import com.l3.CB.server.DO.UserDO;
import com.l3.CB.shared.TO.UserInfo;

public class UserManager {

	public static UserInfo registerUser(UserInfo userInfo) {
		return UserDAO.registerUser(userInfo);
	}

	public static UserInfo getUserByUserId(Long userId) {
		return getUserInfo(UserDAO.getUserByUserId(new UserInfo(userId)));
	}

	private static UserInfo getUserInfo(UserDO userDO) {
		UserInfo userInfo = null;
		if(userDO != null) {
			userInfo = new UserInfo();
			userInfo.setUserId(userDO.getUserId());
			userInfo.setId(userDO.getFbId());
			userInfo.setGender(userDO.getGender());
			userInfo.setName(userDO.getName());
			userInfo.setUsername(userDO.getUserName());
			userInfo.setEmail(userDO.getEmail());
		}
		return userInfo;
	}

	public static boolean validateUserndRequest() {
		return false;
	}
}

