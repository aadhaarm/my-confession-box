package com.l3.CB.shared.TO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Confession implements Serializable {

	/**
	 * Default serial ID
	 */
	private static final long serialVersionUID = 1L;

	private Long confId;
	
	private String confession;

	private Date timeStamp;

	private Long userId;
	
	private boolean shareAsAnyn = true;
	
	private String gender;
	
	private String fbId;
	
	private String userDetailsJSON;
	
	private Map<String, Long> activityCount;

	private List<ConfessionShare> confessedTo;

	public Confession() {
		super();
	}

	public Confession(String confession, boolean shareAsAnyn) {
		super();
		this.confession = confession;
		this.shareAsAnyn = shareAsAnyn;
	}
	
	public Confession(Long confId, String confession, Date timeStamp,
			Long userId, boolean shareAsAnyn) {
		super();
		this.confId = confId;
		this.confession = confession;
		this.timeStamp = timeStamp;
		this.userId = userId;
		this.shareAsAnyn = shareAsAnyn;
	}

	public Confession(Long confId, String confession, Date timeStamp,
			Long userId, boolean shareAsAnyn, List<ConfessionShare> confessedTo) {
		super();
		this.confId = confId;
		this.confession = confession;
		this.timeStamp = timeStamp;
		this.userId = userId;
		this.shareAsAnyn = shareAsAnyn;
		this.confessedTo = confessedTo;
	}

	public Long getConfId() {
		return confId;
	}

	public void setConfId(Long confId) {
		this.confId = confId;
	}

	public String getConfession() {
		return confession;
	}

	public void setConfession(String confession) {
		this.confession = confession;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isShareAsAnyn() {
		return shareAsAnyn;
	}

	public void setShareAsAnyn(boolean shareAsAnyn) {
		this.shareAsAnyn = shareAsAnyn;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<ConfessionShare> getConfessedTo() {
		return confessedTo;
	}

	public void setConfessedTo(List<ConfessionShare> confessedTo) {
		this.confessedTo = confessedTo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getUserDetailsJSON() {
		return userDetailsJSON;
	}

	public void setUserDetailsJSON(String userDetailsJSON) {
		this.userDetailsJSON = userDetailsJSON;
	}

	public Map<String, Long> getActivityCount() {
		return activityCount;
	}

	public void setActivityCount(Map<String, Long> activityCount) {
		this.activityCount = activityCount;
	}
}
