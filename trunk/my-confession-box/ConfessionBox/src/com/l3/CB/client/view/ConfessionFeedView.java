package com.l3.CB.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.presenter.ConfessionFeedPresenter;
import com.l3.CB.client.ui.widgets.ConfessionPanel;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;

public class ConfessionFeedView extends Composite implements ConfessionFeedPresenter.Display {

    private final DecoratorPanel contentTableDecorator;
    private final VerticalPanel vpnlConfessionList;
    private int confessionPagesLoaded;
    private Image loaderImage;
    private boolean isMoreConfessionsAvailable;
    private final ListBox lstFilterOptions;
    private final Button btnRefresh;
    private final HorizontalPanel hPnlTopBar;
    private Map<Long, ConfessionPanel> confessionsThisView;

    public ConfessionFeedView() {
	super();
	lstFilterOptions = new ListBox();
	CommonUtils.getMeFilterListBox(lstFilterOptions);

	btnRefresh = new Button();
	btnRefresh.setTitle("Refresh");
	btnRefresh.setStyleName(Constants.STYLE_CLASS_REFRESH_BUTTON);

	hPnlTopBar = new HorizontalPanel();
	hPnlTopBar.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
	hPnlTopBar.add(lstFilterOptions);
	hPnlTopBar.add(btnRefresh);

	confessionPagesLoaded = 1;
	getMeLoaderImage();
	isMoreConfessionsAvailable = true;

	vpnlConfessionList = new VerticalPanel();
	vpnlConfessionList.add(hPnlTopBar);

	contentTableDecorator = new DecoratorPanel();
	contentTableDecorator.add(vpnlConfessionList);

	initWidget(contentTableDecorator);
    }

    private void getMeLoaderImage() {
	loaderImage = new Image(Constants.LOADER_IMAGE_PATH);
	loaderImage.addStyleName(Constants.STYLE_CLASS_LOADER_IMAGE);
    }

    @Override
    public void setConfessions(List<Confession> confessions, boolean isAnyn, boolean showUserControls) {
	if(confessionsThisView == null) {
	    confessionsThisView = new HashMap<Long, ConfessionPanel>();
	}
	if (confessions != null && !confessions.isEmpty()) {
	    for (final Confession confession : confessions) {
		final ConfessionPanel fPnlConfessionMain = new ConfessionPanel(confession, showUserControls, isAnyn);
		vpnlConfessionList.add(fPnlConfessionMain);
		CommonUtils.parseXFBMLJS(DOM.getElementById("confession-id-" + confession.getConfId()));
		confessionsThisView.put(confession.getConfId(), fPnlConfessionMain);
	    }
	} else {
	    if(confessionsThisView == null || confessionsThisView.isEmpty()) {
		vpnlConfessionList.clear();
		vpnlConfessionList.add(CommonUtils.getEmptyWidget());
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
//	    confessionsThisView.remove(confession.getConfId());
	    if(fPnlConfessionMain != null) {
		fPnlConfessionMain.clear();
		fPnlConfessionMain.getConfessionWidgetsSetup(confession);
//		vpnlConfessionList.add(fPnlConfessionMain);
		CommonUtils.parseXFBMLJS(DOM.getElementById("confession-id-" + confession.getConfId()));
//		confessionsThisView.put(confession.getConfId(), fPnlConfessionMain);
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
	hPnlTopBar.add(lstFilterOptions);
	hPnlTopBar.add(btnRefresh);
	vpnlConfessionList.add(hPnlTopBar);
    }

    @Override
    public HasClickHandlers getRefreshButton() {
	return btnRefresh;
    }

    @Override
    public void showEmptyScreen() {
    }
}