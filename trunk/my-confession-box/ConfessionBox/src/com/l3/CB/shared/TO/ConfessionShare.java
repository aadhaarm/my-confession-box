package com.l3.CB.shared.TO;

import java.io.Serializable;
import java.util.Date;

public class ConfessionShare implements Serializable {

	/**
	 * Default serial ID
	 */
	private static final long serialVersionUID = 1L;

	private Long shareId;
	
	private Long confId;
	
	private Long userId;
	
	private String fbId;
	
	private boolean pardon;
	
	private Date timeStamp;
	
	private String userFullName;
	
	private String username;

	public ConfessionShare() {
		super();
	}

	public Long getShareId() {
		return shareId;
	}

	public void setShareId(Long shareId) {
		this.shareId = shareId;
	}

	public Long getConfId() {
		return confId;
	}

	public void setConfId(Long confId) {
		this.confId = confId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isPardon() {
		return pardon;
	}

	public void setPardon(boolean pardon) {
		this.pardon = pardon;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
