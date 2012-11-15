package com.l3.CB.server.manager;

import com.l3.CB.server.DAO.UserDAO;
import com.l3.CB.server.DO.UserDO;
import com.l3.CB.shared.TO.UserInfo;

public class UserManager {

    public static UserInfo registerUser(UserInfo userInfo) {
	return UserDAO.registerUser(userInfo);
    }

    /**
     * Get User already registered in CB
     * @param userId
     * @return {@link UserInfo}
     */
    public static UserInfo getUserByUserId(Long userId) {
	return getUserInfo(UserDAO.getUserByUserId(new UserInfo(userId)));
    }

    /**
     * Get User already registered in CB
     * @param userId
     * @return {@link UserInfo}
     */
    public static UserInfo getUserByFbId(UserInfo userInfo) {
	return getUserInfo(UserDAO.getUserByFBId(userInfo));
    }

    /**
     * Get User already registered in CB
     * @param userId
     * @return {@link UserInfo}
     */
    public static boolean validateUser(Long userId, String fbId) {
	return UserDAO.validateUser(userId, fbId);
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
	    userInfo.setHumanPoints(userDO.getHumanPoints());
	}
	return userInfo;
    }

    public static int updateHumanPoints(Long userId, int points) {
	return UserDAO.updateHumanPoints(userId, points, true);
    }

    public static int getHumanPoints(Long userId) {
	return UserDAO.updateHumanPoints(userId, 0, false);
    }

    public static UserInfo isUserRegistered(String fbId) {
	if(fbId != null && !fbId.isEmpty()){
	    UserInfo user = new UserInfo();
	    user.setId(fbId);
	    return getUserInfo(UserDAO.getUserByFBId(user));
	}
	return null;
    }
}