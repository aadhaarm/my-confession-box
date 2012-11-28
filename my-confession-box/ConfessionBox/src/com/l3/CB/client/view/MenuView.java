package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
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
	initWidget(contentTableDecorator);
	menuBar = new FlowPanel();
	menuBar.setStyleName(Constants.STYLE_CLASS_MENU);

	feedItem = new Anchor(ConfessionBox.cbText.cbMenuConfessionFeed());
	feedItem.removeStyleName("gwt-Anchor");
	link1 = new HTML(feedItem+"");
	link1.setStyleName("link1");
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
	link2 = new HTML(confessItem+"");
	link2.setStyleName("link2");
	link2.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(ConfessionBox.isLoggedIn) {
		    ConfessionBox.confEventBus.fireEvent(new UpdateMenuEvent());
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_REGISTER_CONFESSION);
		    HelpInfo.cleanToolTip();
		} else {
		    CommonUtils.login();
		}
	    }
	});

	myConItem = new Anchor(ConfessionBox.cbText.cbMenuConfessionMyConfessions());
	myConItem.removeStyleName("gwt-Anchor");
	link3 = new HTML(myConItem+"");
	link3.setStyleName("link3");
	link3.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(ConfessionBox.isLoggedIn) {
		    ConfessionBox.confEventBus.fireEvent(new UpdateMenuEvent());
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
		    HelpInfo.cleanToolTip();
		} else {
		    CommonUtils.login();
		}
	    }
	});

	conToMeItem = new Anchor(ConfessionBox.cbText.cbMenuConfessionConfessToMe());
	conToMeItem.removeStyleName("gwt-Anchor");
	link4 = new HTML(conToMeItem + "");
	link4.setStyleName("link4");
	link4.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(ConfessionBox.isLoggedIn) {
		    ConfessionBox.confEventBus.fireEvent(new UpdateMenuEvent());
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED);
		    HelpInfo.cleanToolTip();
		} else {
		    CommonUtils.login();
		}
	    }
	});

	menuBar.add(link1);
	menuBar.add(link2);
	btnMenuItemMyConf = getPanelWithCount(link3, 0);
	menuBar.add(btnMenuItemMyConf);
	btnMenuItemConfToMe = getPanelWithCount(link4, 0);
	menuBar.add(btnMenuItemConfToMe);

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
	menuBar.add(ancInvite);

	/*
	 * CB RULE BOOK	
	 */
	ancCBRuleBook = new Anchor(ConfessionBox.cbText.cbRuleBookLinkLabelText());
	ancCBRuleBook.setStyleName("link5");
	setupCBRuleBook();
	menuBar.add(ancCBRuleBook);

	/*
	 * Logout link
	 */
	if(ConfessionBox.isLoggedIn) {
	    final Anchor ancLogout = new Anchor(ConfessionBox.cbText.logoutLinkLabelText());
	    ancLogout.setStyleName("link5");
	    ancLogout.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
		    CommonUtils.logout(ConfessionBox.cbText.logoutInfoMessage());
		    ConfessionBox.isLoggedIn = false;
		    menuBar.remove(ancLogout);
		}
	    });
	    menuBar.add(ancLogout);
	}
	
	contentTableDecorator.add(menuBar);
    }

    /**
     * 
     */
    private void setupCBRuleBook() {
	final PopupPanel pPnlAbout = new PopupPanel(true);
	pPnlAbout.setGlassEnabled(true);
	pPnlAbout.setStyleName("infoModalPopupWindow");

	ScrollPanel sPnlContent = new ScrollPanel();
	sPnlContent.add(new HTML("CB Rule-Book: In a professional context it often happens that private or corporate " +
		"clients corder a publication to be made and presented with the actual content still not being ready. " +
		"Think of a news blog that's filled with content hourly on the day of going live. " +
		"However, reviewers tend to be distracted by comprehensible content, say, a random text " +
		"copied from a newspaper or the internet. The are likely to focus on the text, disregarding " +
		"the layout and its elements. Besides, random text risks to be unintendedly humorous or offensive, " +
		"an unacceptable risk in corporate environments. Lorem ipsum and its many variants have been employed since " +
		"the early 1960ies, and quite likely since the sixteenth century."));
	pPnlAbout.add(sPnlContent);
	ancCBRuleBook.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		pPnlAbout.center();
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