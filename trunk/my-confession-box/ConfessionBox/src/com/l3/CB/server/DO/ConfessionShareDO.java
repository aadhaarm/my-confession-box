package com.l3.CB.server.DO;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ConfessionShareDO implements Serializable {

	/**
	 * Default serial ID
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key shareId;
	
	@Persistent
	private Long confId;
	
	@Persistent
	private Long userId;
	
	@Persistent
	private boolean pardon;

	public Key getShareId() {
		return shareId;
	}

	public void setShareId(Key shareId) {
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

	public boolean isPardon() {
		return pardon;
	}

	public void setPardon(boolean pardon) {
		this.pardon = pardon;
	}
}
