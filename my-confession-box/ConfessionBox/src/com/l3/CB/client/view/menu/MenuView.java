package com.l3.CB.client.view.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateMenuEvent;
import com.l3.CB.client.presenter.MenuPresenter;
import com.l3.CB.client.ui.widgets.ApplicationTextWidget;
import com.l3.CB.client.ui.widgets.MenuButton;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.HelpInfo;
import com.l3.CB.shared.Constants;

public class MenuView extends Composite implements MenuPresenter.Display {

    private static MenuViewUiBinder uiBinder = GWT.create(MenuViewUiBinder.class);

    interface MenuViewUiBinder extends UiBinder<Widget, MenuView> {
    }

    @UiField
    Anchor ancConfWall;
    
    @UiField
    Anchor ancWrtConf;
    
    @UiField
    Anchor ancMyConf;
    
    @UiField
    Anchor ancConfMe;
    
    @UiField
    Anchor ancInvFrnd;
    
    @UiField
    Anchor ancRulBok;
    
    public MenuView() {
	initWidget(uiBinder.createAndBindUi(this));
    }

    public MenuView(String firstName) {
	initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("ancConfWall")
    void onConfWallClick(ClickEvent event) {
	ConfessionBox.eventBus.fireEvent(new UpdateMenuEvent());
	CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED);
	HelpInfo.cleanToolTip();
    }
    
    @UiHandler("ancWrtConf")
    void onErtConfClick(ClickEvent event) {
	if(ConfessionBox.isLoggedIn) {
	    ConfessionBox.eventBus.fireEvent(new UpdateMenuEvent());
	    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_REGISTER_CONFESSION);
	    HelpInfo.cleanToolTip();
	} else {
	    CommonUtils.login(0, Constants.HISTORY_ITEM_REGISTER_CONFESSION);
	}
    }
    
    @UiHandler("ancMyConf")
    void onMyConfClick(ClickEvent event) {
	if(ConfessionBox.isLoggedIn) {
	    ConfessionBox.eventBus.fireEvent(new UpdateMenuEvent());
	    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
	    HelpInfo.cleanToolTip();
	} else {
	    CommonUtils.login(0, Constants.HISTORY_ITEM_MY_CONFESSION_FEED);
	}
    }
    
    @UiHandler("ancConfMe")
    void onConfMeClick(ClickEvent event) {
	if(ConfessionBox.isLoggedIn) {
	    ConfessionBox.eventBus.fireEvent(new UpdateMenuEvent());
	    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED);
	    HelpInfo.cleanToolTip();
	} else {
	    CommonUtils.login(0, Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED);
	}
    }
    
    @UiHandler("ancInvFrnd")
    void onInvFrndClick(ClickEvent event) {
	CommonUtils.inviteFriends(ConfessionBox.getLoggedInUserInfo().getName(), ConfessionBox.cbText.inviteFriendsTextMessage());
    }
    
    @UiHandler("ancRulBok")
    void onRulBokClick(ClickEvent event) {
	PopupPanel pPnlRuleBook = ApplicationTextWidget.setupCBRuleBook();
	pPnlRuleBook.center();
    }
    
    @Override
    public HTML setFeedItemSelected() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public MenuButton getBtnMenuItemMyConf() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public MenuButton getBtnMenuItemConfToMe() {
	// TODO Auto-generated method stub
	return null;
    }
}