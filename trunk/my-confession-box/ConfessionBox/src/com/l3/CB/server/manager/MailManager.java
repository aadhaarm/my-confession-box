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
	static String fromMailId = "fbconfess@facebook.com";

	public static boolean sendConfessionEmail(String confessionToEmail, long confId, String confessssionByName, String confessionToName) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String msgBody = getConfessionEmailMsgBody(confId, confessssionByName, confessionToName);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromMailId, "Confession Box (fbconfess admin)"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(confessionToEmail, "Dear " + confessionToName));
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
		return false;
	}

	private static String getConfessionEmailMsgBody(long confId, String confessssionBy, String confessionTo){
		final StringBuilder sb = new StringBuilder();
		sb.append("Dear ").append(confessionTo).append(",\n");
		sb.append(confessssionBy).append(" has confessed to you.\n");
		sb.append("To see what ").append(confessssionBy).append(" has confessed, please check the link ");
		sb.append("<a href='");
		sb.append(FacebookUtil.REDIRECT_URL).append("?conf=").append(confId);
		sb.append("'>").append("Confession Box").append("</a>");
		return sb.toString();
	}

	public static void sendPardonMail(UserInfo pardonedByUser, Long confId, UserInfo pardonedToUser) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String msgBody = getPardonesEmailMsgBody(pardonedByUser, confId, pardonedToUser);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromMailId, "Confession Box (fbconfess admin)"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(pardonedToUser.getUsername()+"@facebook.com", "Dear " + pardonedToUser.getName()));
			msg.setSubject("You have been pardoned");
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
		sb.append("<a href='");
		sb.append(FacebookUtil.REDIRECT_URL).append("?conf=").append(confId);
		sb.append("'>").append("Confession Box").append("</a>");
		return sb.toString();	
	}
}
