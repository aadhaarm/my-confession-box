package com.l3.CB.shared.TO;

public enum Menu {
    WALL("Confession Wall", "feed"), WRITE("Write Confession", "write"), MY_CONF("My Confessions", "meConf"), CONF_TO_ME("Confessions To Me", ""), INVITE("", ""), RULE_BOOK("", ""), LOGOUT("", "");
    
    String displayText;
    String value;
    
    private Menu(String displayText, String value) {
	this.displayText = displayText;
	this.value = value;
    }
}