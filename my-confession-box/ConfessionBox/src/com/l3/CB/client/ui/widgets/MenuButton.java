package com.l3.CB.client.ui.widgets;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.l3.CB.client.ConfessionServiceAsync;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.UserInfo;

public class MenuButton extends AbsolutePanel {

	Logger logger = Logger.getLogger("CBLogger");
	final PushButton btn;
	final Label lblButtonCount;
	
	public MenuButton(final UserInfo userInfo,
			final ConfessionServiceAsync confessionService, int count, PushButton menuBtn) {
		super();
		this.btn = menuBtn;
//		this.addStyleName("activityButtonContainer");
		
		lblButtonCount = new Label(Integer.toString(count));
		this.add(menuBtn);
		lblButtonCount.setStyleName(Constants.STYLE_CLASS_BUTTON_WRAPPER);
		this.add(lblButtonCount, menuBtn.getElement());
	}

	public PushButton getBtn() {
		return btn;
	}

	public Label getBtnCount() {
		return lblButtonCount;
	}
}
