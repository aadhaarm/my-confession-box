package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.FilterEvent;
import com.l3.CB.client.event.UpdateMenuEvent;
import com.l3.CB.client.presenter.HeaderPresenter;
import com.l3.CB.client.ui.widgets.ApplicationTextWidget;
import com.l3.CB.client.ui.widgets.Templates;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.util.HelpInfo;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Filters;

public class HeaderView extends Composite implements HeaderPresenter.Display {

    public static HTML applnTitle;
    public static HTML tagLine;
    public static ListBox lstFilterOptions;
    public FlowPanel headerPanel;
    public FlowPanel pnlpoints;

    public HeaderView() {
	super();
	headerPanel = new FlowPanel();
	applnTitle = new HTML(Templates.TEMPLATES.applicationTitle());
	applnTitle.setStyleName("logoLink");


	tagLine = new HTML(Templates.TEMPLATES.applicationTagLine());
	tagLine.setStyleName("tagLineText");


	if(ConfessionBox.isMobile) {
	    headerPanel.add(setupSmallMenu());
	    lstFilterOptions = CommonUtils.getFilterListBox();
	    lstFilterOptions.setVisible(true);
	    lstFilterOptions.addChangeHandler(new ChangeHandler() {
		@Override
		public void onChange(ChangeEvent event) {
		    ListBox lstBoxFilter = (ListBox)event.getSource();
		    String selectedFilter = lstBoxFilter.getValue(lstBoxFilter.getSelectedIndex());
		    Filters filter = Filters.valueOf(selectedFilter);
		    ConfessionBox.eventBus.fireEvent(new FilterEvent(filter));
		}
	    });
	    headerPanel.add(lstFilterOptions);
	}

	headerPanel.add(applnTitle);
	headerPanel.add(tagLine);
	if(ConfessionBox.isMobile) {
	    pnlpoints = new FlowPanel();
	    pnlpoints.setStyleName("pointsPanel");
	    headerPanel.add(pnlpoints);
	}

	initWidget(headerPanel);
    }

    public void initializeMenuCounts() {
	ConfessionBox.confessionService.getHumanPoints(ConfessionBox.getLoggedInUserInfo().getUserId(), new AsyncCallback<Integer>() {
	    @Override
	    public void onSuccess(Integer result) {
		HTML a = new HTML("Human Points " + Long.toString(result));
		a.setStyleName("pointsLabel");
		pnlpoints.add(a);
	    }
	    
	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("HumanPointPresenter", "onFailure", caught);
	    }
	});

	ConfessionBox.confessionService.getNumberOfConfessionForMe(ConfessionBox.getLoggedInUserInfo().getUserId(), new AsyncCallback<Long>() {
	    @Override
	    public void onSuccess(Long result) {
		HTML a = new HTML("Confessed To Me " + Long.toString(result));
		a.setStyleName("pointsLabel");
		pnlpoints.add(a);
	    }
	    
	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("MenuPresenter", "onFailure", caught);
	    }
	});

	ConfessionBox.confessionService.getMyConfessionNumber(ConfessionBox.getLoggedInUserInfo().getUserId(), new AsyncCallback<Long>() {
	    @Override
	    public void onSuccess(Long result) {
		HTML a = new HTML("My Confessions " + Long.toString(result));
		a.setStyleName("pointsLabel");
		pnlpoints.add(a);
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("MenuPresenter", "onFailure", caught);
	    }
	});
    }


    /**
     * @param ancLogout
     */
    private ListBox setupSmallMenu() {
	final ListBox menuListBox = new ListBox();
	menuListBox.setStyleName("menu");
	menuListBox.addItem(ConfessionBox.cbText.cbMenuConfessionFeed(), "link1");
	menuListBox.addItem(ConfessionBox.cbText.cbMenuConfessionConfess(), "link2");
	menuListBox.addItem(ConfessionBox.cbText.cbMenuConfessionMyConfessions(), "link3");
	menuListBox.addItem(ConfessionBox.cbText.cbMenuConfessionConfessToMe(), "link4");
	menuListBox.addItem(ConfessionBox.cbText.inviteFriendsLinkLabelText(), "invite");
	menuListBox.addItem(ConfessionBox.cbText.cbRuleBookLinkLabelText(), "rulebook");
	menuListBox.addItem(ConfessionBox.cbText.aboutConfessionBoxFooterLinkLabel(), "about");
	menuListBox.addItem(ConfessionBox.cbText.phillosphyFooterLinkLabel(), "philosophy");
	menuListBox.addItem(ConfessionBox.cbText.phillosphyAnonConfFooterLinkLabel(), "anonConf");
//	menuListBox.addItem(ConfessionBox.cbText.privacyPolicyFooterLinkLabel(), "policy");
	
//	if(ConfessionBox.isLoggedIn) {
//	    menuListBox.addItem(ConfessionBox.cbText.logoutLinkLabelText(), "logout");
//	}
	menuListBox.addChangeHandler(new ChangeHandler() {
	    @Override
	    public void onChange(ChangeEvent event) {
		ListBox a = ((ListBox)event.getSource());
		String newValue = a.getValue(a.getSelectedIndex());
		if("link1".equals(newValue)) {
		    ConfessionBox.eventBus.fireEvent(new UpdateMenuEvent());
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED);
		} else if("link2".equals(newValue)) {
		    if(ConfessionBox.isLoggedIn) {
			ConfessionBox.eventBus.fireEvent(new UpdateMenuEvent());
			CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_REGISTER_CONFESSION);
		    } else {
			CommonUtils.login(0);
		    }
		} else if("link3".equals(newValue)) {
		    if(ConfessionBox.isLoggedIn) {
			ConfessionBox.eventBus.fireEvent(new UpdateMenuEvent());
			CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
		    } else {
			CommonUtils.login(0);
		    }
		} else if("link4".equals(newValue)) {
		    if(ConfessionBox.isLoggedIn) {
			ConfessionBox.eventBus.fireEvent(new UpdateMenuEvent());
			CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED);
			HelpInfo.cleanToolTip();
		    } else {
			CommonUtils.login(0);
		    }
		} else if("invite".equals(newValue)) {
		    CommonUtils.inviteFriends(ConfessionBox.getLoggedInUserInfo().getName(), ConfessionBox.cbText.inviteFriendsTextMessage());
		} else if("rulebook".equals(newValue)) {
		    PopupPanel pPnlRuleBook = ApplicationTextWidget.setupCBRuleBook();
		    pPnlRuleBook.center();
		} else if("about".equals(newValue)) {
		    PopupPanel pPnlRuleBook = ApplicationTextWidget.setupAbout();
		    pPnlRuleBook.center();
		} else if("policy".equals(newValue)) {
		    PopupPanel pPnlRuleBook = ApplicationTextWidget.setupPrivacy();
		    pPnlRuleBook.center();
		} else if("philosophy".equals(newValue)) {
		    PopupPanel pPnlPhilosophy = ApplicationTextWidget.setupPhillosphy();
		    pPnlPhilosophy.center();
		} else if("anonConf".equals(newValue)) {
		    PopupPanel pPnlPhilosophyAnonConf = ApplicationTextWidget.setupPhillosphyAnynConf();
		    pPnlPhilosophyAnonConf.center();
		} else if("logout".equals(newValue)) {
		    CommonUtils.logout(ConfessionBox.cbText.logoutInfoMessage());
		    menuListBox.removeItem(8);
		}
	    }
	});
	return menuListBox;
    }

    public HasClickHandlers getApplnTitle() {
        return applnTitle;
    }
}