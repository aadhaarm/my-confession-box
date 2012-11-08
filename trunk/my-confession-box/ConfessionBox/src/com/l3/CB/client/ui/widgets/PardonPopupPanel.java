package com.l3.CB.client.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateFeedToMeEvent;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.PardonStatus;
import com.l3.CB.shared.TO.UserInfo;

public class PardonPopupPanel extends PopupPanel{

    private final Button btnPardon;
    private final Button btnCancel;
    private final CheckBox cbPardonActivityCondition;
    private final CheckBox cbOpenIdentityCondition;
    private final ListBox lbPardonActivityCondition;

    public PardonPopupPanel(Confession confession, UserInfo confessionByUser, Button btnPardonHome) {
	super();
	Grid grid = new Grid(5, 2);
	int row = 0;

	grid.setWidget(row, 0, CommonUtils.getProfilePicture(confession, false));
	grid.setWidget(row, 1, CommonUtils.getConfession(confession));
	row++;

	VerticalPanel vPnlPardonConditions = new VerticalPanel();

	HorizontalPanel hPnlPardonActivityCondition = new HorizontalPanel();
	lbPardonActivityCondition = new ListBox();
	lbPardonActivityCondition.addItem("1");
	lbPardonActivityCondition.addItem("5");
	lbPardonActivityCondition.addItem("10");
	lbPardonActivityCondition.addItem("20");
	lbPardonActivityCondition.addItem("50");
	lbPardonActivityCondition.addItem("100");
	lbPardonActivityCondition.addItem("200");
	cbPardonActivityCondition = new CheckBox(ConfessionBox.cbText.pardonPopupPardonActivityConditionPartOne());
	hPnlPardonActivityCondition.add(cbPardonActivityCondition);
	hPnlPardonActivityCondition.add(lbPardonActivityCondition);
	hPnlPardonActivityCondition.add(new Label(ConfessionBox.cbText.pardonPopupPardonActivityConditionPartTwo()));
	vPnlPardonConditions.add(hPnlPardonActivityCondition);

	cbOpenIdentityCondition = new CheckBox(ConfessionBox.cbText.pardonPopupOpenIdentityCondition());
	vPnlPardonConditions.add(cbOpenIdentityCondition);

	grid.setWidget(row, 1, vPnlPardonConditions);
	row++;
	CheckBox cbAcceptance = new CheckBox(ConfessionBox.cbText.pardonPopupAcceptance());
	grid.setWidget(row, 1, cbAcceptance);
	row++;
	btnPardon = new Button(ConfessionBox.cbText.pardonPopupPardonButtonText());
	btnCancel = new Button(ConfessionBox.cbText.pardonPopupCancelButtonText());
	grid.setWidget(row, 0, btnPardon);
	grid.setWidget(row, 1, btnCancel);
	setWidget(grid);

	bind(confession, confessionByUser, btnPardonHome);
    }

    private void bind(final Confession confession, final UserInfo confessedByUser, final Button btnPardonHome) {
	btnCancel.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		hide();
	    }
	});

	btnPardon.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if(confessedByUser != null) {
		    List<PardonCondition> pardonConditions= getPardonConditions();

		    /**
		     * Pardon if no conditions
		     * Awaiting pardon if pardoned with conditions		    
		     */
		    PardonStatus pardonStatus = PardonStatus.PARDONED;
		    if(pardonConditions != null && !pardonConditions.isEmpty()) {
			pardonStatus = PardonStatus.PARDONED_WITH_CONDITION;
		    }
		    ConfessionBox.confessionService.pardonConfession(ConfessionBox.loggedInUserInfo, confession.getConfId(), confessedByUser, pardonConditions, pardonStatus, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
			    btnPardon.setEnabled(false);
			    btnPardonHome.setEnabled(false);
			    hidePopup();
			    ConfessionBox.confEventBus.fireEvent(new UpdateFeedToMeEvent(confession));
			}

			@Override
			public void onFailure(Throwable caught) {
			    btnPardon.setEnabled(true);
			    Error.handleError("PardonPopupPanel", "bind", caught);
			}
		    });

		}
	    }
	});
    }

    protected List<PardonCondition> getPardonConditions() {
	List<PardonCondition> pardonConditions = new ArrayList<PardonCondition>();
	if(cbOpenIdentityCondition != null && cbOpenIdentityCondition.getValue()) {
	    PardonCondition unHideIdentityCondition = new PardonCondition(Constants.pardonConditionUnhide, 0);
	    pardonConditions.add(unHideIdentityCondition);
	}
	if(cbPardonActivityCondition != null && cbPardonActivityCondition.getValue()) {
	    int count = Integer.parseInt(lbPardonActivityCondition.getValue(lbPardonActivityCondition.getSelectedIndex()));
	    PardonCondition activitySPCondition = new PardonCondition(Constants.pardonConditionSPVotes, count);
	    pardonConditions.add(activitySPCondition);
	}
	return pardonConditions;
    }

    public Button getBtnPardon() {
	return btnPardon;
    }

    public Button getBtnCancel() {
	return btnCancel;
    }

    public void hidePopup() {
	hide();
    }
}