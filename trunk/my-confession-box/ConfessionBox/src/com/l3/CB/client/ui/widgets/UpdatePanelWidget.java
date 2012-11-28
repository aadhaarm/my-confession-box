package com.l3.CB.client.ui.widgets;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.ConfessionUpdate;

public class UpdatePanelWidget extends FlowPanel {

    private final Long confId;
    private final Anchor btnShowUpdates = new Anchor(ConfessionBox.cbText.updateWidgetShowUpdatesLabelText());
    private final Label lblNoUpdates = new Label(ConfessionBox.cbText.updateWidgetNoUpdatesText());
    private VerticalPanel vPnlUpdates = null;
    private Image loaderImage;

    public UpdatePanelWidget(Long confId) {
	super();
	this.setStyleName("buttonShowUpdate");
	this.confId = confId;
	this.add(btnShowUpdates);
	getMeLoaderImage();
	bind();
    }

    private void getMeLoaderImage() {
	loaderImage = new Image(Constants.LOADER_IMAGE_PATH);
	loaderImage.addStyleName(Constants.STYLE_CLASS_LOADER_IMAGE);
    }

    private void addLoaderImage() {
	this.add(loaderImage);
    }

    private void remmoveLoaderImage() {
	this.remove(loaderImage);
    }


    private void bind() {
	btnShowUpdates.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(vPnlUpdates != null && vPnlUpdates.isVisible()) {
		    vPnlUpdates.setVisible(false);
		    btnShowUpdates.setText(ConfessionBox.cbText.updateWidgetShowUpdatesLabelText());
		} else {
		    addLoaderImage();
		    ConfessionBox.confessionService.getConfessionUpdates(confId, new AsyncCallback<List<ConfessionUpdate>>() {
			@Override
			public void onSuccess(List<ConfessionUpdate> result) {
			    if(result != null && !result.isEmpty()) {
				vPnlUpdates = new VerticalPanel();
				for (ConfessionUpdate confessionUpdate : result) {
				    if(confessionUpdate != null && confessionUpdate.getUpdate() != null && confessionUpdate.getUpdate() != null) {
					HTML update = new HTML(Templates.TEMPLATES.confessonUpdate(confessionUpdate.getCommentAs(),
						confessionUpdate.getUpdate(), CommonUtils.getDateInAGOFormat(confessionUpdate.getTimeStamp())));
					update.setStyleName("confessionUpdate");
					vPnlUpdates.add(update);
				    }
				}
				add(vPnlUpdates);
				btnShowUpdates.setText(ConfessionBox.cbText.updateWidgetHideUpdatesLabelText()); 
			    } else {
				btnShowUpdates.setVisible(false);
				add(lblNoUpdates);
			    }
			    remmoveLoaderImage();
			}

			@Override
			public void onFailure(Throwable caught) {
			    Error.handleError("UpdatePanelWidget", "onFailure", caught);
			}
		    });
		}
	    }
	});
    }
}