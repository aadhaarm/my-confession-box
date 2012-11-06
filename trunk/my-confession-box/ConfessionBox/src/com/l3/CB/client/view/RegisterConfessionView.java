package com.l3.CB.client.view;

import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.presenter.RegisterConfessionPresenter;
import com.l3.CB.client.ui.widgets.CBTextArea;
import com.l3.CB.client.ui.widgets.CBTextBox;
import com.l3.CB.client.ui.widgets.FriendsListBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.UserInfo;

public class RegisterConfessionView extends Composite implements RegisterConfessionPresenter.Display {

    private final Grid grdConfessionForm;
    private final VerticalPanel identityPanel;
    private final CheckBox cbHideIdentity;
    private final CheckBox cbConfessTo;
    private final HorizontalPanel hPanelShare;
    private FriendsListBox friendsListBox;
    private final CBTextBox txtTitle;
    private final CBTextArea txtConfession;
    private final TextArea txtAppendedText;
    private final Button btnSubmit;

    public RegisterConfessionView() {
	super();
	DecoratorPanel contentTableDecorator = new DecoratorPanel();
	contentTableDecorator.addStyleName(Constants.STYLE_CLASS_REGISTER_CONFESSION_PAGE);
	initWidget(contentTableDecorator);

	grdConfessionForm = new Grid(8, 2);

	identityPanel = new VerticalPanel();
	cbHideIdentity = new CheckBox(ConfessionBox.cbText.registerPageOptionHideID());
	cbHideIdentity.setValue(true);
	cbConfessTo = new CheckBox(ConfessionBox.cbText.registerPageOptionConfessToFriend());

	identityPanel.add(cbHideIdentity);
	identityPanel.add(cbConfessTo);

	hPanelShare = new HorizontalPanel();
	hPanelShare.add(new Label(ConfessionBox.cbText.registerPageChooseFriend()));
	hPanelShare.setVisible(false);

	txtTitle = new CBTextBox();
	txtConfession = new CBTextArea();
	txtConfession.addStyleName(Constants.STYLE_CLASS_REGISTER_CONFESSION_TXT_BOX);

	btnSubmit = new Button(ConfessionBox.cbText.buttonTextSubmitConfession());

	HorizontalPanel hpnlTitle = new HorizontalPanel();
	final Label titleLabel = new Label(ConfessionBox.cbText.confessionTitleLabel());       
	hpnlTitle.add(titleLabel);
	hpnlTitle.add(txtTitle);

	grdConfessionForm.setWidget(0, 0, new Label(ConfessionBox.cbText.registerPageTitle()));
	grdConfessionForm.setWidget(1, 0, identityPanel);
	grdConfessionForm.setWidget(2, 0, hPanelShare);

	grdConfessionForm.setWidget(3, 0, hpnlTitle);
	grdConfessionForm.setWidget(4, 0, initializeConfessionTextBox());

	txtAppendedText = new TextArea();
	txtAppendedText.setSize("100%", "5em");
	txtAppendedText.setVisible(false);
	
	grdConfessionForm.setWidget(6, 0, btnSubmit);

	contentTableDecorator.add(grdConfessionForm);	
    }
    
    @Override
    public void setTxtAppendedText() {
	txtAppendedText.setVisible(true);
	grdConfessionForm.setWidget(5, 0, txtAppendedText);
    }

    public String getTxtAppendedText() {
        return txtAppendedText.getText();
    }

    /**
     * Initialize this example.
     */
    public Widget initializeConfessionTextBox() {
	txtConfession.setSize("100%", "14em");
	return txtConfession;
    }

    @Override
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
	return this.txtConfession.getText();
    }

    @Override
    public boolean isIdentityHidden() {
	return cbHideIdentity.getValue();
    }

    @Override
    public boolean isFriendsListNull() {
	return (friendsListBox == null);
    }

    @Override
    public String getConfessionTitle() {
	return txtTitle.getText();
    }

    @Override
    public UserInfo getSharedWith() {
	return friendsListBox.getSelectedUser();
    }

    /**
     * @param isAnyn -  Is Anonymous
     * @param gender - String
     * @param fbId - facebook id 
     */
    @Override
    public void setProfilePictureTag(boolean isAnyn, String gender, String fbId) {
	Image profileImage = null;
	if (!isAnyn) {
	    profileImage = new Image(FacebookUtil.getUserImageUrl(fbId));
	} else {
	    profileImage = new Image(FacebookUtil.getFaceIconImage(gender));
	}
	grdConfessionForm.setWidget(0, 1, profileImage);
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

    @Override
    public CBTextBox getTxtTitle() {
	return txtTitle;
    }

    @Override
    public CBTextArea getTxtConfession() {
	return txtConfession;
    }

    @Override
    public CheckBox getCbHideIdentityWidget() {
	return cbHideIdentity;
    }

    @Override
    public CheckBox getCbConfessToWidget() {
	return cbConfessTo;
    }
    @Override
    public void setFriends(List<UserInfo> userfriends) {
	friendsListBox = new FriendsListBox(userfriends);
	hPanelShare.setSpacing(10);
	hPanelShare.add(friendsListBox);
    }

    //	public String getCaptchaField() {
    //		return captchaField.getValue();
    //	}

    //	public void reloadCaptchaImage(String uId) {
    //		hpnlCaptcha.remove(captchaImage);
    //		captchaImage = new Image("/SimpleCaptcha.jpg" + "?" + uId);
    //        hpnlCaptcha.add(captchaImage);
    //	}
}
