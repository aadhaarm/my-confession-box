package com.l3.CB.client.ui.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.shared.Constants;

public class ApplicationTextWidget {

    /**
     * ABOUT CONFESSION BOX
     */
    public static PopupPanel setupAbout() {
	final PopupPanel pPnlAbout = new PopupPanel(true);
	pPnlAbout.setGlassEnabled(true);
	pPnlAbout.setStyleName("infoModalPopupWindow");

	FlowPanel sPnlContent = new FlowPanel();

	PushButton close = new PushButton("Close");
	close.addStyleName("infoModalPopupWindowClose");
	close.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		pPnlAbout.hide();
	    }
	});
	sPnlContent.add(close);

	sPnlContent.add(new HTML("<b>About Confession Box</b><br/><br/>"
		+ "<b>Confession Box</b> is a worldwide online platform for people to <b>write confessions</b> to their friends, family-members, relatives, colleagues etc and <b>request for a Pardon</b> in return.<br/><br/>"
		+ "You may get a Pardon from the person to whom you have confessed, if he/she thinks your confession or act should get a pardon. Else you also get <b>public support</b> by people voting <b>'Same Boat', 'Sympathies', 'Should be pardoned'</b> etc making that person to rethink and grant you a pardon by pressing <b>PARDON</b> button.<br/><br/>"
		+ "And the best part is, <b>you can also confess anonymously</b>. Your identity can be hidden to the world except to the person you have requested for a pardon.<br/><br/>"
		+ "If you do not specifically wish to request for a pardon from someone and want to write an <b>open confession</b>, you can simply <b>confess to the world or god</b>. People from across the globe can express themselves by voting 'Sympathies', 'Same Boat', 'Lame', 'Should be Pardoned', 'Should not be Pardoned', depending upon your confession.<br/><br/>"
		+ "For every such <b>humane activity</b> of writing a confession, granting pardon, voting sympathies, same boat, sharing on FB wall etc, you earn <b>Human Points (HP)</b>. You can use your Human Points to get pardon votes, transfer HP to someone needy and lot more."));
	pPnlAbout.add(sPnlContent);

	return pPnlAbout;
    }

    /**
     * PILLOSPHY CONFESSION BOX
     */
    public static PopupPanel setupPhillosphy() {
	final PopupPanel pPnlAbout = new PopupPanel(true);
	pPnlAbout.setGlassEnabled(true);
	pPnlAbout.setStyleName("infoModalPopupWindow");

	FlowPanel sPnlContent = new FlowPanel();

	PushButton close = new PushButton("Close");
	close.addStyleName("infoModalPopupWindowClose");
	close.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		pPnlAbout.hide();
	    }
	});
	sPnlContent.add(close);

	sPnlContent.add(new HTML("<b>The Philosophy</b><br/><br/>"
		+ "Confession box is an endeavor to provide a platform for the world to express those emotions which are often embedded deep in our conscience and live there for ever. There are many who wish to gather courage to speak up and say the 2 simple words 'I am sorry' for their deeds which brought some discomfort or left a grave mark (good or bad) on someone's life..<br/><br/>" +
		"The world is filled with hearts which want to spread joy, health and smiles.. But the world is equally filled with wounded people who deserve to get a sorry from those who intentionally or unintentionally did that to them..<br/><br/>" +
		"There is a great relief.. An unsettling joy and a pleasant feeling brought upon any soul who gets the most coveted pardon from a friend, a brother, a father, a son, the boss, the uncle, the girl-friend, the wife.. We all just love when pardoned with a smile and patch up those invaluable relationships which were broken for no reasons..<br/><br/>" +
		"Like its said, its never too late to say sorry.. We present our friends with this platform known as Confession Box to say sorry to those to whom your act or words brought any discomfort in past or present. And to feel sympathetic towards those who are going through the same testing time of their life you once did..<br/><br/>" +
		"This platform is also much bigger than just apologies and getting pardons. Its a fun platform too. The tiny naughty day-to-day activities which makes you to show that grin and do annoying or naughty things to people.. Now just get away with that with a simple naughty confession to your friend or brother or colleague or husband or the neighbor..<br/><br/>" +
		"Lets pretend we are sorry.. ;)"));
	pPnlAbout.add(sPnlContent);

	return pPnlAbout;
    }

    /**
     * PILLOSPHY ANONYMOUS CONFESSION BOX
     */
    public static PopupPanel setupPhillosphyAnynConf() {
	final PopupPanel pPnlAbout = new PopupPanel(true);
	pPnlAbout.setGlassEnabled(true);
	pPnlAbout.setStyleName("infoModalPopupWindow");

	FlowPanel sPnlContent = new FlowPanel();

	PushButton close = new PushButton("Close");
	close.addStyleName("infoModalPopupWindowClose");
	close.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		pPnlAbout.hide();
	    }
	});
	sPnlContent.add(close);

	sPnlContent.add(new HTML("<b>Thoughts about Anonymous Confession</b><br/><br/>" +
		"Life is simply complex.. Many of us are often confused when it comes to emotions and feelings. Better we are confused as the world of relationships, love, emotions is a portrait of picasso, a poem by Rabindranath Tagore, the music of Beethoven.. Beautiful and intense..<br/><br/>" +
		"Our thought of Anonymity in confession brings a sense of freedom to apologize and bring our true emotions out in open. Let people know what you want to say to your father, your sister, your best friend, your boy friend, brother, uncle.. but anonymously.. and share the thought with the world..<br/><br/>" +
		"The world we know which tends to be violent, intolerant, materialistic will prove you wrong and tell you that what you wrote in your confession, your true feelings towards someone, some event etc...is also shared by almost every soul who reads your anonymous confession..<br/><br/>" +
		"The votes of 'Same Boat', 'Sympathies', 'Should be Pardoned' will boost your morale and make you feel elated since you find yourself not alone and embrace the blessings of so many strange yet known people via your heartfelt confession..<br/><br/>" +
		"So, write your first confession today, to your 6th standard crush, to your dad whom you love the most, to your best friend who is away and not-in-touch for a long time..<br/><br/>" +
		"Confession Box, speak your heart, live free...."));
	pPnlAbout.add(sPnlContent);

	return pPnlAbout;
    }    

    /**
     * 
     */
    public static PopupPanel setupPrivacy() {
	final PopupPanel pPnlPrivacy = new PopupPanel(false);
	pPnlPrivacy.setGlassEnabled(true);
	pPnlPrivacy.setStyleName("infoModalPopupWindow");

	FlowPanel sPnlContent = new FlowPanel();

	PushButton close = new PushButton("Close");
	close.addStyleName("infoModalPopupWindowClose");

	close.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		pPnlPrivacy.hide();
	    }
	});
	sPnlContent.add(close);

	sPnlContent.add(new HTML("<b>PRIVACY POLICY</b>: This Privacy Policy governs the manner in which Confession Box collects, uses, maintains and discloses information collected from users (each, a \"User\") of the http://apps.facebook.com/fbconfess#privacy website (\"Site\"). This privacy policy applies to the Site and all products and services offered by Confession Box.<br/>" +
		"<b>Personal identification information</b><br/>" +
		"We may collect personal identification information from Users in a variety of ways, including, but not limited to, when Users visit our site, register on the site, and in connection with other activities, services, features or resources we make available on our Site. Users may be asked for, as appropriate, name. Users may, however, visit our Site anonymously. We will collect personal identification information from Users only if they voluntarily submit such information to us. Users can always refuse to supply personally identification information, except that it may prevent them from engaging in certain Site related activities.<br/>" +
		"<b>Non-personal identification information</b><br/>" +
		"We may collect non-personal identification information about Users whenever they interact with our Site. Non-personal identification information may include the browser name, the type of computer and technical information about Users means of connection to our Site, such as the operating system and the Internet service providers utilized and other similar information.<br/>" +
		"<b>Web browser cookies</b><br/>" +
		"Our Site may use \"cookies\" to enhance User experience. User's web browser places cookies on their hard drive for record-keeping purposes and sometimes to track information about them. User may choose to set their web browser to refuse cookies, or to alert you when cookies are being sent. If they do so, note that some parts of the Site may not function properly.<br/>" +
		"<b>How we use collected information</b><br/>" +
		"Confession Box may collect and use Users personal information for the following purposes:-<br/>" +
		"<b>To improve customer service</b><br/>" +
		"Information you provide helps us respond to your customer service requests and support needs more efficiently.<br/>" +
		"<b>How we protect your information</b><br/>" +
		"We adopt appropriate data collection, storage and processing practices and security measures to protect against unauthorized access, alteration, disclosure or destruction of your personal information, username, password, transaction information and data stored on our Site.<br/>" +
		"Sensitive and private data exchange between the Site and its Users happens over a SSL secured communication channel and is encrypted and protected with digital signatures.<br/>" +
		"<b>Sharing your personal information</b><br/>" +
		"We do not sell, trade, or rent Users personal identification information to others. We may share generic aggregated demographic information not linked to any personal identification information regarding visitors and users with our business partners, trusted affiliates and advertisers for the purposes outlined above.<br/>" +
		"<b>Compliance with children's online privacy protection act</b><br/>" +
		"Protecting the privacy of the very young is especially important. For that reason, we never collect or maintain information at our Site from those we actually know are under 13, and no part of our website is structured to attract anyone under 13.<br/>" +
		"<b>Changes to this privacy policy</b><br/>" +
		"Confession Box has the discretion to update this privacy policy at any time. When we do, we will send you an email. We encourage Users to frequently check this page for any changes to stay informed about how we are helping to protect the personal information we collect. You acknowledge and agree that it is your responsibility to review this privacy policy periodically and become aware of modifications.<br/>" +
		"<b>Your acceptance of these terms</b><br/>" +
		"By using this Site, you signify your acceptance of this policy. If you do not agree to this policy, please do not use our Site. Your continued use of the Site following the posting of changes to this policy will be deemed your acceptance of those changes.<br/>" +
		"<b>Contacting us</b><br/>" +
		"If you have any questions about this Privacy Policy, the practices of this site, or your dealings with this site, please contact us at:<br/>" +
		"Confession Box http://apps.facebook.com/fbconfess#privacy<br/>" +
		"This document was last updated on March 03, 2013<br/>" +
		"Privacy policy created by Generate Privacy Policy"));
	pPnlPrivacy.add(sPnlContent);
	return pPnlPrivacy;
    }

    /**
     * 
     */
    public static PopupPanel setupCBRuleBook() {
	final PopupPanel pPnlRuleBook = new PopupPanel(true);
	pPnlRuleBook.setGlassEnabled(true);
	pPnlRuleBook.setStyleName("infoModalPopupWindow");

	FlowPanel sPnlContent = new FlowPanel();

	PushButton close = new PushButton("Close");
	close.addStyleName("infoModalPopupWindowClose");

	close.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		pPnlRuleBook.hide();
	    }
	});
	sPnlContent.add(close);
	sPnlContent.add(new HTML("<b>CB Rule-Book</b><br/><br/>"));

	SafeUri reportAbuseIconSafeUri = new SafeUri() {
	    @Override
	    public String asString() {
		return Constants.IMAGE_REPORT_ABUSE_ICON;
	    }
	};
	SafeUri lameIconSafeUri = new SafeUri() {
	    @Override
	    public String asString() {
		return Constants.IMAGE_LAME_ICON;
	    }
	};
	SafeUri sameBoatIconSafeUri = new SafeUri() {
	    @Override
	    public String asString() {
		return Constants.IMAGE_SAME_BOAT_ICON;
	    }
	};
	SafeUri shouldBePardonedIconSafeUri = new SafeUri() {
	    @Override
	    public String asString() {
		return Constants.IMAGE_SHOULD_BE_PARDONED_ICON;
	    }
	};
	SafeUri shouldNotBePardonedIconSafeUri = new SafeUri() {
	    @Override
	    public String asString() {
		return Constants.IMAGE_SHOULD_NOT_BE_PARDONED_ICON;
	    }
	};
	SafeUri sympathyIconSafeUri = new SafeUri() {
	    @Override
	    public String asString() {
		return Constants.IMAGE_SYMPATHY_ICON;
	    }
	};

	sPnlContent.add(new HTML(Templates.TEMPLATES.voteHelp(reportAbuseIconSafeUri, ConfessionBox.cbText.abuseButtonToolTip())));
	sPnlContent.add(new HTML(Templates.TEMPLATES.voteHelp(lameIconSafeUri, ConfessionBox.cbText.lameButtonToolTip())));
	sPnlContent.add(new HTML(Templates.TEMPLATES.voteHelp(sameBoatIconSafeUri, ConfessionBox.cbText.sameBoatButtonToolTip())));
	sPnlContent.add(new HTML(Templates.TEMPLATES.voteHelp(shouldBePardonedIconSafeUri, ConfessionBox.cbText.shouldBePardonedButtonToolTip())));
	sPnlContent.add(new HTML(Templates.TEMPLATES.voteHelp(shouldNotBePardonedIconSafeUri, ConfessionBox.cbText.shouldNotBePardonedButtonToolTip())));
	sPnlContent.add(new HTML(Templates.TEMPLATES.voteHelp(sympathyIconSafeUri, ConfessionBox.cbText.sympathyButtonToolTip())));

	sPnlContent.add(new HTML("<hr/>1. Confession Box is a secure application and your identity is never disclosed to anyone unless you yourself share your identity to the world.<br/>" +
		"2. You can read all the confessions on the 'Confession Wall' without logging-in on the CB and without providing any of your informations to CB.<br/>" +
		"3. If you register a confession with hidden identity, your identity can not be discovered by any one other than you (Unless you write your details in the confession text).<br/>" +
		"4. You can confess and appeal for pardon from a person in your facebook friend's list. A notification is sent to the person via email if the person is on CB.<br/>" +
		"5. If you appeal for pardon to someone, the person is informed about the confession along with your identity and a link to confession.<br/>" +
		"6. If some one has confessed to you, a notofication is sent to you. You can visit and check the confession and pardon for the same if you may.<br/>" +
		"7. While pardonning, you can set some conditions that should be met for the confession to be pardoned.<br/>" +
		"8. When all the conditions are met, the confession is pardoned and you and the confessee are notified about the pardon!<br/>" +
		"9. You can also subscribe a confession by clicking the 'Subscribe' link. You are notified about the confession when it is pardoned.<br/>" +
		"10. You are provided 'Human Points' for all the activities you do on CB that shall be a count of how good a human you are."));


	pPnlRuleBook.add(sPnlContent);

	return pPnlRuleBook;
    }
}
