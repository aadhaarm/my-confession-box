package com.l3.CB.client.view.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateMenuEvent;
import com.l3.CB.client.event.confession.FilterConfessionsEvent;
import com.l3.CB.client.presenter.MobileMenuPresenter;
import com.l3.CB.client.ui.widgets.MenuButton;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.HelpInfo;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Filters;

public class MobileMenuView extends Composite implements MobileMenuPresenter.Display {

    private static MobileMenuViewUiBinder uiBinder = GWT
	    .create(MobileMenuViewUiBinder.class);

    interface MobileMenuViewUiBinder extends UiBinder<Widget, MobileMenuView> {
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
    @UiField
    Anchor ancAbout;
    @UiField
    Anchor ancPhilosophy;
    @UiField
    Anchor ancAnonymous;
    @UiField
    Anchor ancCommunity;
    @UiField
    Anchor ancFbconfess;
    @UiField
    Anchor ancPrPolicy;
    @UiField
    Anchor ancTerms;
    @UiField
    Anchor ancLogout;

    public MobileMenuView() {
	initWidget(uiBinder.createAndBindUi(this));
	setLogoutButton();
    }

    public MobileMenuView(String firstName) {
	initWidget(uiBinder.createAndBindUi(this));
	setLogoutButton();
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
	CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_RULE_BOOK);
    }
    @UiHandler("ancAbout")
    void onAboutClick(ClickEvent event) {
	CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_ABOUT_CB);
    }
    @UiHandler("ancPhilosophy")
    void onPhilosophyClick(ClickEvent event) {
	CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_PHILOSPHY);
    }
    @UiHandler("ancAnonymous")
    void onAnonymousClick(ClickEvent event) {
	CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_ANONYMOUS_CONFESSION);
    }
    @UiHandler("ancCommunity")
    void onCommunityClick(ClickEvent event) {
	Window.open("http://www.facebook.com/pages/Confession-Box-Community/129927533826479", "Confession Box" , "resizable=yes,scrollbars=yes");
    }
    @UiHandler("ancFbconfess")
    void onFbconfessClick(ClickEvent event) {
	Window.open("http://www.fbconfess.com", "Confession Box" , "resizable=yes,scrollbars=yes");
    }
    @UiHandler("ancPrPolicy")
    void onPrPolicyClick(ClickEvent event) {
	CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_PRIVACY_POLICY);
    }
    @UiHandler("ancTerms")
    void onTermsClick(ClickEvent event) {
	CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_TOS);
    }

    @UiHandler("ancMostVoted")
    void onMostVotedClick(ClickEvent event) {
	ConfessionBox.eventBus.fireEvent(new FilterConfessionsEvent(Filters.MOST_VOTED));
    }
    @UiHandler("ancGlobal")
    void onGlobalFilterClick(ClickEvent event) {
	ConfessionBox.eventBus.fireEvent(new FilterConfessionsEvent(Filters.ALL));
    }
    @UiHandler("ancLocal")
    void onLocalClick(ClickEvent event) {
	ConfessionBox.eventBus.fireEvent(new FilterConfessionsEvent(Filters.LOCALE_SPECIFIC));
    }
    @UiHandler("ancMostSameBoat")
    void onMostSameBoatVotedClick(ClickEvent event) {
	ConfessionBox.eventBus.fireEvent(new FilterConfessionsEvent(Filters.MOST_SAME_BOATS));
    }
    @UiHandler("ancMostLame")
    void onMostLameVotedClick(ClickEvent event) {
	ConfessionBox.eventBus.fireEvent(new FilterConfessionsEvent(Filters.MOST_LAME));
    }
    @UiHandler("ancMostSympathy")
    void onMostSympathyVotedClick(ClickEvent event) {
	ConfessionBox.eventBus.fireEvent(new FilterConfessionsEvent(Filters.MOST_SYMPATHY));
    }
    @UiHandler("ancMostSBP")
    void onMostSBPVotedClick(ClickEvent event) {
	ConfessionBox.eventBus.fireEvent(new FilterConfessionsEvent(Filters.MOST_SHOULD_BE_PARDONED));
    }
    @UiHandler("ancMostSNBP")
    void onMostSNBPVotedClick(ClickEvent event) {
	ConfessionBox.eventBus.fireEvent(new FilterConfessionsEvent(Filters.MOST_SHOULD_NOT_BE_PARDONED));
    }
    @UiHandler("ancUserVoted")
    void onUserVotedClick(ClickEvent event) {
	ConfessionBox.eventBus.fireEvent(new FilterConfessionsEvent(Filters.USER_ACTIVITY));
    }

    @UiHandler("ancLogout")
    void onLogoutClick(ClickEvent event) {
	CommonUtils.logout(ConfessionBox.cbText.logoutInfoMessage());
	setLogoutButton();
    }

    /**
     * Set Logout button status
     */
    private void setLogoutButton() {
	if(ConfessionBox.isLoggedIn) {
	    ancLogout.removeStyleName("hide");
	    ancLogout.addStyleName("show");
	} else {
	    ancLogout.removeStyleName("show");
	    ancLogout.addStyleName("hide");
	}
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
