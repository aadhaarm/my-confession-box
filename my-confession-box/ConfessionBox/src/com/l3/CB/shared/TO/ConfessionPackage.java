package com.l3.CB.shared.TO;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ConfessionPackage implements Serializable, IsSerializable{

    private static final long serialVersionUID = 1L;
    
    private Long userId;
    private String fbId;
    private Long confId;
    private boolean isVisible;
    private Date updateTimeStamp;
    
    private boolean isSelected;
    
    private boolean isAdmin;
    
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getFbId() {
        return fbId;
    }
    public void setFbId(String fbId) {
        this.fbId = fbId;
    }
    public Long getConfId() {
        return confId;
    }
    public void setConfId(Long confId) {
        this.confId = confId;
    }
    public boolean isVisible() {
        return isVisible;
    }
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
    public Date getUpdateTimeStamp() {
        return updateTimeStamp;
    }
    public void setUpdateTimeStamp(Date updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}