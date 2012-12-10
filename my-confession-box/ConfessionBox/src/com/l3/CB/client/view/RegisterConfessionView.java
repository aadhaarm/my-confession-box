package com.l3.CB.client.view;

import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.presenter.RegisterConfessionPresenter;
import com.l3.CB.client.ui.widgets.CBTextArea;
import com.l3.CB.client.ui.widgets.CBTextBox;
import com.l3.CB.client.ui.widgets.FriendsSuggestBox;
import com.l3.CB.client.ui.widgets.RelationSuggestBox;
import com.l3.CB.client.ui.widgets.Templates;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.HelpInfo;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class RegisterConfessionView extends Composite implements RegisterConfessionPresenter.Display {

    private final FlowPanel fPnlConfessionForm;
    private final FlowPanel fPnlTop;
    private final FlowPanel fPnlOptions;
    private final FlowPanel fPnlConfession;
    private final CheckBox cbHideIdentity;
    private final CheckBox cbConfessTo;
    private FriendsSuggestBox friendsSuggestBox;
    private final RelationSuggestBox relationSuggestBox;
    private final CBTextBox txtTitle;
    private final CBTextArea txtConfession;
    private final FlowPanel fPnlButtons;
    private final Button btnSubmit;
    private final Button btnSave;
    private final Button btnDeleteDraft;
    private final HTML htmlConfessionPreview;    
    private Image loaderImage;    
    
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
	
//	SliderBarSimpleHorizontal a = new SliderBarSimpleHorizontal(1, "80px", false);
//	fPnlOptions.add(a);
	cbHideIdentity = new CheckBox(ConfessionBox.cbText.registerPageOptionHideID());
	cbHideIdentity.setValue(true);
	fPnlOptions.add(cbHideIdentity);
	
	cbConfessTo = new CheckBox(ConfessionBox.cbText.registerPageOptionConfessToFriend());
	fPnlOptions.add(cbConfessTo);

	fPnlConfessionForm.add(fPnlOptions);
	
	// Confession
	fPnlConfession = new FlowPanel();
	fPnlConfession.setStyleName("confession");
	
	htmlConfessionPreview = new HTML(Templates.TEMPLATES.confessonPreview(ConfessionBox.cbText.confessedByAnynName(), ConfessionBox.cbText.confessedToWorld(), "the"));
	htmlConfessionPreview.setStyleName("confessionPreview");
	fPnlConfessionForm.add(htmlConfessionPreview);
	
	txtTitle = new CBTextBox();
	fPnlConfessionForm.add(txtTitle);
	
	txtConfession = new CBTextArea(Constants.CONF_MAX_CHARS, true);
	txtConfession.setStyleName(Constants.STYLE_CLASS_REGISTER_CONFESSION_TXT_BOX);
	txtConfession.setSize("100%", "14em");

	fPnlConfessionForm.add(txtConfession);
	
	// Buttons
	fPnlButtons = new FlowPanel();
	fPnlButtons.setStyleName("regConfButtons");

	btnSave = new Button(ConfessionBox.cbText.saveAsDraftButtonLabelText());
	btnSave.setStyleName("save_draft");
	fPnlButtons.add(btnSave);
	
	btnDeleteDraft = new Button("X");
	btnDeleteDraft.setTitle(ConfessionBox.cbText.deleteDraftButtonLabelText());
	btnDeleteDraft.setStyleName("save_draft");
	btnDeleteDraft.setVisible(false);
	fPnlButtons.add(btnDeleteDraft);
	
	btnSubmit = new Button(ConfessionBox.cbText.buttonTextSubmitConfession());
	btnSubmit.setStyleName("submit_confession");
	fPnlButtons.add(btnSubmit);
	
	fPnlConfessionForm.add(fPnlButtons);

	fPnlConfessionForm.setStyleName("reg_main_container");
	initWidget(fPnlConfessionForm);

    }

    private void getMeLoaderImage() {
	loaderImage = new Image(Constants.LOADER_IMAGE_PATH);
	loaderImage.addStyleName(Constants.STYLE_CLASS_LOADER_IMAGE);
    }

    public void setFriendsTrans() {
	getMeLoaderImage();
	if(loaderImage != null) {
	    fPnlOptions.add(loaderImage);
	}
	relationSuggestBox.setVisible(true);
    }
    
    @Override
    public void setFriends(Map<String, UserInfo> userfriends) {
	if(userfriends != null && !userfriends.isEmpty()) {
	    friendsSuggestBox = new FriendsSuggestBox(userfriends);
	    fPnlOptions.add(friendsSuggestBox);
	    friendsSuggestBox.setFocus();
	    fPnlOptions.add(relationSuggestBox);
	} else {
	    fPnlOptions.add(new Label("No friends details available."));
	}
	if(loaderImage != null) {
	    fPnlOptions.remove(loaderImage);
	}
    }
    
    @Override
    public Widget gethFriendSuggestBox() {
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
	return cbHideIdentity.getValue();
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
	if(friendsSuggestBox.validate()) {
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
	Image profileImage = CommonUtils.getProfilePicture(new Confession(gender, fbId), isAnyn);
	profileImage.setStyleName("reg_image");
	fPnlTop.add(profileImage);
//	CommonUtils.parseXFBMLJS(profileImage.getElement());
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
    public Button getBtnSave() {
        return btnSave;
    }

    @Override
    public Button getBtnDeleteDraft() {
        return btnDeleteDraft;
    }
    
    @Override
    public void enableDeleteDraftButton(boolean isVisible){
	btnDeleteDraft.setVisible(isVisible);
    }

    @Override
    public RelationSuggestBox getRelationSuggestBox() {
        return relationSuggestBox;
    }

    @Override
    public HTML getHtmlConfessionPreview() {
        return htmlConfessionPreview;
    }
}