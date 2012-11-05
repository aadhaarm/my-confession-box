package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.presenter.MenuPresenter;
import com.l3.CB.client.ui.widgets.MenuButton;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.UserInfo;

public class MenuView extends Composite implements MenuPresenter.Display {

    private final VerticalPanel menuBar; 
    private final PushButton feedItem;
    private final PushButton confessItem;
    private final PushButton myConItem;
    private final PushButton conToMeItem;
    private PushButton selectedMenuItem;
    UserInfo userInfo;
    ConfessionServiceAsync confessionService;
    MenuButton btnMenuItemMyConf;
    MenuButton btnMenuItemConfToMe;

    public MenuView() {
	DecoratorPanel contentTableDecorator = new DecoratorPanel();
	initWidget(contentTableDecorator);
	menuBar = new VerticalPanel();
	menuBar.addStyleName(Constants.STYLE_CLASS_MENU);

	feedItem = new PushButton(ConfessionBox.cbText.cbMenuConfessionFeed(), new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		feedItem.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		if(selectedMenuItem != null) {
		    selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		}
		selectedMenuItem = feedItem;
		CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED);
	    }
	});

	confessItem = new PushButton(ConfessionBox.cbText.cbMenuConfessionConfess(), new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		confessItem.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		if(selectedMenuItem != null) {
		    selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		}
		selectedMenuItem = confessItem;
		History.newItem(Constants.HISTORY_ITEM_REGISTER_CONFESSION);
	    }
	});

	myConItem = new PushButton(ConfessionBox.cbText.cbMenuConfessionMyConfessions(), new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		myConItem.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		if(selectedMenuItem != null) {
		    selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		}
		selectedMenuItem = myConItem;
		CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);

	    }
	});

	conToMeItem = new PushButton(ConfessionBox.cbText.cbMenuConfessionConfessToMe(), new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		conToMeItem.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		if(selectedMenuItem != null) {
		    selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		}
		selectedMenuItem = conToMeItem;
		CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED);
	    }
	});

	menuBar.add(feedItem);
	menuBar.add(confessItem);
	btnMenuItemMyConf = getPanelWithCount(myConItem, 0);
	menuBar.add(btnMenuItemMyConf);
	btnMenuItemConfToMe = getPanelWithCount(conToMeItem, 0);
	menuBar.add(btnMenuItemConfToMe);

	contentTableDecorator.add(menuBar);
    }

    private MenuButton getPanelWithCount(PushButton myConItem, int i) {
	MenuButton btnMenuItem = new MenuButton(userInfo, confessionService, i, myConItem);
	btnMenuItem.add(myConItem);
	return btnMenuItem;
    }

    @Override
    public Widget asWidget() {
	return this;
    }

    @Override
    public PushButton setFeedItemSelected() {
	feedItem.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
	selectedMenuItem = feedItem;
	return feedItem;
    }

    @Override
    public PushButton getFeedItem() {
	return feedItem;
    }

    @Override
    public PushButton getConfessItem() {
	return confessItem;
    }

    @Override
    public PushButton getMyConItem() {
	return myConItem;
    }

    @Override
    public PushButton getConToMeItem() {
	return conToMeItem;
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
