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
import com.l3.CB.server.DAO.ConfessionBasicDAO;
import com.l3.CB.server.DAO.UserDAO;

@Entity
@Cache
@Index
public class ConfessionUpdateDO implements Serializable {

    /**
     * Default serial version ID
     */
    private static final long serialVersionUID = 1L;

    @Id
    private Long updateId;

    private Long confId;

    @Load
    private Ref<ConfessionDO> refConfession;
    
    private Date timeStamp;
    
    private Text update;
    
    private String commentAs;

    private Long userId;

    @Load
    private Ref<UserDO> refUser;
    
    private boolean shareAsAnyn = true;
    
    private ConfessionUpdateDO() {
	super();
    }

    public ConfessionUpdateDO(Long confId, Date timeStamp, Text update,
	    String commentAs, Long userId) {
	super();
	this.setUserId(userId);
	this.setConfId(confId);
	this.timeStamp = timeStamp;
	this.update = update;
	this.commentAs = commentAs;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Long getConfId() {
        return confId;
    }

    public void setConfId(long confId) {
	this.confId = confId;
	this.refConfession = ConfessionBasicDAO.getConfessionRef(confId);
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

    public void setUserId(long userId) {
	this.userId = userId;
	this.refUser = UserDAO.getUserRef(userId);
    }

    public boolean isShareAsAnyn() {
        return shareAsAnyn;
    }

    public void setShareAsAnyn(boolean shareAsAnyn) {
        this.shareAsAnyn = shareAsAnyn;
    }

    public Text getUpdate() {
        return update;
    }

    public void setUpdate(Text update) {
        this.update = update;
    }

    public String getCommentAs() {
        return commentAs;
    }

    public void setCommentAs(String commentAs) {
        this.commentAs = commentAs;
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
}