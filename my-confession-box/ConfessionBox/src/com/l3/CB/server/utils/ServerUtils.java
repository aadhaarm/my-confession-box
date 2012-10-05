package com.l3.CB.server.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ServerUtils {
//	private static final Log logger = LogFactory.getLog(ServerUtils.class);

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
