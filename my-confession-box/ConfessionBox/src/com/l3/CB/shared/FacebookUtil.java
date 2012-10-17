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
		if(jsonString != null) {
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
				}
			}
		}
		return userInfo;
	}
	
	public static String getString(JSONValue jsonValue) {
		if(jsonValue != null) {
			return jsonValue.isString().stringValue();
		}
		return null;
	}
	
	//"http://127.0.0.1:8888/images/Gerald_G_Boy_Face_Cartoon_4.svg.thumb.png"
	public static String getFaceIconImage(String gender) {
		final StringBuilder sb = new StringBuilder();
		if("male".equalsIgnoreCase(gender)) {
			sb.append("/images/boy50X50.png");
		} else {
			sb.append("/images/girl 50X50.png");
		}
        return sb.toString();
	}

	public static String getFriendsListUrl(String accessToken) {
		final StringBuilder sb = new StringBuilder();
		sb.append(FB_GRAPH_URL).append("me/friends?access_token=").append(accessToken);
		return sb.toString();
	}
}
