package com.l3.CB.client.ui.widgets;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.UserInfo;

public class MenuButton extends AbsolutePanel {

    final HTML btn;
    final Label lblButtonCount;

    public MenuButton(final UserInfo userInfo,
	    final ConfessionServiceAsync confessionService, int count, HTML menuBtn) {
	super();
	this.btn = menuBtn;

	lblButtonCount = new Label(Integer.toString(count));
	this.add(menuBtn);
	lblButtonCount.setStyleName("btnWrapperMenu");
	this.add(lblButtonCount, menuBtn.getElement());
    }

    public HTML getBtn() {
	return btn;
    }

    public Label getBtnCount() {
	return lblButtonCount;
    }
}
