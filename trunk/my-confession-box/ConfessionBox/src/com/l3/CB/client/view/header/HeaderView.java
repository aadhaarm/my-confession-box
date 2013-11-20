package com.l3.CB.client.view.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.presenter.HeaderPresenter;

public class HeaderView extends Composite implements HeaderPresenter.Display {

    private static HeaderViewUiBinder uiBinder = GWT
	    .create(HeaderViewUiBinder.class);

    interface HeaderViewUiBinder extends UiBinder<Widget, HeaderView> {
    }

    @UiField
    SpanElement spanHumanPtCount;

    public HeaderView() {
	initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void initializeMenuCounts(Integer count) {
	if(count == null) {
	    ConfessionBox.confessionService.getHumanPoints(ConfessionBox.getLoggedInUserInfo().getUserId(), new AsyncCallback<Integer>() {

		@Override
		public void onSuccess(Integer result) {
		    spanHumanPtCount.setInnerText(result + "");
		}

		@Override
		public void onFailure(Throwable caught) {
		    com.l3.CB.client.util.Error.handleError("HumanPointPresenter", "onFailure", caught);
		}
	    });
	} else {
	    spanHumanPtCount.setInnerText(count + "");
	}

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