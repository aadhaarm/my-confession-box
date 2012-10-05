package com.l3.CB.server.DO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class ConfessionDO implements Serializable {

	/**
	 * Default serial ID
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long confId;
	
	@Persistent
	private String confession;

	@Persistent
	private Date timeStamp;

	@Persistent
	private Long userId;
	
	@Persistent
	private boolean shareAsAnyn = true;
	
	@Persistent
	private List<ConfessionShareDO> confessedTo;

	public Long getConfId() {
		return confId;
	}

	public void setConfId(Long confId) {
		this.confId = confId;
	}

	public String getConfession() {
		return confession;
	}

	public void setConfession(String confession) {
		this.confession = confession;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isShareAsAnyn() {
		return shareAsAnyn;
	}

	public void setShareAsAnyn(boolean shareAsAnyn) {
		this.shareAsAnyn = shareAsAnyn;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<ConfessionShareDO> getConfessedTo() {
		return confessedTo;
	}

	public void setConfessedTo(List<ConfessionShareDO> confessedTo) {
		this.confessedTo = confessedTo;
	}
}
