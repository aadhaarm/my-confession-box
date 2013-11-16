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
    public static final String DIV_LEFT_MENU = "menuDiv";
    public static final String DIV_APPLN_TITLE = "appTitle";
    public static final String DIV_AUTH_ID = "auth-id";
    public static final String DIV_LEFT_TOOL_TIP = "leftHandToolTipDiv";
    public static final String DIV_LEFT_HUMAN_POINT = "humanPointDiv";
    public static final String DIV_FOOTER = "footerDiv";

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
    public static final String HISTORY_ITEM_CONFESSION_FOR_ME_FEED = "ToMe";

    //General settings
    public static final int FEED_PAGE_SIZE = 4;
    public static final Level LOG_LEVEL = Level.OFF;

    //Loader image animation
    public static final String IMAGE_APPLICATION_LOGO_PATH = "/images/CB-logo.png";
    public static final String LOAD_ACTIVITY_TIMER_PROGRESS = "/images/loader_progress.gif";
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
    public static final String STYLE_CLASS_USER_CONTROL_BUTTON_CONTAINER = "userControlButtonContainer";
    public static final String DIV_USER_CONTROL_BUTTON = "userControlButton";
    public static final String DIV_CONFESSION_PANEL_TOP_CONTAINER = "confessionTop";
    public static final String DIV_CONFESSION_PANEL_MIDDLE_CONTAINER = "middle";
    public static final String DIV_CONFESSION_PANEL_FBWIDGETS_CONTAINER = "fb_widgets";
    public static final String DIV_PROFILE_IMAGE = "image";
    public static final String DIV_PROFILE_NAME = "name";
    public static final String DIV_CONFESSION_TEXT = "confessionText";
    public static final String STYLE_CLASS_MORE_LINK = "moreLink";
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
    public static final int CONF_CACHE_EXPIRATION_SEC = 300; // 5 minuts
    public static final int CONF_LIST_CACHE_EXPIRATION_SEC = 300; // 5 minuts
    public static final int USER_CACHE_EXPIRATION_SEC = 1800; // 30 hour
    public static final Object JSON_CACHE_EXPIRATION_SEC = 1800; // 30 hour

    public static final String STYLE_CLASS_CONFESSED_TO_TEXT = "confessedToName";

    public static final String STYLE_CLASS_FEED_VIEW_LINK = "feedViewLink";

    public static final String IMAGE_PATH_USER_CONTROL_IDENTITY_VISIBILITY = "/images/masks.png";

    public static final String IMAGE_PATH_USER_CONTROL_CONFESSION_VISIBILITY = "/images/hideConf.png";

    public static final String DATE_TIME_FORMAT = "MMM d, ''yy";

    public static final String STYLE_CLASS_TOOLTIP_RIGHT = "tooltip_right";

    public static final String STYLE_CLASS_TOOLTIP_LEFT = "tooltip_left";

    public static final String STYLE_CLASS_PARDON_BUTTON = "pardonButton";

    public static final int POINTS_ON_PARDONING = 10;

    public static final int POINTS_ON_APPEAL_PARDON = 5;

    public static final int POINTS_ON_CONFESSING = 5;

    public static final int POINTS_ON_UNHIDING_IDENTITY = 5;

    public static final String STYLE_CLASS_CONFESION_FILTER_DESCRIPTION_INFO = "filterInfo";

    public static final String STYLE_CLASS_UPDATE_CONFESSION_PANEL = "updateConfessionPanel";

    public static final String PARDON_APPEAL_PANEL = "appealPanel";

    public static final String STYLE_CLASS_PARDON_APPEAL_LINK = "appealLink";

    public static final String STYLE_CLASS_ACTIVITY_BUTTON_PANEL = "activityButtonDiv";

    public static final String STYLE_CLASS_ACTIVITY_BTN_AND_PARDON_STATUS_DIV = "btnAbdPardonstatusDiv";

    public static final String STYLE_CLASS_PARDON_CONDITION_PANEL = "condition";

    public static final String STYLE_CLASS_PARDON_CONDITION = "pardonCondition";

    public static final String STYLE_CLASS_PARDON_BUTTON_PANEL = "pardonButtonDiv";

    public static final String TICK_MARK = "✔";

    public static final String STYLE_CLASS_ACTIVITY_BUTTON_DIV = "buttons";

    public static final String STYLE_CLASS_SUBSCRIBE_LINK_TO_BTN_S = "linkToButtonS";

    public static final String STYLE_CLASS_SUBSCRIBE_LINK_TO_BTN_U = "linkToButtonU";
    
    public static final String STYLE_CLASS_CONF_UPDATE_TEXT_ROW = "confessionUpdate";

    public static final String STYLE_CLASS_SHARE_LINK_TO_BTN = "shareLinkToButton";

    public static final String STYLE_CLASS_MORE_LINK_TO_BTN = "moreLinkToButton";

    public static final String STYLE_CLASS_UPDATE_LINK_TO_BTN = "updateLinkToButton";

    public static final String STYLE_CLASS_CONFESS_TO_ME_TOOLTIP = "confessToMeToolTip";

    public static final String DIV_LOGIN_STATUS_BAR = "loginStatusBar";

    public static final String STYLE_CLASS_STATUS_LINK_TO_BTN = "statusLinkToButton";

    public static final String DIV_OPTIONS_NO_BUTTON = "optionsNoButton";

    public static final String DIV_OPTIONS_YES_BUTTON = "optionsYesButton";

    public static final String STYLE_CLASS_YES_LINK_TO_BTN_S = "linkToButtonYesS";

    public static final String STYLE_CLASS_YES_LINK_TO_BTN_U = "linkToButtonYesU";

    public static final String STYLE_CLASS_NO_LINK_TO_BTN_S = "linkToButtonNoS";

    public static final String STYLE_CLASS_NO_LINK_TO_BTN_U = "linkToButtonNoU";

    public static final String IMAGE_REPORT_ABUSE_ICON = "/images/flag.png";

    public static final String IMAGE_SHOULD_NOT_BE_PARDONED_ICON = "/images/SNBP.png";

    public static final String IMAGE_SHOULD_BE_PARDONED_ICON = "/images/SBP.png";

    public static final String IMAGE_LAME_ICON = "/images/lame.png";

    public static final String IMAGE_SYMPATHY_ICON = "/images/sympathies.png";

    public static final String IMAGE_SAME_BOAT_ICON = "/images/SameBoat.png";

    public static final String HISTORY_ITEM_PRIVACY_POLICY = "privacy";

    public static final String HISTORY_ITEM_TOS = "TermsOfService";

    public static final String DIV_MOBILE_LEFT_MENU = "mobileMenuDiv";

    public static final String HISTORY_ITEM_ABOUT_CB = "aboutUs";

    public static final String HISTORY_ITEM_PHILOSPHY = "philosphy";

    public static final String HISTORY_ITEM_ANONYMOUS_CONFESSION = "anonymousConfession";

    public static final String HISTORY_ITEM_RULE_BOOK = "confessionBoxRuleBook";

    public static final String STYLE_CLASS_DANGER = "uk-form-danger";

    public static final String STYLE_CLASS_BLUE_BUTTON = "uk-button-primary";
}