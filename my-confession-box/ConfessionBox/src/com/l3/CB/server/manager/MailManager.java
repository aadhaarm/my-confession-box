package com.l3.CB.server.manager;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.UserInfo;

public class MailManager {

    static Logger logger = Logger.getLogger("CBLogger");
    static String fromMailAddress = "fbconfess@appspot.gserviceaccount.com";

    public static boolean sendConfessionEmail(UserInfo confessionToUser, UserInfo confessionByUser, long confId) {
	Properties props = new Properties();
	Session session = Session.getDefaultInstance(props, null);
	String msgBody = getConfessionEmailMsgBody(confId, confessionByUser.getName(), confessionToUser.getName());
	try {
	    Message msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(fromMailAddress, "Confession Box"));
	    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(confessionToUser.getEmail(), confessionToUser.getName()));
	    msg.addRecipient(Message.RecipientType.CC, new InternetAddress(confessionByUser.getEmail(), confessionByUser.getName()));
	    msg.setSubject("Someone has confessed to you");
	    msg.setText(msgBody);
	    Transport.send(msg);

	} catch (AddressException e) {
	    logger.log(Constants.LOG_LEVEL,
		    "Exception in MailManager.sendFormSaveEmail()" + e.getCause());
	} catch (MessagingException e) {
	    logger.log(Constants.LOG_LEVEL,
		    "Exception in MailManager.sendFormSaveEmail()" + e.getCause());
	} catch (UnsupportedEncodingException e) {
	    logger.log(Constants.LOG_LEVEL,
		    "Exception in MailManager.sendFormSaveEmail()" + e.getCause());
	}
	return true;
    }

    private static String getConfessionEmailMsgBody(long confId, String confessssionBy, String confessionTo){
	final StringBuilder sb = new StringBuilder();
	sb.append("Dear ").append(confessionTo).append(",\n");
	sb.append(confessssionBy).append(" has confessed to you.\n");
	sb.append("To see what ").append(confessssionBy).append(" has confessed, please check the link ");
	sb.append(FacebookUtil.REDIRECT_URL).append("?conf=").append(confId);
	return sb.toString();
    }

    public static void sendPardonMail(UserInfo pardonedByUser, Long confId, UserInfo pardonedToUser) {
	Properties props = new Properties();
	Session session = Session.getDefaultInstance(props, null);
	String msgBody = getPardonesEmailMsgBody(pardonedByUser, confId, pardonedToUser);
	try {
	    Message msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(fromMailAddress, "Confession Box"));
	    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(pardonedToUser.getEmail(), pardonedToUser.getName()));
	    msg.addRecipient(Message.RecipientType.CC, new InternetAddress(pardonedByUser.getEmail(), pardonedByUser.getName()));
	    msg.setSubject("You have been pardoned!");
	    msg.setText(msgBody);
	    Transport.send(msg);

	} catch (AddressException e) {
	    logger.log(Constants.LOG_LEVEL,
		    "Exception in MailManager.sendFormSaveEmail()" + e.getCause());
	} catch (MessagingException e) {
	    logger.log(Constants.LOG_LEVEL,
		    "Exception in MailManager.sendFormSaveEmail()" + e.getCause());
	} catch (UnsupportedEncodingException e) {
	    logger.log(Constants.LOG_LEVEL,
		    "Exception in MailManager.sendFormSaveEmail()" + e.getCause());
	}
    }

    private static String getPardonesEmailMsgBody(UserInfo pardonedByUser,
	    Long confId, UserInfo pardonedToUser) {
	final StringBuilder sb = new StringBuilder();
	sb.append("Dear ").append(pardonedToUser.getName()).append(",\n");
	sb.append("You have been pardoned by ").append(pardonedByUser.getName()).append("\n");
	sb.append("Please check the link ");
	sb.append(FacebookUtil.REDIRECT_URL).append("?conf=").append(confId);
	return sb.toString();	
    }

    public static void sendSubscriptionMail(UserInfo subscriberUserInfo, Long confId) {
	Properties props = new Properties();
	Session session = Session.getDefaultInstance(props, null);
	String msgBody = getSubscriptionEmailMsgBody(subscriberUserInfo, confId);
	try {
	    Message msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(fromMailAddress, "Confession Box"));
	    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(subscriberUserInfo.getEmail(), subscriberUserInfo.getName()));
	    msg.setSubject("Your subscribed confession has been pardoned!");
	    msg.setText(msgBody);
	    Transport.send(msg);
	} catch (AddressException e) {
	    logger.log(Constants.LOG_LEVEL,
		    "Exception in MailManager.sendFormSaveEmail()" + e.getCause());
	} catch (MessagingException e) {
	    logger.log(Constants.LOG_LEVEL,
		    "Exception in MailManager.sendFormSaveEmail()" + e.getCause());
	} catch (UnsupportedEncodingException e) {
	    logger.log(Constants.LOG_LEVEL,
		    "Exception in MailManager.sendFormSaveEmail()" + e.getCause());
	}
    }

    private static String getSubscriptionEmailMsgBody(UserInfo subscribedUser, Long confId) {
	final StringBuilder sb = new StringBuilder();
	sb.append("Dear ").append(subscribedUser.getName()).append(",\n");
	sb.append("Confession you have subscribed to has been pardoned").append("\n");
	sb.append("Please check the link ");
	sb.append(FacebookUtil.REDIRECT_URL).append("?conf=").append(confId);
	return sb.toString();	
    }
}