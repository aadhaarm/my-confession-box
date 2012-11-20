package com.l3.CB.shared;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.l3.CB.shared.TO.UserInfo;


public class FacebookUtil {

    //    public static final String APPLICATION_ID = "202435696462206"; // replace with real values from Facebook app configuration
    //    public static final String FB_OAUTH_URL = "https://www.facebook.com/dialog/oauth/";
    //    public static final String FB_GRAPH_URL = "https://graph.facebook.com/";
    //    public static final String FB_FRIENDS_URL = FB_GRAPH_URL + "me/friends?";
    //    public static final String FB_USER_URL = FB_GRAPH_URL + "me?";
    //    public static String REDIRECT_URL = "http://apps.facebook.com/fbconfessbeta/";

    //    public static final String APPLICATION_ID = "153945264667385"; // replace with real values from Facebook app configuration
    //    public static final String FB_OAUTH_URL = "https://www.facebook.com/dialog/oauth/";
    //    public static final String FB_GRAPH_URL = "https://graph.facebook.com/";
    //    public static final String FB_FRIENDS_URL = FB_GRAPH_URL + "me/friends?";
    //    public static final String FB_USER_URL = FB_GRAPH_URL + "me?";
    //    public static String REDIRECT_URL = "http://apps.facebook.com/fbconfess/";


    public static final String APPLICATION_ID = "171485962909999"; // replace with real values from Facebook app configuration
    public static final String FB_OAUTH_URL = "https://www.facebook.com/dialog/oauth/";
    public static final String FB_GRAPH_URL = "https://graph.facebook.com/";
    public static final String FB_FRIENDS_URL = FB_GRAPH_URL + "me/friends?";
    public static final String FB_USER_URL = FB_GRAPH_URL + "me?";
    public static String REDIRECT_URL = "http://apps.facebook.com/fbconfessalfa/";

    public static String getApplicationId() {
	return APPLICATION_ID;
    }

    public static String getAuthorizeUrl(String confId) {
	final StringBuilder sb = new StringBuilder(FB_OAUTH_URL);
	sb.append("authorize?client_id=").append(APPLICATION_ID);
	sb.append("&display=popup&redirect_uri=").append(REDIRECT_URL);
	sb.append("&state=").append(confId);
	return sb.toString();
    }

    public static String getAuthorizeUrl(String permission, int a) {
	final StringBuilder sb = new StringBuilder(FB_OAUTH_URL);
	sb.append("authorize?client_id=").append(APPLICATION_ID);
	sb.append("&display=popup&redirect_uri=").append(REDIRECT_URL);
	sb.append("&scope=").append(permission);
	return sb.toString();
    }

    public static String getAuthorizeUrl() {
	final StringBuilder sb = new StringBuilder(FB_OAUTH_URL);
	sb.append("authorize?client_id=").append(APPLICATION_ID);
	sb.append("&display=popup&redirect_uri=").append(REDIRECT_URL);
	return sb.toString();
    }

    public static String getFriendsUrl(final String authToken) {
	return FB_FRIENDS_URL + "access_token=" + authToken;
    }   

    public static String getUserUrl(final String authToken) {
	return FB_USER_URL + "access_token=" + authToken;
    }

    public static String getUserUrl(final String fbId, final String authToken) {
	return FB_GRAPH_URL + fbId + "?access_token=" + authToken;
    }

    public static String getUserImageUrl(String fbId) {
	final StringBuilder sb = new StringBuilder(FB_GRAPH_URL);
	sb.append(fbId);
	sb.append("/picture");
	return sb.toString();
    }

    public static String getUserByIdUrl(String fbId) {
	final StringBuilder sb = new StringBuilder(FB_GRAPH_URL);
	sb.append(fbId);
	return sb.toString();
    }

    public static UserInfo getUserInfo(String jsonString) {
	UserInfo userInfo = null;
	if(jsonString != null && !jsonString.isEmpty()) {
	    // parse the response text into JSON
	    JSONValue jsonValue = JSONParser.parseStrict(jsonString);
	    if(jsonValue != null) {
		JSONObject jsonObject = jsonValue.isObject();
		if(jsonObject != null) {
		    userInfo = new UserInfo();
		    userInfo.setId(getJSONStringValue(jsonObject.get("id")));
		    userInfo.setFirst_name(getJSONStringValue(jsonObject.get("first_name")));
		    userInfo.setLast_name(getJSONStringValue(jsonObject.get("last_name")));
		    userInfo.setLink(getJSONStringValue(jsonObject.get("link")));
		    userInfo.setName(getJSONStringValue(jsonObject.get("name")));
		    userInfo.setUsername(getJSONStringValue(jsonObject.get("username")));
		    userInfo.setGender(getJSONStringValue(jsonObject.get("gender")));
		}
	    }
	}
	return userInfo;
    }

    public static String getJSONStringValue(JSONValue jsonValue) {
	if(jsonValue != null) {
	    return jsonValue.isString().stringValue();
	}
	return null;
    }

    public static String getFaceIconImage(String gender) {
	final StringBuilder sb = new StringBuilder();
	if("male".equalsIgnoreCase(gender)) {
	    sb.append("/images/male.jpg");
	} else {
	    sb.append("/images/female.jpg");
	}
	return sb.toString();
    }

    public static String getFriendsListUrl(String accessToken) {
	final StringBuilder sb = new StringBuilder();
	sb.append(FB_GRAPH_URL).append("me/friends?access_token=").append(accessToken);
	return sb.toString();
    }

    public static String getActivityUrl(Long confId) {
	return REDIRECT_URL +"?conf="+ confId;
    }

    public static String getConfessionNotificationUrl() {
	return REDIRECT_URL + "#FeedToMe";
    }

    public static String getApplicationImage() {
	return "http://fbconfess.appspot.com/images/confession_box_smiley_face.jpg";
    }
}
