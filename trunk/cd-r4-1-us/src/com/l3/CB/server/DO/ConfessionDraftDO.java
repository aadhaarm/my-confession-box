package com.l3.CB.server.DO;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class ConfessionDraftDO implements Serializable {

    /**
     * Default serial ID
     */
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long confId;

    @Persistent
    private Text confession;

    @Persistent
    private Long userId;

    @Persistent
    private String shareToUserIDForSave;
    
    @Persistent
    private String shareToRelationForSave;
    
    @Persistent
    private String confessionTitle;
    
    @Persistent
    private boolean shareAsAnyn = true;

    public Long getConfId() {
	return confId;
    }

    public void setConfId(Long confId) {
	this.confId = confId;
    }

    public Text getConfession() {
	return confession;
    }

    public void setConfession(Text confession) {
	this.confession = confession;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public String getConfessionTitle() {
	return confessionTitle;
    }

    public void setConfessionTitle(String confessionTitle) {
	this.confessionTitle = confessionTitle;
    }

    @Override
    public String toString() {
	return "ConfessionDraftDO [confId=" + confId + ", confession="
		+ confession + ", userId=" + userId + ", confessionTitle="
		+ confessionTitle + "]";
    }

    public String getShareToUserIDForSave() {
        return shareToUserIDForSave;
    }

    public void setShareToUserIDForSave(String shareToUserIDForSave) {
        this.shareToUserIDForSave = shareToUserIDForSave;
    }

    public boolean isShareAsAnyn() {
        return shareAsAnyn;
    }

    public void setShareAsAnyn(boolean shareAsAnyn) {
        this.shareAsAnyn = shareAsAnyn;
    }

    public String getShareToRelationForSave() {
        return shareToRelationForSave;
    }

    public void setShareToRelationForSave(String shareToRelationForSave) {
        this.shareToRelationForSave = shareToRelationForSave;
    }
}