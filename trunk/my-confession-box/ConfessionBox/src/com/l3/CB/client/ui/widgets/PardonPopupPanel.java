package com.l3.CB.client.ui.widgets;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.CBText;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class PardonPopupPanel extends PopupPanel{

	Button btnPardon;
	Button btnCancel;
	
	public PardonPopupPanel(Confession confession, UserInfo loggedInUser, UserInfo confessionByUser, CBText cbText) {
		super();
		Grid grid = new Grid(5, 2);
		int row = 0;

		grid.setHTML(row, 0, CommonUtils.getProfilePictureAndName(confession, false));
		grid.setWidget(row, 1, CommonUtils.getConfessionWithName(confession, confessionByUser, false, cbText));
		row++;
		
		VerticalPanel vPnlPardonConditions = new VerticalPanel();

		HorizontalPanel hPnlPardonActivityCondition = new HorizontalPanel();
		ListBox lbPardonActivityCondition = new ListBox();
		lbPardonActivityCondition.addItem("5");
		lbPardonActivityCondition.addItem("20");
		lbPardonActivityCondition.addItem("50");
		lbPardonActivityCondition.addItem("100");
		lbPardonActivityCondition.addItem("200");
		final CheckBox cbPardonActivityCondition = new CheckBox(cbText.pardonPopupPardonActivityConditionPartOne());
		hPnlPardonActivityCondition.add(cbPardonActivityCondition);
		hPnlPardonActivityCondition.add(lbPardonActivityCondition);
		hPnlPardonActivityCondition.add(new Label(cbText.pardonPopupPardonActivityConditionPartTwo()));
		
		CheckBox cbOpenIdentityCondition = new CheckBox(cbText.pardonPopupOpenIdentityCondition());
		vPnlPardonConditions.add(hPnlPardonActivityCondition);
		vPnlPardonConditions.add(cbOpenIdentityCondition);
		
		grid.setWidget(row, 1, vPnlPardonConditions);
		row++;
		CheckBox cbAcceptance = new CheckBox(cbText.pardonPopupAcceptance());
		grid.setWidget(row, 1, cbAcceptance);
		row++;
		btnPardon = new Button(cbText.pardonPopupPardonButtonText());
		btnCancel = new Button(cbText.pardonPopupCancelButtonText());
		grid.setWidget(row, 0, btnPardon);
		grid.setWidget(row, 1, btnCancel);
		setWidget(grid);
	}

	public Button getBtnPardon() {
		return btnPardon;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}
}
