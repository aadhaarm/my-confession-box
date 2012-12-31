package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.presenter.FooterPresenter;
import com.l3.CB.client.ui.widgets.ApplicationTextWidget;
import com.l3.CB.client.ui.widgets.Templates;

public class FooterView extends Composite implements FooterPresenter.Display {

    private final HTML ancAbout;
    private final HTML ancPrivacy;
    private final HTML ancPhillosphy;
    private final HTML ancPhillosphyAnynConf;
    private final HTML ancAppComunityPage;

    public FooterView() {

	FlowPanel fPnlFooter = new FlowPanel();

	ancAbout = new HTML(Templates.TEMPLATES.infoToolTip(ConfessionBox.cbText.aboutConfessionBoxFooterLinkLabel(), "This is all about Confession Box!"));;
	ancAbout.setStyleName("footerLink");

	ancPrivacy = new HTML(Templates.TEMPLATES.infoToolTip(ConfessionBox.cbText.privacyPolicyFooterLinkLabel(), "Privacy policy it is..."));
	ancPrivacy.setStyleName("footerLink");

	ancAppComunityPage = new HTML(Templates.TEMPLATES.infoToolTip(ConfessionBox.cbText.cbNameFooterTextLabel(), "takes you to the Confession Box community page"));
	ancAppComunityPage.setStyleName("footerLink");

	ancPhillosphy = new HTML(Templates.TEMPLATES.infoToolTip(ConfessionBox.cbText.phillosphyFooterLinkLabel(), "The Philosophy"));
	ancPhillosphy.setStyleName("footerLink");
	
	ancPhillosphyAnynConf = new HTML(Templates.TEMPLATES.infoToolTip(ConfessionBox.cbText.phillosphyAnonConfFooterLinkLabel(), "Thoughts about Anonymous Confession"));
	ancPhillosphyAnynConf.setStyleName("footerLink");
	
	final PopupPanel pPnlAbout = ApplicationTextWidget.setupAbout();
	ancAbout.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		pPnlAbout.center();
	    }
	});
	
	final PopupPanel pPnlPrivacy = ApplicationTextWidget.setupPrivacy();
	ancPrivacy.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		pPnlPrivacy.center();
	    }
	});
	
	final PopupPanel pPnlPhillosphy = ApplicationTextWidget.setupPhillosphy();
	ancPhillosphy.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		pPnlPhillosphy.center();
	    }
	});

	final PopupPanel pPnlPhillosphyAnonymous = ApplicationTextWidget.setupPhillosphyAnynConf();
	ancPhillosphyAnynConf.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		pPnlPhillosphyAnonymous.center();
	    }
	});

	
	if(!ConfessionBox.isMobile) {
	    fPnlFooter.add(ancAbout);
	    fPnlFooter.add(ancPhillosphy);
	    fPnlFooter.add(ancPhillosphyAnynConf);
	    fPnlFooter.add(ancAppComunityPage);
//	    fPnlFooter.add(ancPrivacy);
	}

	initWidget(fPnlFooter);
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