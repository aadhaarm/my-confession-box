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

public class UpdateConfessionWidget extends FlowPanel {

    private final CBTextArea txtUpdate;
    private final Button btnSubmit;

    public UpdateConfessionWidget(Confession confession) {
	super();
	txtUpdate = new CBTextArea(Constants.COMM_MAX_CHARS, false);
	this.add(txtUpdate);

	btnSubmit = new Button("Update");
	this.add(btnSubmit);

	bind(confession);
    }

    private void bind(final Confession confession) {
	btnSubmit.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(txtUpdate.validate(Constants.CONF_MIN_CHARS, Constants.COMM_MAX_CHARS)) {
		    Date updateTimeStamp = new Date();
		    confession.setConfession(confession.getConfession()
			    .concat("<br/>Update - ")
			    .concat(CommonUtils.getDateInFormat(updateTimeStamp)
				    .concat("<hr/>")
				    .concat(CommonUtils.checkForNull(txtUpdate.getCbTextArea().getText()))));
		    
		    ConfessionBox.confessionService.registerConfession(confession, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
			    ConfessionBox.confEventBus.fireEvent(new UpdateIdentityVisibilityEvent(confession));
			}
			@Override
			public void onFailure(Throwable caught) {
			    Error.handleError("RegisterConfessionPresenter", "onFailure", caught);
			}
		    });  

		}
	    }
	});
    }
}
