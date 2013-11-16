/**
 * L3 Confession Box
 * 
 * Nov 5, 2012, 7:39:30 PM
 */
package com.l3.CB.client.controller;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.FilterEvent;
import com.l3.CB.client.event.FilterEventHandler;
import com.l3.CB.client.event.URLEvent;
import com.l3.CB.client.event.UpdateHPEvent;
import com.l3.CB.client.event.UpdateHPEventHandler;
import com.l3.CB.client.event.UpdateMenuEvent;
import com.l3.CB.client.event.UpdateMenuEventHandler;
import com.l3.CB.client.event.confession.FilterConfessionsEvent;
import com.l3.CB.client.event.confession.FilterConfessionsEventHandler;
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.client.presenter.ConfessionForMeFeedPresenter;
import com.l3.CB.client.presenter.CopyOfRegisterConfessionPresenter;
import com.l3.CB.client.presenter.FooterPresenter;
import com.l3.CB.client.presenter.HeaderPresenter;
import com.l3.CB.client.presenter.HumanPointPresenter;
import com.l3.CB.client.presenter.LoginStatusPresenter;
import com.l3.CB.client.presenter.MenuPresenter;
import com.l3.CB.client.presenter.MobileMenuPresenter;
import com.l3.CB.client.presenter.MyConfessionFeedPresenter;
import com.l3.CB.client.presenter.Presenter;
import com.l3.CB.client.presenter.TextPresenter;
import com.l3.CB.client.ui.widgets.ApplicationTextWidget;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.view.ConfessionFeedView;
import com.l3.CB.client.view.FooterView;
import com.l3.CB.client.view.HumanPointView;
import com.l3.CB.client.view.LoginStatusView;
import com.l3.CB.client.view.confession.ConfessionsContainer;
import com.l3.CB.client.view.header.HeaderView;
import com.l3.CB.client.view.menu.MobileMenuView;
import com.l3.CB.client.view.misc.PopupPanelUI;
import com.l3.CB.client.view.register.RegisterConfessionUI;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Filters;

public class ConfessionController implements Presenter, ValueChangeHandler<String> {

    /**
     * Application variables
     */
    private static HasWidgets container;
    private static HumanPointPresenter humanPointPresenter = null;
    private static MenuPresenter menuPresenter = null;
    private static MobileMenuPresenter mobileMenuPresenter = null;
    private static HeaderPresenter headerPresenter = null;
    private static LoginStatusPresenter loginStatusPresenter = null;

    /**
     * Constructors
     * 
     * @param confId
     */
    public ConfessionController(String confId) {
	super();
	//	Track.track("Application loaded for the first time");
	updateUserInfoAndInitializeAPP();
	// Bind events
	bind();
    }

    /**
     * 
     */
    public static void updateUserInfoAndInitializeAPP() {
	/* Call server with logged in user info Register if new, update info or just bring the user UserID with the user info */
	if(ConfessionBox.isLoggedIn) {
	    // Register user and get User ID
	    ConfessionBox.confessionService.registerUser(ConfessionBox.getLoggedInUserInfo(), new AsyncCallback<Long>() {
		@Override
		public void onSuccess(Long result) {
		    if(result != null) {
			ConfessionBox.getLoggedInUserInfo().setUserId(result);
			initializeApp(true);
		    } else {
			Error.handleError("ConfessionController", "onSuccess", null);
		    }
		}
		@Override
		public void onFailure(Throwable caught) {
		    Error.handleError("ConfessionController", "onFailure", caught);
		}
	    });
	}
    }

    /**
     * Initialize Application for first view
     */
    public static void initializeApp(boolean loadAll) {

	// Initialize MENU
	menuPresenter = new MenuPresenter(new com.l3.CB.client.view.menu.MenuView());
	menuPresenter.go(container);

	mobileMenuPresenter = new MobileMenuPresenter(new MobileMenuView());
	mobileMenuPresenter.go(container);

	// Initialize Header
	headerPresenter = new HeaderPresenter(new HeaderView());
	headerPresenter.go(container);

	// Login status
	if(ConfessionBox.isMobile) {
	    loginStatusPresenter = new LoginStatusPresenter(new LoginStatusView());
	    loginStatusPresenter.go(container);
	}

	// Initialize Human points
	if(ConfessionBox.isLoggedIn) {
	    humanPointPresenter = new HumanPointPresenter(new HumanPointView());
	    humanPointPresenter.go(container);
	}

	// Initialize Footer
	FooterPresenter footerPresenter = new FooterPresenter(new FooterView());
	footerPresenter.go(container);

	if(loadAll) {
	    String loadHash = Window.Location.getHash();
	    if(CommonUtils.isNotNullAndNotEmpty(loadHash)) {
		if((Constants.HISTORY_ITEM_PRIVACY_POLICY.equalsIgnoreCase(loadHash.substring(1))) || (Constants.HISTORY_ITEM_TOS.equalsIgnoreCase(loadHash.substring(1)))) {
		    ConfessionBox.eventBus.fireEvent(new URLEvent(loadHash.substring(1)));
		}  else {
		    CommonUtils.fireHistoryEvent(loadHash.substring(1));
		}
	    } else {
		if(null != ConfessionBox.confId) {
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID);
		} else {
		    CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED);
		}
	    }
	}
    }

    /**
     * Bind various events
     */
    private void bind() {
	// Listen all history new item addition
	History.addValueChangeHandler(this);

	// Handle update human points event
	ConfessionBox.eventBus.addHandler(UpdateHPEvent.TYPE, new UpdateHPEventHandler() {
	    @Override
	    public void updateHPContact(UpdateHPEvent event) {
		humanPointPresenter.updateHumanPoints(event.getUpdatedCount());
	    }
	});

	// Update update menu event
	ConfessionBox.eventBus.addHandler(UpdateMenuEvent.TYPE, new UpdateMenuEventHandler() {
	    @Override
	    public void updateMenuCount(UpdateMenuEvent event) {
		if(ConfessionBox.isLoggedIn) {
		    menuPresenter.initializeMenuCounts();
		}
	    }
	});
	
	// Confession filter event
	ConfessionBox.eventBus.addHandler(FilterConfessionsEvent.TYPE, new FilterConfessionsEventHandler() {
	    @Override
	    public void filter(FilterConfessionsEvent event) {
		CommonUtils.fireHistoryEvent(Constants.HISTORY_ITEM_CONFESSION_FEED + "/" + event.getFilter().toString());
	    }
	});
    }

    /**
     * On value change - When history event change item
     */
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
	String token = event.getValue();

	//	 Track.track(token);

	if (token != null) {
	    CommonUtils.showApplicationLoad();	    
	    Presenter presenter = null;
	    if(token.startsWith(Constants.HISTORY_ITEM_CONFESSION_FEED)) {
		String [] strings = token.split("/");
		if(strings != null && strings.length == 2) {
		    presenter = new ConfessionFeedPresenter(new ConfessionsContainer(), Filters.valueOf(strings[1]));
		} else {
		    presenter = new ConfessionFeedPresenter(new ConfessionsContainer(), Filters.ALL);
		}
	    } else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FEED_WITH_ID)) {
		presenter = new ConfessionFeedPresenter(new ConfessionsContainer(), ConfessionBox.confId);
	    } else if(token.equals(Constants.HISTORY_ITEM_ABOUT_CB)) {
		presenter = new TextPresenter(new PopupPanelUI(ApplicationTextWidget.getAboutTitle(), ApplicationTextWidget.getAboutTextBody()));
		CommonUtils.removeApplicationLoad();
	    } else if(token.equals(Constants.HISTORY_ITEM_ANONYMOUS_CONFESSION)) {
		presenter = new TextPresenter(new PopupPanelUI(ApplicationTextWidget.getAnonymousConfTitle(), ApplicationTextWidget.getAnonymousConfTextBody()));
		CommonUtils.removeApplicationLoad();
	    } else if(token.equals(Constants.HISTORY_ITEM_PHILOSPHY)) {
		presenter = new TextPresenter(new PopupPanelUI(ApplicationTextWidget.getPhilosphyTitle(), ApplicationTextWidget.getPhilosphyTextBody()));
		CommonUtils.removeApplicationLoad();
	    } else if(token.equals(Constants.HISTORY_ITEM_PRIVACY_POLICY)) {
		presenter = new TextPresenter(new PopupPanelUI(ApplicationTextWidget.getPrivacyPolicyTitle(), ApplicationTextWidget.getPrivacyPolicyTextBody()));
		CommonUtils.removeApplicationLoad();
	    } else if(token.equals(Constants.HISTORY_ITEM_RULE_BOOK)) {
		presenter = new TextPresenter(new PopupPanelUI(ApplicationTextWidget.getRuleBookTitle(), ApplicationTextWidget.getRuleBookTextBody()));
		CommonUtils.removeApplicationLoad();
	    } else if(token.equals(Constants.HISTORY_ITEM_TOS)) {
		presenter = new TextPresenter(new PopupPanelUI(ApplicationTextWidget.getTOSTitle(), ApplicationTextWidget.getTOSTextBody()));
		CommonUtils.removeApplicationLoad();
	    } else if(ConfessionBox.isLoggedIn) {
		if (token.equals(Constants.HISTORY_ITEM_REGISTER_CONFESSION)) {
		    presenter = new CopyOfRegisterConfessionPresenter(new RegisterConfessionUI());
		    CommonUtils.removeApplicationLoad();
		} else if(token.equals(Constants.HISTORY_ITEM_MY_CONFESSION_FEED)) {
		    presenter = new MyConfessionFeedPresenter(new ConfessionsContainer());
		} else if(token.equals(Constants.HISTORY_ITEM_CONFESSION_FOR_ME_FEED)) {
		    presenter = new ConfessionForMeFeedPresenter(new ConfessionsContainer());
		}
	    } 
	    if(presenter != null) {
		presenter.go(container);
	    } else {
		presenter = new ConfessionFeedPresenter(new ConfessionFeedView());
		presenter.go(container);
	    }
	}
    }

    /**
     * Presenter GO - initiate Controller
     */
    @Override
    public void go(HasWidgets container) {
	// If not logged-in
	if(!ConfessionBox.isLoggedIn) {
	    initializeApp(true);
	}
	this.container = container;
	// Remove loading Animation
	ConfessionBox.removeApplicationLoad();
    }
}