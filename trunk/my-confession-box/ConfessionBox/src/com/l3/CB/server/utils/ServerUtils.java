package com.l3.CB.server.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l3.CB.server.manager.CacheManager;
import com.l3.CB.shared.FacebookUtil;

public class ServerUtils {

    static Logger logger = Logger.getLogger("CBLogger");

    //  public static final String SECRET = "3b8b8583f2e3a3b239068120431ca82d"; // BETA replace with real values from Facebook app configuration
//    public static final String SECRET = "b22214ad8c4e60978e80b23d84279ac1"; // LIVE replace with real values from Facebook app configuration
        public static final String SECRET = "5542ac99210548f480795313bba6a831"; // ALFA replace with real values from Facebook app configuration

    public static String getAccessTokenUrl(final String authCode) {
	final StringBuilder sb = new StringBuilder(FacebookUtil.FB_GRAPH_URL);
	sb.append("oauth/");
	sb.append("access_token?client_id=").append(FacebookUtil.APPLICATION_ID);
	sb.append("&redirect_uri=").append(FacebookUtil.APP_REDIRECT_URL);
	sb.append("&client_secret=").append(SECRET);
	sb.append("&code=").append(authCode);
	return sb.toString();
    }

    public static String encode(final String text) {
	try {
	    return URLEncoder.encode(text, "UTF-8");
	} catch (final UnsupportedEncodingException e) {
	    throw new IllegalStateException(e);
	}
    }

    public static String fetchURL(final String strUrl) {
	String retStr = CacheManager.getCachedJsonObject(strUrl);
	if("".equals(retStr)) {
	    try {
		final URL url = new URL(strUrl);
		final BufferedReader reader = new BufferedReader(
			new InputStreamReader(url.openStream()));
		String line;
		while ((line = reader.readLine()) != null) {
		    retStr += line;
		}
		reader.close();
	    } catch (final MalformedURLException e) {
		logger.log(Level.SEVERE, "MalformedURLException calling url" + strUrl, e);
	    } catch (final IOException e) {
		logger.log(Level.SEVERE, "IOException calling url" + strUrl, e);
	    }
	    CacheManager.cacheJsonObject(strUrl, retStr);
	}
	return retStr;
    }
}
