package com.l3.CB.server.DO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.l3.CB.shared.TO.PardonStatus;

@PersistenceCapable
public class ConfessionShareDO implements Serializable {

    /**
     * Default serial ID
     */
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long shareId;

    @Persistent
    private Long confId;

    @Persistent
    private Long userId;

    @Persistent
    private String pardonStatus;

    @Persistent
    private Date timeStamp;
    
    @Persistent
    private String relation;

    @Persistent
    private List<PardonConditionDO> pardonConditionDOs;

    public ConfessionShareDO() {
	super();
	pardonStatus = PardonStatus.AWAITING_PARDON.name();
    }

    public Long getShareId() {
	return shareId;
    }

    public void setShareId(Long shareId) {
	this.shareId = shareId;
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

    public Date getTimeStamp() {
	return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
	this.timeStamp = timeStamp;
    }

    public List<PardonConditionDO> getPardonConditionDOs() {
	return pardonConditionDOs;
    }

    public void setPardonConditionDOs(List<PardonConditionDO> pardonConditionDOs) {
	this.pardonConditionDOs = pardonConditionDOs;
    }

    public String getPardonStatus() {
        return pardonStatus;
    }

    public void setPardonStatus(String pardonStatus) {
        this.pardonStatus = pardonStatus;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
	return "ConfessionShareDO [shareId=" + shareId + ", confId=" + confId
		+ ", userId=" + userId + ", pardonStatus=" + pardonStatus
		+ ", timeStamp=" + timeStamp + ", pardonConditionDOs="
		+ pardonConditionDOs + "]";
    }
}