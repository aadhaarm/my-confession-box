package com.l3.CB.shared.TO;

import java.io.Serializable;
import java.util.Date;

public class ConfessionUpdate implements Serializable {

    /**
     * Default serial version ID
     */
    private static final long serialVersionUID = 1L;

    private Long updateId;

    private Long confId;
    
    private Date timeStamp;
    
    private String update;
    
    private String commentAs;

    private Long userId;

    private boolean shareAsAnyn = true;
    
    public ConfessionUpdate() {
	super();
    }

    public ConfessionUpdate(Long confId, Date timeStamp, String update,
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

    public String getCommentAs() {
        return commentAs;
    }

    public void setCommentAs(String commentAs) {
        this.commentAs = commentAs;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}