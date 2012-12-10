package com.l3.CB.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
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
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.client.ui.widgets.ConfessionPanel;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.Filters;

public class ConfessionFeedView extends Composite implements ConfessionFeedPresenter.Display {

    private final FlowPanel vpnlConfessionList;
    private int confessionPagesLoaded;
    private Image loaderImage;
    private boolean isMoreConfessionsAvailable;
    private ListBox lstFilterOptions;
    private final Button btnRefresh;
    private final FlowPanel fPnlTopBar;
    private Map<Long, ConfessionPanel> confessionsThisView;
    private Label lblfilterInfo;

    public ConfessionFeedView() {
	super();

	fPnlTopBar = new FlowPanel();
	fPnlTopBar.setStyleName("confessionTopBar");

	btnRefresh = new Button();
	btnRefresh.setTitle(ConfessionBox.cbText.refreshButtonToolTipText());
	btnRefresh.setStyleName(Constants.STYLE_CLASS_REFRESH_BUTTON);
	fPnlTopBar.add(btnRefresh);

	if(!ConfessionBox.isMobile) {
	    lstFilterOptions = CommonUtils.getFilterListBox();
	    lblfilterInfo = new Label();
	    lblfilterInfo.setStyleName(Constants.STYLE_CLASS_CONFESION_FILTER_DESCRIPTION_INFO);
	    fPnlTopBar.add(lstFilterOptions);
	    fPnlTopBar.add(lblfilterInfo);
	} else {
	    fPnlTopBar.setVisible(false);
	}

	confessionPagesLoaded = 1;
	getMeLoaderImage();
	isMoreConfessionsAvailable = true;

	vpnlConfessionList = new FlowPanel();
//	vpnlConfessionList.setHorizontalAlignment(HorizontalPanel.ALIGN_JUSTIFY);
	vpnlConfessionList.add(fPnlTopBar);

	initWidget(vpnlConfessionList);
	bind();
    }

    private void bind() {
	ConfessionBox.eventBus.addHandler(ActivityEvent.TYPE, new ActivityEventHandler() {
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
	ConfessionBox.eventBus.addHandler(CancelActivityEvent.TYPE, new CancelActivityEventHandler() {
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
	ConfessionBox.eventBus.addHandler(ShowToolTipEvent.TYPE, new ShowToolTipEventHandler() {

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
    public FlowPanel getVpnlConfessionList() {
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
	if(lstFilterOptions != null) {
	    lstFilterOptions.setVisible(true);
	}
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
	if(lstFilterOptions != null)
	    fPnlTopBar.add(lstFilterOptions);
	fPnlTopBar.add(btnRefresh);
	if(lblfilterInfo != null)
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
	if(lblfilterInfo != null && filterInfoText != null) {
	    lblfilterInfo.setText(filterInfoText);
	}
    }

    public void resetConfessionsThisView() {
	confessionsThisView = null;
    }
}