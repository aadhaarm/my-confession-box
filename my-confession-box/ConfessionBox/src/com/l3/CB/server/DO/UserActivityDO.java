package com.l3.CB.server.DO;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.l3.CB.server.DAO.ConfessionBasicDAO;
import com.l3.CB.server.DAO.UserDAO;

@Entity
@Cache
@Index
public class UserActivityDO implements Serializable {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 1L;

    public UserActivityDO() {
	super();
    }

    public UserActivityDO(long userId, long confId, String activityType,
	    boolean shareAsAnyn, Date timeStamp) {
	super();
	this.setUserId(userId);
	this.setConfId(confId);
	this.activityType = activityType;
	this.shareAsAnyn = shareAsAnyn;
	this.timeStamp = timeStamp;
    }

    @Id
    private Long activityId;

    private Long userId;

    @Load
    private Ref<UserDO> refUser;

    private Long confId;

    @Load
    private Ref<ConfessionDO> refConfession;

    private String activityType;

    private boolean shareAsAnyn = true;

    private Date timeStamp;

    public Long getActivityId() {
	return activityId;
    }

    public void setActivityId(Long activityId) {
	this.activityId = activityId;
    }

    public long getUserId() {
	return userId;
    }

    public void setUserId(long userId) {
	this.userId = userId;
	this.refUser = UserDAO.getUserRef(userId);
    }

    public long getConfId() {
	return confId;
    }

    public void setConfId(long confId) {
	this.confId = confId;
	this.refConfession = ConfessionBasicDAO.getConfessionRef(confId);
    }

    public String getActivityType() {
	return activityType;
    }

    public void setActivityType(String activityType) {
	this.activityType = activityType;
    }

    public boolean isShareAsAnyn() {
	return shareAsAnyn;
    }

    public void setShareAsAnyn(boolean shareAsAnyn) {
	this.shareAsAnyn = shareAsAnyn;
    }

    public Date getTimeStamp() {
	return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
	this.timeStamp = timeStamp;
    }

    public Ref<UserDO> getRefUser() {
        return refUser;
    }

    public void setRefUser(Ref<UserDO> refUser) {
        this.refUser = refUser;
    }

    public Ref<ConfessionDO> getRefConfession() {
        return refConfession;
    }

    public void setRefConfession(Ref<ConfessionDO> refConfession) {
        this.refConfession = refConfession;
    }

    @Override
    public String toString() {
	return "UserActivityDO [activityId=" + activityId + ", userId="
		+ userId + ", confId=" + confId + ", activityType="
		+ activityType + ", shareAsAnyn=" + shareAsAnyn
		+ ", timeStamp=" + timeStamp + "]";
    }
}
