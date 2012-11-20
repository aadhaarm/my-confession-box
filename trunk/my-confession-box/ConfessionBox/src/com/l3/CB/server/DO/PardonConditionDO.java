package com.l3.CB.server.DO;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;


@PersistenceCapable
public class PardonConditionDO implements Serializable {

    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key pardonConditionId;

    @Persistent
    private Long confId;

    @Persistent
    private Long userId;

    @Persistent
    private boolean isFulfil;

    @Persistent
    private String condition;

    @Persistent
    private int count;

    public int getCount() {
	return count;
    }

    public void setCount(int count) {
	this.count = count;
    }

    public String getCondition() {
	return condition;
    }

    public void setCondition(String condition) {
	this.condition = condition;
    }

    public PardonConditionDO(String condition) {
	super();
	isFulfil = false;
	this.condition = condition;
    }

    public Key getPardonConditionId() {
	return pardonConditionId;
    }

    public void setPardonConditionId(Key pardonConditionId) {
	this.pardonConditionId = pardonConditionId;
    }

    public boolean isFulfil() {
	return isFulfil;
    }

    public void setFulfil(boolean isFulfil) {
	this.isFulfil = isFulfil;
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

    @Override
    public String toString() {
	return "PardonConditionDO [pardonConditionId=" + pardonConditionId
		+ ", confId=" + confId + ", userId=" + userId + ", isFulfil="
		+ isFulfil + ", condition=" + condition + ", count=" + count
		+ "]";
    }
}