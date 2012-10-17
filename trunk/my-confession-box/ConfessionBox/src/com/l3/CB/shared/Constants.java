/**
 * L3 Confession Box
 * 
 * Oct 13, 2012, 9:59:06 AM
 */
package com.l3.CB.shared;

import java.util.logging.Level;


public class Constants {

	//Page DIVs
	public static final String DIV_MAIN_CONTENT = "bodyDiv";
	public static final String DIV_LEFT_MENU = "leftHandMenuDiv";
	
	//Request param name
	public static final String REQ_PARAM_CONF_ID = "conf";
	public static final String REQ_PARAM_AUTH_CODE = "code";
	public static final String REQ_PARAM_ERROR_REASON = "error_reason";
	public static final String FB_ERROR_REASON_USER_DENIED = "user_denied";
	public static final String REQ_PARAM_REDIRECT_PARAM = "state";
	
	//History items
	public static final String HISTORY_ITEM_CONFESSION_FEED = "Feed";
	public static final String HISTORY_ITEM_REGISTER_CONFESSION = "RegisterConfession";
	public static final String HISTORY_ITEM_CONFESSION_FEED_WITH_ID = "Confession";
	public static final String HISTORY_ITEM_MY_CONFESSION_FEED = "MyFeed";
	public static final String HISTORY_ITEM_CONFESSION_FOR_ME_FEED = "FeedToMe";
	
	//General settings
	public static final int FEED_PAGE_SIZE = 5;
	public static final Level LOG_LEVEL = Level.OFF;
	
	//Loader image animation
	public static final String LOADER_IMAGE_PATH = "/images/ajax-loader_1.gif";

	//Style class
	public static final String STYLE_CLASS_LOADER_IMAGE = "loaderImage";
	public static final String STYLE_CLASS_NAME_ANCHOR = "confession";
	public static final String STYLE_CLASS_ABUSE_ACTIVITY = "activityButton";
	public static final String STYLE_CLASS_SNP_ACTIVITY = "activityButton";
	public static final String STYLE_CLASS_SP_ACTIVITY = "activityButton";
	public static final String STYLE_CLASS_LAME_ACTIVITY = "activityButton";
	public static final String STYLE_CLASS_SYM_ACTIVITY = "activityButton";
	public static final String STYLE_CLASS_SB_ACTIVITY = "activityButton";
	public static final String STYLE_CLASS_MENU_ITEM_SELECTED = "menuItemSelected";
	public static final String STYLE_CLASS_MENU = "menuStyleName";
	public static final String STYLE_CLASS_CONFESSED_BY = "confession";
	public static final String STYLE_CLASS_REGISTER_CONFESSION_PAGE = "confessionRegisterPage";
	public static final String STYLE_CLASS_REGISTER_CONFESSION_TXT_BOX = "registerConfessionTextBox";
	public static final String STYLE_CLASS_PARDON_MODAL = "pardonModalPopupWindow";
	public static final String PARDON_GRID_ID = "pardon-confession-grid-id";
	public static final String STYLE_CLASS_CONFESSION_GRID = "confessionGridElement";
}
