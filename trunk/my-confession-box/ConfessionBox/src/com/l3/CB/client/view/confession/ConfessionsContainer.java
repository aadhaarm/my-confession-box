package com.l3.CB.client.view.confession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.ActivityEvent;
import com.l3.CB.client.event.ActivityEventHandler;
import com.l3.CB.client.event.ShowToolTipEvent;
import com.l3.CB.client.event.ShowToolTipEventHandler;
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.client.ui.widgets.ConfessionPanel;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.Filters;

public class ConfessionsContainer extends Composite implements ConfessionFeedPresenter.Display {

    private static ConfessionsContainerUiBinder uiBinder = GWT
	    .create(ConfessionsContainerUiBinder.class);

    interface ConfessionsContainerUiBinder extends UiBinder<Widget, ConfessionsContainer> {
	
    }

    private int confessionPagesLoaded;
    private Image loaderImage;
    private boolean isMoreConfessionsAvailable;
    private Map<Long, ConfessionPanel> confessionsThisView;

    
    @UiField
    FlowPanel fPnlConfContainer;

    public ConfessionsContainer() {
	
	initWidget(uiBinder.createAndBindUi(this));

	getMeLoaderImage();

	bind();
    }

    public ConfessionsContainer(String firstName) {
	initWidget(uiBinder.createAndBindUi(this));
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
//	ConfessionBox.eventBus.addHandler(CancelActivityEvent.TYPE, new CancelActivityEventHandler() {
//	    @Override
//	    public void cancelActivity(CancelActivityEvent event) {
//		if(event != null) {
//		    final ConfessionPanel fPnlConfessionMain = confessionsThisView.get(event.getConfId());
//		    if(fPnlConfessionMain != null) {
//			fPnlConfessionMain.hideUndoToolTip();
//		    }
//		}
//	    }
//	});
	ConfessionBox.eventBus.addHandler(ShowToolTipEvent.TYPE, new ShowToolTipEventHandler() {
	    @Override
	    public void showToolTip(ShowToolTipEvent event) {
		if(event != null && confessionsThisView != null) {
		    final ConfessionPanel fPnlConfessionMain = confessionsThisView.get(event.getConfId());
		    if(fPnlConfessionMain != null) {
//			fPnlConfessionMain.hideUndoToolTip();
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
    public void setConfessions(List<Confession> confessions, boolean isAnyn, boolean showUserControls, Filters filter, boolean showExtendedDetails, boolean showPardonHelpText) {
	fPnlConfContainer.add(loaderImage);
	if(confessionsThisView == null) {
	    confessionsThisView = new HashMap<Long, ConfessionPanel>();
	}
	if (confessions != null && !confessions.isEmpty()) {
	    for (final Confession confession : confessions) {
		if(confession != null) {
		    final ConfessionView confessionView = new ConfessionView(confession, showUserControls, isAnyn, showExtendedDetails, showPardonHelpText);
//		    final ConfessionPanel fPnlConfessionMain = new ConfessionPanel(confession, showUserControls, isAnyn, showExtendedDetails, showPardonHelpText);
		    if(confessionView != null) {
			fPnlConfContainer.add(confessionView);
//			confessionsThisView.put(confession.getConfId(), fPnlConfessionMain);
		    }
		}
	    }
	} 
	else {
	    isMoreConfessionsAvailable = false;
	}
	if(confessionsThisView == null || confessionsThisView.isEmpty()) {
	    fPnlConfContainer.add(CommonUtils.getEmptyWidget(filter.getEmptyPageText()));
	}
	CommonUtils.removeApplicationLoad();
	fPnlConfContainer.remove(loaderImage);
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
	CommonUtils.removeApplicationLoad();
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
	return fPnlConfContainer;
    }

    @Override
    public void addLoaderImage() {
	fPnlConfContainer.add(loaderImage);
    }

    @Override
    public void incrementConfessionPagesLoaded() {
	confessionPagesLoaded++;
    }

    @Override
    public void removeLoaderImage() {
	fPnlConfContainer.remove(loaderImage);
    }

    @Override
    public void showConfessionFilters() {
//	if(lstFilterOptions != null) {
//	    lstFilterOptions.setVisible(true);
//	}
    }

    @Override
    public HasChangeHandlers getConfessionFilterListBox() {
//	return lstFilterOptions;
	return null;
    }

    @Override
    public HasFocusHandlers getConfessionFilterListBoxForHelp() {
//	return lstFilterOptions;
	return null;
    }

    @Override
    public void clearConfessions() {
	fPnlConfContainer.clear();
//	if(lstFilterOptions != null)
//	    fPnlTopBar.add(lstFilterOptions);
//	fPnlTopBar.add(btnRefresh);
//	if(lblfilterInfo != null)
//	    fPnlTopBar.add(lblfilterInfo);
//	vpnlConfessionList.add(fPnlTopBar);
    }

    @Override
    public HasClickHandlers getRefreshButton() {
	return null;
//	return btnRefresh;
    }

    @Override
    public void showEmptyScreen() {
    }

    public void setFilterInfo(String filterInfoText) {
//	if(lblfilterInfo != null && filterInfoText != null) {
//	    lblfilterInfo.setText(filterInfoText);
//	}
    }

    public void resetConfessionsThisView() {
	confessionsThisView = null;
    }
}