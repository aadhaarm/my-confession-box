package com.l3.CB.shared.TO;

import java.io.Serializable;

public class PardonCondition implements Serializable {
	
	/**
	 * Default serial Id.
	 */
	private static final long serialVersionUID = 1L;

	private boolean isFulfil;
	
	String condition;
	
	private int count;
	
	private Long userId;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public PardonCondition() {
		super();
	}

	public PardonCondition(String condition, int count) {
		super();
		this.condition = condition;
		this.count = count;
	}

	public boolean isFulfil() {
		return isFulfil;
	}

	public void setFulfil(boolean isFulfil) {
		this.isFulfil = isFulfil;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}