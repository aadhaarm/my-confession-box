package com.l3.CB.server.DO;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

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
	private Text confession;

	@Persistent
	private Date timeStamp;

	@Persistent
	private Long userId;
	
	@Persistent
	private boolean shareAsAnyn = true;
	
	@Persistent
	private String userIp;
	
	@Persistent
	private String locale;
	
	@Persistent
	private String confessionTitle;
	
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

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getConfessionTitle() {
		return confessionTitle;
	}

	public void setConfessionTitle(String confessionTitle) {
		this.confessionTitle = confessionTitle;
	}

	public Text getConfession() {
		return confession;
	}

	public void setConfession(Text confession) {
		this.confession = confession;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}
