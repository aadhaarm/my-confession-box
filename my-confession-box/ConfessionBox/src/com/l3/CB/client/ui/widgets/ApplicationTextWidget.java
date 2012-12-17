package com.l3.CB.client.ui.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class ApplicationTextWidget {

    /**
     * 
     */
    public static PopupPanel setupAbout() {
	final PopupPanel pPnlAbout = new PopupPanel(true);
	pPnlAbout.setGlassEnabled(true);
	pPnlAbout.setStyleName("infoModalPopupWindow");

	FlowPanel sPnlContent = new FlowPanel();
	sPnlContent.add(new HTML("ABOUT Confession Box: In a professional context it often happens that private or corporate " +
		"clients corder a publication to be made and presented with the actual content still not being ready. " +
		"Think of a news blog that's filled with content hourly on the day of going live. " +
		"However, reviewers tend to be distracted by comprehensible content, say, a random text " +
		"copied from a newspaper or the internet. The are likely to focus on the text, disregarding " +
		"the layout and its elements. Besides, random text risks to be unintendedly humorous or offensive, " +
		"an unacceptable risk in corporate environments. Lorem ipsum and its many variants have been employed since " +
		"the early 1960ies, and quite likely since the sixteenth century."));
	Button close = new Button("CLOSE");
	close.addClickHandler(new ClickHandler() {
	    
	    @Override
	    public void onClick(ClickEvent event) {
		pPnlAbout.hide();
	    }
	});
	sPnlContent.add(close);
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
	sPnlContent.add(new HTML("PRIVACY POLICY: In a professional context it often happens that private or corporate " +
		"clients corder a publication to be made and presented with the actual content still not being ready. " +
		"Think of a news blog that's filled with content hourly on the day of going live. " +
		"However, reviewers tend to be distracted by comprehensible content, say, a random text " +
		"copied from a newspaper or the internet. The are likely to focus on the text, disregarding " +
		"the layout and its elements. Besides, random text risks to be unintendedly humorous or offensive, " +
		"an unacceptable risk in corporate environments. Lorem ipsum and its many variants have been employed since " +
		"the early 1960ies, and quite likely since the sixteenth century."));
	Button close = new Button("CLOSE");
	close.addClickHandler(new ClickHandler() {
	    
	    @Override
	    public void onClick(ClickEvent event) {
		pPnlPrivacy.hide();
	    }
	});
	sPnlContent.add(close);
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
	sPnlContent.add(new HTML("CB Rule-Book<hr/>" +
		"1. Confession Box is a secure application and your identity is never disclosed to anyone unless you yourself share your identity to the world.<br/>" +
		"2. You can read all the confessions on the 'Confession Wall' without logging-in on the CB and without providing ant of your informations to CB.<br/>" +
		"3. If you register a confession with hidden identity, your identity can not be discovered by any one other than you (Unless you write your details in the confession text).<br/>" +
		"4. You can confess and appeal for pardon from a person in your facebook friend's list. A notification is sent to the person via email if the person is on CB.<br/>" +
		"5. If you appeal for pardon to someone, the person is informed about the confession along with your identity to this person.<br/>" +
		"6. If some one has confessed to you, a notofication is sent to you. You can visit and check the confession and pardon for the same if you may.<br/>" +
		"7. While pardonning, you can set some conditions that should be met for the confession to be pardoned.<br/>" +
		"8. When all the conditions are met, the confession is pardoned and you and the confessee is notified about the pardon!<br/>" +
		"9. You can also subscribe a confession by clicking the 'Subscribe' link. You are notified about the confession when it is pardoned.<br/>" +
		"10. You are provided 'Human Points' for all the activities you do on CB that shall be a count of how good a human you are."));
	Button close = new Button("CLOSE");
	close.addClickHandler(new ClickHandler() {
	    
	    @Override
	    public void onClick(ClickEvent event) {
		pPnlRuleBook.hide();
	    }
	});
	sPnlContent.add(close);
	pPnlRuleBook.add(sPnlContent);

	return pPnlRuleBook;
    }
}
