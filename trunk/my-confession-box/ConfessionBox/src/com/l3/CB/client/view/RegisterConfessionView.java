package com.l3.CB.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.presenter.RegisterConfessionPresenter;
import com.l3.CB.client.ui.widgets.RichTextToolbar;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.CBText;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.UserInfo;

public class RegisterConfessionView extends Composite implements RegisterConfessionPresenter.Display {

	private final Grid grdConfessionForm;
	
	private final VerticalPanel identityPanel;
	private final CheckBox cbHideIdentity;
	private final CheckBox cbConfessTo;

	private MultiWordSuggestOracle friendsOracle;
	private final HorizontalPanel hPanelShare;
	private SuggestBox friendsSuggestBox;

	private final TextBox txtTitle;
	
    private final RichTextArea txtConfession;
//
//	private final TextArea txtConfession;
	private final Button btnSubmit;
	
	private String captchaHTMLCode;
	

	public RegisterConfessionView(CBText cbText, UserInfo loggedInUserInfo) {
		super();
		DecoratorPanel contentTableDecorator = new DecoratorPanel();
		contentTableDecorator.addStyleName(Constants.STYLE_CLASS_REGISTER_CONFESSION_PAGE);
		initWidget(contentTableDecorator);
		
		grdConfessionForm = new Grid(7, 2);
		
		identityPanel = new VerticalPanel();
		cbHideIdentity = new CheckBox(cbText.registerPageOptionHideID());
		cbHideIdentity.setValue(true);
		cbConfessTo = new CheckBox(cbText.registerPageOptionConfessToFriend());
		
		identityPanel.add(cbHideIdentity);
		identityPanel.add(cbConfessTo);
		
		hPanelShare = new HorizontalPanel();
		hPanelShare.add(new Label(cbText.registerPageChooseFriend()));
		hPanelShare.setVisible(false);
		
		txtTitle = new TextBox();
		txtConfession = new RichTextArea();
		txtConfession.addStyleName(Constants.STYLE_CLASS_REGISTER_CONFESSION_TXT_BOX);

		btnSubmit = new Button(cbText.buttonTextSubmitConfession());
		
		
		grdConfessionForm.setWidget(0, 0, new Label(cbText.registerPageTitle()));
		grdConfessionForm.setWidget(1, 0, identityPanel);
		grdConfessionForm.setWidget(2, 0, hPanelShare);

		grdConfessionForm.setWidget(3, 0, txtTitle);
		grdConfessionForm.setWidget(4, 0, onInitialize());
		
		grdConfessionForm.setHTML(5, 0, "<div id='dynamic_recaptcha_1'></div>");
		
		
		grdConfessionForm.setWidget(6, 0, btnSubmit);
		
		contentTableDecorator.add(grdConfessionForm);	
	}
	
	/**
	   * Initialize this example.
	   */
	  public Widget onInitialize() {
	    txtConfession.ensureDebugId("cwRichText-area");
	    txtConfession.setSize("100%", "14em");
	    RichTextToolbar toolbar = new RichTextToolbar(txtConfession);
	    toolbar.ensureDebugId("cwRichText-toolbar");
	    toolbar.setWidth("100%");

	    // Add the components to a panel
	    Grid grid = new Grid(2, 1);
	    grid.setStyleName("cw-RichText");
	    grid.setWidget(0, 0, toolbar);
	    grid.setWidget(1, 0, txtConfession);
	    return grid;
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
		return this.txtConfession.getHTML();
	}

	@Override
	public boolean isIdentityHidden() {
		return cbHideIdentity.getValue();
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
		return friendsSuggestBox.getValue();
	}

	/**
	 * @param isAnyn -  Is Anonymous
	 * @param gender - String
	 * @param fbId - facebook id 
	 */
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

	@Override
	public boolean isShared() {
		return cbConfessTo.getValue();
	}

	@Override
	public HasClickHandlers getCbHideIdentity() {
		return cbHideIdentity;
	}

	@Override
	public HasClickHandlers getCbConfessTo() {
		return cbConfessTo;
	}

	public void setCaptchaHTMLCode(String captchaHTMLCode) {
//		this.captchaHTMLCode = captchaHTMLCode;
//		grdConfessionForm.setHTML(5, 0, captchaHTMLCode);
//		CommonUtils.getCaptcha("dynamic_recaptcha_1");
	}

	public TextBox getTxtTitle() {
		return txtTitle;
	}

	public RichTextArea getTxtConfession() {
		return txtConfession;
	}
	
	public CheckBox getCbHideIdentityWidget() {
		return cbHideIdentity;
	}

	public CheckBox getCbConfessToWidget() {
		return cbConfessTo;
	}
}
