package com.l3.CB.server.DO;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.l3.CB.server.DAO.UserDAO;
import com.l3.CB.shared.TO.Confession;

@Entity
@Cache
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
	numOfTotalVote = new Long(0);
    }

    public ConfessionDO(Confession confession) {
	if (confession != null) {
	    this.setConfId(confession.getConfId());
	    this.setUserId(confession.getUserId());
	    this.setShareAsAnyn(confession.isShareAsAnyn());
	    this.setOnlyDedicate(confession.isOnlyDedicate());
	    this.setConfessionTitle(confession.getConfessionTitle());
	    this.setConfession(new Text(confession.getConfession()));
	    this.setTimeStamp(confession.getTimeStamp());
	    this.setLastUpdateTimeStamp(confession.getTimeStamp());
	    this.setUserIp(confession.getUserIp());
	    this.setLocale(confession.getLocale());
	}
    }

    @Id
    @Index
    private Long confId;

    private Text confession;

    @Index
    private Date timeStamp;

    @Index
    private Date lastUpdateTimeStamp;

    @Index
    private Long userId;

    @Load
    @Index
    private Ref<UserDO> refUser;

    @Index
    private boolean shareAsAnyn = true;

    @Index
    private boolean onlyDedicate;
    
    @Index
    private String userIp;

    @Index
    private String locale;

    @Index
    private String confessionTitle;

    @Index
    private boolean isVisibleOnPublicWall = true;

    @Index
    private long numOfSameBoatVote;

    @Index
    private long numOfSympathyVote;

    @Index
    private long numOfLameVote;

    @Index
    private long numOfShouldBePardonedVote;

    @Index
    private long numOfAbuseVote;

    @Index
    private long numOfShouldNotBePardonedVote;

    @Index
    private Long numOfTotalVote;

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

    public void setUserId(long userId) {
	this.userId = userId;
	this.refUser = UserDAO.getUserRef(userId);
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

    public void incrementTotalVote() {
	if(numOfTotalVote == null) {
	    numOfTotalVote = new Long(0);
	}
	numOfTotalVote++;
    }

    public long incrementAbuseVote() {
	numOfAbuseVote++;
	incrementTotalVote();
	return numOfAbuseVote;
    }

    public long incrementSameBoatVote() {
	numOfSameBoatVote++;
	incrementTotalVote();
	return numOfSameBoatVote;
    }

    public long incrementShouldBePardonedVote() {
	numOfShouldBePardonedVote++;
	incrementTotalVote();
	return numOfShouldBePardonedVote;
    }

    public long incrementShouldNotBePardonedVote() {
	numOfShouldNotBePardonedVote++;
	incrementTotalVote();
	return numOfShouldNotBePardonedVote;
    }

    public long incrementLameVote() {
	numOfLameVote++;
	incrementTotalVote();
	return numOfLameVote;
    }

    public long incrementSympathyVote() {
	numOfSympathyVote++;
	incrementTotalVote();
	return numOfSympathyVote;
    }

    public Date getLastUpdateTimeStamp() {
	return lastUpdateTimeStamp;
    }

    public void setLastUpdateTimeStamp(Date lastUpdateTimeStamp) {
	this.lastUpdateTimeStamp = lastUpdateTimeStamp;
    }

    public long getNumOfTotalVote() {
	return numOfTotalVote;
    }

    public void setNumOfTotalVote(long numOfTotalVote) {
	this.numOfTotalVote = numOfTotalVote;
    }

    public Ref<UserDO> getRefUser() {
	return refUser;
    }

    public void setRefUser(Ref<UserDO> refUser) {
	this.refUser = refUser;
    }

    public void setNumOfTotalVote(Long numOfTotalVote) {
	this.numOfTotalVote = numOfTotalVote;
    }
    
    public boolean isOnlyDedicate() {
        return onlyDedicate;
    }

    public void setOnlyDedicate(boolean onlyDedicate) {
        this.onlyDedicate = onlyDedicate;
    }

    public Confession toConfessionTO() {

	Confession confession = new Confession(this.getConfId(), this.getConfessionTitle(),
		this.getConfession().getValue(), this.getTimeStamp(),
		this.getUserId(), this.isShareAsAnyn());

	confession.setVisibleOnPublicWall(this.isVisibleOnPublicWall());
	confession.setNumOfAbuseVote(this.getNumOfAbuseVote());
	confession.setNumOfLameVote(this.getNumOfLameVote());
	confession.setNumOfSameBoatVote(this.getNumOfSameBoatVote());
	confession.setNumOfShouldBePardonedVote(this.getNumOfShouldBePardonedVote());
	confession.setNumOfShouldNotBePardonedVote(this.getNumOfShouldNotBePardonedVote());
	confession.setNumOfSympathyVote(this.getNumOfSympathyVote());
	confession.setUpdateTimeStamp(this.getLastUpdateTimeStamp());
	confession.setOnlyDedicate(this.isOnlyDedicate());

	
	if(this.refUser != null) {
	    confession.setFbId(refUser.get().getFbId());
	    confession.setGender(refUser.get().getGender());
	}
	return confession;
    }

}