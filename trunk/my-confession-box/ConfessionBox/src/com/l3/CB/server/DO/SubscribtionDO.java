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
public class SubscribtionDO implements Serializable {

    /**
     * Default serial version ID
     */
    private static final long serialVersionUID = 1L;

    @Id
    private Long subfId;

    private Long userId;

    @Load
    private Ref<UserDO> refUser;

    private Long confId;

    @Load
    private Ref<ConfessionDO> refConfession;

    private boolean isSubscribed;

    private Date timeStamp;

    private SubscribtionDO() {
	super();
    }

    public SubscribtionDO(Long userId, Long confId, boolean isSubscribed) {
	super();
	this.setUserId(userId);
	this.setConfId(confId);
	this.isSubscribed = isSubscribed;
    }
    
    public Long getSubfId() {
	return subfId;
    }

    public void setSubfId(Long subfId) {
	this.subfId = subfId;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(long userId) {
	this.userId = userId;
	this.refUser = UserDAO.getUserRef(userId);
    }

    public Long getConfId() {
	return confId;
    }

    public void setConfId(long confId) {
	this.confId = confId;
	this.refConfession = ConfessionBasicDAO.getConfessionRef(confId);
    }

    public boolean isSubscribed() {
	return isSubscribed;
    }

    public void setSubscribed(boolean isSubscribed) {
	this.isSubscribed = isSubscribed;
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
	return "SubscribtionDO [subfId=" + subfId + ", userId=" + userId
		+ ", confId=" + confId + ", isSubscribed=" + isSubscribed
		+ ", timeStamp=" + timeStamp + "]";
    }
}