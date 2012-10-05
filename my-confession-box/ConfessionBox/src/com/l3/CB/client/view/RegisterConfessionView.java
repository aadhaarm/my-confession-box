package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.presenter.RegisterConfessionPresenter;

public class RegisterConfessionView extends Composite implements RegisterConfessionPresenter.Display {

	private final VerticalPanel confessionFormPanel;
	
	private final HorizontalPanel identityPanel;
	private final RadioButton rbIdentityAnyn;
	private final RadioButton rbIdentityDisclose;
	
	private final TextArea txtConfession;
	
	private final Button btnSubmit;
	
	public RegisterConfessionView() {
		super();
		DecoratorPanel contentTableDecorator = new DecoratorPanel();
		initWidget(contentTableDecorator);
		
		confessionFormPanel = new VerticalPanel();
		
		identityPanel = new HorizontalPanel();
		rbIdentityAnyn = new RadioButton("identity", "Do not disclose");
		rbIdentityAnyn.setValue(true);
		rbIdentityDisclose = new RadioButton("identity", "Disclose");
		identityPanel.add(rbIdentityAnyn);
		identityPanel.add(rbIdentityDisclose);
		
		txtConfession = new TextArea();
		
		btnSubmit = new Button("Register your confession");
		
		confessionFormPanel.add(identityPanel);
		confessionFormPanel.add(txtConfession);
		confessionFormPanel.add(btnSubmit);
		
		contentTableDecorator.add(confessionFormPanel);	
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public HasClickHandlers getSubmitBtn() {
		return this.btnSubmit;
	}

	@Override
	public String getConfession() {
		return this.txtConfession.getValue();
	}

	@Override
	public boolean isAnynShare() {
		if(this.rbIdentityDisclose.getValue()){
			return false;
		}
		return true;
	}
}
