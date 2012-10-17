package com.l3.CB.client.view;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.presenter.MenuPresenter;
import com.l3.CB.shared.CBText;
import com.l3.CB.shared.Constants;

public class MenuView extends Composite implements MenuPresenter.Display {

	private final MenuBar menuBar; 
	private final MenuItem feedItem;
	private final MenuItem confessItem;
	private final MenuItem myConItem;
	private final MenuItem conToMeItem;
	private MenuItem selectedMenuItem;
	
	public MenuView(CBText cbText) {
		DecoratorPanel contentTableDecorator = new DecoratorPanel();
		initWidget(contentTableDecorator);
		menuBar = new MenuBar(true);
		menuBar.setAnimationEnabled(true);
		menuBar.addStyleName(Constants.STYLE_CLASS_MENU);
	
		feedItem = new MenuItem(cbText.cbMenuConfessionFeed(), new Command() {
			
			@Override
			public void execute() {
				feedItem.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
				if(selectedMenuItem != null) {
					selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
				}
				selectedMenuItem = feedItem;
				History.newItem(Constants.HISTORY_ITEM_CONFESSION_FEED);
			}
		});
		
		confessItem = new MenuItem(cbText.cbMenuConfessionConfess(), new Command() {
			
			@Override
			public void execute() {
				confessItem.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
				if(selectedMenuItem != null) {
					selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
				}
				selectedMenuItem = confessItem;
				History.newItem(Constants.HISTORY_ITEM_REGISTER_CONFESSION);
			}
		});

		myConItem = new MenuItem(cbText.cbMenuConfessionMyConfessions(), new Command() {
			
			@Override
			public void execute() {
				myConItem.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
				if(selectedMenuItem != null) {
					selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
				}
				selectedMenuItem = myConItem;
				History.newItem(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
			}
		});

		conToMeItem = new MenuItem(cbText.cbMenuConfessionConfessToMe(), new Command() {
			
			@Override
			public void execute() {
				conToMeItem.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
				if(selectedMenuItem != null) {
					selectedMenuItem.removeStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
				}
				selectedMenuItem = conToMeItem;
				History.newItem(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED);
			}
		});

		menuBar.addItem(feedItem);
		menuBar.addItem(confessItem);
		menuBar.addItem(myConItem);
		menuBar.addItem(conToMeItem);
		
		contentTableDecorator.add(menuBar);
	}

	public Widget asWidget() {
		return this;
	}

	@Override
	public MenuItem setFeedItemSelected() {
		feedItem.addStyleName(Constants.STYLE_CLASS_MENU_ITEM_SELECTED);
		selectedMenuItem = feedItem;
		return feedItem;
	}

}
