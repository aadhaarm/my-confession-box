package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.client.presenter.MenuPresenter;
import com.l3.CB.client.ui.widgets.MenuButton;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.UserInfo;

public class MenuView extends Composite implements MenuPresenter.Display {

    private final FlowPanel menuBar; 
    private final Anchor feedItem;
    private final Anchor confessItem;
    private final Anchor myConItem;
    private final Anchor conToMeItem;
    private HTML selectedMenuItem;
    UserInfo userInfo;
    ConfessionServiceAsync confessionService;
    MenuButton btnMenuItemMyConf;
    MenuButton btnMenuItemConfToMe;
    HTML link1;
    HTML link2;
    HTML link3;
    HTML link4;
    
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
		link1.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		if(selectedMenuItem != null) {
		    selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		}
		selectedMenuItem = link1;
		CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED);
	    }
	});

	
	confessItem = new Anchor(ConfessionBox.cbText.cbMenuConfessionConfess());
	confessItem.removeStyleName("gwt-Anchor");
	link2 = new HTML(confessItem+"");
	link2.setStyleName("link2");
	link2.addClickHandler(new ClickHandler() {
	    
	    @Override
	    public void onClick(ClickEvent event) {
		link2.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		if(selectedMenuItem != null) {
		    selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		}
		selectedMenuItem = link2;
		CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_REGISTER_CONFESSION);
	    }
	});

	myConItem = new Anchor(ConfessionBox.cbText.cbMenuConfessionMyConfessions());
	myConItem.removeStyleName("gwt-Anchor");
	link3 = new HTML(myConItem+"");
	link3.setStyleName("link3");
	link3.addClickHandler(new ClickHandler() {
	    
	    @Override
	    public void onClick(ClickEvent event) {
		link3.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		if(selectedMenuItem != null) {
		    selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		}
		selectedMenuItem = link3;
		CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
	    }
	});

	conToMeItem = new Anchor(ConfessionBox.cbText.cbMenuConfessionConfessToMe());
	conToMeItem.removeStyleName("gwt-Anchor");
	link4 = new HTML(conToMeItem + "");
	link4.setStyleName("link4");
	link4.addClickHandler(new ClickHandler() {
	    
	    @Override
	    public void onClick(ClickEvent event) {
		link4.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		if(selectedMenuItem != null) {
		    selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		}
		selectedMenuItem = link4;
		CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED);
	    }
	});
	
	menuBar.add(link1);
	menuBar.add(link2);
	btnMenuItemMyConf = getPanelWithCount(link3, 0);
	menuBar.add(btnMenuItemMyConf);
	btnMenuItemConfToMe = getPanelWithCount(link4, 0);
	menuBar.add(btnMenuItemConfToMe);

	contentTableDecorator.add(menuBar);
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
