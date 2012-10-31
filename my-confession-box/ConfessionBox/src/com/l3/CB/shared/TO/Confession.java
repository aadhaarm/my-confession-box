package com.l3.CB.shared.TO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.Persistent;

public class Confession implements Serializable {

	/**
	 * Default serial ID
	 */
	private static final long serialVersionUID = 1L;

	private Long confId;

	private String confessionTitle;
	
	private String confession;

	private Date timeStamp;

	private Long userId;
	
	private String locale;
	
	private String userIp;
	
	private boolean shareAsAnyn = true;
	
	private String gender;
	
	private String fbId;
	
	private String userDetailsJSON;
	
	private Map<String, Long> activityCount;

	private List<ConfessionShare> confessedTo;
	
	private String userFullName;
	
	private String userEmailAddress;
	
	private String username;

	private boolean isVisibleOnPublicWall = true;
	
	private long numOfSameBoatVote;
	
	private long numOfSympathyVote;
	
	private long numOfLameVote;
	
	private long numOfShouldBePardonedVote;
	
	private long numOfAbuseVote;
	
	private long numOfShouldNotBePardonedVote;
	
	public Confession() {
		super();
	}

	public Confession(String confession, boolean shareAsAnyn) {
		super();
		this.confession = confession;
		this.shareAsAnyn = shareAsAnyn;
	}
	
	public Confession(Long confId, String confessionTitle, String confession, Date timeStamp,
			Long userId, boolean shareAsAnyn) {
		super();
		this.confId = confId;
		this.confession = confession;
		this.timeStamp = timeStamp;
		this.userId = userId;
		this.shareAsAnyn = shareAsAnyn;
		this.confessionTitle = confessionTitle;
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

	public String getConfessionTitle() {
		return confessionTitle;
	}

	public void setConfessionTitle(String confessionTitle) {
		this.confessionTitle = confessionTitle;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
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

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public boolean isVisibleOnPublicWall() {
		return isVisibleOnPublicWall;
	}

	public void setVisibleOnPublicWall(boolean isVisibleOnPublicWall) {
		this.isVisibleOnPublicWall = isVisibleOnPublicWall;
	}

	public long getNumOfSameBoatVote() {
		return numOfSameBoatVote;
	}

	public void setNumOfSameBoatVote(long numOfSameBoatVote) {
		this.numOfSameBoatVote = numOfSameBoatVote;
	}

	public long getNumOfSympathyVote() {
		return numOfSympathyVote;
	}

	public void setNumOfSympathyVote(long numOfSympathyVote) {
		this.numOfSympathyVote = numOfSympathyVote;
	}

	public long getNumOfLameVote() {
		return numOfLameVote;
	}

	public void setNumOfLameVote(long numOfLameVote) {
		this.numOfLameVote = numOfLameVote;
	}

	public long getNumOfShouldBePardonedVote() {
		return numOfShouldBePardonedVote;
	}

	public void setNumOfShouldBePardonedVote(long numOfShouldBePardonedVote) {
		this.numOfShouldBePardonedVote = numOfShouldBePardonedVote;
	}

	public long getNumOfAbuseVote() {
		return numOfAbuseVote;
	}

	public void setNumOfAbuseVote(long numOfAbuseVote) {
		this.numOfAbuseVote = numOfAbuseVote;
	}

	public long getNumOfShouldNotBePardonedVote() {
		return numOfShouldNotBePardonedVote;
	}

	public void setNumOfShouldNotBePardonedVote(long numOfShouldNotBePardonedVote) {
		this.numOfShouldNotBePardonedVote = numOfShouldNotBePardonedVote;
	}

	public String getUserEmailAddress() {
		return userEmailAddress;
	}

	public void setUserEmailAddress(String userEmailAddress) {
		this.userEmailAddress = userEmailAddress;
	}
}
