package com.l3.CB.server.DO;

import java.io.Serializable;

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
public class PardonConditionDO implements Serializable {

    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    @Id
    private Long pardonConditionId;

    private Long confId;

    @Load
    private Ref<ConfessionDO> refConfession;

    private Long userId;

    @Load
    private Ref<UserDO> refUser;

    private boolean isFulfil;

    private String condition;

    private int count;

    private PardonConditionDO() {
	super();
    }

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

    public Long getPardonConditionId() {
        return pardonConditionId;
    }

    public void setPardonConditionId(Long pardonConditionId) {
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

    public void setConfId(long confId) {
	this.confId = confId;
	this.refConfession = ConfessionBasicDAO.getConfessionRef(confId);
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(long userId) {
	this.userId = userId;
	this.refUser = UserDAO.getUserRef(userId);
    }

    public Ref<ConfessionDO> getRefConfession() {
        return refConfession;
    }

    public void setRefConfession(Ref<ConfessionDO> refConfession) {
        this.refConfession = refConfession;
    }

    public Ref<UserDO> getRefUser() {
        return refUser;
    }

    public void setRefUser(Ref<UserDO> refUser) {
        this.refUser = refUser;
    }

    @Override
    public String toString() {
	return "PardonConditionDO [pardonConditionId=" + pardonConditionId
		+ ", confId=" + confId + ", userId=" + userId + ", isFulfil="
		+ isFulfil + ", condition=" + condition + ", count=" + count
		+ "]";
    }
}