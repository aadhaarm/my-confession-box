package com.l3.CB.client.util;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Element;
import com.l3.CB.shared.TO.UserInfo;


public class CommonUtils {

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
}
