package com.l3.CB.client.view;

import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.kiouri.sliderbar.client.view.SliderBarHorizontal;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.presenter.RegisterConfessionPresenter;
import com.l3.CB.client.ui.widgets.CBTextArea;
import com.l3.CB.client.ui.widgets.CBTextBox;
import com.l3.CB.client.ui.widgets.FriendsSuggestBox;
import com.l3.CB.client.ui.widgets.ShareSliderWidget;
import com.l3.CB.client.ui.widgets.RelationSuggestBox;
import com.l3.CB.client.ui.widgets.IdentitySliderWidget;
import com.l3.CB.client.ui.widgets.Templates;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.HelpInfo;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class RegisterConfessionView extends Composite implements RegisterConfessionPresenter.Display {

    private boolean isFriendNotRegistered = true;
    
    private final FlowPanel fPnlConfessionForm;
    private final FlowPanel fPnlTop;
    private final FlowPanel fPnlOptions;
    private final FlowPanel fPnlConfession;
    private final FlowPanel fPnlPreview;
    private final FlowPanel fPnlConfirm;

    private CheckBox cbHideIdentity;
    private SliderBarHorizontal identitySlideBar;

    private CheckBox cbConfessTo;
    private SliderBarHorizontal shareToSlider;

    private FriendsSuggestBox friendsSuggestBox;
    private RelationSuggestBox relationSuggestBox;

    private final CBTextBox txtTitle;
    private final CBTextArea txtConfession;

    private final FlowPanel fPnlButtons;
    private final Button btnSubmit;
    private final Button btnProceed;
    private final Button btnEdit;
    private final Button btnSave;

    private final HTML htmlConfessionPreview;    

    private Image loaderImage;    
    private Image profileImage;

    public RegisterConfessionView() {
	super();
	//TOP panel
	fPnlConfessionForm = new FlowPanel();

	fPnlTop = new FlowPanel();
	fPnlTop.setStyleName("selections");

	HTML topTitle = new HTML(Templates.TEMPLATES.registerConfessionInstructionText(
		ConfessionBox.cbText.registerConfessionInstructionTextOne(),
		ConfessionBox.cbText.registerConfessionInstructionTextTwo()));
	topTitle.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		HelpInfo.showHelpInfo(HelpInfo.type.REGISTER_CONFESSION_TITLE_HELP);
	    }
	});

	topTitle.setStyleName("header_text");
	fPnlTop.add(topTitle);

	fPnlConfessionForm.add(fPnlTop);

	// Options panel
	fPnlOptions = new FlowPanel();
	fPnlOptions.setStyleName("options");

	HTML optionsTitle = new HTML("Confession Sharing Options [?]");
	optionsTitle.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		HelpInfo.showHelpInfo(HelpInfo.type.REGISTER_CONFESION_OPTIONS);
	    }
	});
	optionsTitle.setStyleName("options_title");
	fPnlOptions.add(optionsTitle);

	relationSuggestBox = new RelationSuggestBox();
	relationSuggestBox.setVisible(false);
	if(ConfessionBox.isTouchEnabled) {
	    identitySlideBar = new IdentitySliderWidget();
	    identitySlideBar.addStyleName("identitySlider");

	    fPnlOptions.add(new Label(ConfessionBox.cbText.registerPageOptionHideIDSlider()));
	    fPnlOptions.add(identitySlideBar);

	    shareToSlider = new ShareSliderWidget();
	    fPnlOptions.add(new Label(ConfessionBox.cbText.registerPageOptionConfessToFriend()));
	    fPnlOptions.add(shareToSlider);
	} else {
	    cbHideIdentity = new CheckBox(ConfessionBox.cbText.registerPageOptionHideID());
	    cbHideIdentity.setValue(true);
	    fPnlOptions.add(cbHideIdentity);

	    cbConfessTo = new CheckBox(ConfessionBox.cbText.registerPageOptionConfessToFriend());
	    fPnlOptions.add(cbConfessTo);
	}

	fPnlConfessionForm.add(fPnlOptions);

	// Preview
	fPnlPreview = new FlowPanel();
	htmlConfessionPreview = new HTML(Templates.TEMPLATES.confessonPreview(ConfessionBox.cbText.confessedByAnynName(), ConfessionBox.cbText.confessedToWorld(), "the"));
	htmlConfessionPreview.setStyleName("confessionPreview");
	fPnlPreview.add(htmlConfessionPreview);
	fPnlPreview.setStyleName("previewBar");
	fPnlConfessionForm.add(fPnlPreview);

	// Confession
	fPnlConfession = new FlowPanel();
	fPnlConfession.setStyleName("confession");


	txtTitle = new CBTextBox();
	fPnlConfessionForm.add(txtTitle);

	txtConfession = new CBTextArea(Constants.CONF_MAX_CHARS, true, "write your confession here..");
	txtConfession.setStyleName(Constants.STYLE_CLASS_REGISTER_CONFESSION_TXT_BOX);

	fPnlConfessionForm.add(txtConfession);

	// Buttons
	fPnlButtons = new FlowPanel();
	fPnlButtons.setStyleName("regConfButtons");

	btnSave = new Button(ConfessionBox.cbText.saveAsDraftButtonLabelText());
	btnSave.setStyleName("save_draft");
	fPnlButtons.add(btnSave);
	
	btnProceed = new Button("Proceed");
	btnProceed.setStyleName("submit_confession");
	fPnlButtons.add(btnProceed);

	btnEdit = new Button("Edit");
	btnEdit.setStyleName("submit_confession");
	btnEdit.setVisible(false);
	fPnlButtons.add(btnEdit);
	
	fPnlConfirm = new FlowPanel();

	btnSubmit = new Button(ConfessionBox.cbText.buttonTextSubmitConfession());
	btnSubmit.setStyleName("submit_confession");
//	fPnlButtons.add(btnSubmit);

	fPnlConfessionForm.add(fPnlButtons);

	fPnlConfessionForm.setStyleName("reg_main_container");
	initWidget(fPnlConfessionForm);

    }
    
    private void disableForm() {
	txtConfession.disable();
	txtTitle.disable();
	fPnlOptions.setVisible(false);
    }
    
    private void enableForm() {
	txtConfession.enable();
	txtTitle.enable();
	fPnlOptions.setVisible(true);
    }
    
    public void setupConfirmPanel(boolean isFriendNotRegistered, String pardonerName) {
	fPnlConfirm.clear();
	disableForm();
	this.isFriendNotRegistered = isFriendNotRegistered;
	if(isFriendNotRegistered && pardonerName != null) {
	    fPnlConfirm.add(new HTML(Templates.TEMPLATES.confessorNotRegisteredMessage(pardonerName)));
	}
	
	btnProceed.setVisible(false);
	btnEdit.setVisible(true);
	
	fPnlConfirm.add(new HTML(Templates.TEMPLATES.confessionConfirmMessage(ConfessionBox.cbText.confirmMessageWhenSubmittingConfession())));
	fPnlConfirm.add(btnSubmit);
	fPnlConfessionForm.add(fPnlConfirm);
    }
    
    public void removeConfirmPanel() {
	enableForm();
	btnProceed.setVisible(true);
	btnEdit.setVisible(false);
	fPnlConfessionForm.remove(fPnlConfirm);
    }

    public boolean isFriendNotRegistered() {
        return isFriendNotRegistered;
    }
    
    public Button getBtnEdit() {
        return btnEdit;
    }

    private void getMeLoaderImage() {
	loaderImage = new Image(Constants.LOADER_IMAGE_PATH);
	loaderImage.addStyleName(Constants.STYLE_CLASS_LOADER_IMAGE);
    }

    public void setFriendsTrans() {
	if(loaderImage == null) {
	    getMeLoaderImage();
	}
	if(loaderImage != null) {
	    if(!loaderImage.isAttached()) {
		fPnlOptions.add(loaderImage);
	    }
	}
	relationSuggestBox.setVisible(true);
    }

    @Override
    public void setFriends(Map<String, UserInfo> userfriends) {
	if(friendsSuggestBox == null){
	    if(userfriends != null && !userfriends.isEmpty()) {
		friendsSuggestBox = new FriendsSuggestBox(userfriends);
		fPnlOptions.add(friendsSuggestBox);
		friendsSuggestBox.setFocus();
		
		fPnlOptions.add(relationSuggestBox);
		relationSuggestBox.setVisible(true);
	    } else {
		fPnlOptions.add(new Label("No friends details available."));
	    }
	}
	if(loaderImage != null) {
	    fPnlOptions.remove(loaderImage);
	}
    }

    @Override
    public FriendsSuggestBox getFriendSuggestBox() {
	return friendsSuggestBox;
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
	return this.txtConfession.getCbTextArea().getHTML();
    }

    @Override
    public boolean isIdentityHidden() {
	if(cbHideIdentity != null) {
	    return cbHideIdentity.getValue();
	} else {
	    return identitySlideBar.getValue() == 0? true : false;
	}
    }

    @Override
    public boolean isFriendsListNull() {
	return (friendsSuggestBox == null);
    }

    @Override
    public String getConfessionTitle() {
	return txtTitle.getTxtTitle().getText();
    }

    @Override
    public UserInfo getSharedWith() {
	if(friendsSuggestBox != null && friendsSuggestBox.validate()) {
	    return friendsSuggestBox.getSelectedUser();
	}
	return null;
    }

    /**
     * @param isAnyn -  Is Anonymous
     * @param gender - String
     * @param fbId - facebook id 
     */
    @Override
    public void setProfilePictureTag(boolean isAnyn, String gender, String fbId) {
	if(profileImage != null) {
	    fPnlPreview.remove(profileImage);
	}
	profileImage = CommonUtils.getProfilePicture(new Confession(gender, fbId), isAnyn);
	profileImage.setStyleName("reg_image");
	fPnlPreview.add(profileImage);
    }

    @Override
    public boolean isShared() {
	if(cbConfessTo != null) {
	    return cbConfessTo.getValue();
	} else {
	    return shareToSlider.getValue() == 0? false : true;
	}
    }

    @Override
    public HasClickHandlers getCbHideIdentity() {
	return cbHideIdentity;
    }

    @Override
    public HasClickHandlers getCbConfessTo() {
	return cbConfessTo;
    }

    @Override
    public CheckBox getCbHideIdentityCheckBox() {
	return cbHideIdentity;
    }

    @Override
    public CheckBox getCbConfessToCheckBox() {
	return cbConfessTo;
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
    public Button getBtnSave() {
	return btnSave;
    }

    @Override
    public RelationSuggestBox getRelationSuggestBox() {
	return relationSuggestBox;
    }

    @Override
    public HTML getHtmlConfessionPreview() {
	return htmlConfessionPreview;
    }

    public SliderBarHorizontal getIdentitySlideBar() {
	return identitySlideBar;
    }

    public SliderBarHorizontal getShareToSlider() {
	return shareToSlider;
    }

    public Button getBtnProceed() {
        return btnProceed;
    }
}