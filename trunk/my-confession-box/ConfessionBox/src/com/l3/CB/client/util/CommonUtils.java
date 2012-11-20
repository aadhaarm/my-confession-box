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
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
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
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.Filters;
import com.l3.CB.shared.TO.Relations;
import com.l3.CB.shared.TO.UserInfo;

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

    // redirect the browser to the given url
    public static native void redirect(String url)/*-{
        $wnd.top.location = url;
    }-*/;

    // Parse the XFBML tags on page, in the given ELEMENT
    public static native void parseXFBMLJS(Element element) /*-{
		if($wnd.FB && element) {
			$wnd.FB.XFBML.parse(element);
		}
	}-*/;

    public static native void login() /*-{
	if($wnd.FB) {
	  $wnd.FB.login(function(response) {
           if (response.authResponse) {
               @com.l3.CB.client.ConfessionBox::accessToken = response.authResponse.accessToken;
		$wnd.FB.api('/me', function(response) {
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserFbId = response.id;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserFullName = response.name;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserFirst_name = response.first_name;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserLast_name = response.last_name;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserLink = response.link;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserUsername = response.username;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserGender = response.gender;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserLocale = response.locale;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserEmail = response.email;
        	});
            } else {
     		$wnd.FB.api('/me', function(response) {
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserFbId = response.id;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserFullName = response.name;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserFirst_name = response.first_name;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserLast_name = response.last_name;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserLink = response.link;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserUsername = response.username;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserGender = response.gender;
		    	@com.l3.CB.client.util.CommonUtils::loggedInUserLocale = response.locale;
        	});
   		}
           }, {scope: 'email'});
	}
	}-*/;

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

    public static native void inviteFriends(String userFullName) /*-{
	if($wnd.FB) {
          $wnd.FB.ui({method: 'apprequests',
            message: userFullName + ' invites you to Confession Box'
          });
	}
    }-*/;

    public static String getString(JSONValue jsonValue) {
	String returnVal = null;
	if(jsonValue != null) {
	    returnVal = jsonValue.isString().stringValue();
	}
	return returnVal;
    }

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

    private static String stripOutComment(String jsonString) {
	return jsonString.substring(2, jsonString.length()-2);
    }

    public static Map<String, UserInfo> getFriendsUserInfo(String jsonString) {
	Map<String, UserInfo> friends = null;

	if(jsonString != null && jsonString.length() > 0) {
	    jsonString = stripOutComment(jsonString);
	    if(jsonString != null && jsonString.length() > 0) {

		// parse the response text into JSON
		JSONValue jsonValue = JSONParser.parseStrict(jsonString);

		if(jsonValue != null) {
		    JSONObject jsonObject = jsonValue.isObject();

		    if(jsonObject != null) {
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
	return friends;
    }

    public static Image getProfilePicture(Confession confession, boolean isAnyn) {
	Image profileImage = null;
	if (!confession.isShareAsAnyn() || !isAnyn) {
	    profileImage = new Image(FacebookUtil.getUserImageUrl(confession.getFbId()));
	} else {
	    profileImage = new Image(FacebookUtil.getFaceIconImage(confession.getGender()));
	}
	profileImage.setStyleName(Constants.DIV_PROFILE_IMAGE);
	return profileImage;
    }

    public static FlowPanel getName(Confession confession, UserInfo userInfo, boolean isAnyn, boolean showConfessedTo) {
	FlowPanel fPnlNameWidget = null;
	if(confession != null) {
	    fPnlNameWidget = new FlowPanel();
	    fPnlNameWidget.setStyleName(Constants.DIV_PROFILE_NAME);
	    if (!confession.isShareAsAnyn() || !isAnyn) {
		if (userInfo != null) {
		    Anchor ancUserName = new Anchor(userInfo.getName(),	userInfo.getLink());
		    ancUserName.removeStyleName("gwt-Anchor");
		    ancUserName.setTarget("_BLANK");
		    fPnlNameWidget.add(ancUserName);
		}
	    } else {
		Anchor ancAnynUser = new Anchor();
		ancAnynUser.setText(ConfessionBox.cbText.confessedByAnynName());
		fPnlNameWidget.add(ancAnynUser);
	    }
	    if(confession.getConfessedTo() != null && !confession.getConfessedTo().isEmpty()) {
		for (ConfessionShare confessionShare : confession.getConfessedTo()) {
		    if(showConfessedTo) {
			Anchor ancUserName = new Anchor(confessionShare.getUserFullName(), "http://www.facebook.com/"+confessionShare.getFbId());
			ancUserName.setTarget("_BLANK");
			fPnlNameWidget.add(new HTML("Confessed to " + ancUserName + " (as '" +checkForNullConfessedTo(confessionShare.getRelation()) + "' for feed wall)"));
		    } else {
			Label lblDateTimeStamp = new Label("Confessed to " + checkForNullConfessedTo(confessionShare.getRelation()));
			fPnlNameWidget.add(lblDateTimeStamp);
		    }
		}
	    }
	}
	return fPnlNameWidget;
    }

    private static String checkForNullConfessedTo(Relations relation) {
	if(relation != null) {
	    return relation.getDisplayText();
	}
	return "world";
    }

    public static Widget getConfession(Confession confession) {
	if(confession != null) {
	    VerticalPanel pnlConfession = new VerticalPanel();
	    pnlConfession.add(new Label(confession.getConfessionTitle()));
	    pnlConfession.add(new Label(confession.getConfession()));
	    return pnlConfession;
	}
	return null;
    }

    public static Widget getUserControls(final Confession confession) {
	final FlowPanel fPnlControls = new FlowPanel();
	fPnlControls.setStyleName(Constants.DIV_USER_CONTROL_BUTTON_CONTAINER);

	ChangeVisibilityButton btnChangeVisibility = new ChangeVisibilityButton(confession, new Image("/images/sympathies.png",0,0,27,30), confession.isShareAsAnyn());
	fPnlControls.add(btnChangeVisibility);

	DeleteConfessionButton btnDeleteConfession = new DeleteConfessionButton(confession, new Image("/images/sympathies.png",0,0,27,30), confession.isVisibleOnPublicWall());
	fPnlControls.add(btnDeleteConfession);

	return fPnlControls;
    }

    public static FlowPanel getTextTruncated(final String confession) {
	final FlowPanel fPnlConfession = new FlowPanel();
	fPnlConfession.setStyleName(Constants.STYLE_CLASS_CONFESSION_BODY);
	if(confession != null) {
	    if(confession.length() > Constants.TEXT_LENGTH_ON_LOAD) {
		final HTML lblConfession = new HTML(confession);
		lblConfession.setStyleName(Constants.DIV_CONFESSION_TEXT);
		lblConfession.setHeight("80px");
		fPnlConfession.add(lblConfession);

		final Anchor anchMore = new Anchor(ConfessionBox.cbText.moreLink());
		anchMore.setStyleName(Constants.DIV_MORE_LINK);
		fPnlConfession.add(anchMore);

		anchMore.addClickHandler(new ClickHandler() {

		    @Override
		    public void onClick(ClickEvent event) {
			if(ConfessionBox.cbText.moreLink().equalsIgnoreCase(anchMore.getText())) {
			    lblConfession.setHeight("auto");
			    anchMore.setText(ConfessionBox.cbText.lessLink());
			} else {
			    lblConfession.setHeight("80px");
			    anchMore.setText(ConfessionBox.cbText.moreLink());
			}
		    }
		});
	    } else {
		HTML lblConfession = new HTML(SafeHtmlUtils.fromSafeConstant(confession));
		fPnlConfession.add(lblConfession);
	    }
	}
	return fPnlConfession;
    }

    public static String checkForNull(String confessionTitle) {
	if(confessionTitle != null) {
	    return confessionTitle.trim();
	}
	return "";
    }

    /**
     * @param historyItem 
     * 
     */
    public static void fireHistoryEvent(String historyItem) {
	if(History.getToken() != null && History.getToken().equals(historyItem)) {
	    History.fireCurrentHistoryState();
	} else {
	    History.newItem(historyItem);
	}
    }

    /**
     * @param lstFilterOptions 
     * 
     */
    public static ListBox getMeFilterListBox(ListBox lstFilterOptions) {
	if(lstFilterOptions != null) {
	    lstFilterOptions.setVisible(false);
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterAllConfessions(), Filters.ALL.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterSubscribedConfessions(), Filters.SUBSCRIBED .name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterHiddenIdlConfessions(), Filters.CLOSED .name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterLocaleConfessions(), Filters.LOCALE_SPECIFIC.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterSBVoteConfessions(), Filters.MOST_SAME_BOATS.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterLameVotedConfessions(), Filters.MOST_LAME.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterSymVoteConfessions(), Filters.MOST_SYMPATHY.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterSPVoteConfessions(), Filters.MOST_SHOULD_BE_PARDONED.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterOpenIdConfessions(), Filters.OPEN.name());
	    lstFilterOptions.addItem(ConfessionBox.cbText.filterUserActivityConfessions(), Filters.USER_ACTIVITY.name());
	}
	return lstFilterOptions;
    }

    public static String getDateInAGOFormat(Date date) {
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
	    return DateTimeFormat.getFormat("MMM d, ''yy - h:mm a").format(date);
	}
    }

    public static String getDateInFormat(Date date) {
	return DateTimeFormat.getFormat("MMM d, ''yy - h:mm a").format(date);
    }


    public static Widget getEmptyWidget() {
	Label lblEmptyPage = new Label(ConfessionBox.cbText.noConfessionsInViewMessage());
	return lblEmptyPage;
    }

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

    public static FlowPanel getPardonStatus(Confession confession) {
	FlowPanel pardonStatusPanel = null;
	if(confession != null && confession.getConfessedTo() != null && !confession.getConfessedTo().isEmpty()) {
	    pardonStatusPanel = new FlowPanel();
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
		    }
		}
		pardonStatusPanel.add(pardonStatus);
		Label lblDateTimeStamp = new Label("Since " + CommonUtils.getDateInAGOFormat(confessionShare.getTimeStamp()));
		lblDateTimeStamp.setStyleName(Constants.DIV_TIME_STAMP);
		pardonStatusPanel.add(lblDateTimeStamp);
	    }
	}
	return pardonStatusPanel;
    }

    public static Widget getConditionStatus(Confession confession) {
	// TODO Auto-generated method stub
	return null;
    }

    public static UserInfo getLoggedInUserInfo() {
	UserInfo userInfo = null;
	if(loggedInUserFbId != null && !loggedInUserFbId.isEmpty()) {
	    userInfo = new UserInfo();
	    userInfo.setId(loggedInUserFbId);
	    userInfo.setUsername(loggedInUserUsername);
	    userInfo.setName(loggedInUserFullName);
	    userInfo.setFirst_name(loggedInUserFirst_name);
	    userInfo.setLast_name(loggedInUserLast_name);
	    userInfo.setEmail(loggedInUserEmail);
	    userInfo.setGender(loggedInUserGender);
	    userInfo.setLink(loggedInUserLink);
	    userInfo.setLocale(loggedInUserLocale);
	}
	return userInfo;
    }
}
