package com.l3.CB.shared.TO;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long commId;

    private Long confId;

    private Date timeStamp;

    private Long userId;

    private String fbId;
    
    private boolean shareAsAnyn = true;
    
    private String userIp;

    private String locale;

    private long numOfAbuseVote;

    private String gender;

    private String userDetailsJSON;
    
    private String comment;
    
    private long numOfSecond;
    
    public Long getCommId() {
	return commId;
    }

    public void setCommId(Long commId) {
	this.commId = commId;
    }

    public Date getTimeStamp() {
	return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
	this.timeStamp = timeStamp;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public boolean isShareAsAnyn() {
	return shareAsAnyn;
    }

    public void setShareAsAnyn(boolean shareAsAnyn) {
	this.shareAsAnyn = shareAsAnyn;
    }

    public String getUserIp() {
	return userIp;
    }

    public void setUserIp(String userIp) {
	this.userIp = userIp;
    }

    public String getLocale() {
	return locale;
    }

    public void setLocale(String locale) {
	this.locale = locale;
    }

    public long getNumOfAbuseVote() {
	return numOfAbuseVote;
    }

    public void setNumOfAbuseVote(long numOfAbuseVote) {
	this.numOfAbuseVote = numOfAbuseVote;
    }

    public String getGender() {
	return gender;
    }

    public void setGender(String gender) {
	this.gender = gender;
    }

    public Long getConfId() {
	return confId;
    }

    public void setConfId(Long confId) {
	this.confId = confId;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserDetailsJSON() {
        return userDetailsJSON;
    }

    public void setUserDetailsJSON(String userDetailsJSON) {
        this.userDetailsJSON = userDetailsJSON;
    }

    public long getNumOfSecond() {
        return numOfSecond;
    }

    public void setNumOfSecond(long numOfSecond) {
        this.numOfSecond = numOfSecond;
    }
}
