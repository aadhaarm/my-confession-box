package com.l3.CB.server.DO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.l3.CB.server.DAO.ConfessionBasicDAO;
import com.l3.CB.server.DAO.UserDAO;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.PardonStatus;

@Entity
@Cache
@Index
public class ConfessionShareDO implements Serializable {

    /**
     * Default serial ID
     */
    private static final long serialVersionUID = 1L;

    @Id
    private Long shareId;
    
    private Long confId;

    @Load
    private Ref<ConfessionDO> refConfession;
    
    private Long userId;
    
    @Load
    private Ref<UserDO> refUser;

    private String pardonStatus = PardonStatus.AWAITING_PARDON.name();

    private Date timeStamp;
    
    private String relation;

    private List<PardonConditionDO> pardonConditionDOs;

    public ConfessionShareDO() {
	super();
    }
    
    public ConfessionShareDO(ConfessionShare confessedTo) {
	if(confessedTo != null) {

	    this.setConfId(confessedTo.getConfId());
	    this.setUserId(confessedTo.getUserId());

	    this.setTimeStamp(confessedTo.getTimeStamp());
	    
	    if(confessedTo.getRelation() != null) {
		this.setRelation(confessedTo.getRelation().name());
	    }
	}
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
	return "ConfessionShareDO [shareId=" + shareId + ", confId=" + confId
		+ ", userId=" + userId + ", pardonStatus=" + pardonStatus
		+ ", timeStamp=" + timeStamp + ", pardonConditionDOs="
		+ pardonConditionDOs + "]";
    }
}