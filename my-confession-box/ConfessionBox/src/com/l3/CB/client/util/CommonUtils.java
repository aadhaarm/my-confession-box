/**
 * L3 Confession Box
 * 
 * Oct 13, 2012, 9:59:38 AM
 */
package com.l3.CB.client.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.ui.widgets.ChangeVisibilityButton;
import com.l3.CB.client.ui.widgets.DeleteConfessionButton;
import com.l3.CB.shared.CBText;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class CommonUtils {

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

	public static native String login(Element elementId) /*-{
		if($wnd.FB) {
	  		$wnd.FB.login(function(response) {
		   if (response.authResponse) {
	   		 elementId.innerHTML = response.authResponse.accessToken;
		   } else {
		     console.log('User cancelled login or did not fully authorize.');
		   }
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
					userInfo.setLocale(getString(jsonObject.get("locale")));
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
	
	// <fb:profile-pic uid="12345" width="32" height="32" linked="true"
	// \><fb:profile-pic>
	public static String getProfilePicture(Confession confession, boolean isAnyn) {
		final StringBuilder sb = new StringBuilder();

		if (!confession.isShareAsAnyn() || !isAnyn) {
			sb.append("<fb:profile-pic uid=\"")
			.append(confession.getFbId())
			.append("\"linked=\"true\"></fb:profile-pic>");
		} else {
			sb.append("<img src=");
			sb.append("'")
			.append(FacebookUtil.getFaceIconImage(confession
					.getGender())).append("'");
			sb.append("/>");
		}
		return sb.toString();
	}
	
	public static Widget getName(Confession confession, UserInfo userInfo, boolean isAnyn, CBText cbText) {
		Widget nameWidget = null;
		if(confession != null) {
			if (!confession.isShareAsAnyn() || ! isAnyn) {
				if (userInfo != null) {
					Anchor ancUserName = new Anchor(userInfo.getName(),	userInfo.getLink());
					ancUserName.addStyleName(Constants.STYLE_CLASS_CONFESSED_BY);
					nameWidget = ancUserName;
				}
			} else {
				Label lblConf = new Label();
				lblConf.addStyleName(Constants.STYLE_CLASS_CONFESSED_BY);
				lblConf.setText(cbText.confessedByAnynName());
				nameWidget = lblConf;
			}
		}
		return nameWidget;
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

	public static Widget getUserControls(Confession confession, UserInfo loggedInUserInfo, ConfessionServiceAsync confessionService) {
		HorizontalPanel hPnlControls = new HorizontalPanel();
		
		ChangeVisibilityButton btnChangeVisibility = new ChangeVisibilityButton(confession, loggedInUserInfo, confessionService, "",new Image("/images/sympathies.png",0,0,27,30), confession.isShareAsAnyn());
		hPnlControls.add(btnChangeVisibility);

		DeleteConfessionButton btnDeleteConfession = new DeleteConfessionButton(confession, loggedInUserInfo, confessionService, "",new Image("/images/sympathies.png",0,0,27,30), confession.isVisibleOnPublicWall());
		hPnlControls.add(btnDeleteConfession);
		
		return hPnlControls;
	}

	public static Widget getTextTruncated(final String confession) {
		final FlowPanel fPnlConfession = new FlowPanel();
		if(confession != null) {
			final Anchor anchMore = new Anchor("more..");

			if(confession.length() > Constants.TEXT_LENGTH_ON_LOAD) {
				Label lblConfession = new Label(confession.substring(0, Constants.TEXT_LENGTH_ON_LOAD));
				fPnlConfession.add(lblConfession);
				fPnlConfession.add(anchMore);
				
				anchMore.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						Label lblConfession = new Label(confession.substring(Constants.TEXT_LENGTH_ON_LOAD));
						fPnlConfession.remove(anchMore);
						fPnlConfession.add(lblConfession);
					}
				});
			} else {
				Label lblConfession = new Label(confession);
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
}
