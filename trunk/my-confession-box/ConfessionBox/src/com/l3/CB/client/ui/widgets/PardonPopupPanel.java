package com.l3.CB.client.ui.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateFeedToMeEvent;
import com.l3.CB.client.event.UpdateHPEvent;
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

    private Confession confession;
    private UserInfo confessedByUserInfo;
    private Image imgProfileImage;
    private FlowPanel fPnlName;
    private Label lblConfessionTitle;
    private FlowPanel fPnlConfessionText;

    public PardonPopupPanel(Confession confession, UserInfo confessionByUser, Button btnPardonHome) {
	super();
	this.confession = confession;
	FlowPanel fPnlpardonPopup = new FlowPanel();
	
	//Profile picture
	imgProfileImage = CommonUtils.getProfilePicture(this.confession, false);
	// User Profile name or ANYN
	fPnlName = CommonUtils.getName(this.confession, confessedByUserInfo, false, true);
	// Confession Text
	fPnlConfessionText = CommonUtils.getTextTruncated(this.confession.getConfession());

	// TOP Container
	FlowPanel fPnlTopContent = new FlowPanel();
	fPnlTopContent.setStyleName(Constants.DIV_CONFESSION_PANEL_TOP_CONTAINER);

	// Set USER PROFILE PIC or ANYN PIC 
	fPnlTopContent.add(imgProfileImage);

	// Set USER NAME
	fPnlTopContent.add(fPnlName);

	fPnlpardonPopup.add(fPnlTopContent);

	// MIDDLE Container
	FlowPanel fPnlMiddleContent = new FlowPanel();
	fPnlMiddleContent.setStyleName(Constants.DIV_CONFESSION_PANEL_MIDDLE_CONTAINER);

	// Confession Title
	lblConfessionTitle = new Label(confession.getConfessionTitle());
	lblConfessionTitle.setStyleName(Constants.STYLE_CLASS_CONFESSION_TITLE_TEXT);
	fPnlMiddleContent.add(lblConfessionTitle);

	// Confession text
	fPnlMiddleContent.add(fPnlConfessionText);
	fPnlpardonPopup.add(fPnlMiddleContent);
	

	VerticalPanel vPnlPardonConditions = new VerticalPanel();

	HorizontalPanel hPnlPardonActivityCondition = new HorizontalPanel();
	lbPardonActivityCondition = new ListBox();
	
	//TODO: remove 1 option
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
	hPnlPardonActivityCondition.setSpacing(5);
	vPnlPardonConditions.add(hPnlPardonActivityCondition);

	HorizontalPanel hPnlPardonHideCondition = new HorizontalPanel();
	cbOpenIdentityCondition = new CheckBox(ConfessionBox.cbText.pardonPopupOpenIdentityCondition());
	hPnlPardonHideCondition.add(cbOpenIdentityCondition);
	hPnlPardonHideCondition.setSpacing(5);
	vPnlPardonConditions.add(hPnlPardonHideCondition);

	fPnlpardonPopup.add(vPnlPardonConditions);

	btnPardon = new Button(ConfessionBox.cbText.pardonPopupPardonButtonText());
	btnPardon.setStyleName(Constants.STYLE_CLASS_PARDON_BUTTON);
	btnCancel = new Button(ConfessionBox.cbText.pardonPopupCancelButtonText());
	btnCancel.setStyleName("popupCancelButton");
	fPnlpardonPopup.add(btnPardon);
	fPnlpardonPopup.add(btnCancel);
	
	setWidget(fPnlpardonPopup);

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
		    ConfessionBox.confessionService.pardonConfession(
			    ConfessionBox.getLoggedInUserInfo(),
			    confession.getConfId(), confessedByUser,
			    pardonConditions, pardonStatus, new Date(),
			    new AsyncCallback<Void>() {
				@Override
				public void onSuccess(Void result) {
				    btnPardon.setEnabled(false);
				    btnPardonHome.setEnabled(false);
				    hidePopup();
				    ConfessionBox.confEventBus.fireEvent(new UpdateFeedToMeEvent(confession));
				    ConfessionBox.confEventBus.fireEvent(new UpdateHPEvent(Constants.POINTS_ON_PARDONING));
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