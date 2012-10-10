package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.presenter.RegisterConfessionPresenter;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.FacebookUtil;

public class RegisterConfessionView extends Composite implements RegisterConfessionPresenter.Display {

	private final Grid grdConfessionForm;
	
	private final VerticalPanel identityPanel;
	private final RadioButton rbIdentityAnyn;
	private final RadioButton rbIdentityDisclose;
	private final RadioButton rbIdentityDiscloseToSome;
	private final TextArea txtConfession;
	private final Button btnSubmit;
	private final HorizontalPanel hPanelShare;
	private MultiWordSuggestOracle friendsOracle;
	private SuggestBox friendsSuggestBox;
	private final TextBox txtTitle;
	
	public RegisterConfessionView() {
		super();
		DecoratorPanel contentTableDecorator = new DecoratorPanel();
		contentTableDecorator.addStyleName("confessionRegisterPage");
		initWidget(contentTableDecorator);
		
		grdConfessionForm = new Grid(7, 2);
		
		identityPanel = new VerticalPanel();
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
		txtTitle = new TextBox();
		
		btnSubmit = new Button("Register your confession");
		
		grdConfessionForm.setWidget(0, 0, new Label("Confess"));
		grdConfessionForm.setWidget(1, 0, txtTitle);
		grdConfessionForm.setWidget(2, 0, txtConfession);
		grdConfessionForm.setWidget(3, 0, identityPanel);
		grdConfessionForm.setWidget(4, 0, hPanelShare);
		grdConfessionForm.setWidget(5, 0, btnSubmit);
		
		contentTableDecorator.add(grdConfessionForm);	
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


	public String getConfessionTitle() {
		return txtTitle.getText();
	}

	public String getSharedWith() {
		Window.alert(friendsSuggestBox.getValue());
		Window.alert(friendsSuggestBox.getText());
		return friendsSuggestBox.getValue();
	}

	public void setProfilePictureTag(boolean isAnyn, String gender, String fbId) {
		final StringBuilder sb = new StringBuilder();

		if (isAnyn) {
			sb.append("<img src=");
			sb.append("'")
					.append(FacebookUtil.getFaceIconImage(gender)).append("'");
			sb.append("/>");
		} else {
			sb.append("<fb:profile-pic uid=\"")
					.append(fbId)
					.append("\" width=\"50\" height=\"50\" linked=\"true\"></fb:profile-pic>");
		}

		grdConfessionForm.setHTML(0, 1, sb.toString());
		CommonUtils.parseXFBMLJS();
	}

}
