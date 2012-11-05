package com.l3.CB.client.ui.widgets;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class ShareTooltipBarWidget extends FlowPanel {

	public ShareTooltipBarWidget() {
		super();
		this.setStyleName("tooltip_right");
		Anchor ancShare = new Anchor("Share");
		Label lblShareToolTip = new Label("all ur votes on your wall");
		Anchor ancHelpInfo = new Anchor("[?]");
		this.add(ancShare);
		this.add(lblShareToolTip);
		this.add(ancHelpInfo);
	}
	
}
