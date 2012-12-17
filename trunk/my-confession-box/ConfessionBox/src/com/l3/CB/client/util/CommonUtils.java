/**
 * L3 Confession Box
 * 
 * Oct 13, 2012, 9:59:38 AM
 */
package com.l3.CB.client.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ui.widgets.ChangeVisibilityButton;
import com.l3.CB.client.ui.widgets.DeleteConfessionButton;
import com.l3.CB.client.ui.widgets.SubscribeAnchor;
import com.l3.CB.client.ui.widgets.Templates;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.Filters;
import com.l3.CB.shared.TO.Relations;
import com.l3.CB.shared.TO.UserInfo;

/**
 * All client utils
 * 
 * @author aadmac
 *
 */
public class CommonUtils {

    public static String loggedInUserFbId;
    public static String loggedInUserFullName;
    public static String loggedInUserFirst_name;
    public static String loggedInUserLast_name;
    public static String loggedInUserLink;
    public static String loggedInUserUsername;
    public static String loggedInUserGender;
    public static String loggedInUserLocale;
    public static String loggedInUserEmail;

    public static Map<String, UserInfo> friendsMap;

    /**
     * Reload window
     */
    public static void reloadWindow() {
	Window.Location.reload();
    }

    public static native boolean isTouchEnabled()/*-{
 	var is_touch_device = 'ontouchstart' in document.documentElement;
    	if(is_touch_device) { 
		return true;
	} else {
		return false;
	}
    }-*/;

    public static boolean isMobile() {
	boolean isMobile = false;
	String userAgent = Navigator.getUserAgent();
	if(userAgent != null) {
	    userAgent = userAgent.toLowerCase();
	    isMobile = userAgent.contains("mobile");
	}
	return isMobile;
    }

    /**
     * Redirect the browser to the given url
     * @param url
     */
    public static native void redirect(String url)/*-{
        $wnd.top.location = url;
    }-*/;

    public static native void logout(String logoutMessage)/*-{
    	if($wnd.FB) {
        	$wnd.FB.logout(function(response) {
			alert(logoutMessage);
			document.location.reload();
    		});
    	}
    }-*/;

    /**
     * Parse the XFBML tags on page, in the given ELEMENT
     * @param element
     */
    public static native void parseXFBMLJS(Element element) /*-{
  	try {
        	if($wnd.FB && element) {
        	  $wnd.FB.XFBML.parse(element);
        	}
  	   } catch(err) {
          // debug
          alert('FAILURE: to send in event to google analytics: ' + err);
        }
    }-*/;

    public static void login(int i) {
	String confId = Location.getParameter(Constants.REQ_PARAM_CONF_ID);
	if(Navigator.isJavaEnabled()) {
	    loginInFB(true);
	} else {
	    if(i == 0) {
		if(Window.confirm(ConfessionBox.cbText.requireLoginToBeActiveInfoMessage())) {
		    if(null != confId){
			CommonUtils.redirect(FacebookUtil.getAuthorizeUrl(confId));
		    } else {
			CommonUtils.redirect(FacebookUtil.getAuthorizeUrl());
		    }
		}
	    } else {
		if(null != confId){
		    CommonUtils.redirect(FacebookUtil.getAuthorizeUrl(confId));
		} else {
		    CommonUtils.redirect(FacebookUtil.getAuthorizeUrl());
		}
	    }
	}
    }

    /**
     * Login user into FB and get the information about user
     */
    public static native void loginInFB(boolean reload) /*-{
	if($wnd.FB) {
	  $wnd.FB.login(function(response) {
           if (response.authResponse) {
               // All information with Email
               @com.l3.CB.client.ConfessionBox::accessToken = response.authResponse.accessToken;
		$wnd.FB.api('/me', function(response) {
		    	@com.l3.CB.client.ConfessionBox::isLoggedIn = true;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserFbId = response.id;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserFullName = response.name;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserFirst_name = response.first_name;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserLast_name = response.last_name;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserLink = response.link;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserUsername = response.username;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserGender = response.gender;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserLocale = response.locale;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserEmail = response.email;
		    	var i = 0;
        		$entry(@com.l3.CB.client.util.CommonUtils::getLoggedInUserInfo(I)(i));
        		@com.l3.CB.client.ConfessionBox::loginStatus = "login";
        	});
            } else {
		// Without Email
     		$wnd.FB.api('/me', function(response) {
     		    if(response.id) {
		    	@com.l3.CB.client.ConfessionBox::isLoggedIn = true;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserFbId = response.id;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserFullName = response.name;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserFirst_name = response.first_name;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserLast_name = response.last_name;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserLink = response.link;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserUsername = response.username;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserGender = response.gender;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserLocale = response.locale;
		    	var i = 0;
        		$entry(@com.l3.CB.client.util.CommonUtils::getLoggedInUserInfo(I)(i));
     		    	@com.l3.CB.client.ConfessionBox::loginStatus = "login";
     		    } else {
//TODO: Remove commenting
//     		        @com.l3.CB.client.ConfessionBox::isLoggedIn = false;
//     		        @com.l3.CB.client.ConfessionBox::loginStatus = "rejected";
		    }
        	});
   		}
           }, {scope: 'email'});
	} else {
	    	var i = 0;
//TODO: Remove commenting
//		$entry(@com.l3.CB.client.util.CommonUtils::login(I)(i));
	}
    }-*/;

    /**
     * Method to show post on FB wall dialog
     * 
     * @param linkUrl
     * @param pictureUrl
     * @param activityTitle
     * @param activityCaption
     * @param activityDescription
     * @param addPoints
     */
    public static native void postOnWall(String linkUrl, String pictureUrl, String activityTitle, String activityCaption, String activityDescription, int addPoints) /*-{
        // calling the API ...
        var obj = {
          method: 'feed',
          link: linkUrl,
          picture: pictureUrl,
          name: activityTitle,
          caption: activityCaption,
          description: activityDescription
        };

        function callback(response) {
            if(response['post_id']) {
        	$entry(@com.l3.CB.client.util.EventUtils::raiseUpdateHPEvent(I)(addPoints));
            }
        }

        $wnd.FB.ui(obj, callback);
    }-*/;

    /**
     * Send Confession SEND notification FB dialog
     *  
     * @param toUserFbId
     * @param toUserFullName
     * @param fromUserFullName
     * @param link
     * @param imageUrl
     * @param message
     */
    public static native void sendConfessionNotification(String toUserFbId, String toUserFullName, String fromUserFullName, String link, String imageUrl, String message) /*-{
        // calling the API ...
        var obj = {
          method: 'send',
          to: toUserFbId,
          link: link,
          picture: imageUrl,
          name: fromUserFullName + message,
        };

        function callback(response) {

        }

        $wnd.FB.ui(obj, callback);
    }-*/;

    /**
     * Show FB invite friends dialog
     * 
     * @param userFullName
     */
    public static native void inviteFriends(String userFullName, String inviteTextMessage) /*-{
	if($wnd.FB) {
          $wnd.FB.ui({method: 'apprequests',
            message: userFullName + ' ' + inviteTextMessage
          });
	}
    }-*/;

    /**
     * Get the string value of the JSON value
     * 
     * @param jsonValue
     * @return string value of the JSON value
     */
    private static String getString(JSONValue jsonValue) {
	String returnVal = null;
	if(jsonValue != null) {
	    returnVal = jsonValue.isString().stringValue();
	}
	return returnVal;
    }

    /**
     * Get UserInfo object from JSON string object
     * 
     * @param jsonString
     * @return {@link UserInfo}
     */
    public static UserInfo getUserInfo(String jsonString) {
	UserInfo userInfo = null;
	if(jsonString != null && jsonString.length() > 0) {
	    jsonString = stripOutComment(jsonString);
	    if(jsonString != null && jsonString.length() > 0) {

		// parse the response text into JSON
		JSONValue jsonValue = JSONParser.parseStrict(jsonString);
		if(jsonValue != null) {
		    JSONObject jsonObject = jsonValue.isObject();
		    if(jsonObject != null) {
			userInfo = new UserInfo();
			userInfo.setId(getString(jsonObject.get("id")));
			userInfo.setFirst_name(getString(jsonObject.get("first_name")));
			userInfo.setLast_name(getString(jsonObject.get("last_name")));
			userInfo.setLink(getString(jsonObject.get("link")));
			userInfo.setName(getString(jsonObject.get("name")));
			userInfo.setUsername(getString(jsonObject.get("username")));
			userInfo.setGender(getString(jsonObject.get("gender")));
			userInfo.setLocale(getString(jsonObject.get("locale")));
			userInfo.setEmail(getString(jsonObject.get("email")));
		    }
		}
	    }
	}
	return userInfo;
    }

    /**
     * Strip the security COMMENT added to the JSON string over network
     * 
     * @param jsonString
     * @return Strip out JSON string
     */
    private static String stripOutComment(String jsonString) {
	if(jsonString != null && jsonString.length() > 4) {
	    return jsonString.substring(2, jsonString.length()-2);
	}
	return null;
    }

    /**
     * Get user friends map from friends list JSON
     * 
     * @param jsonString
     * @return Map<String, UserInfo> String - user full name
     */
    public static Map<String, UserInfo> getFriendsUserInfo(String jsonString) {
	Map<String, UserInfo> friends = friendsMap;
	if(friends == null) {
	    if(jsonString != null && jsonString.length() > 0) {
		jsonString = stripOutComment(jsonString);
		if(jsonString != null && jsonString.length() > 0) {

		    // parse the response text into JSON
		    JSONValue jsonValue = JSONParser.parseStrict(jsonString);

		    if(jsonValue != null) {
			JSONObject jsonObject = jsonValue.isObject();

			if(jsonObject != null && jsonObject.get("data") != null) {
			    JSONArray jsonArray = jsonObject.get("data").isArray();

			    if(jsonArray != null) {
				friends = new HashMap<String, UserInfo>();

				for (int i = 0; i < jsonArray.size(); i++) {
				    JSONValue userJSONValue = jsonArray.get(i);
				    if(userJSONValue != null) {
					JSONObject friendJson = userJSONValue.isObject();
					UserInfo userInfo = new UserInfo();
					userInfo.setId(getString(friendJson.get("id")));
					userInfo.setName(getString(friendJson.get("name")));
					friends.put(userInfo.getName(), userInfo);
				    }
				}
			    }
			}
		    }

		}
	    }
	    friendsMap = friends;
	}
	return friends;
    }

    /**
     * Get profile image object
     * 
     * @param confession
     * @param isAnyn
     * @return {@link Image} - user image or anonymous image
     */
    public static Image getProfilePicture(Confession confession, boolean isAnyn) {
	Image profileImage = null;
	if (confession != null && !confession.isShareAsAnyn() || !isAnyn) {
	    profileImage = new Image(FacebookUtil.getUserImageUrl(confession.getFbId()));
	} else {
	    profileImage = new Image(FacebookUtil.getFaceIconImage(confession.getGender()));
	}
	profileImage.setWidth("50px");
	profileImage.setHeight("50px");
	profileImage.setStyleName(Constants.DIV_PROFILE_IMAGE);
	return profileImage;
    }

    /**
     * Get confessee name and with confessed to details
     * @param confession
     * @param userInfo
     * @param isAnyn
     * @param showConfessedTo
     * @return flow panel
     */
    public static FlowPanel getName(final Confession confession, UserInfo userInfo, boolean isAnyn, boolean showConfessedTo) {
	FlowPanel fPnlNameWidget = null;
	if(confession != null) {
	    fPnlNameWidget = new FlowPanel();
	    fPnlNameWidget.setStyleName(Constants.DIV_PROFILE_NAME);

	    // User name or Anonymous
	    if (!confession.isShareAsAnyn() || !isAnyn) {
		if (userInfo != null) {
		    Anchor ancUserName = new Anchor(userInfo.getName(),	userInfo.getLink());
		    ancUserName.setStyleName("profileName");
		    ancUserName.setTarget("_BLANK");
		    fPnlNameWidget.add(ancUserName);
		}
	    } else {
		// Anonymous user
		Anchor ancAnynUser = new Anchor();
		ancAnynUser.setStyleName("profileName");
		// Tooltil HTML title
		ancAnynUser.setTitle(ConfessionBox.cbText.profileNameAnonymousTileText());
		// Confessed by anonymous
		ancAnynUser.setText(ConfessionBox.cbText.confessedByAnynName());
		fPnlNameWidget.add(ancAnynUser);
	    }

	    // Get confessed to text if any
	    if(confession.getConfessedTo() != null && !confession.getConfessedTo().isEmpty()) {
		for (ConfessionShare confessionShare : confession.getConfessedTo()) {
		    if(confessionShare != null && showConfessedTo && confessionShare.getUserFullName() != null) {
			HTML confessedToHtml = new HTML(Templates.TEMPLATES.confessedToPersomalWall(
				FacebookUtil.getProfileFBLink(confessionShare.getFbId()), 
				confessionShare.getUserFullName(), checkForNullConfessedTo(confessionShare.getRelation())));
			confessedToHtml.setStyleName(Constants.STYLE_CLASS_CONFESSED_TO_TEXT);

			fPnlNameWidget.add(confessedToHtml);
		    } else {
			HTML confessedToHtml = new HTML(Templates.TEMPLATES.confessedToFeedWall(getPronoun(confession.getGender()) ,checkForNullConfessedTo(confessionShare.getRelation())));
			confessedToHtml.setStyleName(Constants.STYLE_CLASS_CONFESSED_TO_TEXT);

			fPnlNameWidget.add(confessedToHtml);
		    }
		}
	    } else {
		HTML confessedToHtml = new HTML("confessed to World");
		confessedToHtml.setStyleName(Constants.STYLE_CLASS_CONFESSED_TO_TEXT);

		fPnlNameWidget.add(confessedToHtml);
	    }
	    // Link to Feed preview of the confession
	    if(showConfessedTo) {
		Anchor feedViewLink = new Anchor(ConfessionBox.cbText.feedViewPreviewLink());
		feedViewLink.setTitle(ConfessionBox.cbText.feedPreviewLinkToolTip());
		feedViewLink.setStyleName(Constants.STYLE_CLASS_FEED_VIEW_LINK);
		feedViewLink.addClickHandler(new ClickHandler() {
		    @Override
		    public void onClick(ClickEvent event) {
			ConfessionBox.confId = confession.getConfId().toString();
			CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID);
		    }
		});
		fPnlNameWidget.add(feedViewLink);
	    }
	}
	return fPnlNameWidget;
    }

    public static String getPronoun(String gender) {
	if(gender != null && gender.equalsIgnoreCase("male")) {
	    return ConfessionBox.cbText.malePronoun();
	}
	return ConfessionBox.cbText.femalePronoun();
    }

    /**
     * Check for the relation if null
     * 
     * @param relation
     * @return default text if relation null
     */
    private static String checkForNullConfessedTo(Relations relation) {
	if(relation != null) {
	    return relation.getDisplayText();
	}
	return ConfessionBox.cbText.relationDefaultDisplayText();
    }

    /**
     * Get confession for pardon panel
     * 
     * @param confession
     * @return widget - verticle panel
     */
    public static Widget getConfession(Confession confession) {
	if(confession != null) {
	    VerticalPanel pnlConfession = new VerticalPanel();
	    pnlConfession.add(new Label(confession.getConfessionTitle()));
	    pnlConfession.add(new Label(confession.getConfession()));
	    return pnlConfession;
	}
	return null;
    }

    /**
     * Get user control panel
     * 
     * @param confession
     * @return User control panel
     */
    public static Widget getUserControls(final Confession confession) {
	final FlowPanel fPnlControls = new FlowPanel();
	fPnlControls.setStyleName(Constants.STYLE_CLASS_USER_CONTROL_BUTTON_CONTAINER);

	ChangeVisibilityButton btnChangeVisibility = new ChangeVisibilityButton(confession, new Image(Constants.IMAGE_PATH_USER_CONTROL_IDENTITY_VISIBILITY,20,20,20,20), confession.isShareAsAnyn());
	fPnlControls.add(btnChangeVisibility);

	DeleteConfessionButton btnDeleteConfession = new DeleteConfessionButton(confession, new Image(Constants.IMAGE_PATH_USER_CONTROL_CONFESSION_VISIBILITY,20,20,20,20), confession.isVisibleOnPublicWall());
	fPnlControls.add(btnDeleteConfession);

	return fPnlControls;
    }

    /**
     * Get confession text for feed walls with 'more' and 'less'
     * 
     * @param confession
     * @return Confession text with more and less links
     */
    public static FlowPanel getTextTruncated(final String confession) {
	final String DEFAULT_TEXT_HEIGHT = "88px";
	final FlowPanel fPnlConfession = new FlowPanel();
	fPnlConfession.setStyleName(Constants.STYLE_CLASS_CONFESSION_BODY);

	if(confession != null) {
	    if(confession.length() > Constants.TEXT_LENGTH_ON_LOAD) {
		final HTML lblConfession = new HTML(confession);
		lblConfession.setStyleName(Constants.DIV_CONFESSION_TEXT);
		lblConfession.setHeight(DEFAULT_TEXT_HEIGHT);
		fPnlConfession.add(lblConfession);

		final Anchor anchMore = new Anchor(ConfessionBox.cbText.moreLink());
		anchMore.setStyleName(Constants.STYLE_CLASS_MORE_LINK);
		fPnlConfession.add(anchMore);
		anchMore.addClickHandler(new ClickHandler() {
		    @Override
		    public void onClick(ClickEvent event) {
			if(ConfessionBox.cbText.moreLink().equalsIgnoreCase(anchMore.getText())) {
			    lblConfession.setHeight("auto");
			    anchMore.setText(ConfessionBox.cbText.lessLink());
			} else {
			    lblConfession.setHeight(DEFAULT_TEXT_HEIGHT);
			    anchMore.setText(ConfessionBox.cbText.moreLink());
			}
		    }
		});
		if(ConfessionBox.isTouchEnabled) {
		    anchMore.addStyleName(Constants.STYLE_CLASS_MORE_LINK_TO_BTN);
		}
	    } else {
		HTML lblConfession = new HTML(SafeHtmlUtils.fromSafeConstant(confession));
		fPnlConfession.add(lblConfession);
	    }
	}
	return fPnlConfession;
    }

    /**
     * General check for null string
     * 
     * @param strText
     * @return "" if null also trims the text
     */
    public static String checkForNull(String strText) {
	if(strText != null) {
	    return strText.trim();
	}
	return "";
    }

    /**
     * Fire event even again
     * 
     * @param historyItem - String 
     */
    public static void fireHistoryEvent(String historyItem) {
	if(History.getToken() != null && History.getToken().equals(historyItem)) {
	    History.fireCurrentHistoryState();
	} else {
	    History.newItem(historyItem);
	}
    }

    /**
     * Returns list box with all filter options
     * 
     * @param lstFilterOptions
     * @return list box with all filter options
     */
    public static ListBox getFilterListBox() {
	final ListBox lstFilterOptions = new ListBox();
	if(lstFilterOptions != null) {
	    lstFilterOptions.setStyleName("confessionFilterOptionsList");
	    lstFilterOptions.setVisible(false);
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterAllConfessions(), Filters.ALL.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterGlobalConfessions(), Filters.ALL.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterLocaleConfessions(), Filters.LOCALE_SPECIFIC.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterUserActivityConfessions(), Filters.USER_ACTIVITY.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterSubscribedConfessions(), Filters.SUBSCRIBED .name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterHiddenIdlConfessions(), Filters.CLOSED .name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterOpenIdConfessions(), Filters.OPEN.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterSBVoteConfessions(), Filters.MOST_SAME_BOATS.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterSymVoteConfessions(), Filters.MOST_SYMPATHY.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterSPVoteConfessions(), Filters.MOST_SHOULD_BE_PARDONED.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterSNPVoteConfessions(), Filters.MOST_SHOULD_NOT_BE_PARDONED.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterLameVotedConfessions(), Filters.MOST_LAME.name());
	}
	return lstFilterOptions;
    }

    /**
     * Get date format in AGO format
     * 
     * @param date
     * @return String representation in AGO format
     */
    public static String getDateInAGOFormat(Date date) {
	if(date != null) {
	    Date currentTimeStamp = new Date();
	    long timeDifference = currentTimeStamp.getTime() - date.getTime();
	    if(timeDifference <= (3600000  * 24)) {
		long seconds = timeDifference/1000;
		long minuts = seconds/60;
		long hours = minuts / 60;
		if(seconds < 60) {
		    return ConfessionBox.cbText.timestampLessThanMinut();
		} else if(seconds >= 60 && seconds < 3600) {
		    return minuts + ConfessionBox.cbText.timestampMinutsAgo();
		} else {
		    return hours + ConfessionBox.cbText.timestampHoursAgo();
		}
	    } else {
		return DateTimeFormat.getFormat(Constants.DATE_TIME_FORMAT).format(date);
	    }
	}
	return null;
    }

    /**
     * Get date object in the format
     * 
     * @param date
     * @return String date representation in Format
     */
    public static String getDateInFormat(Date date) {
	return DateTimeFormat.getFormat(Constants.DATE_TIME_FORMAT).format(date);
    }

    /**
     * NO confessions for you in this view
     * @return widget
     */
    public static Widget getEmptyWidget(String emptyPageText) {
	Label lblEmptyPage = new Label(emptyPageText);
	lblEmptyPage.setStyleName("emptyPageText");
	return lblEmptyPage;
    }

    /**
     * Get status bar (subscribe and time stamp of confession)
     * @param confession
     * @return {@link FlowPanel} status bar (subscribe and time stamp of confession)
     */
    public static FlowPanel getStatusBar(Confession confession) {
	FlowPanel fPnlStatusBar = new FlowPanel();
	fPnlStatusBar.setStyleName(Constants.DIV_STATUS_BAR);

	// Subscribe link
	fPnlStatusBar.add(new SubscribeAnchor(confession.getConfId()));

	// Time stamp
	Label lblDateTimeStamp = new Label("| " + CommonUtils.getDateInAGOFormat(confession.getTimeStamp()));
	lblDateTimeStamp.setStyleName(Constants.DIV_TIME_STAMP);
	fPnlStatusBar.add(lblDateTimeStamp);

	return fPnlStatusBar;
    }

    /**
     * Show confession pardon status
     * 
     * @param confession
     * @return {@link FlowPanel}Confession pardon status
     */
    public static FlowPanel getPardonStatus(Confession confession) {
	FlowPanel pardonStatusPanel = null;
	if(confession != null && confession.getConfessedTo() != null && !confession.getConfessedTo().isEmpty()) {
	    pardonStatusPanel = new FlowPanel();
	    pardonStatusPanel.setStyleName("pardonStatusDiv");
	    Label pardonStatus = new Label();
	    for (ConfessionShare confessionShare : confession.getConfessedTo()) {
		if(confessionShare != null && confessionShare.getPardonStatus() != null) {
		    switch (confessionShare.getPardonStatus()) {
		    case PARDONED:
			pardonStatus.setText(ConfessionBox.cbText.pardonStatusLabel());
			pardonStatus.setStyleName(Constants.DIV_PARDONED_STATUS);
			break;
		    default:
			pardonStatus.setText(ConfessionBox.cbText.awaitingPardonStatusLabel());
			pardonStatus.setStyleName(Constants.DIV_AWAITING_PARDON_STATUS);
			break;
		    }
		}
		pardonStatusPanel.add(pardonStatus);
		Label lblDateTimeStamp = new Label(ConfessionBox.cbText.dateTimeStampPrefix() + " " + CommonUtils.getDateInAGOFormat(confessionShare.getTimeStamp()));
		lblDateTimeStamp.setStyleName(Constants.DIV_TIME_STAMP);
		pardonStatusPanel.add(lblDateTimeStamp);
	    }
	}
	return pardonStatusPanel;
    }

    /**
     * Get logged-in user info just returned from FB
     * @param int 1: set user details automatically, 0: Do not set user details in CB
     * @return {@link UserInfo}
     */
    public static UserInfo getLoggedInUserInfo(int i) {
	UserInfo userInfo = new UserInfo();
	if(loggedInUserFbId != null && !loggedInUserFbId.isEmpty()) {
	    userInfo.setId(loggedInUserFbId);
	    userInfo.setUsername(loggedInUserUsername);
	    userInfo.setName(loggedInUserFullName);
	    userInfo.setFirst_name(loggedInUserFirst_name);
	    userInfo.setLast_name(loggedInUserLast_name);
	    userInfo.setEmail(loggedInUserEmail);
	    userInfo.setGender(loggedInUserGender);
	    userInfo.setLink(loggedInUserLink);
	    userInfo.setLocale(loggedInUserLocale);
	} else {
	    userInfo.setLocale(LocaleInfo.getCurrentLocale().getLocaleName());
	}
	// Set login userinfo and reset app
	if(i == 0) {
	    ConfessionBox.setLoggedInUserInfoAndResetApp(userInfo);
	}
	return userInfo;
    }

    public static String processAccessToken(String result) {
	String accessToken = null;
	if(result != null) {
	    String [] resultArgs = result.split("&");
	    if(resultArgs != null && resultArgs.length > 0) {
		String [] accessTokenParam = resultArgs[0].split("=");
		if(accessTokenParam != null && accessTokenParam.length >= 2) {
		    accessToken = accessTokenParam[1];
		}
	    }
	}
	return accessToken;
    }

    /**
     * Decide when to consider device to be small.
     */
    public static void setupScreen() {
	ConfessionBox.isMobile = isMobile();
	ConfessionBox.isTouchEnabled = isTouchEnabled();
    }

    public static boolean isNotNullAndNotEmpty(String str) {

	if((null != str) && (str.length() > 0)) {
	    return true;
	}
	else {
	    return false;
	}
    }

    public static boolean isNullOrEmpty(String str) {
	if((null == str) || (str.length() == 0)) {
	    return true;
	}
	else {
	    return false;
	}
    }
}