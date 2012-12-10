package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.presenter.FooterPresenter;
import com.l3.CB.client.ui.widgets.Templates;

public class FooterView extends Composite implements FooterPresenter.Display {

    private final HTML ancAbout;
    private final HTML ancPrivacy;
    private final HTML ancAppText;

    public FooterView() {

	FlowPanel fPnlFooter = new FlowPanel();

	ancAbout = new HTML(Templates.TEMPLATES.infoToolTip(ConfessionBox.cbText.aboutConfessionBoxFooterLinkLabel(), "This is all about Confession Box!"));;
	ancAbout.setStyleName("footerLink");

	ancPrivacy = new HTML(Templates.TEMPLATES.infoToolTip(ConfessionBox.cbText.privacyPolicyFooterLinkLabel(), "Privacy policy it is..."));
	ancPrivacy.setStyleName("footerLink");

	ancAppText = new HTML(Templates.TEMPLATES.infoToolTip(ConfessionBox.cbText.cbNameFooterTextLabel(), "takes you to the Confession Box community page"));
	ancAppText.setStyleName("footerLink");
	//	ancAppText.
	//	setHref("http://www.facebook.com/pages/Confession-Box-Community/");

	setupAbout();
	setupPrivacy();

	if(!ConfessionBox.isMobile) {
	    fPnlFooter.add(ancAbout);
	    fPnlFooter.add(ancPrivacy);
	    fPnlFooter.add(ancAppText);
	}

//	DecoratorPanel contentTableDecorator = new DecoratorPanel();
//	contentTableDecorator.removeStyleName("gwt-DecoratorPanel");
//	contentTableDecorator.add(fPnlFooter);
	initWidget(fPnlFooter);
    }

    /**
     * 
     */
    private void setupAbout() {
	final PopupPanel pPnlAbout = new PopupPanel(true);
	pPnlAbout.setGlassEnabled(true);
	pPnlAbout.setStyleName("infoModalPopupWindow");

	ScrollPanel sPnlContent = new ScrollPanel();
	sPnlContent.add(new HTML("ABOUT Confession Box: In a professional context it often happens that private or corporate " +
		"clients corder a publication to be made and presented with the actual content still not being ready. " +
		"Think of a news blog that's filled with content hourly on the day of going live. " +
		"However, reviewers tend to be distracted by comprehensible content, say, a random text " +
		"copied from a newspaper or the internet. The are likely to focus on the text, disregarding " +
		"the layout and its elements. Besides, random text risks to be unintendedly humorous or offensive, " +
		"an unacceptable risk in corporate environments. Lorem ipsum and its many variants have been employed since " +
		"the early 1960ies, and quite likely since the sixteenth century."));
	pPnlAbout.add(sPnlContent);
	ancAbout.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		pPnlAbout.center();
	    }
	});
    }

    /**
     * 
     */
    private void setupPrivacy() {
	final PopupPanel pPnlAbout = new PopupPanel(true);
	pPnlAbout.setGlassEnabled(true);
	pPnlAbout.setStyleName("infoModalPopupWindow");

	ScrollPanel sPnlContent = new ScrollPanel();
	sPnlContent.add(new HTML("PRIVACY POLICY: In a professional context it often happens that private or corporate " +
		"clients corder a publication to be made and presented with the actual content still not being ready. " +
		"Think of a news blog that's filled with content hourly on the day of going live. " +
		"However, reviewers tend to be distracted by comprehensible content, say, a random text " +
		"copied from a newspaper or the internet. The are likely to focus on the text, disregarding " +
		"the layout and its elements. Besides, random text risks to be unintendedly humorous or offensive, " +
		"an unacceptable risk in corporate environments. Lorem ipsum and its many variants have been employed since " +
		"the early 1960ies, and quite likely since the sixteenth century."));
	pPnlAbout.add(sPnlContent);
	ancPrivacy.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		pPnlAbout.center();
	    }
	});
    }

    @Override
    public Widget asWidget() {
	return this;
    }

    @Override
    public HTML getAncAbout() {
	return ancAbout;
    }

    @Override
    public HTML getAncPrivacy() {
	return ancPrivacy;
    }
}