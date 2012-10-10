package com.l3.CB.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Element;
import com.l3.CB.shared.TO.UserInfo;

public class CommonUtils {

	Logger logger = Logger.getLogger("CBLogger");
	
	// redirect the browser to the given url
	public static native void redirect(String url)/*-{
        $wnd.top.location = url;
    }-*/;

	public static native void parseXFBMLJS(Element element) /*-{
	  $wnd.FB.XFBML.parse(element);
	}-*/;

	public static native void parseXFBMLJS() /*-{
	  $wnd.FB.XFBML.parse();
	}-*/;

	public static String getString(JSONValue jsonValue) {
		if(jsonValue != null) {
			return jsonValue.isString().stringValue();
		}
		return null;
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
	
	public static Map<String, UserInfo> getFriendsUserInfo(String jsonString) {
		Map<String, UserInfo> friends = null;
		if(jsonString != null) {
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
		return friends;
	}

}
