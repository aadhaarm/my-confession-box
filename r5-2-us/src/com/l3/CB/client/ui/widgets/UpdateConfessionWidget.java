package com.l3.CB.client.ui.widgets;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateIdentityVisibilityEvent;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.ConfessionUpdate;

public class UpdateConfessionWidget extends FlowPanel {

    private final CBTextArea txtUpdate;
    private final Button btnSubmit;

    public UpdateConfessionWidget(Confession confession) {
	super();
	txtUpdate = new CBTextArea(Constants.COMM_MAX_CHARS, false, "write your update/response here..");
	this.add(txtUpdate);

	btnSubmit = new Button(ConfessionBox.cbText.confessionUpdateButtonLabelText());
	this.add(btnSubmit);

	bind(confession);
    }

    private void bind(final Confession confession) {
	btnSubmit.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(txtUpdate.validate(Constants.CONF_MIN_CHARS, Constants.COMM_MAX_CHARS)) {
		    btnSubmit.setEnabled(false);
		    Date updateTimeStamp = new Date();

		    String relationName = ConfessionBox.cbText.confesseeRelationNameUpdateLabel();
		    if(!ConfessionBox.getLoggedInUserInfo().getUserId().equals(confession.getUserId())) {
			if(confession.getConfessedTo() != null && !confession.getConfessedTo().isEmpty()) {
			    for (ConfessionShare confessionShare : confession.getConfessedTo()) {
				if(confessionShare != null && confessionShare.getRelation() != null && confessionShare.getRelation().getDisplayText() != null) {
				    relationName = confessionShare.getRelation().getDisplayText();
				}
			    }
			}
		    }

		    String confessionText;
		    if(txtUpdate.getCbTextArea() != null) {
			confessionText = txtUpdate.getCbTextArea().getText();
		    } else {
			confessionText = txtUpdate.getTextArea().getText();
		    }
		    ConfessionUpdate confessionUpdate = new ConfessionUpdate(
			    confession.getConfId(), updateTimeStamp,
			    CommonUtils.checkForNull(confessionText), 
			    relationName, ConfessionBox.getLoggedInUserInfo().getUserId());

		    ConfessionBox.confessionService.registerConfessionUpdate(confessionUpdate , new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
			    ConfessionBox.eventBus.fireEvent(new UpdateIdentityVisibilityEvent(confession));
			}
			@Override
			public void onFailure(Throwable caught) {
			    Error.handleError("UpdateConfessionWidget", "onFailure", caught);
			}
		    });
		}
	    }
	});
    }
}
