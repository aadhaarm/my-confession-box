package com.l3.CB.client.ui.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;

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
     * PILLOSPHY CONFESSION BOX
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
	final PopupPanel pPnlPrivacy = new PopupPanel(true);
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
	
	sPnlContent.add(new HTML("PRIVACY POLICY: In a professional context it often happens that private or corporate " +
		"clients corder a publication to be made and presented with the actual content still not being ready. " +
		"Think of a news blog that's filled with content hourly on the day of going live. " +
		"However, reviewers tend to be distracted by comprehensible content, say, a random text " +
		"copied from a newspaper or the internet. The are likely to focus on the text, disregarding " +
		"the layout and its elements. Besides, random text risks to be unintendedly humorous or offensive, " +
		"an unacceptable risk in corporate environments. Lorem ipsum and its many variants have been employed since " +
		"the early 1960ies, and quite likely since the sixteenth century."));
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

	sPnlContent.add(new HTML("<b>CB Rule-Book</b><br/><br/>" +
		"1. Confession Box is a secure application and your identity is never disclosed to anyone unless you yourself share your identity to the world.<br/>" +
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
