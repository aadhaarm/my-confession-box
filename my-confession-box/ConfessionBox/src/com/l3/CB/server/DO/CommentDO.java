package com.l3.CB.server.DO;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.l3.CB.shared.TO.Comment;

@Entity
@Cache
public class CommentDO {

    @Id
    @Index
    private Long commId;

    @Index
    private Long userId;
    
    @Index
    private Long confId;

    String comment;
    
    @Index
    private Date timeStamp;

    @Index
    private String fbId;
    
    private boolean shareAsAnyn = true;

    private String userIp;

    private String locale;

    @Index
    private long numOfAbuseVote;

    @Index
    private long numOfSecond;
    
    @Index
    private String gender;
    
    public CommentDO() {
	super();
    }

    public CommentDO(Comment comment) {
	setConfId(comment.getConfId());
	setCommId(comment.getCommId());
	setTimeStamp(new Date());

	setFbId(comment.getFbId());
	setComment(comment.getComment());
	setGender(comment.getGender());
	setLocale(comment.getLocale());
	setShareAsAnyn(comment.isShareAsAnyn());
	setUserId(comment.getUserId());
	setUserIp(comment.getUserIp());
	setNumOfAbuseVote(comment.getNumOfAbuseVote());
	setNumOfSecond(comment.getNumOfSecond());
    }

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }
    
    public long getNumOfSecond() {
        return numOfSecond;
    }

    public void setNumOfSecond(long numOfSecond) {
        this.numOfSecond = numOfSecond;
    }

    public Comment toCommentTO() {
	Comment comment  = new Comment();
	
	comment.setCommId(commId);
	comment.setConfId(confId);
	comment.setGender(gender);
	comment.setLocale(locale);
	comment.setNumOfAbuseVote(numOfAbuseVote);
	comment.setNumOfSecond(numOfSecond);
	comment.setTimeStamp(timeStamp);
	comment.setComment(getComment());
	comment.setFbId(fbId);
	
	comment.setShareAsAnyn(shareAsAnyn);
	if(!shareAsAnyn) {
	    comment.setUserId(userId);
	    comment.setUserIp(userIp);
	}
	
	return comment;
    }

    public void incNumOfAbuseVotes() {
	this.numOfAbuseVote = ++numOfAbuseVote;
    }

    public void incNumOfSecondVotes() {
	this.numOfSecond = ++numOfSecond;
    }
}
