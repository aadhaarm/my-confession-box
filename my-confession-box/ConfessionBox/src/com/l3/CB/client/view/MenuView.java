package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.event.UpdateMenuEvent;
import com.l3.CB.client.presenter.MenuPresenter;
import com.l3.CB.client.ui.widgets.ApplicationTextWidget;
import com.l3.CB.client.ui.widgets.MenuButton;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.HelpInfo;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.UserInfo;

public class MenuView extends Composite implements MenuPresenter.Display {

    private final FlowPanel menuBar; 
    private final Anchor feedItem;
    private final Anchor confessItem;
    private final Anchor myConItem;
    private final Anchor conToMeItem;
    private static HTML selectedMenuItem;
    UserInfo userInfo;
    ConfessionServiceAsync confessionService;
    MenuButton btnMenuItemMyConf;
    MenuButton btnMenuItemConfToMe;
    static HTML link1;
    static HTML link2;
    static HTML link3;
    static HTML link4;
    private final Anchor ancCBRuleBook;
    private final Anchor ancInvite; 
    PopupPanel pPnlRuleBook;

    public static void selectMenuItem(int item) {
	switch (item) {
	case 1: 
	    if(selectedMenuItem != null) {
		selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
	    }
	    link1.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
	    selectedMenuItem = link1;
	    break;
	case 2:
	    if(selectedMenuItem != null) {
		selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
	    }
	    link2.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
	    selectedMenuItem = link2;
	    break;
	case 3:
	    if(selectedMenuItem != null) {
		selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
	    }
	    link3.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
	    selectedMenuItem = link3;
	    break;
	case 4:
	    if(selectedMenuItem != null) {
		selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
	    }
	    link4.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
	    selectedMenuItem = link4;
	    break;
	}
    }

    public MenuView() {
	feedItem = new Anchor(ConfessionBox.cbText.cbMenuConfessionFeed());
	feedItem.removeStyleName("gwt-Anchor");
	feedItem.setStyleName("link1");
	link1 = new HTML(feedItem+"");
	link1.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		ConfessionBox.eventBus.fireEvent(new UpdateMenuEvent());
		CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED);
		HelpInfo.cleanToolTip();
	    }
	});


	confessItem = new Anchor(ConfessionBox.cbText.cbMenuConfessionConfess());
	confessItem.removeStyleName("gwt-Anchor");
	confessItem.setStyleName("link2");
	link2 = new HTML(confessItem+"");
	link2.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(ConfessionBox.isLoggedIn) {
		    ConfessionBox.eventBus.fireEvent(new UpdateMenuEvent());
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_REGISTER_CONFESSION);
		    HelpInfo.cleanToolTip();
		} else {
		    CommonUtils.login(0, Constants.HISTORY_ITEM_REGISTER_CONFESSION);
		}
	    }
	});

	myConItem = new Anchor(ConfessionBox.cbText.cbMenuConfessionMyConfessions());
	myConItem.removeStyleName("gwt-Anchor");
	myConItem.setStyleName("link3");
	link3 = new HTML(myConItem+"");
	link3.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(ConfessionBox.isLoggedIn) {
		    ConfessionBox.eventBus.fireEvent(new UpdateMenuEvent());
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
		    HelpInfo.cleanToolTip();
		} else {
		    CommonUtils.login(0, Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
		}
	    }
	});

	conToMeItem = new Anchor(ConfessionBox.cbText.cbMenuConfessionConfessToMe());
	conToMeItem.removeStyleName("gwt-Anchor");
	conToMeItem.setStyleName("link4");
	link4 = new HTML(conToMeItem + "");
	link4.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(ConfessionBox.isLoggedIn) {
		    ConfessionBox.eventBus.fireEvent(new UpdateMenuEvent());
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED);
		    HelpInfo.cleanToolTip();
		} else {
		    CommonUtils.login(0, Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED);
		}
	    }
	});
	btnMenuItemMyConf = getPanelWithCount(link3, 0);
	btnMenuItemConfToMe = getPanelWithCount(link4, 0);

	/*
	 * Invite friends	
	 */
	ancInvite = new Anchor(ConfessionBox.cbText.inviteFriendsLinkLabelText());
	ancInvite.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		CommonUtils.inviteFriends(ConfessionBox.getLoggedInUserInfo().getName(), ConfessionBox.cbText.inviteFriendsTextMessage());
	    }
	});
	ancInvite.setStyleName("link5");


	/*
	 * CB RULE BOOK	
	 */
	ancCBRuleBook = new Anchor(ConfessionBox.cbText.cbRuleBookLinkLabelText());
	ancCBRuleBook.setStyleName("link5");
	pPnlRuleBook = ApplicationTextWidget.setupCBRuleBook();
	ancCBRuleBook.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		pPnlRuleBook.center();
	    }
	});
	
	/*
	 * Logout link
	 */
	final Anchor ancLogout = new Anchor(ConfessionBox.cbText.logoutLinkLabelText());
	if(ConfessionBox.isLoggedIn) {
	    ancLogout.setStyleName("link5");
	    ancLogout.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
		    CommonUtils.logout(ConfessionBox.cbText.logoutInfoMessage());
		    menuBar.remove(ancLogout);
		}
	    });
	}

	menuBar = new FlowPanel();
	menuBar.setStyleName(Constants.STYLE_CLASS_MENU);

	
	// NOT Mobile
	if(!ConfessionBox.isMobile) {
	    menuBar.add(link1);
	    menuBar.add(link2);
	    menuBar.add(btnMenuItemMyConf);
	    menuBar.add(btnMenuItemConfToMe);
	    menuBar.add(ancInvite);
	    menuBar.add(ancCBRuleBook);
//	    if(ConfessionBox.isLoggedIn) {
//		menuBar.add(ancLogout);
//	    }
	} 
	initWidget(menuBar);
    }

    private MenuButton getPanelWithCount(HTML myConItem, int i) {
	MenuButton btnMenuItem = new MenuButton(userInfo, confessionService, i, myConItem);
	btnMenuItem.add(myConItem);
	return btnMenuItem;
    }

    @Override
    public Widget asWidget() {
	return this;
    }

    @Override
    public HTML setFeedItemSelected() {
	link1.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
	selectedMenuItem = link1;
	return link1;
    }

    @Override
    public HTML getFeedItem() {
	return link1;
    }

    @Override
    public HTML getConfessItem() {
	return link2;
    }

    @Override
    public HTML getMyConItem() {
	return link3;
    }

    @Override
    public HTML getConToMeItem() {
	return link4;
    }

    public boolean setMyConfCount(int count) {
	return true;
    }

    public boolean setConfToMeCount(int count) {
	return true;
    }

    @Override
    public MenuButton getBtnMenuItemMyConf() {
	return btnMenuItemMyConf;
    }

    @Override
    public MenuButton getBtnMenuItemConfToMe() {
	return btnMenuItemConfToMe;
    }
}