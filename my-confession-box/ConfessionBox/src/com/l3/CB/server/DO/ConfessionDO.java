package com.l3.CB.server.DO;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class ConfessionDO implements Serializable {

	/**
	 * Default serial ID
	 */
	private static final long serialVersionUID = 1L;
	
	public ConfessionDO() {
		super();
		numOfAbuseVote = 0;
		numOfLameVote = 0;
		numOfSameBoatVote = 0;
		numOfShouldBePardonedVote = 0;
		numOfShouldNotBePardonedVote = 0;
		numOfSympathyVote = 0;
	}

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long confId;
	
	@Persistent
	private Text confession;

	@Persistent
	private Date timeStamp;

	@Persistent
	private Long userId;
	
	@Persistent
	private boolean shareAsAnyn = true;
	
	@Persistent
	private String userIp;
	
	@Persistent
	private String locale;
	
	@Persistent
	private String confessionTitle;
	
	@Persistent
	private boolean isVisibleOnPublicWall = true;

	@Persistent
	private long numOfSameBoatVote;
	
	@Persistent
	private long numOfSympathyVote;
	
	@Persistent
	private long numOfLameVote;
	
	@Persistent
	private long numOfShouldBePardonedVote;
	
	@Persistent
	private long numOfAbuseVote;
	
	@Persistent
	private long numOfShouldNotBePardonedVote;
	
	public Long getConfId() {
		return confId;
	}

	public void setConfId(Long confId) {
		this.confId = confId;
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

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getConfessionTitle() {
		return confessionTitle;
	}

	public void setConfessionTitle(String confessionTitle) {
		this.confessionTitle = confessionTitle;
	}

	public Text getConfession() {
		return confession;
	}

	public void setConfession(Text confession) {
		this.confession = confession;
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

	public Long getNumOfSameBoatVote() {
		return numOfSameBoatVote;
	}

	public void setNumOfSameBoatVote(Long numOfSameBoatVote) {
		this.numOfSameBoatVote = numOfSameBoatVote;
	}

	public Long getNumOfSympathyVote() {
		return numOfSympathyVote;
	}

	public void setNumOfSympathyVote(Long numOfSympathyVote) {
		this.numOfSympathyVote = numOfSympathyVote;
	}

	public Long getNumOfLameVote() {
		return numOfLameVote;
	}

	public void setNumOfLameVote(Long numOfLameVote) {
		this.numOfLameVote = numOfLameVote;
	}

	public Long getNumOfShouldBePardonedVote() {
		return numOfShouldBePardonedVote;
	}

	public void setNumOfShouldBePardonedVote(Long numOfShouldBePardonedVote) {
		this.numOfShouldBePardonedVote = numOfShouldBePardonedVote;
	}

	public Long getNumOfAbuseVote() {
		return numOfAbuseVote;
	}

	public void setNumOfAbuseVote(Long numOfAbuseVote) {
		this.numOfAbuseVote = numOfAbuseVote;
	}

	public Long getNumOfShouldNotBePardonedVote() {
		return numOfShouldNotBePardonedVote;
	}

	public void setNumOfShouldNotBePardonedVote(Long numOfShouldNotBePardonedVote) {
		this.numOfShouldNotBePardonedVote = numOfShouldNotBePardonedVote;
	}

	public void setNumOfSameBoatVote(long numOfSameBoatVote) {
		this.numOfSameBoatVote = numOfSameBoatVote;
	}

	public void setNumOfSympathyVote(long numOfSympathyVote) {
		this.numOfSympathyVote = numOfSympathyVote;
	}

	public void setNumOfLameVote(long numOfLameVote) {
		this.numOfLameVote = numOfLameVote;
	}

	public void setNumOfShouldBePardonedVote(long numOfShouldBePardonedVote) {
		this.numOfShouldBePardonedVote = numOfShouldBePardonedVote;
	}

	public void setNumOfAbuseVote(long numOfAbuseVote) {
		this.numOfAbuseVote = numOfAbuseVote;
	}

	public void setNumOfShouldNotBePardonedVote(long numOfShouldNotBePardonedVote) {
		this.numOfShouldNotBePardonedVote = numOfShouldNotBePardonedVote;
	}

	public long incrementAbuseVote() {
		numOfAbuseVote++;
		return numOfAbuseVote;
	}

	public long incrementSameBoatVote() {
		numOfSameBoatVote++;
		return numOfSameBoatVote;
	}

	public long incrementShouldBePardonedVote() {
		numOfShouldBePardonedVote++;
		return numOfShouldBePardonedVote;
	}

	public long incrementShouldNotBePardonedVote() {
		numOfShouldNotBePardonedVote++;
		return numOfShouldNotBePardonedVote;
	}

	public long incrementLameVote() {
		numOfLameVote++;
		return numOfLameVote;
	}

	public long incrementSympathyVote() {
		numOfSympathyVote++;
		return numOfSympathyVote;
	}
}
