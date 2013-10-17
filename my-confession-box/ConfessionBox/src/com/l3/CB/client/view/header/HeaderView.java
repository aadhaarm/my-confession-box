package com.l3.CB.client.view.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.presenter.HeaderPresenter;

public class HeaderView extends Composite implements HeaderPresenter.Display {

    private static HeaderViewUiBinder uiBinder = GWT
	    .create(HeaderViewUiBinder.class);

    interface HeaderViewUiBinder extends UiBinder<Widget, HeaderView> {
    }

    public HeaderView() {
	initWidget(uiBinder.createAndBindUi(this));
    }

    public HeaderView(String firstName) {
	initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void initializeMenuCounts() {
	// TODO
	//	ConfessionBox.confessionService.getHumanPoints(ConfessionBox.getLoggedInUserInfo().getUserId(), new AsyncCallback<Integer>() {
	//	    @Override
	//	    public void onSuccess(Integer result) {
	//		HTML a = new HTML("Human Points " + Long.toString(result));
	//		a.setStyleName("pointsLabel");
	//		pnlpoints.add(a);
	//	    }
	//	    
	//	    @Override
	//	    public void onFailure(Throwable caught) {
	//		Error.handleError("HumanPointPresenter", "onFailure", caught);
	//	    }
	//	});
	//
	//	ConfessionBox.confessionService.getNumberOfConfessionForMe(ConfessionBox.getLoggedInUserInfo().getUserId(), new AsyncCallback<Long>() {
	//	    @Override
	//	    public void onSuccess(Long result) {
	//		HTML a = new HTML("Confessed To Me " + Long.toString(result));
	//		a.setStyleName("pointsLabel");
	//		pnlpoints.add(a);
	//	    }
	//	    
	//	    @Override
	//	    public void onFailure(Throwable caught) {
	//		Error.handleError("MenuPresenter", "onFailure", caught);
	//	    }
	//	});
	//
	//	ConfessionBox.confessionService.getMyConfessionNumber(ConfessionBox.getLoggedInUserInfo().getUserId(), new AsyncCallback<Long>() {
	//	    @Override
	//	    public void onSuccess(Long result) {
	//		HTML a = new HTML("My Confessions " + Long.toString(result));
	//		a.setStyleName("pointsLabel");
	//		pnlpoints.add(a);
	//	    }
	//
	//	    @Override
	//	    public void onFailure(Throwable caught) {
	//		Error.handleError("MenuPresenter", "onFailure", caught);
	//	    }
	//	});
    }

    @Override
    public HasClickHandlers getApplnTitle() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ListBox getFilterListBox() {
	// TODO Auto-generated method stub
	return null;
    }

}