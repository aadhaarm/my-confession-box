package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.URLEvent;
import com.l3.CB.client.event.URLEventHandler;
import com.l3.CB.client.presenter.FooterPresenter;
import com.l3.CB.client.ui.widgets.ApplicationTextWidget;
import com.l3.CB.client.ui.widgets.Templates;
import com.l3.CB.shared.Constants;

public class FooterView extends Composite implements FooterPresenter.Display {

    private final HTML ancAbout;
    private final HTML ancPrivacy;
    private final HTML ancPhillosphy;
    private final HTML ancPhillosphyAnynConf;
    private final HTML ancAppComunityPage;
    private final HTML ancAppWebLink;
    private final HTML ancTOSLink;
    
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
	
	ancAppWebLink = new HTML(Templates.TEMPLATES.infoToolTip("fbconfess.com", "Confession box web link"));
	ancAppWebLink.setStyleName("footerLink");

	ancTOSLink = new HTML(Templates.TEMPLATES.infoToolTip("Terms of service", "Confession Box terms of service"));
	ancTOSLink.setStyleName("footerLink");
	
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
	
	ancAppComunityPage.addClickHandler(new ClickHandler() {
	    
	    @Override
	    public void onClick(ClickEvent event) {
		Window.open("http://www.facebook.com/pages/Confession-Box-Community/129927533826479", "Confession Box" , "resizable=yes,scrollbars=yes");
	    }
	});
	
	ancAppWebLink.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		Window.open("http://www.fbconfess.com", "Confession Box" , "resizable=yes,scrollbars=yes");
	    }
	});
	
	final PopupPanel pPnlTOS = ApplicationTextWidget.setupTOS();
	ancTOSLink.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		pPnlTOS.center();
	    }
	});
	
	if(!ConfessionBox.isMobile) {
	    fPnlFooter.add(ancAbout);
	    fPnlFooter.add(ancPhillosphy);
	    fPnlFooter.add(ancPhillosphyAnynConf);
	    fPnlFooter.add(ancAppComunityPage);
	    fPnlFooter.add(ancAppWebLink);
	    fPnlFooter.add(ancPrivacy);
	    fPnlFooter.add(ancTOSLink);
	}

	initWidget(fPnlFooter);
	
	ConfessionBox.eventBus.addHandler(URLEvent.TYPE, new URLEventHandler() {
	    @Override
	    public void loadPage(URLEvent event) {
		if(event != null && event.getUrlType() != null) {
		    if(Constants.HISTORY_ITEM_PRIVACY_POLICY.equalsIgnoreCase(event.getUrlType())) {
			pPnlPrivacy.center();
		    } else if(Constants.HISTORY_ITEM_TOS.equalsIgnoreCase(event.getUrlType())) {
			pPnlTOS.center();
		    }
		}
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