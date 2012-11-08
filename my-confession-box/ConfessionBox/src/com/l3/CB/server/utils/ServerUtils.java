package com.l3.CB.server.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.l3.CB.shared.FacebookUtil;

public class ServerUtils {
    //	private static final Log logger = LogFactory.getLog(ServerUtils.class);

    public static final String SECRET = "3b8b8583f2e3a3b239068120431ca82d"; // BETA replace with real values from Facebook app configuration
//    	public static final String SECRET = "5542ac99210548f480795313bba6a831"; // ALFA replace with real values from Facebook app configuration


    public static String getAccessTokenUrl(final String authCode) {
	final StringBuilder sb = new StringBuilder(FacebookUtil.FB_GRAPH_URL);
	sb.append("oauth/");
	sb.append("access_token?client_id=").append(FacebookUtil.APPLICATION_ID);
	sb.append("&redirect_uri=").append(FacebookUtil.REDIRECT_URL);
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

    public static String fetchURL(final String u) {
	String retStr = "";
	try {
	    final URL url = new URL(u);
	    final BufferedReader reader = new BufferedReader(
		    new InputStreamReader(url.openStream()));
	    String line;
	    while ((line = reader.readLine()) != null) {
		retStr += line;
	    }
	    reader.close();

	} catch (final MalformedURLException e) {
	    //            logger.error("MalformedURLException calling url" + u, e);
	} catch (final IOException e) {
	    //            logger.error("IOException calling url" + u, e);
	}
	return retStr;
    }
}
