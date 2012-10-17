package com.l3.CB.shared.TO;

import java.io.Serializable;

public class UserInfo implements Serializable {
	
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	
	// CB User ID
	private Long userId;
	// Facebook ID
	private String id;	
	// Name
	private String name;
	// First Name
	private String first_name;
	// Last name
	private String last_name;
	// User name
	private String username;
	// User profile link
	private String link;
	// User gender
	private String gender;
	private String locale;
	
	public UserInfo(Long userId) {
		super();
		this.userId = userId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public UserInfo() {
		super();
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", id=" + id + ", name=" + name
				+ ", first_name=" + first_name + ", last_name=" + last_name
				+ ", username=" + username + ", link=" + link + ", gender="
				+ gender + ", locale=" + locale + "]";
	}
}
