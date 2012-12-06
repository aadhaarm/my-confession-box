package com.l3.CB.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.ActivityEvent;
import com.l3.CB.client.event.ActivityEventHandler;
import com.l3.CB.client.event.CancelActivityEvent;
import com.l3.CB.client.event.CancelActivityEventHandler;
import com.l3.CB.client.event.ShowToolTipEvent;
import com.l3.CB.client.event.ShowToolTipEventHandler;
import com.l3.CB.client.event.UpdateMenuEvent;
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.client.ui.widgets.ConfessionPanel;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.HelpInfo;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.Filters;

public class ConfessionFeedView extends Composite implements ConfessionFeedPresenter.Display {

    private final DecoratorPanel contentTableDecorator;
    private final VerticalPanel vpnlConfessionList;
    private int confessionPagesLoaded;
    private Image loaderImage;
    private boolean isMoreConfessionsAvailable;
    private final ListBox lstFilterOptions;
    private final Button btnRefresh;
    private final FlowPanel fPnlTopBar;
    private Map<Long, ConfessionPanel> confessionsThisView;
    private final Label lblfilterInfo;

    public ConfessionFeedView() {
	super();
	lstFilterOptions = CommonUtils.getFilterListBox();
	lblfilterInfo = new Label();
	lblfilterInfo.setStyleName("filterInfo");


	fPnlTopBar = new FlowPanel();
	fPnlTopBar.setStyleName("confessionTopBar");

//	if(ConfessionBox.isSmallScreen) {
//	    fPnlTopBar.add(setupSmallMenu());
//	}

	btnRefresh = new Button();
	btnRefresh.setTitle("Refresh");
	btnRefresh.setStyleName(Constants.STYLE_CLASS_REFRESH_BUTTON);
	fPnlTopBar.add(btnRefresh);

	fPnlTopBar.add(lstFilterOptions);
	fPnlTopBar.add(lblfilterInfo);

	confessionPagesLoaded = 1;
	getMeLoaderImage();
	isMoreConfessionsAvailable = true;

	vpnlConfessionList = new VerticalPanel();
	vpnlConfessionList.setHorizontalAlignment(HorizontalPanel.ALIGN_JUSTIFY);
	vpnlConfessionList.add(fPnlTopBar);

	contentTableDecorator = new DecoratorPanel();
	contentTableDecorator.add(vpnlConfessionList);

	contentTableDecorator.setStyleName("confessionListDecoratoePanel");
	initWidget(contentTableDecorator);

	bind();
    }

    /**
     * @param ancLogout
     */
    private ListBox setupSmallMenu() {
	/*
	 * Logout link
	 */
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
		    //		pPnlRuleBook.center();
		} else if("logout".equals(newValue)) {
		    CommonUtils.logout(ConfessionBox.cbText.logoutInfoMessage());
		    ConfessionBox.isLoggedIn = false;
		    menuListBox.removeItem(6);
		}
	    }
	});
	menuListBox.setStyleName("showMenuButton");
	return menuListBox;
    }


    private void bind() {
	ConfessionBox.confEventBus.addHandler(ActivityEvent.TYPE, new ActivityEventHandler() {
	    @Override
	    public void registerVote(ActivityEvent event) {
		if(event != null) {
		    final ConfessionPanel fPnlConfessionMain = confessionsThisView.get(event.getConfId());
		    if(fPnlConfessionMain != null) {
			fPnlConfessionMain.showUndoTooltip();
		    }
		}
	    }
	});
	ConfessionBox.confEventBus.addHandler(CancelActivityEvent.TYPE, new CancelActivityEventHandler() {
	    @Override
	    public void cancelActivity(CancelActivityEvent event) {
		if(event != null) {
		    final ConfessionPanel fPnlConfessionMain = confessionsThisView.get(event.getConfId());
		    if(fPnlConfessionMain != null) {
			fPnlConfessionMain.hideUndoToolTip();
		    }
		}
	    }
	});
	ConfessionBox.confEventBus.addHandler(ShowToolTipEvent.TYPE, new ShowToolTipEventHandler() {

	    @Override
	    public void showToolTip(ShowToolTipEvent event) {
		if(event != null && confessionsThisView != null) {
		    final ConfessionPanel fPnlConfessionMain = confessionsThisView.get(event.getConfId());
		    if(fPnlConfessionMain != null) {
			fPnlConfessionMain.hideUndoToolTip();
			fPnlConfessionMain.showShareToolTip();
		    }
		}
	    }
	});
    }

    private void getMeLoaderImage() {
	loaderImage = new Image(Constants.LOADER_IMAGE_PATH);
	loaderImage.addStyleName(Constants.STYLE_CLASS_LOADER_IMAGE);
    }

    @Override
    public void setConfessions(List<Confession> confessions, boolean isAnyn, boolean showUserControls, Filters filter) {
	if(confessionsThisView == null) {
	    confessionsThisView = new HashMap<Long, ConfessionPanel>();
	}
	if (confessions != null && !confessions.isEmpty()) {
	    for (final Confession confession : confessions) {
		if(confession != null) {
		    final ConfessionPanel fPnlConfessionMain = new ConfessionPanel(confession, showUserControls, isAnyn);
		    if(fPnlConfessionMain != null) {
			vpnlConfessionList.add(fPnlConfessionMain);
			confessionsThisView.put(confession.getConfId(), fPnlConfessionMain);
		    }
		}
	    }
	} else {
	    if(confessionsThisView == null || confessionsThisView.isEmpty()) {
		vpnlConfessionList.add(CommonUtils.getEmptyWidget(filter.getEmptyPageText()));
	    }
	    isMoreConfessionsAvailable = false;
	}
    }

    /**
     * Update one confession
     * @param confession - updated confession object
     */
    @Override
    public void setConfessions(Confession confession) {
	if (confession != null) {
	    final ConfessionPanel fPnlConfessionMain = confessionsThisView.get(confession.getConfId());
	    if(fPnlConfessionMain != null) {
		fPnlConfessionMain.clear();
		fPnlConfessionMain.getConfessionWidgetsSetup(confession);
		//		CommonUtils.parseXFBMLJS(DOM.getElementById("confession-id-" + confession.getConfId()));
	    }
	}
    }

    @Override
    public Widget asWidget() {
	return this;
    }

    @Override
    public void setConfessionPagesLoaded(int confessionPagesLoaded) {
	this.confessionPagesLoaded = confessionPagesLoaded;
    }

    public void setMoreConfessions(boolean moreConfessions) {
	this.isMoreConfessionsAvailable = moreConfessions;
    }

    @Override
    public int getConfessionPagesLoaded() {
	return confessionPagesLoaded;
    }

    @Override
    public Image getLoaderImage() {
	return loaderImage;
    }

    @Override
    public boolean isMoreConfessions() {
	return isMoreConfessionsAvailable;
    }

    @Override
    public VerticalPanel getVpnlConfessionList() {
	return vpnlConfessionList;
    }

    @Override
    public void addLoaderImage() {
	vpnlConfessionList.add(loaderImage);
    }

    @Override
    public void incrementConfessionPagesLoaded() {
	confessionPagesLoaded++;
    }

    @Override
    public void removeLoaderImage() {
	vpnlConfessionList.remove(loaderImage);
    }

    @Override
    public void showConfessionFilters() {
	lstFilterOptions.setVisible(true);
    }

    @Override
    public HasChangeHandlers getConfessionFilterListBox() {
	return lstFilterOptions;
    }

    @Override
    public HasFocusHandlers getConfessionFilterListBoxForHelp() {
	return lstFilterOptions;
    }

    @Override
    public void clearConfessions() {
	vpnlConfessionList.clear();
	fPnlTopBar.add(lstFilterOptions);
	fPnlTopBar.add(btnRefresh);
	fPnlTopBar.add(lblfilterInfo);
	vpnlConfessionList.add(fPnlTopBar);
    }

    @Override
    public HasClickHandlers getRefreshButton() {
	return btnRefresh;
    }

    @Override
    public void showEmptyScreen() {
    }

    public void setFilterInfo(String filterInfoText) {
	lblfilterInfo.setText(filterInfoText);
    }

    public void resetConfessionsThisView() {
	confessionsThisView = null;
    }
}