package com.l3.CB.client.view;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.presenter.MenuPresenter;
import com.l3.CB.shared.Constants;

public class MenuView extends Composite implements MenuPresenter.Display {

	private final MenuBar menuBar; 
	
	public MenuView() {
		DecoratorPanel contentTableDecorator = new DecoratorPanel();
		initWidget(contentTableDecorator);
		
		menuBar = new MenuBar(true);
		menuBar.setAnimationEnabled(true);
		menuBar.addItem("Feed", new Command() {
			
			@Override
			public void execute() {
				History.newItem(Constants.HISTORY_ITEM_CONFESSION_FEED);
			}
		});
		
		menuBar.addItem("Confess", new Command() {
			
			@Override
			public void execute() {
				History.newItem(Constants.HISTORY_ITEM_REGISTER_CONFESSION);
			}
		});

		menuBar.addItem("My confessions", new Command() {
			
			@Override
			public void execute() {
				History.newItem(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
			}
		});
		
		contentTableDecorator.add(menuBar);
	}

	public Widget asWidget() {
		return this;
	}
}
