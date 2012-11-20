package com.l3.CB.server.DO;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class UserDO implements Serializable {

    /**
     * Default serial ID
     */
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long userId;

    @Persistent
    private String fbId;

    @Persistent
    private String gender;

    @Persistent
    private String name;

    @Persistent
    private String userName;

    @Persistent
    private String email;

    @Persistent
    private int humanPoints = 0;

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public String getFbId() {
	return fbId;
    }

    public void setFbId(String fbId) {
	this.fbId = fbId;
    }

    public UserDO(String fbId) {
	super();
	this.fbId = fbId;
    }

    public UserDO(String fbId, String gender) {
	super();
	this.fbId = fbId;
	this.gender = gender;
    }

    public String getGender() {
	return gender;
    }

    public void setGender(String gender) {
	this.gender = gender;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public int getHumanPoints() {
	return humanPoints;
    }

    public void setHumanPoints(int humanPoints) {
	this.humanPoints = humanPoints;
    }

    @Override
    public String toString() {
	return "UserDO [userId=" + userId + ", fbId=" + fbId + ", gender="
		+ gender + ", name=" + name + ", userName=" + userName
		+ ", email=" + email + ", humanPoints=" + humanPoints + "]";
    }
}
