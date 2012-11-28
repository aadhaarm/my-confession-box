package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.presenter.FooterPresenter;

public class FooterView extends Composite implements FooterPresenter.Display {

    private final Anchor ancAbout;
    private final Anchor ancPrivacy;


    public FooterView() {
	DecoratorPanel contentTableDecorator = new DecoratorPanel();
	initWidget(contentTableDecorator);

	VerticalPanel fPnlFooter = new VerticalPanel();
	fPnlFooter.setWidth("140px");
	ancAbout = new Anchor(ConfessionBox.cbText.aboutConfessionBoxFooterLinkLabel());
	ancPrivacy = new Anchor(ConfessionBox.cbText.privacyPolicyFooterLinkLabel());

	Label lblAppText = new Label(ConfessionBox.cbText.cbNameFooterTextLabel());


	setupAbout();
	setupPrivacy();

	fPnlFooter.add(ancAbout);
	fPnlFooter.add(ancPrivacy);
	fPnlFooter.add(lblAppText);
	
	contentTableDecorator.add(fPnlFooter);
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
    public Anchor getAncAbout() {
        return ancAbout;
    }

    @Override
    public Anchor getAncPrivacy() {
        return ancPrivacy;
    }
}