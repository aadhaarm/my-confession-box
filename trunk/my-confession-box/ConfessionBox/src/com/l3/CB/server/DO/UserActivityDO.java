package com.l3.CB.server.DO;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class UserActivityDO implements Serializable {
	
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	public UserActivityDO() {
		super();
	}

	public UserActivityDO(long userId, long confId, String activityType,
			boolean shareAsAnyn) {
		super();
		this.userId = userId;
		this.confId = confId;
		this.activityType = activityType;
		this.shareAsAnyn = shareAsAnyn;
	}

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long activityId;
	
	@Persistent
	private long userId;
	
	@Persistent
	private long confId;
	
	private String activityType;
	
	@Persistent
	private boolean shareAsAnyn = true;

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getConfId() {
		return confId;
	}

	public void setConfId(long confId) {
		this.confId = confId;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public boolean isShareAsAnyn() {
		return shareAsAnyn;
	}

	public void setShareAsAnyn(boolean shareAsAnyn) {
		this.shareAsAnyn = shareAsAnyn;
	}
}
