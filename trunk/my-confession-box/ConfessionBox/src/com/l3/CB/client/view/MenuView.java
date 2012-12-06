package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.event.UpdateMenuEvent;
import com.l3.CB.client.presenter.MenuPresenter;
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
	DecoratorPanel contentTableDecorator = new DecoratorPanel();
	contentTableDecorator.removeStyleName("gwt-DecoratorPanel");
	initWidget(contentTableDecorator);

	feedItem = new Anchor(ConfessionBox.cbText.cbMenuConfessionFeed());
	feedItem.removeStyleName("gwt-Anchor");
	feedItem.setStyleName("link1");
	link1 = new HTML(feedItem+"");
	link1.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		ConfessionBox.confEventBus.fireEvent(new UpdateMenuEvent());
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
		    ConfessionBox.confEventBus.fireEvent(new UpdateMenuEvent());
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_REGISTER_CONFESSION);
		    HelpInfo.cleanToolTip();
		} else {
		    CommonUtils.login(0);
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
		    ConfessionBox.confEventBus.fireEvent(new UpdateMenuEvent());
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
		    HelpInfo.cleanToolTip();
		} else {
		    CommonUtils.login(0);
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
		    ConfessionBox.confEventBus.fireEvent(new UpdateMenuEvent());
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED);
		    HelpInfo.cleanToolTip();
		} else {
		    CommonUtils.login(0);
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
	setupCBRuleBook();

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
		    ConfessionBox.isLoggedIn = false;
		    menuBar.remove(ancLogout);
		}
	    });
	}

	menuBar = new FlowPanel();
	menuBar.setStyleName(Constants.STYLE_CLASS_MENU);

	if(!ConfessionBox.isSmallScreen) {
	    menuBar.add(link1);
	    menuBar.add(link2);
	    menuBar.add(btnMenuItemMyConf);
	    menuBar.add(btnMenuItemConfToMe);
	    menuBar.add(ancInvite);
	    menuBar.add(ancCBRuleBook);
	    if(ConfessionBox.isLoggedIn) {
		menuBar.add(ancLogout);
	    }
	} else {
	    setupSmallMenu(ancLogout);
	}
	contentTableDecorator.add(menuBar);
    }

    /**
     * @param ancLogout
     */
    private void setupSmallMenu(final Anchor ancLogout) {
	final ListBox menuListBox = new ListBox();
	menuListBox.addItem(ConfessionBox.cbText.cbMenuConfessionFeed(), "link1");
	menuListBox.addItem(ConfessionBox.cbText.cbMenuConfessionConfess(), "link2");
	menuListBox.addItem(ConfessionBox.cbText.cbMenuConfessionMyConfessions(), "link3");
	menuListBox.addItem(ConfessionBox.cbText.cbMenuConfessionConfessToMe(), "link4");
	menuListBox.addItem(ConfessionBox.cbText.inviteFriendsLinkLabelText(), "invite");
	menuListBox.addItem(ConfessionBox.cbText.cbRuleBookLinkLabelText(), "rulebook");
	if(ConfessionBox.isLoggedIn) {
	menuListBox.addItem(ConfessionBox.cbText.logoutLinkLabelText(), "logout");
	}
	menuListBox.addChangeHandler(new ChangeHandler() {
	@Override
	public void onChange(ChangeEvent event) {
	    ListBox a = ((ListBox)event.getSource());
	    String newValue = a.getValue(a.getSelectedIndex());
	    if("link1".equals(newValue)) {
		ConfessionBox.confEventBus.fireEvent(new UpdateMenuEvent());
		CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED);
	    } else if("link2".equals(newValue)) {
		if(ConfessionBox.isLoggedIn) {
		    ConfessionBox.confEventBus.fireEvent(new UpdateMenuEvent());
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_REGISTER_CONFESSION);
		} else {
		    CommonUtils.login(0);
		}
	    } else if("link3".equals(newValue)) {
		if(ConfessionBox.isLoggedIn) {
		    ConfessionBox.confEventBus.fireEvent(new UpdateMenuEvent());
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
		} else {
		    CommonUtils.login(0);
		}
	    } else if("link4".equals(newValue)) {
		if(ConfessionBox.isLoggedIn) {
		    ConfessionBox.confEventBus.fireEvent(new UpdateMenuEvent());
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED);
		    HelpInfo.cleanToolTip();
		} else {
		    CommonUtils.login(0);
		}
	    } else if("invite".equals(newValue)) {
		CommonUtils.inviteFriends(ConfessionBox.getLoggedInUserInfo().getName(), ConfessionBox.cbText.inviteFriendsTextMessage());
	    } else if("rulebook".equals(newValue)) {
		pPnlRuleBook.center();
	    } else if("logout".equals(newValue)) {
		CommonUtils.logout(ConfessionBox.cbText.logoutInfoMessage());
		ConfessionBox.isLoggedIn = false;
		menuBar.remove(ancLogout);
	    }
	}
	});
	menuListBox.setStyleName("showMenuButton");
	menuBar.add(menuListBox);
    }

    /**
     * 
     */
    private void setupCBRuleBook() {
	pPnlRuleBook = new PopupPanel(true);
	pPnlRuleBook.setGlassEnabled(true);
	pPnlRuleBook.setStyleName("infoModalPopupWindow");

	ScrollPanel sPnlContent = new ScrollPanel();
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
	pPnlRuleBook.add(sPnlContent);
	ancCBRuleBook.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		pPnlRuleBook.center();
	    }
	});
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