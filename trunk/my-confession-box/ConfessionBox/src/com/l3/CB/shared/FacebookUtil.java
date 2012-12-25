package com.l3.CB.shared;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeUri;
import com.l3.CB.server.utils.ServerUtils;
import com.l3.CB.shared.TO.UserInfo;


public class FacebookUtil {

    //    public static final String APPLICATION_ID = "202435696462206"; // replace with real values from Facebook app configuration
    //    public static final String FB_OAUTH_URL = "https://www.facebook.com/dialog/oauth/";
    //    public static final String FB_GRAPH_URL = "https://graph.facebook.com/";
    //    public static final String FB_FRIENDS_URL = FB_GRAPH_URL + "me/friends?";
    //    public static final String FB_USER_URL = FB_GRAPH_URL + "me?";
    //    public static String REDIRECT_URL = "http://apps.facebook.com/fbconfessbeta/";

        public static final String APPLICATION_ID = "153945264667385"; // replace with real values from Facebook app configuration
        public static final String FB_OAUTH_URL = "https://www.facebook.com/dialog/oauth/";
        public static final String FB_GRAPH_URL = "https://graph.facebook.com/";
        public static final String FB_FRIENDS_URL = FB_GRAPH_URL + "me/friends?";
        public static final String FB_USER_URL = FB_GRAPH_URL + "me?";
        public static String FB_APP_URL = "http://apps.facebook.com/fbconfess/";
        public static String APP_REDIRECT_URL = "http://www.fbconfess.com/";

//    public static final String APPLICATION_ID = "171485962909999"; // replace with real values from Facebook app configuration
//    public static final String FB_OAUTH_URL = "https://www.facebook.com/dialog/oauth/";
//    public static final String FB_GRAPH_URL = "https://graph.facebook.com/";
//    public static final String FB_FRIENDS_URL = FB_GRAPH_URL + "me/friends?";
//    public static final String FB_USER_URL = FB_GRAPH_URL + "me?";
//    public static String FB_APP_URL = "http://apps.facebook.com/fbconfessalfa/";
//    public static String APP_REDIRECT_URL = "http://localhost:8888/";

    public static String getApplicationId() {
	return APPLICATION_ID;
    }

    /**
     * https://graph.facebook.com/oauth/access_token?client_id=YOUR_APP_ID&client_secret=YOUR_APP_SECRET&grant_type=client_credentials
     * @param confId
     * @return
     */
    public static String getAuthorizeUrl(String confId) {
	final StringBuilder sb = new StringBuilder(FB_OAUTH_URL);
	sb.append("?client_id=").append(APPLICATION_ID);
	sb.append("&display=page&redirect_uri=").append(APP_REDIRECT_URL);
	sb.append("&state=").append(confId);
	return sb.toString();
    }

    public static String getAuthorizeUrl() {
	final StringBuilder sb = new StringBuilder(FB_OAUTH_URL);
	sb.append("?client_id=").append(APPLICATION_ID);
	sb.append("&display=page&redirect_uri=").append(APP_REDIRECT_URL);
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
	return FB_APP_URL +"?conf="+ confId;
    }

    public static String getConfessionNotificationUrl() {
	return FB_APP_URL + "?" + Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED;
    }

    public static String getApplicationImage() {
	return "http://fbconfess.appspot.com/images/confession_box_smiley_face.jpg";
    }

    /**
     * @param confessionShare
     * @return
     */
    public static SafeUri getProfileFBLink(final String fbId) {
	SafeUri safeUri = new SafeUri() {
	    @Override
	    public String asString() {
		return "http://www.facebook.com/" + fbId;
	    }
	};
	return safeUri;
    }

    public static String getAccessTokenUrl(final String authCode) {
	final StringBuilder sb = new StringBuilder(FB_GRAPH_URL);
	sb.append("oauth/");
	sb.append("access_token?client_id=").append(APPLICATION_ID);
	sb.append("&redirect_uri=").append(APP_REDIRECT_URL);
	sb.append("&client_secret=").append(ServerUtils.SECRET);
	sb.append("&code=").append(authCode);
	return sb.toString();
    }
}