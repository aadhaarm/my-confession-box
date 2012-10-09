package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.presenter.RegisterConfessionPresenter;

public class RegisterConfessionView extends Composite implements RegisterConfessionPresenter.Display {

	private final VerticalPanel confessionFormPanel;
	
	private final HorizontalPanel identityPanel;
	private final RadioButton rbIdentityAnyn;
	private final RadioButton rbIdentityDisclose;
	private final RadioButton rbIdentityDiscloseToSome;
	private final TextArea txtConfession;
	private final Button btnSubmit;
	private final HorizontalPanel hPanelShare;
	private MultiWordSuggestOracle friendsOracle;
	private SuggestBox friendsSuggestBox;
	
	public RegisterConfessionView() {
		super();
		DecoratorPanel contentTableDecorator = new DecoratorPanel();
		contentTableDecorator.addStyleName("confessionRegisterPage");
		initWidget(contentTableDecorator);
		
		confessionFormPanel = new VerticalPanel();
		
		identityPanel = new HorizontalPanel();
		rbIdentityAnyn = new RadioButton("identity", "Hide my identity.");
		rbIdentityAnyn.setTitle("Share confession as 'Anonymous'");
		rbIdentityAnyn.setValue(true);
		rbIdentityDisclose = new RadioButton("identity", "Disclose my identity!");
		rbIdentityDiscloseToSome = new RadioButton("identity", "Only Disclose to some, hide from the rest!");
		
		identityPanel.add(rbIdentityAnyn);
		identityPanel.add(rbIdentityDisclose);
		identityPanel.add(rbIdentityDiscloseToSome);
		
		hPanelShare = new HorizontalPanel();
		hPanelShare.add(new Label("Share with:"));
		hPanelShare.setVisible(false);
		
		txtConfession = new TextArea();
		txtConfession.addStyleName("registerConfessionTextBox");
		
		btnSubmit = new Button("Register your confession");
		
		confessionFormPanel.add(identityPanel);
		confessionFormPanel.add(hPanelShare);
		confessionFormPanel.add(txtConfession);
		confessionFormPanel.add(btnSubmit);
		
		contentTableDecorator.add(confessionFormPanel);	
	}
	
	public RadioButton getRbIdentityAnyn() {
		return rbIdentityAnyn;
	}

	public RadioButton getRbIdentityDisclose() {
		return rbIdentityDisclose;
	}

	public RadioButton getRbIdentityDiscloseToSome() {
		return rbIdentityDiscloseToSome;
	}

	public HorizontalPanel gethPanelShare() {
		return hPanelShare;
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

	public void setFriendsOracle(MultiWordSuggestOracle friendsOracle) {
		this.friendsOracle = friendsOracle;
		friendsSuggestBox = new SuggestBox(friendsOracle);
		hPanelShare.add(friendsSuggestBox);
	}

	public boolean isFriendsOracleNull() {
		return (friendsOracle == null);
	}
}
