package com.l3.CB.shared.TO;

import java.io.Serializable;

public class ConfessionShare implements Serializable {

	/**
	 * Default serial ID
	 */
	private static final long serialVersionUID = 1L;

	private Long shareId;
	
	private Long confId;
	
	private Long userId;
	
	private boolean pardon;

	public Long getShareId() {
		return shareId;
	}

	public void setShareId(Long shareId) {
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
