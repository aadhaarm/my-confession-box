package com.l3.CB.server.DO;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class ConfessionUpdateDO implements Serializable {

    /**
     * Default serial version ID
     */
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long updateId;

    @Persistent
    private Long confId;
    
    @Persistent
    private Date timeStamp;
    
    @Persistent
    private Text update;
    
    @Persistent
    private String commentAs;

    @Persistent
    private Long userId;

    @Persistent
    private boolean shareAsAnyn = true;
    
    public ConfessionUpdateDO(Long confId, Date timeStamp, Text update,
	    String commentAs, Long userId) {
	super();
	this.confId = confId;
	this.timeStamp = timeStamp;
	this.update = update;
	this.commentAs = commentAs;
	this.userId = userId;
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

    public void setConfId(Long confId) {
        this.confId = confId;
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
}