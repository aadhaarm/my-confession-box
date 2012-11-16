/**
 * L3 Confession Box
 * 
 * Oct 13, 2012, 9:59:06 AM
 */
package com.l3.CB.shared;

import java.util.logging.Level;


public class Constants {

    // Application settings
    public static final String APPLICATION_DOMAIN = "localhost";

    //Page DIVs
    public static final String DIV_MAIN_CONTENT = "bodyDiv";
    public static final String DIV_LEFT_MENU = "leftHandMenuDiv";
    public static final String DIV_APPLN_TITLE = "appTitle";
    public static final String DIV_AUTH_ID = "auth-id";
    public static final String DIV_LEFT_TOOL_TIP = "leftHandToolTipDiv";
    public static final String DIV_LEFT_HUMAN_POINT = "humanPointDiv";

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
    public static final String LOADER_IMAGE_PATH = "/images/ajax-loader.gif";
    public static final String LOAD_APPLICATION_IMAGE_PATH = "/images/loading.gif";
    public static final String APPLICATION_IMAGE_PATH = "/images/confession_box_smiley_face.jpg";

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
    public static final String STYLE_CLASS_MENU = "menu";
    public static final String STYLE_CLASS_CONFESSED_BY = "confession";
    public static final String STYLE_CLASS_REGISTER_CONFESSION_PAGE = "confessionRegisterPage";
    public static final String STYLE_CLASS_REGISTER_CONFESSION_TXT_BOX = "confession_text";
    public static final String STYLE_CLASS_PARDON_MODAL = "pardonModalPopupWindow";
    public static final String STYLE_CLASS_PARDON_STATUS_PANEL = "pardonStatusPanel";
    public static final String STYLE_CLASS_DATE_TIME_STAMP = "dateTimeStamp";
    public static final String STYLE_CLASS_CONFESSION_MAIN_CONTAINER = "main_container";
    public static final String STYLE_CLASS_BUTTON_WRAPPER = "btnWrapper";
    public static final String STYLE_CLASS_FIELD_ERROR = "fieldError";
    public static final String STYLE_CLASS_REFRESH_BUTTON = "refreshButton";
    public static final String STYLE_CLASS_JUST_OPEN = "justOpen";
    public static final String STYLE_CLASS_CONFESSION_TITLE_TEXT = "confession_title";
    public static final String STYLE_CLASS_CONFESSION_BODY = "confession_body";
    public static final String STYLE_CLASS_HELP_INFO = "helpInfoPanel";
    public static final String DIV_ACTIVITY_BUTTON_CONTAINER = "activityButtonContainer";
    public static final String DIV_ACTIVITY_BUTTON_SHARE_BUTTON = "shareWrap";
    public static final String DIV_USER_CONTROL_BUTTON_CONTAINER = "userControlButtonContainer";
    public static final String DIV_USER_CONTROL_BUTTON = "userControlButton";
    public static final String DIV_CONFESSION_PANEL_TOP_CONTAINER = "top";
    public static final String DIV_CONFESSION_PANEL_MIDDLE_CONTAINER = "middle";
    public static final String DIV_CONFESSION_PANEL_FBWIDGETS_CONTAINER = "fb_widgets";
    public static final String DIV_PROFILE_IMAGE = "image";
    public static final String DIV_PROFILE_NAME = "name";
    public static final String DIV_CONFESSION_TEXT = "confessionText";
    public static final String DIV_MORE_LINK = "moreLink";
    public static final String DIV_STATUS_BAR = "status_bar";
    public static final String DIV_TIME_STAMP = "time_stamp";
    public static final String DIV_PARDONED_STATUS = "pardon_status_yes";
    public static final String DIV_AWAITING_PARDON_STATUS = "pardon_status_no";
    public static final String DIV_FRIENDS_SUGGEST_BOX = "appeal";
    public static final String DIV_FRIENDS_SUGGEST_IMAGE = "friend_image";

    public static final String PARDON_GRID_ID = "pardon-confession-grid-id";

    public static final String pardonConditionUnhide = "UN_HIDE_IDENTITY";
    public static final String pardonConditionSPVotes = "MIN_SP_VOTES";
    public static final int TEXT_LENGTH_ON_LOAD = 400;

    // Register confession page validation
    public static final int CONF_MIN_CHARS = 3;
    public static final int CONF_MAX_CHARS = 1500;
    public static final int COMM_MAX_CHARS = 300;

    // MEMCACHE
    public static final int CONF_CACHE_EXPIRATION_SEC = 300;



}
