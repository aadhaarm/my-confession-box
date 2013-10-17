package com.l3.CB.server.DO;

import java.io.Serializable;

import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.l3.CB.server.DAO.UserDAO;

@Entity
@Cache
public class ConfessionDraftDO implements Serializable {

    /**
     * Default serial ID
     */
    private static final long serialVersionUID = 1L;

    @Id
    private Long confId;

    private Text confession;
    @Index

    private Long userId;

    @Index
    @Load
    private Ref<UserDO> refUser;    
    
    @Index
    private String shareToUserIDForSave;
    
    @Index
    private String shareToRelationForSave;
    
    @Index
    private String confessionTitle;
    
    @Index
    private boolean shareAsAnyn = true;

    public ConfessionDraftDO() {
	super();
    }

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

    public void setUserId(long userId) {
	this.userId = userId;
	this.refUser = UserDAO.getUserRef(userId);
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

    public Ref<UserDO> getRefUser() {
        return refUser;
    }

    public void setRefUser(Ref<UserDO> refUser) {
        this.refUser = refUser;
    }
}