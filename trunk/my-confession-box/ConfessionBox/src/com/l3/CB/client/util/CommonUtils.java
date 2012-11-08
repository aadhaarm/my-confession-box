/**
 * L3 Confession Box
 * 
 * Oct 13, 2012, 9:59:38 AM
 */
package com.l3.CB.client.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.EditConfessionEvent;
import com.l3.CB.client.ui.widgets.ChangeVisibilityButton;
import com.l3.CB.client.ui.widgets.DeleteConfessionButton;
import com.l3.CB.client.ui.widgets.ShareConfessionPopup;
import com.l3.CB.client.ui.widgets.SubscribeAnchor;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.Filters;
import com.l3.CB.shared.TO.UserInfo;
import com.sun.tools.javac.comp.Flow;

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
		if($wnd.FB) {
			$wnd.FB.XFBML.parse(element);
		}
	}-*/;

    // Parse all the XFBML tags on the page
    public static native void parseXFBMLJS() /*-{
		if($wnd.FB) {
	  		$wnd.FB.XFBML.parse();
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

    public static native void sendConfessionNotification(String toUserFbId, String toUserFullName, String fromUserFullName, String link, String imageUrl) /*-{
        // calling the API ...
        var obj = {
          method: 'send',
          to: toUserFbId,
          link: link,
          picture: imageUrl,
          name: fromUserFullName + ' has confessed to you on Confession Box.',
        };

        function callback(response) {

        }

        $wnd.FB.ui(obj, callback);
    }-*/;


    public static String getString(JSONValue jsonValue) {
	String returnVal = null;
	if(jsonValue != null) {
	    returnVal = jsonValue.isString().stringValue();
	}
	return returnVal;
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

    public static List<UserInfo> getFriendsUserInfo(String jsonString) {
	List<UserInfo> friends = null;

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
			    friends = new ArrayList<UserInfo>();

			    for (int i = 0; i < jsonArray.size(); i++) {
				JSONValue userJSONValue = jsonArray.get(i);
				if(userJSONValue != null) {
				    JSONObject friendJson = userJSONValue.isObject();
				    UserInfo userInfo = new UserInfo();
				    userInfo.setId(getString(friendJson.get("id")));
				    userInfo.setName(getString(friendJson.get("name")));
				    friends.add(userInfo);
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
	profileImage.setStyleName("image");
	return profileImage;
    }

    public static FlowPanel getName(Confession confession, UserInfo userInfo, boolean isAnyn) {
	FlowPanel fPnlNameWidget = null;
	if(confession != null) {
	    fPnlNameWidget = new FlowPanel();
	    fPnlNameWidget.setStyleName("name");
	    if (!confession.isShareAsAnyn() || ! isAnyn) {
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
	}
	return fPnlNameWidget;
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

    public static HorizontalPanel getUserControls(final Confession confession) {
	HorizontalPanel hPnlControls = new HorizontalPanel();
	hPnlControls.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

	ChangeVisibilityButton btnChangeVisibility = new ChangeVisibilityButton(confession, new Image("/images/sympathies.png",0,0,27,30), confession.isShareAsAnyn());
	hPnlControls.add(btnChangeVisibility);

	DeleteConfessionButton btnDeleteConfession = new DeleteConfessionButton(confession, new Image("/images/sympathies.png",0,0,27,30), confession.isVisibleOnPublicWall());
	hPnlControls.add(btnDeleteConfession);

	PushButton btnShareConfession = new PushButton();
	btnShareConfession.addStyleName("userControlButtonContainer");
	btnShareConfession.setTitle("Ask for pardon from some one in your friends.");
	hPnlControls.add(btnShareConfession);
	if(confession != null && confession.getConfessedTo() != null && !confession.getConfessedTo().isEmpty()) {
	    btnShareConfession.setEnabled(false);
	} else {
	    btnShareConfession.addClickHandler(new ClickHandler() {
		ShareConfessionPopup shareConfessionPopup = new ShareConfessionPopup(confession);
		@Override
		public void onClick(ClickEvent event) {
		    shareConfessionPopup.populateFriendsList();
		    shareConfessionPopup.setGlassEnabled(true);
		    shareConfessionPopup.center();
		}
	    });
	}

	PushButton btnEditConfession = new PushButton();
	btnEditConfession.addStyleName("userControlButtonContainer");
	btnEditConfession.setTitle("Update your confession");
	hPnlControls.add(btnEditConfession);
	btnEditConfession.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		ConfessionBox.confEventBus.fireEvent(new EditConfessionEvent(confession));
	    }
	});

	return hPnlControls;
    }

    public static FlowPanel getTextTruncated(final String confession) {
	final FlowPanel fPnlConfession = new FlowPanel();
	fPnlConfession.setStyleName(Constants.STYLE_CLASS_CONFESSION_BODY);
	if(confession != null) {
	    if(confession.length() > Constants.TEXT_LENGTH_ON_LOAD) {
		final HTML lblConfession = new HTML(confession);
		lblConfession.setStyleName("confessionText");
		lblConfession.setHeight("80px");
		fPnlConfession.add(lblConfession);

		final Anchor anchMore = new Anchor("more..");
		anchMore.setStyleName("moreLink");
		fPnlConfession.add(anchMore);

		anchMore.addClickHandler(new ClickHandler() {

		    @Override
		    public void onClick(ClickEvent event) {
			if("more..".equalsIgnoreCase(anchMore.getText())) {
			    lblConfession.setHeight("auto");
			    anchMore.setText("less..");
			} else {
			    lblConfession.setHeight("80px");
			    anchMore.setText("more..");
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
	    lstFilterOptions.addItem("All confessions", Filters.ALL.name());
	    lstFilterOptions.addItem("Hidden identity", Filters.CLOSED .name());
	    lstFilterOptions.addItem("Your language", Filters.LOCALE_SPECIFIC.name());
	    lstFilterOptions.addItem("Most 'SAME BOAT' voted", Filters.MOST_SAME_BOATS.name());
	    lstFilterOptions.addItem("Most 'LAME' voted", Filters.MOST_LAME.name());
	    lstFilterOptions.addItem("Most 'SYMPATHAISED' voted", Filters.MOST_SYMPATHY.name());
	    lstFilterOptions.addItem("Most 'SHOULD BE PARDONED' voted", Filters.MOST_SHOULD_BE_PARDONED.name());
	    lstFilterOptions.addItem("Open identity", Filters.OPEN.name());
	    lstFilterOptions.addItem("Confessions with your activity", Filters.USER_ACTIVITY.name());
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
		return "less than a min ago";
	    } else if(seconds >= 60 && seconds < 3600) {
		return minuts + " minuts ago";
	    } else {
		return hours + " hours ago";
	    }
	} else {
	    return DateTimeFormat.getFormat("EEE, MMM d, ''yy - h:mm a").format(date);
	}
    }

    public static String getDateInFormat(Date date) {
	return DateTimeFormat.getFormat("EEE, MMM d, ''yy - h:mm a").format(date);
    }


    public static Widget getEmptyWidget() {
	Label lblEmptyPage = new Label("No confessions for you in this view.");
	return lblEmptyPage;
    }

    public static FlowPanel getStatusBar(Confession confession) {
	FlowPanel fPnlStatusBar = new FlowPanel();
	fPnlStatusBar.setStyleName("status_bar");

	// Subscribe link
	fPnlStatusBar.add(new SubscribeAnchor(confession.getConfId()));

	// Time stamp
	Label lblDateTimeStamp = new Label("| " + CommonUtils.getDateInAGOFormat(confession.getTimeStamp()));
	lblDateTimeStamp.setStyleName("time_stamp");
	fPnlStatusBar.add(lblDateTimeStamp);

	return fPnlStatusBar;
    }

    public static Widget getUndoToolTipBar() {
	FlowPanel fPnlToolTipBar = new FlowPanel();
	fPnlToolTipBar.setStyleName("tooltip_left");
	Anchor ancHelpInfo = new Anchor("[?]");
	Label lblUndoToolTip = new Label("Click again to undo");
	fPnlToolTipBar.add(lblUndoToolTip);
	fPnlToolTipBar.add(ancHelpInfo);
	return fPnlToolTipBar;
    }

    public static Widget getShareToolTipBar() {
	FlowPanel fPnlToolTipBar = new FlowPanel();
	fPnlToolTipBar.setStyleName("tooltip_right");
	Anchor ancShare = new Anchor("Share");
	Label lblShareToolTip = new Label("all ur votes on your wall");
	Anchor ancHelpInfo = new Anchor("[?]");
	fPnlToolTipBar.add(ancShare);
	fPnlToolTipBar.add(lblShareToolTip);
	fPnlToolTipBar.add(ancHelpInfo);
	return fPnlToolTipBar;
    }

    public static Label getPardonStatus(Confession confession) {
	if(confession != null && confession.getConfessedTo() != null && !confession.getConfessedTo().isEmpty()) {
	    Label pardonStatus = new Label();
	    for (ConfessionShare confessionShare : confession.getConfessedTo()) {
		if(confessionShare != null && confessionShare.getPardonStatus() != null) {
		    switch (confessionShare.getPardonStatus()) {
		    case PARDONED:
			pardonStatus.setText("PARDONED");
			pardonStatus.setStyleName("pardon_status_yes");
			return pardonStatus;
		    default:
			pardonStatus.setText("Awaiting PARDONED");
			pardonStatus.setStyleName("pardon_status_no");
			return pardonStatus;
		    }
		}
	    }
	}
	return null;
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
