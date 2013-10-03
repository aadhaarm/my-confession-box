package com.l3.CB.server.DO;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class SubscribtionDO implements Serializable {

    /**
     * Default serial version ID
     */
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long subfId;

    @Persistent
    private Long userId;

    @Persistent
    private Long confId;

    @Persistent
    private boolean isSubscribed;

    @Persistent
    private Date timeStamp;

    public Long getSubfId() {
	return subfId;
    }

    public void setSubfId(Long subfId) {
	this.subfId = subfId;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Long getConfId() {
	return confId;
    }

    public void setConfId(Long confId) {
	this.confId = confId;
    }

    public boolean isSubscribed() {
	return isSubscribed;
    }

    public void setSubscribed(boolean isSubscribed) {
	this.isSubscribed = isSubscribed;
    }

    public SubscribtionDO(Long userId, Long confId, boolean isSubscribed) {
	super();
	this.userId = userId;
	this.confId = confId;
	this.isSubscribed = isSubscribed;
    }

    public Date getTimeStamp() {
	return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
	this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
	return "SubscribtionDO [subfId=" + subfId + ", userId=" + userId
		+ ", confId=" + confId + ", isSubscribed=" + isSubscribed
		+ ", timeStamp=" + timeStamp + "]";
    }
}